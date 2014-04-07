package org.virtual.sr;

import static org.virtualrepository.Utils.*;

import java.net.URI;
import java.util.Properties;

public class RepositoryConfiguration {

	private final Properties properties;
	
	public RepositoryConfiguration(Properties properties) {
		this.properties=properties;
		
		notNull("configuration", properties);
		notNull("publish in sr_staging URI", properties.getProperty("publish_in_sr_staging_uri"));
		notNull("publish in sr_public URI", properties.getProperty("publish_in_sr_public_uri"));
		notNull("discovery URI", properties.getProperty("discoveryURI"));
		notNull("discovery query", properties.getProperty("sparqlQueryForCodelists"));
	}
	
	public URI publish_in_sr_staging_uri() {
		return URI.create(properties.getProperty("publish_in_sr_staging_uri"));
	}
        
        public URI publish_in_sr_public_uri() {
		return URI.create(properties.getProperty("publish_in_sr_public_uri"));
	}
	
	public URI discoveryURI() {
		return URI.create(properties.getProperty("discoveryURI"));
	}
        
	public String sparqlQueryForCodelists() {
		return properties.getProperty("sparqlQueryForCodelists");
	}
	public String sparqlQueryForCodelist(String codelistUri) {
		String p = properties.getProperty("sparqlQueryForCodelist");
                return  p.replace("cl_uri", codelistUri);
	}
}
