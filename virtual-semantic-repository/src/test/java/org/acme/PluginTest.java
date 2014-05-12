package org.acme;

import static org.junit.Assert.*;

import org.junit.Test;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.impl.Services;


public class PluginTest {

	
	@Test
	public void pluginLoads() {
		
		Services repos = new Services();
		repos.load();
		
		for (RepositoryService service : repos)
			System.out.println(service.publishedTypes());
		
		assertTrue(repos.size()>0);
		
	}
}
