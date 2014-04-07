package org.acme;

import static org.sdmx.SdmxServiceFactory.*;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtual.sr.RepositoryPlugin;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;


public class PublishIntegrationTests {

	@BeforeClass
	public static void setup() {
		
		System.setProperty("org.slf4j.simpleLogger.log.org.virtual.sr ","trace");
	}
	
	@Test
	public void publishSdmxCodelist() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("mini-asfis.xml");
		
		CodelistBean list = parser().parseStructures(new ReadableDataLocationTmp(stream)).
										getStructureBeans(false).getCodelists().iterator().next();
		
		VirtualRepository repo = new Repository();
		
		RepositoryService service = repo.services().lookup(RepositoryPlugin.name);
		
		SdmxCodelist asset = new SdmxCodelist("test", service);
		
		repo.publish(asset,list);
                
		
	}
	
	@Test
	public void another() {
		
		SdmxServiceFactory.init();
		
		//start vr
		VirtualRepository repo = new Repository();
		
		//discover codelists in sdmx
		repo.discover(SdmxCodelist.type);

		//get first discovered codelist
		Iterator<Asset> it = repo.iterator();
		
		it.next();
		
		Asset discoveredAsset = it.next();
		
		//retrieve its content
		CodelistBean sdmx = repo.retrieve(discoveredAsset,CodelistBean.class);
		
		//get virtual semantic repo 
		RepositoryService service = repo.services().lookup(RepositoryPlugin.name);
		
		//create asset for publication
		SdmxCodelist publishAsset = new SdmxCodelist("test", service);
		
		//public sdmx with virtual repo
		repo.publish(publishAsset,sdmx);
		
	
	}
}
