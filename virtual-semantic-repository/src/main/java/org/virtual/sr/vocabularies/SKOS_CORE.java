/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.virtual.sr.vocabularies;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 *
 * @author Claudio Baldassarre <c.baldassarre@me.com>
 */

   public class SKOS_CORE {

    public final static String BASE_URI = "http://www.w3.org/2004/02/skos/core";

    public final static String NS = BASE_URI + "#";

    public final static String PREFIX = "skos";


    public final static Resource Collection = ResourceFactory.createResource(NS + "Collection");

    public final static Resource Concept = ResourceFactory.createResource(NS + "Concept");

    public final static Resource ConceptScheme = ResourceFactory.createResource(NS + "ConceptScheme");

    public final static Resource OrderedCollection = ResourceFactory.createResource(NS + "OrderedCollection");

    public final static Property altLabel = ResourceFactory.createProperty(NS + "altLabel");

    public final static Property broadMatch = ResourceFactory.createProperty(NS + "broadMatch");

    public final static Property broader = ResourceFactory.createProperty(NS + "broader");

    public final static Property broaderTransitive = ResourceFactory.createProperty(NS + "broaderTransitive");

    public final static Property changeNote = ResourceFactory.createProperty(NS + "changeNote");

    public final static Property closeMatch = ResourceFactory.createProperty(NS + "closeMatch");

    public final static Property definition = ResourceFactory.createProperty(NS + "definition");

    public final static Property editorialNote = ResourceFactory.createProperty(NS + "editorialNote");

    public final static Property exactMatch = ResourceFactory.createProperty(NS + "exactMatch");

    public final static Property example = ResourceFactory.createProperty(NS + "example");

    public final static Property hasTopConcept = ResourceFactory.createProperty(NS + "hasTopConcept");

    public final static Property hiddenLabel = ResourceFactory.createProperty(NS + "hiddenLabel");

    public final static Property historyNote = ResourceFactory.createProperty(NS + "historyNote");

    public final static Property inScheme = ResourceFactory.createProperty(NS + "inScheme");

    public final static Property mappingRelation = ResourceFactory.createProperty(NS + "mappingRelation");

    public final static Property member = ResourceFactory.createProperty(NS + "member");

    public final static Property memberList = ResourceFactory.createProperty(NS + "memberList");

    public final static Property narrowMatch = ResourceFactory.createProperty(NS + "narrowMatch");

    public final static Property narrower = ResourceFactory.createProperty(NS + "narrower");

    public final static Property narrowerTransitive = ResourceFactory.createProperty(NS + "narrowerTransitive");

    public final static Property notation = ResourceFactory.createProperty(NS + "notation");

    public final static Property note = ResourceFactory.createProperty(NS + "note");

    public final static Property prefLabel = ResourceFactory.createProperty(NS + "prefLabel");

    public final static Property related = ResourceFactory.createProperty(NS + "related");

    public final static Property relatedMatch = ResourceFactory.createProperty(NS + "relatedMatch");

    public final static Property scopeNote = ResourceFactory.createProperty(NS + "scopeNote");

    public final static Property semanticRelation = ResourceFactory.createProperty(NS + "semanticRelation");

    public final static Property topConceptOf = ResourceFactory.createProperty(NS + "topConceptOf");


    public static String getURI() {
        return NS;
    }
}
