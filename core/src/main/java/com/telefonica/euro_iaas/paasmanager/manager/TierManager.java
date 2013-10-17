/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

public interface TierManager {

    /**
     * Update an tierInstance
     * 
     * @param tierInstance
     * @return the tierInstance created
     */
    Tier update(Tier tier) throws InvalidEntityException;

    Tier create(ClaudiaData claudiaData, String envName, Tier tier) throws InvalidEntityException,
            InvalidSecurityGroupRequestException, InfrastructureException;

    /**
     * Find the Environment using the given name.
     * 
     * @param name
     *            the name
     * @return the environment
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    // Tier load(String name) throws EntityNotFoundException;
    Tier load(String name, String vdc, String environmentNamae) throws EntityNotFoundException;

    /**
     * Retrieve all Environment created in the system.
     * 
     * @return the existent environments.
     */
    List<Tier> findAll();

    /**
     * Delete a TierInstance
     * 
     * @param claudiaData
     * @param tier
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, Tier tier) throws EntityNotFoundException, InvalidEntityException,
            InfrastructureException;

    /**
     * Find the TierInstance by Environment
     * 
     * @param environment
     * @return
     */
    List<Tier> findByEnvironment(Environment environment);

    /**
     * Find the Tier Instance by a certain criteria
     * 
     * @param criteria
     * @return
     * @throws EntityNotFoundException
     */
    List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException;

    /**
     * Add a Security Group to a ProductRelease
     * 
     * @param claudiaData
     * @param tier
     * @param productRelease
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws InvalidEntityException
     */
    void addSecurityGroupToProductRelease(ClaudiaData claudiaData, Tier tier, ProductRelease productRelease)
            throws InvalidEntityException, AlreadyExistsEntityException, InfrastructureException;

}
