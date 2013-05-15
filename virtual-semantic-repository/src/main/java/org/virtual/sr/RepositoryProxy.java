package org.virtual.sr;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;

public class RepositoryProxy implements ServiceProxy, Lifecycle {

	private static final String CONFIGURATION_FILE = "sr.properties";

	private final RepositoryBrowser browser = new RepositoryBrowser();
	private final List<SdmxPublisher> publishers = new ArrayList<SdmxPublisher>();
	private final List<SdmxImporter> importers = new ArrayList<SdmxImporter>();
	
	@Override
	public void init() throws Exception {
	
		Properties properties = new Properties();
		
		try {
			properties.load(RepositoryProxy.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE));
		}
		catch(Exception e) {
			throw new IllegalStateException("missing configuration: configuration file "+CONFIGURATION_FILE+" not on classpath");
		}
		
		RepositoryConfiguration configuration = null;
		try {
			configuration = new RepositoryConfiguration(properties);
		}
		catch(Exception e) {
			throw new IllegalStateException("invalid configuration (see cause) ",e);	
		}
		
		
		publishers.add(new SdmxPublisher(configuration));
		importers.add(new SdmxImporter(configuration));
		
	}
	
	
	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		return importers;
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return publishers;
	}

	
	
}
