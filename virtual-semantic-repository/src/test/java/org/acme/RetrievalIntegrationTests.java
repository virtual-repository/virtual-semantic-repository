package org.acme;

import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtual.sr.utils.SdmxServiceFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;


public class RetrievalIntegrationTests {

	@Test
	public void retrieveSdmxCodelists() {
		
		SdmxServiceFactory.init();
		
		VirtualRepository repo = new Repository();
		
		repo.discover(SdmxCodelist.type);
		
		Asset codelist = repo.iterator().next();
		
		CodelistBean content = repo.retrieve(codelist,CodelistBean.class);
		
		System.out.println(content);
	}
}
