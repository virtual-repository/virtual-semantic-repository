package org.virtual.sr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtual.sr.utils.SdmxServiceFactory;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Transform;

import com.hp.hpl.jena.rdf.model.Model;

public class SdmxCodelistTransform implements Transform<SdmxCodelist,CodelistBean,Model> {

	@Override
	public Model apply(SdmxCodelist asset, CodelistBean content) throws Exception {
		
		Xml2Rdf converter = new Xml2Rdf();
		
		Source source = null;
		
		try {
			source = xmlOf(content);
		}
		catch(Exception e) {
			throw new Exception("cannot transform SDMX codelist "+content.getId()+" to XML (see cause)",e);
		}
		
		return converter.triplify(source);
	}

	@Override
	public Class<CodelistBean> inputAPI() {
		return CodelistBean.class;
	}

	@Override
	public Class<Model> outputAPI() {
		return Model.class;
	}

	
	//helpers
	
	private Source xmlOf(CodelistBean bean) {

		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = SdmxServiceFactory.writer();
		manager.writeStructures(beans,format, stream);
		
		return new StreamSource(new ByteArrayInputStream(stream.toByteArray()));
	}
}
