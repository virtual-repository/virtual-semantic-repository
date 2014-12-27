package org.virtual.sr.transforms;

import javax.xml.transform.Source;

import org.virtualrepository.Asset;

/**
 * Transforms the content of a given asset to XML
 * @author Fabio Simeoni
 *
 * @param <T> the type of the content to transform
 */
public interface XmlTransform<T> {

	/**
	 * Transforms the content of a given asset to XML.
	 * 
	 * @param content the content
	 * @param asset the asset
	 * @return the transformed content
	 */
	Source toXml(T content, Asset asset) throws Exception;
	
	/**
	 * Returns the type of the data to transform.
	 * @return the type
	 */
	Class<T> api();
	
	/**
	 * Returns a name for the transformed type.
	 * @return the name
	 */
	String name();
}
