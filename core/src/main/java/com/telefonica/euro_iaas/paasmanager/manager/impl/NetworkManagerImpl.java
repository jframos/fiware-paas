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
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.SubNetworkManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author henar
 */
public class NetworkManagerImpl implements NetworkManager {

    private NetworkDao networkDao = null;
    private SubNetworkManager subNetworkManager = null;
    private NetworkInstanceManager networkInstanceManager = null;

    private static Logger log = Logger.getLogger(NetworkManagerImpl.class);

    /**
     * To create a network.
     * 
     * @throws AlreadyExistsEntityException
     * @throws EntityNotFoundException
     * @throws InfrastructureException
     * @params claudiaData
     * @params network
     */
    public Network create(ClaudiaData claudiaData, Network network, String region) throws InvalidEntityException,
            AlreadyExistsEntityException, EntityNotFoundException, InfrastructureException {
        log.debug("Create network " + network.getNetworkName());

        if (exists(network.getNetworkName(), claudiaData.getVdc())) {
            log.debug("The network " + network.getNetworkName() + " already exists");
            Network networkDB = networkDao.load(network.getNetworkName(), claudiaData.getVdc());
            for (SubNetwork subnet : network.getSubNets()) {
                if (!networkDB.contains(subnet)) {
                    createSubNetwork(network, subnet);
                }
            }

            if (network.getSubNets().isEmpty()) {
                createDefaultSubNetwork(claudiaData, network, region);
            }
            networkDao.update(network);

        } else {
            for (SubNetwork subnet : network.getSubNets()) {
                createSubNetwork(network, subnet);
            }

            if (network.getSubNets().isEmpty()) {
                createDefaultSubNetwork(claudiaData, network, region);
            }
            if (network.getVdc()== null ){
                network.setVdc("");
            }
            network = networkDao.create(network);
        }
        
        return network;
    }

    public void createPublicNetwork() {

    }

    /**
     * It creates a subnet in the network.
     * 
     * @param network
     * @param subNetwork
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws InfrastructureException
     */
    private void createSubNetwork(Network network, SubNetwork subNetwork) throws InvalidEntityException,
            AlreadyExistsEntityException {

        subNetwork = subNetworkManager.create(subNetwork);
        network.updateSubNet(subNetwork);
        log.debug("SubNetwork " + subNetwork.getName() + " in network  " + network.getNetworkName() + " deployed");
    }

    /**
     * It creates a subnet in the network.
     * 
     * @param claudiaData
     * @param network
     * @throws InvalidEntityException
     * @throws InfrastructureException
     * @throws AlreadyExistsEntityException
     * @throws InfrastructureException
     */
    private void createDefaultSubNetwork(ClaudiaData claudiaData, Network network, String region)
            throws InvalidEntityException, AlreadyExistsEntityException, InfrastructureException {
        int cidrOpenstack =1;
        if (!(claudiaData.getVdc() == null || claudiaData.getVdc().isEmpty())) {
            cidrOpenstack = networkInstanceManager.getNumberDeployedNetwork(claudiaData, region) + 1;
        }

        int cidrdb = this.findAll().size();
        int cidrCount = cidrdb+cidrOpenstack;
      
        SubNetwork subNet = new SubNetwork("sub-net-" + network.getNetworkName() + "-" + cidrCount, "" + cidrCount);
        subNet = subNetworkManager.create(subNet);
        network.addSubNet(subNet);
        log.debug("SubNetwork " + subNet.getName() + " in network  " + network.getNetworkName() + " deployed");
    }

    /**
     * To remove a network.
     * 
     * @params claudiaData
     * @params network
     */
    public void delete(Network network) throws EntityNotFoundException, InvalidEntityException {
        log.debug("Destroying network " + network.getNetworkName());

        log.debug("Deleting the subnets");
        List<SubNetwork> subNetsAux = new ArrayList<SubNetwork>();
        for (SubNetwork subNet : network.getSubNets()) {
            subNetsAux.add(subNet);
        }

        for (SubNetwork subNet : subNetsAux) {
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
     * @param networkName
     * @return the network
     */
    public Network load(String networkName, String vdc) throws EntityNotFoundException {
        return networkDao.load(networkName, vdc);
    }

    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    public void setSubNetworkManager(SubNetworkManager subNetworkManager) {
        this.subNetworkManager = subNetworkManager;
    }

    public void setNetworkInstanceManager(NetworkInstanceManager networkInstanceManager) {
        this.networkInstanceManager = networkInstanceManager;
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
    
    public Network update(Network network, Network network2) throws InvalidEntityException {
    	network.setNetworkName(network2.getNetworkName());
    	network.setVdc(network2.getVdc());
        return networkDao.update(network);
    }

    /**
     * It checks if the network already exists.
     */
    public boolean exists(String networkName, String vdc) {
        try {
            networkDao.load(networkName, vdc);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }


}
