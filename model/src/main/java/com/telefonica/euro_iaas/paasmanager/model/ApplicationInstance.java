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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an instance of a application.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstance extends InstallableInstance {

    public static final String VDC_FIELD = "vdc";
    
    public static final String APP_FIELD = "name";
    @ManyToOne
    private ApplicationRelease applicationRelease;

    @ManyToOne
    private EnvironmentInstance environmentInstance;

    /**
     * Default Constructor.
     */
    public ApplicationInstance() {
  
    }

    /**
     * @param applicationRelease
     * @param environmentInstance
     */
    public ApplicationInstance(ApplicationRelease applicationRelease, EnvironmentInstance environmentInstance) {
        this.applicationRelease = applicationRelease;
        this.environmentInstance = environmentInstance;
        this.setVdc(environmentInstance.getVdc());
        setName();
    }

    /**
     * @param applicationRelease
     * @param environmentInstance
     * @param vdc
     */
    public ApplicationInstance(ApplicationRelease applicationRelease, EnvironmentInstance environmentInstance,
            String vdc) {
        this.applicationRelease = applicationRelease;
        this.environmentInstance = environmentInstance;
        this.setVdc(vdc);
        setName();
    }

    /**
     * @param applicationRelease
     * @param environmentInstance
     * @param vdc
     * @param status
     */
    public ApplicationInstance(ApplicationRelease applicationRelease, EnvironmentInstance environmentInstance,
            String vdc, Status status) {
        this.applicationRelease = applicationRelease;
        this.environmentInstance = environmentInstance;
        this.setVdc(vdc);
        setName();
        this.setStatus(status);
    }

    /**
     * @return the applicationRelease
     */
    public ApplicationRelease getApplicationRelease() {
        return applicationRelease;
    }

    /**
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    /**
     * @param applicationRelease
     *            the applicationRelease to set
     */
    public void setApplicationRelease(ApplicationRelease applicationRelease) {
        this.applicationRelease = applicationRelease;
    }

    /**
     * @param environmentInstance
     *            the environmentInstance to set
     */
    public void setEnvironmentInstance(EnvironmentInstance environmentInstance) {
        this.environmentInstance = environmentInstance;
    }

    /**
     * setting the name as fuction of applicationRelease.name and environmentInstance.name.
     */
    private void setName() {
        this.name = applicationRelease.getName() + "-" + environmentInstance.getBlueprintName();
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString() {
       StringBuilder sb = new StringBuilder("[[ApplicationInstance]");
       sb.append("[applicationRelease = ").append(this.applicationRelease).append("]");
       sb.append("[environmentInstance = ").append(this.environmentInstance).append("]");
       sb.append("]");
       return sb.toString();
    }
    
    

}
