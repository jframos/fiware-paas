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
