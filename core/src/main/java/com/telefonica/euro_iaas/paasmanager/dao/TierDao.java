package com.telefonica.euro_iaas.paasmanager.dao;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Defines the methods needed to persist Tier objects.
 *
 * @author Jesus M. Movilla
 */
public interface TierDao extends BaseDAO<Tier, Long> {
    /**
     * Find the environment that matches with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    //List<Tier> findByCriteria(TierSearchCriteria criteria);

}