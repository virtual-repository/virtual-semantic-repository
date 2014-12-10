package org.virtual.sr.transforms;

import static java.lang.String.*;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class Csv2Xml implements XmlTransform<Table> {
	
	@Override
	public Source toXml(Table table, Asset asset) throws JAXBException {
		
		
		String xtable = "";
		//write
		List<Column> cols = table.columns();
		
		for (Row row : table) {
			
			String cells = "";
			
			for  (Column col : cols) {
				String val =  row.get(col);
				if (val!=null)
					cells = cells+ format("<%1$s>%2$s</%1$s>",col.name().toString(),val);
			}
			
			xtable = xtable + format("<row>%s</row>",cells);
		}
		
		
		String xml = format("<?xml version='1.0'?><table>%s</table>",xtable);
		
		return new StreamSource(new StringReader(xml));
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
