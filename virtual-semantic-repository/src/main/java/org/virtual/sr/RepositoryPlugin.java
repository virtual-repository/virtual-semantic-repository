package org.virtual.sr;

import static java.util.Collections.*;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Plugin;

public class RepositoryPlugin implements Plugin {

	public static QName name = new QName("semantic-repository");
	
	
	@Override
	public Collection<RepositoryService> services() {
		return singleton(new RepositoryService(name, new RepositoryProxy()));
	}


}
