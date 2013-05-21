package org.virtual.sr;

import static org.virtualrepository.Utils.*;

import java.net.URI;
import java.util.Properties;

public class RepositoryConfiguration {

	private final Properties properties;
	
	public RepositoryConfiguration(Properties properties) {
		this.properties=properties;
		
		notNull("configuration", properties);
		notNull("publish URI", properties.getProperty("publishURI"));
		notNull("discovery URI", properties.getProperty("discoveryURI"));
	}
	
	public URI publishURI() {
		return URI.create(properties.getProperty("publishURI"));
	}
	
	public URI discoveryURI() {
		return URI.create(properties.getProperty("discoveryURI"));
	}
        
	public String sparqlQueryForCodelists() {
		return properties.getProperty("sparqlQueryForCodelists");
	}
}
