package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.OS;


/**
 * A product release is a concrete version of a given product.
 * @author Jesus M. Movilla
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRelease  {

	@Id
	@XmlTransient
	private String id;
	    
	@Column(nullable=false, length=256)
	private String name;
	@Column(nullable=false, length=256)
	private String version;	  
	  
	@Column(length=2048)
	private String description;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Attribute> attributes;
	  
	@XmlTransient
	@ManyToMany
	private List<ProductRelease> transitableReleases;

    @ManyToMany
    private List<OS> supportedOOSS;
    
    private Boolean withArtifact;
    
    @ManyToOne
    private ProductType productType;
	
    /**
	 */
	public ProductRelease() {
	}
	
    /**
	 * @param name
	 * @param version
	 */
	public ProductRelease(String name, String version) {
		this.id =name + "-" + version;
		this.name = name;
		this.version = version;
	}
	
	/**
	 * @param name
	 * @param version
	 * @param description
	 * @param attributes
	 * @param transitableReleases
	 * @param supportedOOSS
	 * @param withArtifact
	 * @param productType
	 */
	public ProductRelease(String name, String version, String description,
			List<Attribute> attributes,
			List<ProductRelease> transitableReleases, List<OS> supportedOOSS,
			Boolean withArtifact, ProductType productType) {
		this.id =name + "-" + version;
		this.name = name;
		this.version = version;
		this.description = description;
		this.attributes = attributes;
		this.transitableReleases = transitableReleases;
		this.supportedOOSS = supportedOOSS;
		this.withArtifact = withArtifact;
		this.productType = productType;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the transitableReleases
	 */
	public List<ProductRelease> getTransitableReleases() {
		return transitableReleases;
	}

	/**
	 * @param transitableReleases the transitableReleases to set
	 */
	public void setTransitableReleases(List<ProductRelease> transitableReleases) {
		this.transitableReleases = transitableReleases;
	}

	/**
     * Add a transitable release.
     * @param transitableRelease the new release.
     */
    public void addTransitableRelease(ProductRelease transitableRelease) {
        if (transitableReleases == null) {
            transitableReleases = new ArrayList<ProductRelease>();
        }
        transitableReleases.add(transitableRelease);
    }
    
	/**
	 * @return the supportedOOSS
	 */
	public List<OS> getSupportedOOSS() {
		return supportedOOSS;
	}

	/**
	 * @param supportedOOSS the supportedOOSS to set
	 */
	public void setSupportedOOSS(List<OS> supportedOOSS) {
		this.supportedOOSS = supportedOOSS;
	}

	/**
	 * @return the withArtifact
	 */
	public Boolean getWithArtifact() {
		return withArtifact;
	}

	/**
	 * @param withArtifact the withArtifact to set
	 */
	public void setWithArtifact(Boolean withArtifact) {
		this.withArtifact = withArtifact;
	}

	/**
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRelease other = (ProductRelease) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
}
