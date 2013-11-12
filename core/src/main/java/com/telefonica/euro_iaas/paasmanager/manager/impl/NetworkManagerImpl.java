/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
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
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * @author henar
 */
public class NetworkManagerImpl implements NetworkManager {


    private  NetworkDao networkDao = null;
    private  NetworkClient networkClient = null;
    private  SubNetworkManager subNetworkManager = null;
    private  RouterManager routerManager = null;
    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(NetworkManagerImpl.class);

    /**
     * To create a network.
     * 
     * @throws AlreadyExistsEntityException
     * @params claudiaData
     * @params network
     */
    public Network create(ClaudiaData claudiaData, Network network) throws InvalidEntityException,
    InfrastructureException, AlreadyExistsEntityException {
        log.debug("Create network " + network.getNetworkName());

        try {
            Network dbNetwork = networkDao.load(network.getNetworkName());
            log.debug("The network already exists");
            for (SubNetwork subnet: network.getSubNets()) {
                if (!dbNetwork.contains(subnet)) {
                    createSubNetwork(claudiaData, dbNetwork, subnet);
                }
            }
            return network;

        } catch (EntityNotFoundException e1) {
            try {
                networkClient.deployNetwork(claudiaData, network);
                log.debug("Network " + network.getNetworkName() + " : " + network.getIdNetwork() + " deployed");
                createSubNetwork(claudiaData, network, null);
                addNetworkToInternetRouter(claudiaData,network);
                network = networkDao.create(network);
            } catch (Exception e) {
                log.error("Error to create the network " + e.getMessage());
                throw new InvalidEntityException(network);
            }
        }

        return network;
    }

    /**
     * It creates a router and associated it to the network.
     * @param claudiaData
     * @param network
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    public void createRouter(ClaudiaData claudiaData, Network network)
        throws InvalidEntityException, InfrastructureException {
        log.debug("The internet network is in "
            + systemPropertiesProvider.getProperty(SystemPropertiesProvider.PUBLIC_NETWORK_ID));
        Router router = new Router(systemPropertiesProvider.getProperty(SystemPropertiesProvider.PUBLIC_NETWORK_ID),
            "router-" + network.getNetworkName());

        routerManager.create(claudiaData, router, network);
    }
    
    /**
     * It adds the network to the internet router.
     * @param network
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    public void addNetworkToInternetRouter(ClaudiaData claudiaData, Network network )
        throws InvalidEntityException, InfrastructureException {
        networkClient.addNetworkToPublicRouter(claudiaData,network);
    }

    
    
    /**
     * It creates a subnet in the network.
     * @param claudiaData
     * @param network
     * @param subNetwork
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     */
    public void createSubNetwork(ClaudiaData claudiaData, Network network, SubNetwork subNetwork)
        throws InvalidEntityException, InfrastructureException, AlreadyExistsEntityException
    {

        SubNetwork subNet = subNetwork;
        if (subNet == null) {
            int cidrCount = findAll().size() + 1;
            subNet = new SubNetwork("sub-net-" + network.getNetworkName() + "-"
                    + cidrCount, "" + cidrCount);
        }

        network.addSubNet(subNet);
        subNetworkManager.create(claudiaData, subNet);
        log.debug("SubNetwork " + subNet.getName() + " in network  " + network.getNetworkName() + " deployed");
    }

    /**
     * To remove a network.
     * 
     * @params claudiaData
     * @params network
     */
    public void delete(ClaudiaData claudiaData, Network network) throws EntityNotFoundException,
    InvalidEntityException, InfrastructureException {
        log.debug("Destroying network " + network.getNetworkName());
        log.debug("Deleting the router and their interfaces");
        for (Router router: network.getRouters()) {
            routerManager.delete(claudiaData, router, network);
        }
        
        log.debug("Deleting the subnets");
        List<SubNetwork> subNetsAux = new ArrayList<SubNetwork> ();
        for (SubNetwork subNet: network.getSubNets()) {
        	subNetsAux.add(subNet);
        }
        
        for (SubNetwork subNet: subNetsAux) {
        	network.deleteSubNet(subNet);
        	networkDao.update(network);
            subNetworkManager.delete(claudiaData, subNet);
        }

        log.debug("Deleting the network");
        networkClient.destroyNetwork(claudiaData, network);
        try {
            networkDao.remove(network);
        } catch (Exception e) {
            log.error("Error to remove the network in BD " + e.getMessage());
            throw new InvalidEntityException(network);
        }

    }

    /**
     * To obtain the list of networks.
     * 
     * @return the network list
     */
    public List<Network> findAll() {
        return networkDao.findAll();
    }

    /**
     * To obtain the network.
     * 
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
     * @param systemPropertiesProvider the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(
            SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * To update the network.
     * 
     * @param network
     * @return the network
     */
    public Network update(Network network) throws InvalidEntityException {
        // TODO Auto-generated method stub
        return null;
    }

}
