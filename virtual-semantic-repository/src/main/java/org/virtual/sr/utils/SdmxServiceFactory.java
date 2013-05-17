package org.virtual.sr.utils;

import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * A factory of SDMX services.
 * <p>
 * This is a utility class that:
 * 
 * <ul>
 * 	<li> bootstraps the Spring container required by MetadataTechnology SdmxSource, without any XML configuration
 *  <li> makes available SdmxSource services from the Spring container, to DI-agnostic clients as well as CDI-enabled clients 
 * </ul>
 *  
 *  
 * @author Fabio Simeoni
 *
 */
public class SdmxServiceFactory {

	//single instance injected by Spring via programmatic contanier configuration
	private static SdmxServiceFactory instance;
	
	//upon first access to this factory, Spring is configured programmatically
	static {
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/sdmxsource-context.xml");
		
		//fetch singleton instance of this factory
		instance=ctx.getBean(SdmxServiceFactory.class);
	}
	
	//no other instances available to clients
	SdmxServiceFactory() {}
	
	@Autowired
	StructureParsingManager parser; 
	
	@Autowired
	StructureWritingManager writer; 
	
	//add more services if and when needed...
	
	
	//factory methods for generic clients and CDI clients
	
	/**
	 * Returns a {@link StructureParsingManager} service.
	 * @return the service
	 */
	public static StructureParsingManager parser() {
		return instance.parser;
	}
	
	/**
	 * Returns a {@link StructureWritingManager} service.
	 * @return the service
	 */
	public static StructureWritingManager writer() {
		return instance.writer;
	}
}
