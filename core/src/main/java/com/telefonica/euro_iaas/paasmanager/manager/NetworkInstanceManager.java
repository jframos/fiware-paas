/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;

/**
 * @author henar
 */
public interface NetworkInstanceManager {
    /**
     * Create a network.
     * 
     * @param claudiaData
     * @param network
     * @return the tierInstance created
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws EntityNotFoundException
     */
    NetworkInstance create(ClaudiaData claudiaData, NetworkInstance network, String region)
            throws InvalidEntityException, InfrastructureException, AlreadyExistsEntityException,
            EntityNotFoundException;

    /**
     * Delete a Network.
     * 
     * @param claudiaData
     * @param network
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, NetworkInstance network, String region) throws EntityNotFoundException,
            InvalidEntityException, InfrastructureException;
    
    /**
     * 
     * @param networkInstance
     * @return
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     */
    NetworkInstance createInDB (NetworkInstance networkInstance) 
        throws InvalidEntityException, AlreadyExistsEntityException ;

    /**
     * Retrieve all Network created in the system.
     * 
     * @return the existent networks.
     */
    List<NetworkInstance> findAll();

    /**
     * Load the network.
     * 
     * @return the network.
     */
    NetworkInstance load(String networkName, String vdc) throws EntityNotFoundException;

    /**
     * Update a network.
     * 
     * @param network
     * @return the tierInstance created
     */
    NetworkInstance update(NetworkInstance network) throws InvalidEntityException;
    
    /**
     * 
     * @param claudiaData
     * @param region
     * @return
     * @throws InfrastructureException
     */
    List<NetworkInstance> listNetworks (ClaudiaData claudiaData, String region) throws InfrastructureException;

    int getNumberDeployedNetwork(ClaudiaData claudiaData, String region) throws InfrastructureException;

}
