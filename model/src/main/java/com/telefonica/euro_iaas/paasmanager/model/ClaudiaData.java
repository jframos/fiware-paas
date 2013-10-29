/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * Defines the data to be used with Claudia.
 * 
 * @author jesus.movilla
 */
public class ClaudiaData {

    /** The org. */
    private final String org;
    /** The vdc. */
    private final String vdc;
    /** The service. */
    private final String service;

    /** The user. **/
    private PaasManagerUser user;

    /** The replica. **/
    private String replica;

    /**
     * @param org
     * @param vdc
     * @param service
     */
    public ClaudiaData(String org, String vdc, String service) {
        this.org = org;
        this.vdc = vdc;
        this.service = service;
    }

    /**
     * @return the org
     */
    public String getOrg() {
        return org;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @return the user
     */
    public PaasManagerUser getUser() {
        return user;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(PaasManagerUser user) {
        this.user = user;
    }

}
