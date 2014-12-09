package org.virtual.sr.transforms;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class Csv2Xml implements XmlTransform<Table> {
	
	@XmlRootElement(name="table")
	static class XmlTable {
		
		@XmlElement(name="row")
		List<XmlRow> rows = new ArrayList<XmlRow>();
		
	}
	
	static class XmlRow {
		
		@XmlElement(name="cell")
		List<XmlCell> cells = new ArrayList<XmlCell>();
		
	}
	
	
	static class XmlCell {
		
		public XmlCell(String col, String val) {
			this.col=col;
			this.val=val;
		}
		@XmlAttribute
		String col;
		
		@XmlAttribute
		String val;
		
	}
	
	private JAXBContext _ctx;

	public Csv2Xml() {
		
		try {
			this._ctx = JAXBContext.newInstance(XmlTable.class);
		} catch (JAXBException ex) {
			throw new RuntimeException("Failed JAXB initialization", ex);
		}
	}

	@Override
	public Source toXml(Table table, Asset asset) throws JAXBException {
		
		XmlTable xtable = new XmlTable();
		
		//write
		List<Column> cols = table.columns();
		
		for (Row row : table) {
			
			XmlRow xrow= new XmlRow();
			
			for  (Column col : cols) {
				String val =  row.get(col);
				if (val!=null)
					xrow.cells.add(new XmlCell(col.name().toString(),val));
			}
			
			xtable.rows.add(xrow);
		}
		
		StringWriter sw = new StringWriter();

		this._ctx.createMarshaller().marshal(xtable, sw);
		
		return new StreamSource(new StringReader(sw.toString()));
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
