package org.virtual.sr.transforms;

import static javax.xml.stream.XMLStreamConstants.*;

import java.net.URI;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Converts XML data into RDF data.
 * 
 * @author Fabio Simeoni
 *
 */
public class Xml2Rdf {

	private static final String pseudoNS = "s:r/";

	static XMLInputFactory factory = XMLInputFactory.newInstance(); 
	
	private final String assetType;
	private XMLStreamReader reader;
	private Model model;
	
	static {
		factory.setProperty(XMLInputFactory.IS_COALESCING, true);
	}
	
	public Xml2Rdf(String assetType) {
		this.assetType=assetType;
	}
	
	/**
	 * Converts XML data into RDF data.
	 * @param xml the XML data
	 * @return the RDF conversion
	 * @throws Exception if the data cannot not be converted
	 */
	public Model triplify(Source xml) throws Exception {
		
		try {
			
			reader = factory.createXMLStreamReader(xml); 
			
			model = ModelFactory.createDefaultModel();
			
			mapRoot();
			
		}
		catch(Exception e) {
			throw new Exception("cannot triplify xml source (see cause) ",e);
		}
		
		return model;
	}
	
	
	//helpers
	
	private void mapRoot() throws Exception {
		
		reader.nextTag(); //move to root
		
		mapComplex(null);
	}
	
	private URI mapComplex(URI parentUri) throws Exception {
		
		QName name = reader.getName();
		URI uri = mint(name);
		
		if (parentUri==null) //root case
			parentUri = uri; 
		
		boolean complex = false;
		
		for  (int i = 0;i< reader.getAttributeCount();i++) {
			complex=true;
			emit(uri,reader.getAttributeName(i),reader.getAttributeValue(i));
		}
		
		loop: while (reader.hasNext()) {
			
			int next = reader.next();
			
			switch (next) {
				
				case START_ELEMENT: //child of this element
					
					QName childName = reader.getName();
					URI childURI = mapComplex(uri); //recursion
		            
					if (childURI!=null)
		            	emit(uri,childName,childURI); //link parent to child
					
					complex=true;
					
					break;
				
				case CHARACTERS: //content of this element
					
					if (!reader.getText().trim().isEmpty())
						emit(complex?uri:parentUri,name,reader.getText());
					
					break;
				
				case END_ELEMENT: //end of this element
					break loop;
			}
		}
		
		return complex?uri:null;
	}
	
	void emit(URI source,QName predicate,String literal) {
		//System.out.println(source+"->"+predicate+"->"+literal);
		Resource s = model.createResource(source.toString());
		Property p = model.createProperty(pseudoNS,predicate.getLocalPart());
		model.add(s,p,literal);
	}
	
	void emit(URI source,QName predicate,URI target) {
		//System.out.println(source+"->"+predicate+"->"+target);
		Resource s = model.createResource(source.toString());
		Property p = model.createProperty(pseudoNS,predicate.getLocalPart());
		Resource o = model.createResource(target.toString());
		model.add(s, p, o);
	}
	
	URI mint(QName name) throws Exception {
		return URI.create(pseudoNS+name.getLocalPart()+"/"+UUID.randomUUID());
	}
	
}
