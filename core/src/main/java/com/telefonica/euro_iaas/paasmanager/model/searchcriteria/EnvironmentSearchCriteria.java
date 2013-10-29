/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Provides some criteria to search Environment entities.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentSearchCriteria extends AbstractSearchCriteria {

    /**
     * The Tier.
     */
    private Tier tier;
    private String vdc;
    private String org;
    private String environmentName;

    /**
     * Default constructor
     */
    public EnvironmentSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param tier
     */
    public EnvironmentSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, Tier tier,
            String org, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.tier = tier;
        this.org = org;

    }

    /**
     * @param orderBy
     * @param orderType
     * @param tier
     */
    public EnvironmentSearchCriteria(String orderBy, String orderType, Tier tier) {
        super(orderBy, orderType);
        this.tier = tier;
    }

    /**
     * @param page
     * @param pagesize
     * @param tier
     */
    public EnvironmentSearchCriteria(Integer page, Integer pageSize, Tier tier) {
        super(page, pageSize);
        this.tier = tier;
    }

    /**
     * @param tier
     */
    public EnvironmentSearchCriteria(Tier tier) {
        this.tier = tier;
    }

    /**
     * @return the tier
     */
    public Tier getTier() {
        return tier;
    }

    /**
     * @param tier
     *            the tier set
     */
    public void setTier(Tier tier) {
        this.tier = tier;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    /**
     * @return the org
     */
    public String getOrg() {
        return org;
    }

    /**
     * @param vdc
     *            the org to set
     */
    public void setOrg(String org) {
        this.org = org;
    }
}
