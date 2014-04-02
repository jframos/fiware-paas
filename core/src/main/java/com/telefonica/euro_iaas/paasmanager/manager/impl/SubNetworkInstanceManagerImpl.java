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
import com.telefonica.euro_iaas.paasmanager.dao.SubNetworkInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.SubNetworkInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author henar
 */
public class SubNetworkInstanceManagerImpl implements SubNetworkInstanceManager {

    private SubNetworkInstanceDao subNetworkInstanceDao = null;
    private NetworkClient networkClient = null;
    private static Logger log = Logger.getLogger(SubNetworkInstanceManagerImpl.class);

    /**
     * To create a network.
     * 
     * @throws AlreadyExistsEntityException
     *             , InfrastructureException, InvalidEntityException
     * @params claudiaData
     * @params network
     */
    public SubNetworkInstance create(ClaudiaData claudiaData, SubNetworkInstance subNetwork, String region)
            throws InvalidEntityException, InfrastructureException, AlreadyExistsEntityException {
        log.debug("Create subnetwork instance " + subNetwork.getName());
        if (!subNetworkInstanceDao.exists(subNetwork.getName())) {
            networkClient.deploySubNetwork(claudiaData, subNetwork, region);
            log.debug("SubNetwork " + subNetwork.getName() + " in network " + subNetwork.getIdNetwork()
                    + " deployed with id " + subNetwork.getIdSubNet());
            subNetwork = subNetworkInstanceDao.create(subNetwork);
        } else {
            log.warn("Subred already created " + subNetwork.getName());
            throw new AlreadyExistsEntityException(subNetwork);
        }
        return subNetwork;
    }

    /**
     * To remove a subnetwork.
     * 
     * @params claudiaData
     * @params subNetwork
     */
    public void delete(ClaudiaData claudiaData, SubNetworkInstance subNetworkInstance, String region)
            throws EntityNotFoundException, InvalidEntityException, InfrastructureException {
        log.debug("Destroying the subnetwork " + subNetworkInstance.getName());
        try {
            networkClient.destroySubNetwork(claudiaData, subNetworkInstance, region);
            subNetworkInstanceDao.remove(subNetworkInstance);
        } catch (Exception e) {
            log.error("Error to remove the subnetwork in BD " + e.getMessage());
            throw new InvalidEntityException(subNetworkInstance);
        }

    }

    /**
     * To obtain the list of subnetworks.
     * 
     * @return the subnetwork list
     */
    public List<SubNetworkInstance> findAll() {
        return subNetworkInstanceDao.findAll();
    }

    /**
     * Is the subNetwork deployed.
     * 
     * @param claudiaData
     * @param subNet
     * @return
     */
    public boolean isSubNetworkDeployed(ClaudiaData claudiaData, SubNetworkInstance subNet, String region) {
        try {
            networkClient.loadSubNetwork(claudiaData, subNet, region);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /**
     * To obtain the subnetwork.
     */
    public SubNetworkInstance load(String name) throws EntityNotFoundException {
        return subNetworkInstanceDao.load(name);
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public void setSubNetworkInstanceDao(SubNetworkInstanceDao subNetworkInstanceDao) {
        this.subNetworkInstanceDao = subNetworkInstanceDao;
    }

    /**
     * To update the subnetwork.
     */
    public SubNetworkInstance update(SubNetworkInstance subNetworkInstance) throws InvalidEntityException {
        return subNetworkInstanceDao.update(subNetworkInstance);
    }

}
