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
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * @author henar
 */
public class NetworkManagerImpl implements NetworkManager {


    private  NetworkDao networkDao = null;
    private  SubNetworkManager subNetworkManager = null;

    private static Logger log = Logger.getLogger(NetworkManagerImpl.class);

    /**
     * To create a network.
     * 
     * @throws AlreadyExistsEntityException
     * @params claudiaData
     * @params network
     */
    public Network create(Network network) throws InvalidEntityException,
    InfrastructureException, AlreadyExistsEntityException {
        log.debug("Create network " + network.getNetworkName());

        try {
        	Network networkDB = networkDao.load(network.getNetworkName());
            log.debug("The network already exists");
            for (SubNetwork subnet: network.getSubNets()) {
                if (!networkDB.contains(subnet)) {
                    createSubNetwork(network, subnet);
                }
            }
            if (network.getSubNets().size() ==0) {
            	 createSubNetwork(network);
            }
            return network;

        } catch (EntityNotFoundException e1) {
            try {
                createSubNetwork(network);
                network = networkDao.create(network);
            } catch (Exception e) {
                log.error("Error to create the network " + e.getMessage());
                throw new InvalidEntityException(network);
            }
        }

        return network;
    }


    /**
     * It creates a subnet in the network.
     * @param claudiaData
     * @param network
     * @param subNetwork
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws InfrastructureException 
     */
    public void createSubNetwork(Network network, SubNetwork subNetwork)
        throws InvalidEntityException, AlreadyExistsEntityException
    {

        SubNetwork subNet = subNetwork;
        if (subNet == null) {
            int cidrCount = findAll().size() + 1;
            subNet = new SubNetwork("sub-net-" + network.getNetworkName() + "-"
                    + cidrCount, "" + cidrCount);
        }
        network.addSubNet(subNet);
        subNetworkManager.create(subNet);
        log.debug("SubNetwork " + subNet.getName() + " in network  " + network.getNetworkName() + " deployed");
    }
    
    /**
     * It creates a subnet in the network.
     * @param claudiaData
     * @param network
     * @param subNetwork
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws InfrastructureException 
     */
    public void createSubNetwork(Network network)
        throws InvalidEntityException, AlreadyExistsEntityException
    {
        int cidrCount = findAll().size() + 1;
        SubNetwork subNet = new SubNetwork("sub-net-" + network.getNetworkName() + "-"
                   + cidrCount, "" + cidrCount);
        createSubNetwork (network, subNet);
    }

    /**
     * To remove a network.
     * 
     * @params claudiaData
     * @params network
     */
    public void delete(Network network) throws EntityNotFoundException,
    InvalidEntityException {
        log.debug("Destroying network " + network.getNetworkName());

        log.debug("Deleting the subnets");
        List<SubNetwork> subNetsAux = new ArrayList<SubNetwork> ();
        for (SubNetwork subNet: network.getSubNets()) {
        	subNetsAux.add(subNet);
        }
        
        for (SubNetwork subNet: subNetsAux) {
        	network.deleteSubNet(subNet);
        	networkDao.update(network);
            subNetworkManager.delete(subNet);
        }

        log.debug("Deleting the network");
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


    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }


    public void setSubNetworkManager(SubNetworkManager subNetworkManager) {
        this.subNetworkManager = subNetworkManager;
    }


    /**
     * To update the network.
     * 
     * @param network
     * @return the network
     */
    public Network update(Network network) throws InvalidEntityException {
        return networkDao.update(network);
    }

}
