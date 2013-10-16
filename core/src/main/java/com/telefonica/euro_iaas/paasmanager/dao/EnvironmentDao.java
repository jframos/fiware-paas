package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

/**
 * Defines the methods needed to persist Environment objects.
 * 
 * @author Jesus M. Movilla
 */
public interface EnvironmentDao extends BaseDAO<Environment, String> {
	/**
	 * Find the environment that matches with the given criteria.
	 * 
	 * @param criteria
	 *            the search criteria
	 * @return the list of elements that match with the criteria.
	 */
	List<Environment> findByCriteria(EnvironmentSearchCriteria criteria);
	
	/**
	 * Loads an environment from vdc and its name
	 * @param name
	 * @param vdc
	 * @return
	 * @throws EntityNotFoundException
	 */
	Environment load(String name, String vdc) throws EntityNotFoundException;
	
	//Environment filterEqualTiers(Environment environment);
	
}