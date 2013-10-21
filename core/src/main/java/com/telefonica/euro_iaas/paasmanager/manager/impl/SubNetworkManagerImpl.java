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
import com.telefonica.euro_iaas.paasmanager.dao.SubNetworkDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.SubNetworkManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author henar
 * 
 */
public class SubNetworkManagerImpl implements SubNetworkManager {

    private  SubNetworkDao subNetworkDao = null;
    private  NetworkClient networkClient = null;
    private static Logger log = Logger.getLogger(SubNetworkManagerImpl.class);

    /**
     * To create a network.
     * @throws AlreadyExistsEntityException, InfrastructureException, InvalidEntityException
     * @params claudiaData
     * @params network
     */
    public void create(ClaudiaData claudiaData, SubNetwork subNetwork)
    throws InvalidEntityException, InfrastructureException, AlreadyExistsEntityException {
        log.debug("Create subnetwork " + subNetwork.getName());

        try {
            subNetworkDao.load(subNetwork.getName());
            throw new AlreadyExistsEntityException(subNetwork);

        } catch (EntityNotFoundException e1) {
            try {
                networkClient.deploySubNetwork(claudiaData, subNetwork);
                subNetwork = subNetworkDao.create(subNetwork);
            } catch (Exception e) {
                log.error("Error to create the subnetwork in BD " + e.getMessage());
                throw new InvalidEntityException(subNetwork);
            }
        }
        // return subNetwork;
    }

    /**
     * To remove a subnetwork.
     * @params claudiaData
     * @params subNetwork
     */
    public void delete(ClaudiaData claudiaData, SubNetwork subNetwork)
    throws  EntityNotFoundException,  InvalidEntityException, InfrastructureException {
        log.debug("Destroying network " + subNetwork.getName());
        try {
            networkClient.destroySubNetwork(claudiaData, subNetwork);
            subNetworkDao.remove(subNetwork);
        } catch (Exception e) {
            log.error("Error to remove the subnetwork in BD " + e.getMessage());
            throw new InvalidEntityException(subNetwork);
        }

    }

    /**
     * To obtain the list of subnetworks.
     * @return the subnetwork list
     */
    public List<SubNetwork> findAll() {
        return subNetworkDao.findAll();
    }

    /**
     * To obtain the subnetwork.
     * @param name
     * @param vdc
     * @param networkName
     * @return the network
     */
    public SubNetwork load(String name) throws EntityNotFoundException {
        return subNetworkDao.load(name);
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
    public void setSubNetworkDao(SubNetworkDao subNetworkDao) {
        this.subNetworkDao = subNetworkDao;
    }

    /**
     * To update the subnetwork.
     * @param subNetwork
     * @return the subNetwork
     */
    public SubNetwork update(SubNetwork subNetwork) throws InvalidEntityException {
        // TODO Auto-generated method stub
        return null;
    }

}
