package org.virtual.sr.transforms;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class Csv2Xml implements XmlTransform<Table> {
	
	XMLOutputFactory factory = XMLOutputFactory.newInstance();
	
	@Override
	public Source toXml(Table table, Asset asset) throws Exception {
		
		StringWriter writer = new StringWriter();
		
		XMLStreamWriter xml = factory.createXMLStreamWriter(writer);
		
		xml.writeStartDocument();
		xml.writeStartElement("table");
		
		//write
		List<Column> cols = table.columns();
		
		for (Row row : table) {
			
			xml.writeStartElement("row");
			
			for  (Column col : cols) {
				String val =  row.get(col);
				if (val!=null) {
					xml.writeStartElement(col.name().toString());
					xml.writeCharacters(val);
					xml.writeEndElement();
				}
			}
			
			xml.writeEndElement();
			
		}
		
		xml.writeEndElement();
		xml.writeEndDocument();;
		
		return new StreamSource(new StringReader(writer.toString()));
	}

	@Override
	public Class<Table> api() {
		return Table.class;
	}

	@Override
	public String type() {
		return "csv";
	}
}
