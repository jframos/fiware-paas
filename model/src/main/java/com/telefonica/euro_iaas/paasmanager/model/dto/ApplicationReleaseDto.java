package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO for application Instance to receive rest request
 * @author  Jesus M. Movilla
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationReleaseDto {

    private String applicationName;
    private String version;
    private List<Artifact> artifacts;
    private String applicationType;

    /**
     */
    public ApplicationReleaseDto() {
    }

	/**
	 * @param applicationName
	 * @param version
	 * @param attributes
	 * @param artifacts
	 * @param environmentInstanceName
	 */
	public ApplicationReleaseDto(String applicationName, String version,
			 List<Artifact> artifacts,
			 String applicationType) {
		this.applicationName = applicationName;
		this.version = version;
		this.artifacts = artifacts;
		this.applicationType = applicationType;
	}
	
	public ApplicationReleaseDto(String applicationName, String version,
			 List<Artifact> artifacts) {
		this.applicationName = applicationName;
		this.version = version;
		this.artifacts = artifacts;

	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	 * @return the artifacts
	 */
	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	/**
	 * @param artifacts the artifacts to set
	 */
	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	
	/**
	 * @return the applicationType
	 */
	public String getApplicationType() {
		return applicationType;
	}

	/**
	 * @param applicationType the applicationType to set
	 */
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

}

