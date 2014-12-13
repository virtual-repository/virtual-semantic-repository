package org.virtual.sr.transforms;

import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.virtualrepository.Asset;

public class GeoFeatureStream2Xml implements XmlTransform<InputStream> {
	
	@Override
	public Source toXml(InputStream stream, Asset asset) throws JAXBException {
		
		return new StreamSource(stream);
	}
	
	@Override
	public Class<InputStream> api() {
		return InputStream.class;
	}

	@Override
	public String type() {
		return "geofeature stream";
	}
}
