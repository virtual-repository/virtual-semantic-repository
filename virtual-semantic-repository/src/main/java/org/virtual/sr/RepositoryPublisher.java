package org.virtual.sr;

import com.hp.hpl.jena.graph.Node;
import static org.virtual.sr.utils.Constants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory;
import com.hp.hpl.jena.sparql.util.FmtUtils;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.jena.riot.RiotWriter;

import org.virtual.sr.transforms.Raw2CustomRdf;
import org.virtual.sr.utils.Constants;
import org.virtualrepository.fmf.CometAsset;
import org.virtualrepository.sdmx.SdmxCodelist;

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
    private final String publishEndpoint;
    private final String graph_ns = "http://www.fao.org/figis/flod/graph/";

    public RepositoryPublisher(Type<A> assetType, RepositoryConfiguration configuration) {
        this.assetType = assetType;
        this.configuration = configuration;
        this.publishEndpoint = configuration.publish_in_sr_staging_uri().toString();
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

        if (asset.type() == CometAsset.type) {
            log.info("I received a mapping list " + asset.name());
        }

        if (asset.type() == SdmxCodelist.type) {
            log.info("I received a code list " + asset.name() + ", ingestionId=" + asset.properties().lookup(ingestionId).value());
//            publishRawRdf(asset, rdf);
//        //super-temporary hack
            publishCustomRdf(asset, rdf);
        }
    }

    private void publishRawRdf(Asset asset, Model rdf) {
        String codelistId = asset.properties().lookup(ingestionId).value().toString();
        String assetVersion = rdf.getProperty(null, rdf.getProperty(pseudoNS+"version")).getString();
        String graphId = graph_ns + assetVersion + "/" + codelistId;
        publish_on_file(rdf,graphId, "raw_" + codelistId + ".rdf");
//        publishRDF(asset, rdf);
    }

    private void publishCustomRdf(Asset asset, Model rdf) {
        String codelistId = asset.properties().lookup(ingestionId).value().toString();
        String assetVersion = rdf.getProperty(null, rdf.getProperty(pseudoNS+"version")).getString();
        String graphId = graph_ns + assetVersion + "/" + codelistId;
        if (configuration.isKnownCodelist(codelistId + "-sdmx")) {
            rdf = Raw2CustomRdf.FLODcustomize(rdf, configuration.sparqlFLODdataset(codelistId + "-sdmx", graphId));
//            rdf = Raw2CustomRdf.pruneExistingCodes(rdf);

            publish_on_file(rdf, graphId, codelistId + ".rdf");
        }
//        publishRDF(asset, rdf);
    }

    private void publishRDF(Asset asset, Model rdf) {
        StmtIterator stmts = rdf.listStatements();
        long time = System.currentTimeMillis();

        StringBuilder triples = new StringBuilder();
        int published = 0;
        int queries = 0;
        while (stmts.hasNext()) {
            int genTime = 0;
            Statement s = stmts.next();
            triples.append(FmtUtils.stringForTriple(s.asTriple()) + " . ");
            published++;
            if (published == TRIPLE_BUFFER_SIZE) {
                log.trace("prepared {} triples for {} in {} ms.", TRIPLE_BUFFER_SIZE, asset.name(), System.currentTimeMillis() - genTime);
                flush(asset, published, triples.toString(), publishEndpoint);
                triples = new StringBuilder();
                published = 0;
                queries++;
            }
        }

        if (published > 0) {
            flush(asset, published, triples.toString(), publishEndpoint);
            queries++;
        }
        log.info("published {} triples for {} with {} queries in {} ms.", rdf.size(), asset.name(), queries, System.currentTimeMillis() - time);
    }

    private void flush(Asset asset, int accumulated, String triples, String flushEndpoint) {
        log.trace("publishing {} custom triples for {} ", accumulated, asset.name());
        UpdateExecutionFactory.createRemote(UpdateFactory.create(Constants.prefixes + " insert data {" + triples + "}"), flushEndpoint).execute();

    }

    private void publish_on_file(Model rdf, String graphId, String filename) {
        DatasetGraph dsg = DatasetGraphFactory.createMem();
        dsg.addGraph(Node.createURI(graphId), rdf.getGraph());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename));
            RiotWriter.writeNQuads(fos, dsg);
            fos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

}
