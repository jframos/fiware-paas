package com.telefonica.euro_iaas.paasmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.json.JSONObject;

/**
 * Defines an attribute which can be configured
 * 
 * @author Jesus M. Movilla
 * 
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlTransient
	private Long id;

	@SuppressWarnings("unused")
	@Version
	@XmlTransient
	private Long v;

	/** the attribute key */
	@Column(nullable = false, length = 256)
	private String key;
	/** the attribute value */
	@Column(nullable = false, length = 2048)
	private String value;
	/* the description of that attribute* */
	@Column(nullable = true, length = 2048)
	private String description;

	/**
     */
	public Metadata() {
	}

	/**
	 * @param key
	 * @param value
	 */
	public Metadata(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @param key
	 * @param value
	 * @param description
	 */
	public Metadata(String key, String value, String description) {
		this.key = key;
		this.value = value;
		this.description = description;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the id.
	 */
	public Long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Metadata other = (Metadata) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public void fromJson(JSONObject jsonNode) {
		key = jsonNode.getString("key");
		value = jsonNode.getString("value");
		
		if (jsonNode.containsKey("description"))
			description = jsonNode.getString("description");
	}
}