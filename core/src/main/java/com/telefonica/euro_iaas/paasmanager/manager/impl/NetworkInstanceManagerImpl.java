/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;


import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkInstanceManager;

import com.telefonica.euro_iaas.paasmanager.manager.RouterManager;
import com.telefonica.euro_iaas.paasmanager.manager.SubNetworkInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * @author henar
 */
public class NetworkInstanceManagerImpl implements NetworkInstanceManager {


    private  NetworkInstanceDao networkInstanceDao = null;
    private  NetworkClient networkClient = null;
    private  SubNetworkInstanceManager subNetworkInstanceManager = null;
    private  RouterManager routerManager = null;
    private  SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(NetworkInstanceManagerImpl.class);

    /**
     * To create a network.
     *
     * @throws AlreadyExistsEntityException
     * @params claudiaData
     * @params network
     * @return NetworkInstance.class
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     */
    public NetworkInstance create(ClaudiaData claudiaData, NetworkInstance networkInstance)
        throws InvalidEntityException, InfrastructureException,
        AlreadyExistsEntityException {
        log.debug("Create network instance " + networkInstance.getNetworkName());
        try {
            
            networkInstance = networkInstanceDao.
                load(networkInstance.getNetworkName());
            log.debug("The network already exists");
            return networkInstance;

        } catch (EntityNotFoundException e1) {
            try {
                networkClient.deployNetwork(claudiaData, networkInstance);
                log.debug("Network isntance " + networkInstance.getNetworkName()
                    + " : " + networkInstance.getIdNetwork() + " deployed");
                createSubNetworksInstance(claudiaData, networkInstance);
                networkClient.addNetworkToPublicRouter(claudiaData, networkInstance);
            
               networkInstance = networkInstanceDao.create(networkInstance);
            } catch (Exception e) {
                log.error("Error to create the network in BD " + e.getMessage());
                throw new InvalidEntityException(networkInstance);
            }
        }

        return networkInstance;
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
    private void createSubNetworksInstance(ClaudiaData claudiaData, NetworkInstance networkInstance)
        throws InvalidEntityException, InfrastructureException, AlreadyExistsEntityException
    {
    	List<SubNetworkInstance> subNetAxu = new ArrayList ();
    	for (SubNetworkInstance subNet: networkInstance.getSubNets()) {
    		subNetAxu.add(subNet);
    	}
    	for (SubNetworkInstance subNet: subNetAxu) {
    		log.debug("SubNetwork " + subNet.getName() + " id net " + subNet.getIdNetwork() );
    		subNet.setIdNetwork(networkInstance.getIdNetwork());
    		subNet = subNetworkInstanceManager.create(claudiaData, subNet);
    		networkInstance.updateSubNet(subNet);
            log.debug("SubNetwork " + subNet.getName() + " id net " + subNet.getIdNetwork() + " in network  " + networkInstance.getNetworkName() + " deployed");
    	}
    }

    /**
     * To remove a network.
     * 
     * @params claudiaData
     * @params network
     */
    public void delete(ClaudiaData claudiaData, NetworkInstance networkInstance) throws EntityNotFoundException,
    InvalidEntityException, InfrastructureException {
        log.debug("Destroying network " + networkInstance.getNetworkName());
        log.debug("Deleting the router and their interfaces");
        for (RouterInstance router: networkInstance.getRouters()) {
            routerManager.delete(claudiaData, router, networkInstance);
        }
        log.debug("Deleting the subnets");
        for (SubNetworkInstance subNet: networkInstance.getSubNets()) {
            subNetworkInstanceManager.delete(claudiaData, subNet);
        }
        log.debug("Deleting the network");
        networkClient.destroyNetwork(claudiaData, networkInstance);
        try {
            networkInstanceDao.remove(networkInstance);
        } catch (Exception e) {
            log.error("Error to remove the network in BD " + e.getMessage());
            throw new InvalidEntityException(networkInstance);
        }

    }

    /**
     * To obtain the list of networks.
     * 
     * @return the network list
     */
    public List<NetworkInstance> findAll() {
        return networkInstanceDao.findAll();
    }

    /**
     * To obtain the network.
     * 
     * @param networkName
     * @return the network
     */
    public NetworkInstance load(String networkName) throws EntityNotFoundException {
        return networkInstanceDao.load(networkName);
    }
    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public void setNetworkInstanceDao(NetworkInstanceDao networkInstanceDao) {
        this.networkInstanceDao = networkInstanceDao;
    }

    public void setRouterManager(RouterManager routerManager) {
        this.routerManager = routerManager;
    }

    public void setSubNetworkInstanceManager(SubNetworkInstanceManager subNetworkInstanceManager) {
        this.subNetworkInstanceManager = subNetworkInstanceManager;
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
    public NetworkInstance update(NetworkInstance networkInstance) throws InvalidEntityException {
    	return networkInstanceDao.update(networkInstance);
    }

}