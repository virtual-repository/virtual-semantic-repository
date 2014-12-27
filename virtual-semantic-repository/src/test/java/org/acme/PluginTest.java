package org.acme;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.virtual.sr.RepositoryPlugin;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.AbstractType;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.impl.ServiceInspector;
import org.virtualrepository.impl.Type;


public class PluginTest {

	VirtualRepository repo = new Repository();
	
	@Test
	public void loads() {
		
		assertTrue(repo.services().contains(RepositoryPlugin.name));
		
	}
	
	@Test
	public void can_publish_streams_of_any_asset_type() {
		
		RepositoryService service = repo.services().lookup(RepositoryPlugin.name);
		
		Type<Asset> sometype =  new AbstractType<Asset>("sometype") {};
	
		ServiceInspector inspector = new ServiceInspector(service);
		
		assertTrue(inspector.takes(sometype,InputStream.class));
		
	}
	
}
