package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Defines the methods needed to persist ApplicationInstance objects.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
public interface ApplicationInstanceDao extends BaseDAO<ApplicationInstance, String> {
    /**
     * Find the applicationInstancs that matches with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria);
}
