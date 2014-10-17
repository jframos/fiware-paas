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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO for application Instance to receive rest request.
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
                                  List<Artifact> artifacts, String environmentInstanceName) {
        this.applicationName = applicationName;
        this.version = version;
        this.attributes = attributes;
        this.artifacts = artifacts;
        this.environmentInstanceName = environmentInstanceName;
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
     * @return the environmentInstanceName
     */
    public String getEnvironmentInstanceName() {
        return environmentInstanceName;
    }

    /**
     * @param environmentInstanceName the environmentInstanceName to set
     */
    public void setEnvironmentInstanceName(String environmentInstanceName) {
        this.environmentInstanceName = environmentInstanceName;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ApplicationInstanceDto]");
        sb.append("[applicationName = ").append(this.applicationName).append("]");
        sb.append("[version = ").append(this.version).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("[artifacts = ").append(this.artifacts).append("]");
        sb.append("[environmentInstanceName = ").append(this.environmentInstanceName).append("]");
        sb.append("]");
        return sb.toString();
    }


}
