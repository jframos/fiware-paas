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
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.exception.TierInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public interface TierInstanceManager {

    /**
     * Update an tierInstance
     * 
     * @param tierInstance
     * @return the tierInstance created
     */
    TierInstance update(ClaudiaData claudiaData, String envName, TierInstance tierInstance)
            throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException;

    /**
     * Scale a TierInstance
     * 
     * @param org
     * @param vdc
     * @param payload
     * @param tierInstance
     * @throws InfrastructureException
     * @throws EntityNotFoundException
     * @throws AlreadyExistsEntityException
     * @throws InvalidEntityException
     * @throws NotUniqueResultException
     * @throws InvalidProductInstanceRequestException
     */
    void create(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstance,
            SystemPropertiesProvider propertiesProvider) throws InfrastructureException, EntityNotFoundException,
            InvalidEntityException, AlreadyExistsEntityException, NotUniqueResultException,
            InvalidProductInstanceRequestException, ProductInstallatorException;

    /**
     * @param criteria
     * @return
     * @throws EntityNotFoundException
     */
    List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria) throws EntityNotFoundException;

    /**
     * @param vdc
     * @param environmentInstance
     * @return
     * @throws EntityNotFoundException
     */
    List<TierInstance> findByEnvironment(String vdc, EnvironmentInstance environmentInstance)
            throws EntityNotFoundException;

    /**
     * Load a TierInstance by its name
     * 
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    TierInstance load(String name) throws EntityNotFoundException;

    /**
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    TierInstance loadByName(String name) throws EntityNotFoundException;

    /**
     * @param tierInstance
     * @throws InvalidEntityException
     */
    void remove(TierInstance tierInstance) throws InvalidEntityException;

    /**
     * @param tierInstance
     * @return
     * @throws InvalidEntityException
     */
    TierInstance create(ClaudiaData claudiaa, String envName, TierInstance tierInstance) throws InvalidEntityException;

    /**
     * @param org
     * @param service
     * @param tierInstance
     * @throws InfrastructureException
     * @throws InvalidEntityException
     * @throws EntityNotFoundException
     * @throws TierInstanceNotFoundException
     */
    void delete(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstan)
            throws InfrastructureException, InvalidEntityException, EntityNotFoundException;

    /**
     * @param claudiaData
     * @param tierInstance
     * @param envInstance
     * @throws InvalidEntityException
     * @throws NotUniqueResultException
     * @throws InvalidProductInstanceRequestException
     * @throws ProductInstallatorException
     * @throws AlreadyExistsEntityException
     * @throws ProductReconfigurationException
     * @throws EntityNotFoundException
     */
    void update(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstance)
            throws ProductInstallatorException, InvalidProductInstanceRequestException, NotUniqueResultException,
            InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException,
            ProductReconfigurationException;

}
