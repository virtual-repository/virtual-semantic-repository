package org.virtual.sr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.rdf.model.Model;

@SuppressWarnings("unused")
public class SdmxPublisher implements Publisher<SdmxCodelist,CodelistBean> {

	private final RepositoryConfiguration configuration;
	
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

		
		Xml2Rdf converter = new Xml2Rdf();
		
		Source source = null;
		
		try {
			source = xmlOf(content);
		}
		catch(Exception e) {
			throw new Exception("cannot serialise SDMX codelist "+content.getId()+" to XML (see cause)",e);
		}
		
		Model rdf = converter.triplify(source);
		
		
		//TODO send RDF to endpoint in configuration
	}

	
	//helpers
	
	Source xmlOf(CodelistBean bean) {

		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = new StructureWritingManagerImpl();
		manager.writeStructures(beans,format, stream);
		
		return new StreamSource(new ByteArrayInputStream(stream.toByteArray()));
	}
}
