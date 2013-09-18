package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;

/**
 * Defines the methods needed to persist EnvironmentInstace objects.
 *
 * @author Jesus M. Movilla
 */
public interface TierInstanceDao extends BaseDAO<TierInstance, Long> {
    /**
     * Find the environment that matches with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria);

}