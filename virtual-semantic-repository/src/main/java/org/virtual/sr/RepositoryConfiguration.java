package org.virtual.sr;

import static org.virtualrepository.Utils.*;
import static org.virtual.sr.utils.Constants.*;
import java.net.URI;
import java.util.Properties;


public class RepositoryConfiguration {

	private final Properties properties;
	
	public RepositoryConfiguration(Properties properties) {
		this.properties=properties;
		
		notNull("configuration", properties);
		notNull("staging_endpoint_update", properties.getProperty("staging_endpoint_update"));
		notNull("staging_endpoint_data", properties.getProperty("staging_endpoint_data"));
		notNull("staging_endpoint_query", properties.getProperty("staging_endpoint_query"));
		notNull("public_endpoint_query", properties.getProperty("public_endpoint_query"));
		notNull("discovery query", properties.getProperty("sparqlQueryForCodelists"));
		notNull("construct query for FLOD dataset", properties.getProperty("sparqlFLODdataset"));
	}
	
	public URI stagingEndpointUpdate() {
		return URI.create(properties.getProperty("staging_endpoint_update"));
	}
        
	public URI stagingEndpointData() {
		return URI.create(properties.getProperty("staging_endpoint_data"));
	}
      
	public URI staging_endpoint_query() {
		return URI.create(properties.getProperty("staging_endpoint_query"));
	}
        
	public URI public_endpoint_query() {
		return URI.create(properties.getProperty("public_endpoint_query"));
	}
        
	public String sparqlQueryForCodelists() {
		return properties.getProperty("sparqlQueryForCodelists");
	}
        
	public String sparqlQueryForCodelist(String codelistUri) {
		String p = properties.getProperty("sparqlQueryForCodelist");
                return  p.replace("cl_uri", codelistUri);
	}
        
	public String sparqlFLODdataset(String codelistId, String graphId) {
		String p = properties.getProperty("sparqlFLODdataset");
                String codeClassUri = properties.getProperty(codelistId);
                String localname = codeClassUri.split("#")[1].toLowerCase();
                p = p.replace("code_class_uri", codeClassUri);
                p = p.replace("code_entities_ns", codedentity_ns+localname+"/");
                p = p.replace("graph_id", graphId);
                return p;
	}
        
        public boolean isKnownCodelist(String codelistId){
            return properties.containsKey(codelistId);
        }
}
