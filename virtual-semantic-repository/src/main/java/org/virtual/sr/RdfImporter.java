package org.virtual.sr;

import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class RdfImporter<A extends Asset> implements Importer<A, Model> {

    private final RepositoryConfiguration configuration;
    private final Type<A> type;

    public RdfImporter(Type<A> type, RepositoryConfiguration configuration) {
        this.configuration = configuration;
        this.type = type;
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
        
        Query q = QueryFactory.create(configuration.query_get_codelist(asset.id()));
        String endpoint = configuration.public_endpoint_query().toString();
        return QueryExecutionFactory.sparqlService(endpoint, q).execConstruct();
    }
}
