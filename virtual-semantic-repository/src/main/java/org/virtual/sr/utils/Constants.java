package org.virtual.sr.utils;

import org.virtualrepository.Property;

public class Constants {

    public static final String ingestionId = "ingestionid";
    public static final String ownerName = "owner";
    public static final String pseudoNS = "s:r/";
    
    public static final String prefixes =  
            " PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#> \n"
            + " PREFIX dc:       <http://purl.org/dc/elements/1.1/> \n"
            + " PREFIX dct:      <http://purl.org/dc/terms/> \n"
            + " PREFIX owl:      <http://www.w3.org/2002/07/owl#> \n"
            + " PREFIX dwc:      <http://rs.tdwg.org/dwc/terms/> \n"
            + " PREFIX skos:     <http://www.w3.org/2004/02/skos/core#> \n"
            + " PREFIX sys:      <http://www.fao.org/figis/flod/onto/codedentitycollection.owl#> \n"
            + " PREFIX cls:      <http://www.fao.org/figis/flod/onto/codedentityclassification.owl#> \n"
            + " PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
            + " PREFIX j.0:      <s:r/> \n"
            + " PREFIX fn:      <http://www.w3.org/2005/xpath-functions#> \n";

    
    public static Property ingestionId(String value) {
    	return new Property(ingestionId, value, false);
    }
    
    public static Property ownerProperty(String value) {
        return new Property(ownerName, value);
    }

}
