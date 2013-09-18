package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Provides some criteria to search Environment entities.
 *
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentSearchCriteria extends AbstractSearchCriteria {

    /**
     * The Tier.
     */
    private Tier tier;
    
    
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
    public EnvironmentSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, Tier tier) {
        super(page, pageSize, orderBy, orderType);
        this.tier = tier;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param tier
     */
    public EnvironmentSearchCriteria(String orderBy, String orderType,
    		Tier tier) {
        super(orderBy, orderType);
        this.tier = tier;
    }

    /**
     * @param page
     * @param pagesize
     * @param tier
     */
    public EnvironmentSearchCriteria(Integer page, Integer pageSize,
    		Tier tier) {
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
     * @param tier the tier set
     */
    public void setTier(Tier tier) {
        this.tier = tier;
    }
}
