package org.virtual.sr.transforms;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


public class Fmf2Xml implements XmlTransform<MappingBean> {

	
	private static JAXBContext cxt;
        
        static { 
            try {
                cxt = JAXBContext.newInstance(MappingBean.class);
            } catch (JAXBException ex) {
                throw new RuntimeException("Failed JAXB initialization", ex);
            }
        }
        
	@Override
	public Source toXml(MappingBean bean) throws JAXBException {
		StringWriter sw = new StringWriter();
		cxt.createMarshaller().marshal(bean, sw);
		StringReader sr = new StringReader(sw.toString());
		return new StreamSource(sr);
	}
	
	@Override
	public Class<MappingBean> api() {
		return MappingBean.class;
	}
	
	@Override
	public String type() {
		return "mapping";
	}
}
