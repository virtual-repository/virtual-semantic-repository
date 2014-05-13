package org.virtual.sr;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.NodeFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.util.ModelUtils;
import org.apache.jena.web.DatasetGraphAccessorHTTP;

public class RdfImporter<A extends Asset> implements Importer<A, Model> {

    private final RepositoryConfiguration configuration;
    private final Type<A> type;
    private final DatasetGraphAccessorHTTP datasetAccessor;
    
    public RdfImporter(Type<A> type, RepositoryConfiguration configuration) {
        this.configuration = configuration;
        this.type = type;
        this.datasetAccessor = new DatasetGraphAccessorHTTP(this.configuration.public_endpoint_data().toString());
    }

    @Override
    public Type<A> type() {
        return type;
    }

    @Override
    public Class<Model> api() {
        return Model.class;
    }

    @Override
    public Model retrieve(A asset) throws Exception {   
        Graph graph = this.datasetAccessor.httpGet(NodeFactory.createURI(asset.id()));
        Model m = ModelFactory.createDefaultModel();
        m.add(ModelUtils.triplesToStatements(GraphUtil.findAll(graph), m));
        return m;
    }
}
