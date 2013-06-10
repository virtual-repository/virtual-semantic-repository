package org.acme;

import static org.virtual.sr.utils.SdmxServiceFactory.*;

import java.io.InputStream;

import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtual.sr.RepositoryPlugin;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;


public class PublishIntegrationTests {

	@Test
	public void publishSdmxCodelist() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("asfis.xml");
		
		CodelistBean list = parser().parseStructures(new ReadableDataLocationTmp(stream)).
										getStructureBeans(false).getCodelists().iterator().next();
		
		VirtualRepository repo = new Repository();
		
		RepositoryService service = repo.services().lookup(RepositoryPlugin.name);
		
		SdmxCodelist asset = new SdmxCodelist("test", service);
		
		repo.publish(asset,list);
                
		
	}
}
