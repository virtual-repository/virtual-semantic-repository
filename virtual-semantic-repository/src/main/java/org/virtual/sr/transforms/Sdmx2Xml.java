package org.virtual.sr.transforms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;

public class Sdmx2Xml implements XmlTransform<CodelistBean> {

	
	@Override
	public Source toXml(CodelistBean bean) {
		
		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = SdmxServiceFactory.writer();
		manager.writeStructures(beans,format, stream);
		
		return new StreamSource(new ByteArrayInputStream(stream.toByteArray()));
	}
	
	@Override
	public Class<CodelistBean> api() {
		return CodelistBean.class;
	}
	
	@Override
	public String type() {
		return "codelist";
	}
}
