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

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * An application Release is a concrete version of a given application.
 *
 * @author Jesus M. Movilla
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationRelease {

    @Id
    @XmlTransient
    private String id;

    @Column(nullable = false, length = 256)
    private String name;
    @Column(nullable = false, length = 256)
    private String version;

    @Column(length = 2048)
    private String description;


    @XmlTransient
    @ManyToMany
    private List<ApplicationRelease> transitableReleases;

    @ManyToMany
    private List<Artifact> artifacts;

    /**
     * Default constructor.
     */
    public ApplicationRelease() {
    }

    /**
     * Constructor.
     *
     * @param name
     * @param version
     */
    public ApplicationRelease(String name, String version) {
        this.id = name + "-" + version;
        this.name = name;
        this.version = version;
    }

    /**
     * @param id
     * @param name
     * @param version
     * @param description
     * @param applicationType
     * @param attributes
     * @param transitableReleases
     * @param artifacts
     */
    public ApplicationRelease(String name, String version, String description,
                              List<ApplicationRelease> transitableReleases, List<Artifact> artifacts) {
        this.id = name + "-" + version;
        this.name = name;
        this.version = version;
        this.description = description;
        this.artifacts = artifacts;
        this.transitableReleases = transitableReleases;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApplicationRelease other = (ApplicationRelease) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * @return the artifacts
     */
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the transitableReleases
     */
    public List<ApplicationRelease> getTransitableReleases() {
        return transitableReleases;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    /**
     * @param artifacts the artifacts to set
     */
    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param transitableReleases the transitableReleases to set
     */
    public void setTransitableReleases(List<ApplicationRelease> transitableReleases) {
        this.transitableReleases = transitableReleases;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ApplicationRelease]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[version = ").append(this.version).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("[transitableReleases = ").append(this.transitableReleases).append("]");
        sb.append("[artifacts = ").append(this.artifacts).append("]");
        sb.append("]");
        return sb.toString();
    }


}
