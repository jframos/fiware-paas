package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

/**
 * Defines the methods needed to persist Tier objects.
 *
 * @author Jesus M. Movilla
 */
public interface TierDao extends BaseDAO<Tier, String> {
    /**
     * Find the environment that matches with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    //List<Tier> findByCriteria(TierSearchCriteria criteria);
	
	/**
	 * 
	 * @param TierId
	 * @return
	 * @throws EntityNotFoundException
	 */
	Tier findByTierId (Long tierId) throws EntityNotFoundException;
	
	List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException;

}