package com.telefonica.euro_iaas.paasmanager.model.dto;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;




/**
 * Represents an artifact to be installed on a ProductRelease
 *
 * @author Henar Muñoz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierDto {

	
	private String name;
	private String flavour;
	private String image;
	
	private Integer maximum_number_instances;
	private Integer minimum_number_instances;
	private Integer initial_number_instances;
	
	private List<ProductReleaseDto> productReleaseDtos;


	/**
	 * Default Constructor
	 */
	public TierDto() {
	}

	/**
	 * @param name
	 * @param maximum_number_instances
	 * @param minimum_number_instances
	 * @param initial_number_instances
	 * @param productReleases
	 */
	public TierDto(String name, Integer maximum_number_instances,
			Integer minimum_number_instances, Integer initial_number_instances,
			List<ProductReleaseDto> productReleaseDtos) {
		this.name = name;
		this.maximum_number_instances = maximum_number_instances;
		this.minimum_number_instances = minimum_number_instances;
		this.initial_number_instances = initial_number_instances;
		this.productReleaseDtos = productReleaseDtos;
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
	public List<ProductReleaseDto> getProductReleaseDtos() {
		return productReleaseDtos;
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
	 * @param name the name to set
	 */
	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}
	
	public String getFlavour() {
		return flavour;
	}
	
	public void setImage (String image) {
		this.image = image;
	}
	
	public String getImage() {
		return this.image;
	}


	/**
	 * @param productReleases the productReleases to set
	 */
	public void setProductReleaseDtos(List<ProductReleaseDto> productReleaseDtos) {
		this.productReleaseDtos = productReleaseDtos;
	}
	
	public void addProductRelease(ProductReleaseDto productReleaseDto) {
		if (this.productReleaseDtos == null)
			productReleaseDtos = new ArrayList ();
		
		productReleaseDtos.add (productReleaseDto);
	}
	
	public void removeProductRelease(ProductReleaseDto productReleaseDto) {
		
		productReleaseDtos.remove(productReleaseDto);
	}
	
 
	
	

	
	
	

    
}
