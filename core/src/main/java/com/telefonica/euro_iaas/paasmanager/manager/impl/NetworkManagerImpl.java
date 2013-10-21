/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.RouterManager;
import com.telefonica.euro_iaas.paasmanager.manager.SubNetworkManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author henar
 * 
 */
public class NetworkManagerImpl implements NetworkManager {

    private  NetworkDao networkDao = null;
    private  NetworkClient networkClient = null;
    private  SubNetworkManager subNetworkManager = null;
    private  RouterManager routerManager = null;
    private static Logger log = Logger.getLogger(NetworkManagerImpl.class);

    /**
     * To create a network.
     * @throws AlreadyExistsEntityException
     * @params claudiaData
     * @params network
     */
    public Network create(ClaudiaData claudiaData, Network network)
    throws InvalidEntityException,InfrastructureException, AlreadyExistsEntityException {
        log.debug("Create network " + network.getNetworkName());

        try {
            networkDao.load(network.getNetworkName());
            throw new AlreadyExistsEntityException(network);

        } catch (EntityNotFoundException e1) {
            try {
                networkClient.deployNetwork(claudiaData, network);
                SubNetwork subNet = new SubNetwork("sub-net-" + network.getNetworkName() + "-"
                        + network.getSubNetCounts(), "" + network.getSubNetCounts());
                subNet.setIdNetwork(network.getIdNetwork());
                network.addSubNet(subNet);
                subNetworkManager.create(claudiaData, subNet);

                Router router = new Router("router-" + network.getNetworkName());
                // This network id can be the internet network...
                router.setIdNetwork(network.getIdNetwork());
                routerManager.create(claudiaData, router);
                routerManager.addNetwork(claudiaData, router, network);
                network = networkDao.create(network);
            } catch (Exception e) {
                log.error("Error to create the network in BD " + e.getMessage());
                throw new InvalidEntityException(network);
            }
        }

        return network;
    }

    /**
     * To remove a network.
     * @params claudiaData
     * @params network
     */
    public void delete(ClaudiaData claudiaData, Network network)
    throws  EntityNotFoundException,  InvalidEntityException, InfrastructureException {
        log.debug("Destroying network " + network.getNetworkName());
        try {
            networkDao.remove(network);
        } catch (Exception e) {
            log.error("Error to remove the network in BD " + e.getMessage());
            throw new InvalidEntityException(network);
        }

    }

    /**
     * To obtain the list of networks.
     * @return the network list
     */
    public List<Network> findAll() {
        return networkDao.findAll();
    }

    /**
     * To obtain the network.
     * @param name
     * @param vdc
     * @param networkName
     * @return the network
     */
    public Network load(String networkName) throws EntityNotFoundException {
        return networkDao.load(networkName);
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }
    public void setRouterManager(RouterManager routerManager) {
        this.routerManager = routerManager;
    }
    public void setSubNetworkManager(SubNetworkManager subNetworkManager) {
        this.subNetworkManager = subNetworkManager;
    }

    /**
     * To update the network.
     * @param network
     * @return the network
     */
    public Network update(Network network) throws InvalidEntityException {
        // TODO Auto-generated method stub
        return null;
    }

}
