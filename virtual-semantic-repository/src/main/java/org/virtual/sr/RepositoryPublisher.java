package org.virtual.sr;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import static org.virtual.sr.utils.Constants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import org.apache.jena.web.DatasetGraphAccessorHTTP;

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

    private final RepositoryConfiguration configuration;
    private final Type<A> assetType;
    private static Logger log = LoggerFactory.getLogger(RepositoryPublisher.class);
    private final String publishEndpoint;
    private final String graph_ns = "http://semanticrepository/staging/graph/";
    private final DatasetGraphAccessorHTTP accessor;

    public RepositoryPublisher(Type<A> assetType, RepositoryConfiguration configuration) {
        this.assetType = assetType;
        this.configuration = configuration;
        this.publishEndpoint = configuration.staging_endpoint_update().toString();
        this.accessor = new DatasetGraphAccessorHTTP(configuration.staging_endpoint_data().toString());
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
            publishRawRdf(asset, rdf);
        }

        if (asset.type() == SdmxCodelist.type) {
            log.info("I received a code list " + asset.name() + ", ingestionId=" + asset.properties().lookup(ingestionId).value());
            publishRawRdf(asset, rdf);
        }
    }

    private void publishRawRdf(Asset asset, Model rdf) {
//        String codelistId = asset.properties().lookup(ingestionId).value().toString();
        String assetVersion = rdf.getProperty(null, rdf.getProperty(pseudoNS + "version")).getString();
        String graphId = graph_ns + assetVersion + "/" + asset.name();
        Node gNode = NodeFactory.createURI(graphId);
        publishRDF(asset, rdf, gNode);
    }
    
    private void publishRDF(Asset asset, Model rdf, Node gNode) {
        Graph existinG = accessor.httpGet(gNode);
        if (existinG != null) {
            accessor.httpDelete(gNode);
        }
        UpdateDataInsert insert = new UpdateDataInsert(makeQuadAcc(gNode,rdf.getGraph()));
        long time = System.currentTimeMillis();
        UpdateExecutionFactory.createRemote(insert, publishEndpoint).execute();
        log.info("published {} triples for {} in {} ms.", rdf.size(), asset.name(), System.currentTimeMillis() - time);
    }

    private QuadDataAcc makeQuadAcc(Node gNode, Graph graph) {
        ExtendedIterator<Triple> allTriples = graph.find(Node.ANY, Node.ANY, Node.ANY);
        QuadDataAcc qda = new QuadDataAcc();
        qda.setGraph(gNode);
        while (allTriples.hasNext()) {
            Triple triple = allTriples.next();
            qda.addTriple(triple);
        }
        return qda;
    }
}
    