package com.telefonica.euro_iaas.paasmanager.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Application Type(ex: e-businees, java-spring...
 * @author Jesus M. Movilla
 *
 */

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationType {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(unique=true, nullable=false, length=256)
	private String name;
	  
	@Column(length=2048)
	private String description;
	
	//OneToMany, ManyToMany?
	@OneToMany
	private List<EnvironmentType> environmentTypes;

	/**
	 * @param name
	 * @param description
	 * @param environmentTypes
	 */
	public ApplicationType(String name, String description,
			List<EnvironmentType> environmentTypes) {
		this.name = name;
		this.description = description;
		this.environmentTypes = environmentTypes;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the environmentTypes
	 */
	public List<EnvironmentType> getEnvironmentTypes() {
		return environmentTypes;
	}

	/**
	 * @param environmentTypes the environmentTypes to set
	 */
	public void setEnvironmentTypes(List<EnvironmentType> environmentTypes) {
		this.environmentTypes = environmentTypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ApplicationType other = (ApplicationType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
