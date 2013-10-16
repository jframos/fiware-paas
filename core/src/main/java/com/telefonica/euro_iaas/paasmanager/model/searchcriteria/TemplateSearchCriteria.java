/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * @author jesus.movilla
 */
public class TemplateSearchCriteria extends AbstractSearchCriteria {

    private TierInstance tierInstance;

    /**
     * Default constructor
     */
    public TemplateSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param tierInstance
     */
    public TemplateSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            ProductRelease productRelease) {
        super(page, pageSize, orderBy, orderType);
        this.tierInstance = tierInstance;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param tierInstance
     */
    public TemplateSearchCriteria(String orderBy, String orderType, TierInstance tierInstance) {
        super(orderBy, orderType);
        this.tierInstance = tierInstance;
    }

    /**
     * @param page
     * @param pagesize
     * @param tierInstance
     */
    public TemplateSearchCriteria(Integer page, Integer pageSize, TierInstance tierInstance) {
        super(page, pageSize);
        this.tierInstance = tierInstance;
    }

    /**
     * @param productRelease
     */
    public TemplateSearchCriteria(TierInstance tierInstance) {
        this.tierInstance = tierInstance;
    }

    /**
     * @return the tierInstance
     */
    public TierInstance getTierInstance() {
        return tierInstance;
    }

    /**
     * @param TierInstance
     *            the tierInstance to set
     */
    public void setTierInstance(TierInstance tierInstance) {
        this.tierInstance = tierInstance;
    }

}
