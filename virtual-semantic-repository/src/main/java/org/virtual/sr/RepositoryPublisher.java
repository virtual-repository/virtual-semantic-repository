package org.virtual.sr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.util.FmtUtils;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;

/**
 * A {@link Publisher} for the Semantic Repository that works with RDF models of
 * arbitrary asset types.
 *
 * @author Fabio Simeoni
 *
 * @param <A> the type of Assets published by this publisher
 */
public class RepositoryPublisher<A extends Asset> implements Publisher<A, Model> {

    private static final int TRIPLE_BUFFER_SIZE = 20000;
	private final RepositoryConfiguration configuration;
    private final Type<A> assetType;
    private static Logger log = LoggerFactory.getLogger(RepositoryPublisher.class);

    public RepositoryPublisher(Type<A> assetType, RepositoryConfiguration configuration) {
        this.assetType = assetType;
        this.configuration = configuration;
    }

    @Override
    public Type<A> type() {
        return assetType;
    }

    @Override
    public Class<Model> api() {
        return Model.class;
    }

    @Override
    public void publish(A asset, Model rdf) throws Exception {

        StmtIterator stmts = rdf.listStatements();
        
        long time = System.currentTimeMillis();
        
        StringBuilder triples = new StringBuilder();
        int published=0;
        int queries=0;
     
        while (stmts.hasNext()) {
        	int genTime=0;
            Statement s = stmts.next();
            triples.append(FmtUtils.stringForTriple(s.asTriple()) + " . ");
            published++;
        	if (published==TRIPLE_BUFFER_SIZE) {
        		log.trace("prepared {} triples for {} in {} ms.",TRIPLE_BUFFER_SIZE,asset.name(),System.currentTimeMillis()-genTime);
	            flush(asset,published,triples.toString());
	            triples = new StringBuilder();
	            published=0;
	            queries++;
        	}
        }
        
        if (published>0) {
        	flush(asset,published,triples.toString());
        	queries++;
        }
        
        log.info("published {} triples for {} with {} queries in {} ms.",rdf.size(),asset.name(),queries,System.currentTimeMillis()-time);

    }
    
    private void flush(Asset asset, int accumulated, String triples) {
    	log.trace("publishing {} triples for {} ",accumulated,asset.name());
        UpdateExecutionFactory.createRemote(UpdateFactory.create("insert data {" + triples + "}"), configuration.publishURI().toString()).execute();
    }

    //helpers
    Source xmlOf(CodelistBean bean) {

        SdmxBeans beans = new SdmxBeansImpl();
        beans.addCodelist(bean);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);

        STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;

        StructureWritingManager manager = new StructureWritingManagerImpl();
        manager.writeStructures(beans, format, stream);

        return new StreamSource(new ByteArrayInputStream(stream.toByteArray()));
    }
}
