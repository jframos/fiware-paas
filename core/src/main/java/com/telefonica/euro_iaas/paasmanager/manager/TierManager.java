package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;


public interface TierManager {

	/**
     * Update an tierInstance
     * @param tierInstance
     * @return the tierInstance created
     */
	Tier update(Tier tier) 
    		throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException ;
	
	  
	 Tier create(Tier tier) throws  InvalidEntityException;
	 
	 Tier create(String name, String image, String flavour, List<ProductRelease> productReleases , int initial_number_instances, int maximum_number_instances,
			int minimum_number_instances) throws InvalidEntityException;

	    /**
	     * Find the Environment using the given name.
	     * @param name the name
	     * @return the environment
	     * @throws EntityNotFoundException if the product instance does not exists
	     */
	    Tier load(String name) throws EntityNotFoundException;

	   /**
	     * Retrieve all Environment created in the system.
	     * @return the existent environments.
	     */
	    List<Tier> findAll();
	    
	    void delete(Tier tier) throws EntityNotFoundException;


		List<Tier> findByEnvironment(Environment environment);


		List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException;


	
	
}
