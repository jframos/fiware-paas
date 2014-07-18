/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
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

        return applicationRelease;
    }
    
    private List<Artifact> convertToArtifact(List<ArtifactDto> artifactsDto) {
        List<Artifact> artifacts = new ArrayList<Artifact>();

        for (ArtifactDto artifactDto : artifactsDto) {
            artifacts.add(artifactDto.fromDto());

        }
        return artifacts;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString() {
       StringBuilder sb = new StringBuilder("[[ApplicationReleaseDto]");
       sb.append("[applicationName = ").append(this.applicationName).append("]");
       sb.append("[version = ").append(this.version).append("]");
       sb.append("[artifactsDto = ").append(this.artifactsDto).append("]");
       sb.append("[applicationType = ").append(this.applicationType).append("]");
       sb.append("]");
       return sb.toString();
    }
    
    

}
