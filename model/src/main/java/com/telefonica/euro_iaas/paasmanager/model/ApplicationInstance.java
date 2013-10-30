/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
        this.name = applicationRelease.getName() + "-" + environmentInstance.getName();
    }

}
