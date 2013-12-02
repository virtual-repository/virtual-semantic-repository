package org.acme;

import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;


public class RetrievalIntegrationTests {

	@Test
	public void retrieveSdmxCodelists() {
		
		SdmxServiceFactory.init();
		
		VirtualRepository repo = new Repository();
		
		repo.discover(SdmxCodelist.type);
		
		Asset codelist = repo.iterator().next();
		
		CodelistBean bean = repo.retrieve(codelist,CodelistBean.class);
		
		System.out.println(bean);
	}
	
	@Test
	public void retrieveTableCodelists() {
		
		SdmxServiceFactory.init();
		
		VirtualRepository repo = new Repository();
		
		repo.discover(SdmxCodelist.type);
		
		Asset codelist = repo.iterator().next();
		
		Table table = repo.retrieve(codelist,Table.class);
		
		System.out.println(table.columns());
		for (Row row : table)
			System.out.println(row);
	}
}
