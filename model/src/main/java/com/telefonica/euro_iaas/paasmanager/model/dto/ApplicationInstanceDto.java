/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO for application Instance to receive rest request
 * 
 * @author Jesus M. Movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstanceDto {

    private String applicationName;
    private String version;
    private List<Attribute> attributes;
    private List<Artifact> artifacts;
    private String environmentInstanceName;
    private String applicationType;

    /**
     */
    public ApplicationInstanceDto() {
    }

    /**
     * @param applicationName
     * @param version
     * @param attributes
     * @param artifacts
     * @param environmentInstanceName
     */
    public ApplicationInstanceDto(String applicationName, String version, List<Attribute> attributes,
            List<Artifact> artifacts, String environmentInstanceName, String applicationType) {
        this.applicationName = applicationName;
        this.version = version;
        this.attributes = attributes;
        this.artifacts = artifacts;
        this.environmentInstanceName = environmentInstanceName;
        this.applicationType = applicationType;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName
     *            the applicationName to set
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
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the artifacts
     */
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    /**
     * @param artifacts
     *            the artifacts to set
     */
    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * @return the environmentInstanceName
     */
    public String getEnvironmentInstanceName() {
        return environmentInstanceName;
    }

    /**
     * @param environmentInstanceName
     *            the environmentInstanceName to set
     */
    public void setEnvironmentInstanceName(String environmentInstanceName) {
        this.environmentInstanceName = environmentInstanceName;
    }

    /**
     * @return the applicationType
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * @param applicationType
     *            the applicationType to set
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

}
