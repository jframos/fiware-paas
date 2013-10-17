/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.Environment;

/**
 * Provides some criteria to search TierInstance entities.
 * 
 * @author Jesus M. Movilla
 */
public class TierSearchCriteria extends AbstractSearchCriteria {

    private Environment environment;

    private String name;
    private String environmentName;
    private String vdc;

    /**
     * Default constructor
     */
    public TierSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vdc
     * @param name
     * @param environmentName
     * @param environment
     */
    public TierSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, String vdc,
            String name, String environmentName, Environment environment) {
        super(page, pageSize, orderBy, orderType);
        this.vdc = vdc;
        this.environmentName = environmentName;
        this.vdc = vdc;
        this.environment = environment;

    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVdc() {
        return vdc;
    }

    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

}
