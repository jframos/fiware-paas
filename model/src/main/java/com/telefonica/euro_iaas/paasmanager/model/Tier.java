package com.telefonica.euro_iaas.paasmanager.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


/**
 * Represents an artifact to be installed on a ProductRelease
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
public class Tier {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(nullable=false, length=256)
	private String name;
	
	private Integer maximum_number_instances;
	private Integer minimum_number_instances;
	private Integer initial_number_instances;
	
	//@ManyToOne
	//private Service service;
	
	@ManyToMany	
	private List<ProductRelease> productReleases;

	/**
	 * Default Constructor
	 */
	public Tier() {
	}

	/**
	 * @param name
	 * @param maximum_number_instances
	 * @param minimum_number_instances
	 * @param initial_number_instances
	 * @param productReleases
	 */
	public Tier(String name, Integer maximum_number_instances,
			Integer minimum_number_instances, Integer initial_number_instances,
			List<ProductRelease> productReleases) {
		this.name = name;
		this.maximum_number_instances = maximum_number_instances;
		this.minimum_number_instances = minimum_number_instances;
		this.initial_number_instances = initial_number_instances;
		this.productReleases = productReleases;
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
	 * @return the maximum_number_instances
	 */
	public Integer getMaximum_number_instances() {
		return maximum_number_instances;
	}

	/**
	 * @return the minimum_number_instances
	 */
	public Integer getMinimum_number_instances() {
		return minimum_number_instances;
	}

	/**
	 * @return the initial_number_instances
	 */
	public Integer getInitial_number_instances() {
		return initial_number_instances;
	}

	/**
	 * @return the productReleases
	 */
	public List<ProductRelease> getProductReleases() {
		return productReleases;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param maximum_number_instances the maximum_number_instances to set
	 */
	public void setMaximum_number_instances(Integer maximum_number_instances) {
		this.maximum_number_instances = maximum_number_instances;
	}

	/**
	 * @param minimum_number_instances the minimum_number_instances to set
	 */
	public void setMinimum_number_instances(Integer minimum_number_instances) {
		this.minimum_number_instances = minimum_number_instances;
	}

	/**
	 * @param initial_number_instances the initial_number_instances to set
	 */
	public void setInitial_number_instances(Integer initial_number_instances) {
		this.initial_number_instances = initial_number_instances;
	}

	/**
	 * @param productReleases the productReleases to set
	 */
	public void setProductReleases(List<ProductRelease> productReleases) {
		this.productReleases = productReleases;
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
		Tier other = (Tier) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

    
}
