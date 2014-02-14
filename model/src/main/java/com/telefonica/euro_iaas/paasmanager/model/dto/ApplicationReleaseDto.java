/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;

/**
 * DTO for application Instance to receive rest request
 * 
 * @author Jesus M. Movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationReleaseDto {

    private String applicationName;
    private String version;
    private List<ArtifactDto> artifactsDto;
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
    public ApplicationReleaseDto(String applicationName, String version, List<ArtifactDto> artifactsDto,
            String applicationType) {
        this.applicationName = applicationName;
        this.version = version;
        this.artifactsDto = artifactsDto;
        this.applicationType = applicationType;
    }

    public ApplicationReleaseDto(String applicationName, String version, List<ArtifactDto> artifactsDto) {
        this.applicationName = applicationName;
        this.version = version;
        this.artifactsDto = artifactsDto;

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
     * @return the artifacts
     */
    public List<ArtifactDto> getArtifactsDto() {
        return artifactsDto;
    }

    /**
     * @param artifacts
     *            the artifacts to set
     */
    public void setArtifactsDto(List<ArtifactDto> artifactsDto) {
        this.artifactsDto = artifactsDto;
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
    
    public ApplicationRelease fromDto () {
        ApplicationRelease applicationRelease = new ApplicationRelease ();
        if (this.getApplicationName() != null) {
            applicationRelease.setName(this.getApplicationName());
        }

        if (this.getArtifactsDto() != null) {
            applicationRelease.setArtifacts(this.convertToArtifact(this.getArtifactsDto()));
        }

        if (this.getVersion() != null)
            applicationRelease.setVersion(this.getVersion());

        if (this.getApplicationType() != null) {
            ApplicationType appType = new ApplicationType();
            appType.setName(this.getApplicationType());
            applicationRelease.setApplicationType(appType);
        }
        return applicationRelease;
    }
    
    private List<Artifact> convertToArtifact(List<ArtifactDto> artifactsDto) {
        List<Artifact> artifacts = new ArrayList<Artifact>();

        for (ArtifactDto artifactDto : artifactsDto) {
            artifacts.add(artifactDto.fromDto());

        }
        return artifacts;
    }

}
