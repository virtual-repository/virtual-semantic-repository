package org.virtual.sr.transforms;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.fao.fi.comet.mapping.model.MappingData;
import org.virtualrepository.Asset;

public class Comet2Xml implements XmlTransform<MappingData> {
	
	private JAXBContext _ctx;

	public Comet2Xml() {
		
		try {
			this._ctx = JAXBContext.newInstance(MappingData.class);
		} catch (JAXBException ex) {
			throw new RuntimeException("Failed JAXB initialization", ex);
		}
	}

	@Override
	public Source toXml(MappingData bean, Asset asset) throws JAXBException {
		StringWriter sw = new StringWriter();

		this._ctx.createMarshaller().marshal(bean, sw);
		
		return new StreamSource(new StringReader(sw.toString()));
	}
	
	public String emitXml(MappingData bean, Asset asset) throws JAXBException {
		StringWriter sw = new StringWriter();

		this._ctx.createMarshaller().marshal(bean, sw);
		
		return sw.toString();
	}

	@Override
	public Class<MappingData> api() {
		return MappingData.class;
	}

	@Override
	public String name() {
		return "mapping";
	}
}
