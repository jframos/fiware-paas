package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author henar
 * 
 */
public interface SubNetworkManager {
    /**
     * Create a subnetwork.
     * 
     * @param claudiaData
     * @param subNetwork
     * @return the tierInstance created
     * @throws InfrastructureException
     */
    SubNetwork create(ClaudiaData claudiaData, SubNetwork subNetwork)
    throws InvalidEntityException, InfrastructureException;


    /**
     * Delete a subnetwork.
     * @param claudiaData
     * @param subNetwork
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, SubNetwork subNetwork) throws EntityNotFoundException,
    InvalidEntityException, InfrastructureException;

    /**
     * Retrieve all Network created in the system.
     * 
     * @return the existent networks.
     */
    List<SubNetwork> findAll();

    /**
     * Load the SubNetwork.
     * 
     * @return the subnetwork.
     */
    SubNetwork load(String name) throws EntityNotFoundException;

    /**
     * Update a subNetwork.
     * @param subNetwork
     * @return the subnet updated
     */
    SubNetwork update(SubNetwork subNetwork) throws InvalidEntityException;

}
