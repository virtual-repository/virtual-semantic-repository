/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.virtual.sr.transforms;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.virtual.sr.utils.Constants;

/**
 *
 * @author Claudio Baldassarre <c.baldassarre@me.com>
 */
public class Raw2CustomRdf {

    public static Model FLODcustomize(Model rdf, String query) {
        Query q = QueryFactory.create(Constants.prefixes + query);
        Query trasnformationQuery = QueryFactory.create(q);
        Model transformedModel = QueryExecutionFactory.create(trasnformationQuery, rdf).execConstruct();
        return transformedModel;
    }
}
