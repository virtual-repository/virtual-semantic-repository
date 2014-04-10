package org.virtual.sr.transforms;

import javax.xml.transform.Source;

/**
 * Transforms data of a given type to XML
 * @author Fabio Simeoni
 *
 * @param <T> the type of the data to transform
 */
public interface XmlTransform<T> {

	/**
	 * Transforms data of a given type to XML.
	 * 
	 * @param data the data to transform
	 * @return the transformed data
	 */
	Source toXml(T data) throws Exception;
	
	/**
	 * Returns the type of the data to transform.
	 * @return the type
	 */
	Class<T> api();
	
	/**
	 * Returns a name for the transformed type.
	 * @return the name
	 */
	String type();
}
