package org.virtual.sr;

import static com.hp.hpl.jena.rdf.model.ModelFactory.*;
import static org.virtual.sr.utils.Constants.*;

import java.util.Calendar;

import org.apache.jena.web.DatasetGraphAccessorHTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A {@link Publisher} for the Semantic Repository that works with RDF models of
 * arbitrary asset types.
 *
 * @author Fabio Simeoni
 *
 * @param <A>
 *            the type of Assets published by this publisher
 */
public class RepositoryPublisher<A extends Asset> implements
		Publisher<A, Model> {

	private final RepositoryConfiguration configuration;
	private final Type<A> assetType;
	private static Logger log = LoggerFactory
			.getLogger(RepositoryPublisher.class);
	private final String publishEndpoint;
	private final String queryEndpoint;
	private static final Property void_sparqlEndpoint = ResourceFactory.createProperty("http://rdfs.org/ns/void#sparqlEndpoint");
	private static final Property void_sparqlEndpoint_write = ResourceFactory.createProperty("http://gradesystem.io/onto/void_ext.owl#sparqlEndpoint_write");
	private final DatasetGraphAccessorHTTP accessor;

	public RepositoryPublisher(Type<A> assetType,
			RepositoryConfiguration configuration) {
		this.assetType = assetType;
		this.configuration = configuration;
		this.publishEndpoint = configuration.staging_endpoint_update()
				.toString();
		this.queryEndpoint = configuration.staging_endpoint_query()
				.toString();
		this.accessor = new DatasetGraphAccessorHTTP(this.configuration
				.staging_endpoint_data().toString());
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
		log.info("Received a asset type {} with name {} ", asset.type(),
				asset.name());
		
		Statement stmt = rdf.getProperty(null,rdf.getProperty(pseudoNS + "version"));
	
		String assetVersion = stmt ==null ? "1.0" : stmt.getString() ;

		String graphId = staging_graph_ns + assetVersion + "/" + asset.name();
		Node gNode = NodeFactory.createURI(graphId);

		String datasetId = staging_dataset_ns + asset.name(); // no version
		Node dsNode = NodeFactory.createURI(datasetId);

		addGraphMetadata(asset, rdf, gNode, dsNode);

		Model dsMetadataModel = createDefaultModel();
		
		addDatasetMetadata(asset, dsMetadataModel, gNode, dsNode);
		
		Graph existinG = accessor.httpGet(gNode);
		if (existinG != null) {
			accessor.httpDelete(gNode);
		}

		UpdateDataInsert insertGraph = new UpdateDataInsert(makeQuadAcc(gNode,
				rdf.getGraph()));
		long time = System.currentTimeMillis();
		UpdateExecutionFactory.createRemote(insertGraph, publishEndpoint).execute();
		log.info("Staged {} triples for {} in {} ms.", rdf.size(), graphId,
				System.currentTimeMillis() - time);
                
                UpdateDataInsert insertDataset = new UpdateDataInsert(makeQuadAcc(gNode,
				dsMetadataModel.getGraph()));
                UpdateExecutionFactory.createRemote(insertDataset, publishEndpoint).execute();
	}

	private QuadDataAcc makeQuadAcc(Node gNode, Graph graph) {
		ExtendedIterator<Triple> allTriples = graph.find(Node.ANY, Node.ANY,
				Node.ANY);
		QuadDataAcc qda = new QuadDataAcc();
		qda.setGraph(gNode);
		while (allTriples.hasNext()) {
			Triple triple = allTriples.next();
			qda.addTriple(triple);
		}
		return qda;
	}

	private void addGraphMetadata(Asset asset, Model model, Node gNode,
			Node dsNode) {
		XSDDateTime now = new XSDDateTime(Calendar.getInstance());
		model.createResource(gNode.getURI()).addLiteral(DCTerms.created,
				model.createTypedLiteral(now));// serve a verificare se ci sono
												// stati cambi dall'ultima
												// passata
		model.createResource(gNode.getURI()).addProperty(DCTerms.isPartOf,
				dsNode.getURI());// links graphs and dataset
		model.createResource(gNode.getURI()).addProperty(RDFS.label,
				asset.name());
                model.createResource(gNode.getURI()).addProperty(DCTerms.creator, model.createResource("http://virtualrepository/plugin/sr"));
	}

	private void addDatasetMetadata(Asset asset, Model model, Node gNode, Node dsNode) {
    	XSDDateTime now = new XSDDateTime(Calendar.getInstance()); 
        model.createResource(dsNode.getURI()).addLiteral(DCTerms.modified, model.createTypedLiteral(now));//note MODIFIED property
        model.createResource(dsNode.getURI()).addProperty(DCTerms.hasPart, gNode.getURI()); // links graphs and dataset
        model.createResource(dsNode.getURI()).addProperty(RDF.type, model.createResource("http://rdfs.org/ns/void#Dataset"));
        model.createResource(dsNode.getURI()).addProperty(void_sparqlEndpoint, model.createResource(queryEndpoint));
        model.createResource(dsNode.getURI()).addProperty(void_sparqlEndpoint_write, model.createResource(publishEndpoint));
        model.createResource(dsNode.getURI()).addProperty(RDFS.label, asset.name());
	}

}
