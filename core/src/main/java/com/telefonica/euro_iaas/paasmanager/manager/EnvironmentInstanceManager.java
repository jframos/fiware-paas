package com.telefonica.euro_iaas.paasmanager.manager;


import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

public interface EnvironmentInstanceManager {

	/**
     * Create an environentInstance
     * @param the vdc
     * @param the environment
     * @return the of installed product.
     */
	EnvironmentInstance create(String vdc, Environment environment) 
	//EnvironmentInstance create(EnvironmentInstance environmentInstance)
			throws EntityNotFoundException, InvalidEntityException, 
    		AlreadyExistsEntityException, NotUniqueResultException;

	/**
     * Load an environentInstance
     * @param name
     * @return the environentInstance
     */
	EnvironmentInstance load (String name) throws EntityNotFoundException;

}
