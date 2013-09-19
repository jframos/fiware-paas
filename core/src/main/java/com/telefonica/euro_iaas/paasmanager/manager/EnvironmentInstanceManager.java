/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;

public interface EnvironmentInstanceManager {

	/**
	 * Create an environentInstance
	 * @param vdc
	 * @param environment
	 * @param envInstanceName
	 * @return
	 * @throws EntityNotFoundException
	 * @throws InvalidEntityException
	 * @throws AlreadyExistsEntityException
	 * @throws NotUniqueResultException
	 * @throws InfrastructureException
	 * @throws IPNotRetrievedException
	 * @throws ProductInstallatorException
	 * @throws InvalidEnvironmentRequestException
	 * 	 */
	/*EnvironmentInstance create(String org, String vdc, PaasManagerUser user,
			String envInstanceName,	
			Environment environment) 
			throws EntityNotFoundException, InvalidEntityException, 
    		AlreadyExistsEntityException, NotUniqueResultException,
    		InfrastructureException, IPNotRetrievedException, ProductInstallatorException,
    		InvalidEnvironmentRequestException, 
    		InvalidProductInstanceRequestException, InvalidOVFException;*/
	EnvironmentInstance create(ClaudiaData claudiaData,	Environment environment) 
			throws EntityNotFoundException, InvalidEntityException, 
    		AlreadyExistsEntityException, NotUniqueResultException,
    		InfrastructureException, IPNotRetrievedException, ProductInstallatorException,
    		InvalidEnvironmentRequestException, 
    		InvalidProductInstanceRequestException, InvalidOVFException;
	
	/**
     * Load an environentInstance
     * @param vdc
     * @param name
     * @return the environentInstance
     */
	public EnvironmentInstance load (String vdc, String name) 
			throws EntityNotFoundException;
	
	/**
	 * 
	 * @param envInst
	 * @return
	 * @throws InvalidEntityException
	 */
	public EnvironmentInstance update(EnvironmentInstance envInst) 
			throws InvalidEntityException;
	
	/**
	 * Find all EnvironmentInstances 
	 * @return the environentInstances
	 */
	List<EnvironmentInstance> findAll();
	
    /**
     * Find the environment instances that match with the given criteria.
     *
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<EnvironmentInstance> findByCriteria(
            EnvironmentInstanceSearchCriteria criteria);
    
    
    /**
     * Destroy the Environment (Infrastructure + environment in Database)
     * @param envInstance
     * @param org
     * @param vdc
     */
    void destroy (ClaudiaData claudiaData, EnvironmentInstance envInstance) 
    		throws InvalidEntityException;
	
    /**
     * 
     * @param vdc
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    EnvironmentInstance loadForDelete(String vdc, String name) 
    		throws EntityNotFoundException;

}
