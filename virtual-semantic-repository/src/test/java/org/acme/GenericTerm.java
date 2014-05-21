/**
 * (c) 2014 FAO / UN (project: virtual-semantic-repository)
 */
package org.acme;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 16 Apr 2014   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 16 Apr 2014
 */
@XmlRootElement(name="GenericTerm")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericTerm implements Serializable {
	/** Field serialVersionUID */
	private static final long serialVersionUID = 2994599313026400869L;
	
	@XmlElement(name="Description")
	private String _description;
	
	/**
	 * Class constructor
	 *
	 */
	public GenericTerm() {
	}

	/**
	 * Class constructor
	 *
	 * @param description
	 */
	public GenericTerm(String description) {
		super();
		this._description = description;
	}

	final static public GenericTerm describing(String data) {
		return new GenericTerm(data);
	}
	
	/**
	 * @return the 'description' value
	 */
	public String getDescription() {
		return this._description;
	}

	/**
	 * @param description the 'description' value to set
	 */
	public void setDescription(String description) {
		this._description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this._description == null) ? 0 : this._description
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericTerm other = (GenericTerm) obj;
		if (this._description == null) {
			if (other._description != null)
				return false;
		} else if (!this._description.equals(other._description))
			return false;
		return true;
	}
}
