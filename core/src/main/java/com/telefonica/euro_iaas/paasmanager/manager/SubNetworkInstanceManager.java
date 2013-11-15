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
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author henar
 */
public interface SubNetworkInstanceManager {
    /**
     * Create a subnetwork.
     * 
     * @param claudiaData
     * @param subNetwork
     * @return the tierInstance created
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     */
    SubNetworkInstance create(ClaudiaData claudiaData, SubNetworkInstance subNetworkInstance) throws InvalidEntityException, InfrastructureException,
            AlreadyExistsEntityException;

    /**
     * Delete a subnetwork.
     * 
     * @param claudiaData
     * @param subNetwork
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, SubNetworkInstance subNetworkInstance) throws EntityNotFoundException, InvalidEntityException,
            InfrastructureException;

    /**
     * Retrieve all Network created in the system.
     * 
     * @return the existent networks.
     */
    List<SubNetworkInstance> findAll();

    /**
     * Load the SubNetwork.
     * 
     * @return the subnetwork.
     */
    SubNetworkInstance load(String name) throws EntityNotFoundException;

    /**
     * Update a subNetwork.
     * 
     * @param subNetworkInstance
     * @return the subnet updated
     */
    SubNetworkInstance update(SubNetworkInstance subNetworkInstance) throws InvalidEntityException;
    
    /**
     * Check if the network has been deployed.
     * @param claudiaData
     * @param subNet
     */
    boolean isSubNetworkDeployed (ClaudiaData claudiaData, SubNetworkInstance subNet);

}
