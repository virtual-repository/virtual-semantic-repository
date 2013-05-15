package org.virtual.sr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.hp.hpl.jena.rdf.model.Model;

@SuppressWarnings("unused")
public class SdmxPublisher implements Publisher<SdmxCodelist,CodelistBean> {

	private final RepositoryConfiguration configuration;
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public SdmxPublisher(RepositoryConfiguration configuration) {
		this.configuration=configuration;
	}
	
	@Override
	public Type<SdmxCodelist> type() {
		return SdmxCodelist.type;
	}

	@Override
	public Class<CodelistBean> api() {
		return CodelistBean.class;
	}

	@Override
	public void publish(SdmxCodelist asset, CodelistBean content) throws Exception {

		//convert content into XML
		Document xml = factory.newDocumentBuilder().parse(xmlOf(content));
		
		RDFConverter converter = new RDFConverter();
		
		Model rdf = converter.convert(xml);
		
		
		//TODO send RDF to endpoint in configuration
	}

	
	//helpers
	
	InputSource xmlOf(CodelistBean bean) {

		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = new StructureWritingManagerImpl();
		manager.writeStructures(beans,format, stream);
		
		return new InputSource(new ByteArrayInputStream(stream.toByteArray()));
	}
}
