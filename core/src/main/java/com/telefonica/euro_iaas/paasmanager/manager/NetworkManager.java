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
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;

/**
 * @author henar
 */
public interface NetworkManager {
    /**
     * Create a network.
     * 
     * @param claudiaData
     * @param network
     * @return the tierInstance created
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     */
    Network create(ClaudiaData claudiaData, Network network) throws InvalidEntityException, InfrastructureException,
            AlreadyExistsEntityException;

    /**
     * Delete a Network.
     * 
     * @param claudiaData
     * @param network
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, Network network) throws EntityNotFoundException, InvalidEntityException,
            InfrastructureException;

    /**
     * Retrieve all Network created in the system.
     * 
     * @return the existent networks.
     */
    List<Network> findAll();

    /**
     * Load the network.
     * 
     * @return the network.
     */
    Network load(String networkName) throws EntityNotFoundException;

    /**
     * Update a network.
     * 
     * @param network
     * @return the tierInstance created
     */
    Network update(Network network) throws InvalidEntityException;

}
