/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.dao.RouterDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.RouterManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;

/**
 * @author henar
 * 
 */
public class RouterManagerImpl implements RouterManager {

    private  RouterDao routerDao = null;
    private  NetworkClient networkClient = null;
    private static Logger log = Logger.getLogger(RouterManagerImpl.class);

    /**
     * It adds a network to the router.
     * @throws InfrastructureException
     * @params claudiaData
     * @params router
     * @network
     */
    public void addNetwork(ClaudiaData claudiaData, Router router, Network network) throws InfrastructureException {
        networkClient.addNetworkToRouter(claudiaData, router, network);

    }

    /**
     * To create a router.
     * @params claudiaData
     * @params router
     */
    public void create(ClaudiaData claudiaData, Router router, Network network)
    throws InvalidEntityException,InfrastructureException {
        log.debug("Create router " + router.getName());


        try {
            routerDao.load(router.getName());

        } catch (EntityNotFoundException e1) {
            try {
                networkClient.deployRouter(claudiaData, router);
                addNetwork(claudiaData, router, network);
                router = routerDao.create(router);
            } catch (Exception e) {
                log.error("Error to create the subnetwork in BD " + e.getMessage());
                throw new InvalidEntityException(router);
            }
        }
        //return router;
    }

    /**
     * To remove a router.
     * @params claudiaData
     * @params router
     */
    public void delete(ClaudiaData claudiaData,  Router router)
    throws  EntityNotFoundException,  InvalidEntityException, InfrastructureException {
        log.debug("Destroying network " + router.getName());
        try {
            networkClient.destroyRouter(claudiaData, router);
            routerDao.remove(router);
        } catch (Exception e) {
            log.error("Error to remove the router in BD " + e.getMessage());
            throw new InvalidEntityException(router);
        }

    }

    /**
     * To obtain the list of router.
     * @return the router list
     */
    public List<Router> findAll() {
        return routerDao.findAll();
    }

    /**
     * To obtain the router.
     * @param name
     * @param vdc
     * @param networkName
     * @return the network
     */
    public Router load(String name) throws EntityNotFoundException {
        return routerDao.load(name);
    }
    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public void setRouterDao(RouterDao routerDao) {
        this.routerDao = routerDao;
    }

    /**
     * To update the subnetwork.
     * @param router
     * @return the router
     */
    public Router update(Router subNetwork) throws InvalidEntityException {
        // TODO Auto-generated method stub
        return null;
    }



}
