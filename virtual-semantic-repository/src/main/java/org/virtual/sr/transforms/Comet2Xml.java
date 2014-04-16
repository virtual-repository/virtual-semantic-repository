package org.virtual.sr.transforms;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.fao.fi.comet.vr.model.MappingData;
import org.virtualrepository.Asset;

@SuppressWarnings("rawtypes")
public class Comet2Xml implements XmlTransform<MappingData> {
	@SuppressWarnings("unused")
	private Class<?>[] _classes;

	private JAXBContext _ctx;

	public Comet2Xml() {
		this((Class<?>[])null);
	}
	
	@Deprecated
	public Comet2Xml(Class<?>... classes) {
		this._classes = classes;

		List<Class<?>> allClasses = new ArrayList<Class<?>>(Arrays.asList(classes == null ? new Class<?>[0] : classes));
		allClasses.add(MappingData.class);

		try {
			this._ctx = JAXBContext.newInstance(allClasses
					.toArray(new Class<?>[allClasses.size()]));
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
	public String type() {
		return "mapping";
	}
}
