package org.virtual.sr;

import static java.util.Collections.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Plugin;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;

public class RepositoryPlugin implements Plugin, ServiceProxy {

	public static QName name = new QName("semantic-repository");
	
	private final RepositoryBrowser browser = new RepositoryBrowser();
	private final List<SdmxPublisher> publishers = Arrays.asList(new SdmxPublisher());
	
	@Override
	public Collection<RepositoryService> services() {
		return singleton(new RepositoryService(name, this));
	}

	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		return emptyList();
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return publishers;
	}

}
