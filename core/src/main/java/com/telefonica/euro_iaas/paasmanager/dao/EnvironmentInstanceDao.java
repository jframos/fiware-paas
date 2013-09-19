package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;

public interface EnvironmentInstanceDao extends BaseDAO<EnvironmentInstance, String> {

	/**
     * Find the applicationInstancs that matches with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<EnvironmentInstance> findByCriteria(EnvironmentInstanceSearchCriteria criteria);
    
    EnvironmentInstance loadForDelete(String  name) throws EntityNotFoundException ;
    
}
