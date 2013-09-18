package com.telefonica.euro_iaas.paasmanager.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Represents an artifact to be installed on a ProductRelease
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */

@Entity
public class Artifact {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;
    
	@Column(unique=true, nullable=false, length=256)
	private String name;
	@Column(length=2048)
	private String path;
	
	@ManyToOne
	private ArtifactType artifactType;
	
	//productrelease.id?
	@ManyToOne
	private ProductRelease productRelease;

	/**
	 * @param name
	 * @param path
	 * @param artifactType
	 * @param productRelease
	 */
	public Artifact(String name, String path, ArtifactType artifactType,
			ProductRelease productRelease) {
		this.name = name;
		this.path = path;
		this.artifactType = artifactType;
		this.productRelease = productRelease;
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the artifactType
	 */
	public ArtifactType getArtifactType() {
		return artifactType;
	}

	/**
	 * @param artifactType the artifactType to set
	 */
	public void setArtifactType(ArtifactType artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * @return the productRelease
	 */
	public ProductRelease getProductRelease() {
		return productRelease;
	}

	/**
	 * @param productRelease the productRelease to set
	 */
	public void setProductRelease(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}

    
}
