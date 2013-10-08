package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;

/**
 * @author henar
 * 
 */
public interface RouterManager {

    /**
     * It adds a network to the router.
     * 
     */
    void addNetwork(ClaudiaData claudiaData, Router router, Network network) throws InfrastructureException;


    /**
     * Create a router.
     * 
     * @param claudiaData
     * @param router
     * @return the tierInstance created
     * @throws InfrastructureException
     */
    void create(ClaudiaData claudiaData, Router subNetwork)
    throws InvalidEntityException, InfrastructureException;

    /**
     * Delete a router.
     * @param claudiaData
     * @param router
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, Router router) throws EntityNotFoundException,
    InvalidEntityException, InfrastructureException;

    /**
     * Retrieve all router created in the system.
     * 
     * @return the existent networks.
     */
    List<Router> findAll();

    /**
     * Load the router.
     * 
     * @return the router.
     */
    Router load(String name) throws EntityNotFoundException;


    /**
     * Update a router.
     * @param router
     * @return the router updated
     */
    Router update(Router router) throws InvalidEntityException;

}
