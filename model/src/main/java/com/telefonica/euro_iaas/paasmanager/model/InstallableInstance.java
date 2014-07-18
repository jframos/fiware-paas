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

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * InstallableInstance represents an instance of an execution unit that is (or has been) installed.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("restriction")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InstallableInstance {

    /**
     * Defines the value of the different status the Application could be. See the diagram bellow to know the relations
     * between the different states. <img src= >
     */
    public enum Status {
        INIT,
        DEPLOYING,
        DEPLOYED,
        UNDEPLOYED,
        UNDEPLOYING,
        INSTALLING,
        INSTALLED,
        ERROR,
        UNINSTALLING,
        UNINSTALLED,
        CONFIGURING,
        UPGRADING,
        DEPLOYING_ARTEFACT,
        UNDEPLOYING_ARTEFACT,
        ARTEFACT_DEPLOYED,
        ARTEFACT_UNDEPLOYED
    }

    public static final String VM_FIELD = "vm";
    public static final String STATUS_FIELD = "status";
    public static final String VDC_FIELD = "vdc";

    public static final String ENVIRONMENT_INSTANCE_FIELD = "environmentinstance";;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private Date date;

    @Column(nullable = false, length = 256)
    protected String name;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INIT;

    private String vdc;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Attribute> privateAttributes;

    /**
     * Constructor.
     * 
     * @param status
     */
    public InstallableInstance() {
        this.date = new Date();
        this.status = Status.INIT;
    }

    /**
     * Constructor.
     * 
     * @param status
     */
    public InstallableInstance(Long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor.
     * 
     * @param status
     */
    public InstallableInstance(Status status) {
        this();
        this.status = status;
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
        InstallableInstance other = (InstallableInstance) obj;
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
     * @return the date
     */
    public Date getDate() {
        return date;
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
     * @return the privateAttributes
     */
    public Set<Attribute> getPrivateAttributes() {
        return privateAttributes;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
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
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param privateAttributes
     *            the privateAttributes to set
     */
    public void setPrivateAttributes(Set<Attribute> privateAttributes) {
        this.privateAttributes = privateAttributes;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString() {
       StringBuilder sb = new StringBuilder("[[InstallableInstance]");
       sb.append("[id = ").append(this.id).append("]");
       sb.append("[date = ").append(this.date).append("]");
       sb.append("[name = ").append(this.name).append("]");
       sb.append("[status = ").append(this.status).append("]");
       sb.append("[vdc = ").append(this.vdc).append("]");
       sb.append("[privateAttributes = ").append(this.privateAttributes).append("]");
       sb.append("]");
       return sb.toString();
    }

    
    
}
