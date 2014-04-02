/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.Port;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author henar.munoz
 */
public class NetworkClientDummyImpl implements NetworkClient {

    /**
     * It adds the network to the router.
     * 
     * @params claudiaData
     * @params router
     * @params network
     * @throws InfrastructureException
     */

    public void addNetworkToRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance network)
            throws InfrastructureException {

    }

    /**
     * Its delete the network from the router.
     * 
     * @params claudiaData
     * @params router
     * @params network
     * @throws InfrastructureException
     */
    public void deleteNetworkFromRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance network,
            String region) throws InfrastructureException {

    }

    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployNetwork(ClaudiaData claudiaData, NetworkInstance network, String region)
            throws InfrastructureException {
        network.setIdNetwork("id");
    }

    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployRouter(ClaudiaData claudiaData, RouterInstance router, String region)
            throws InfrastructureException {
        router.setIdRouter("id");

    }

    /**
     * The deploy the subnet in Openstack.
     * 
     * @params claudiaData
     * @params subNet
     */
    public void deploySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet, String region)
            throws InfrastructureException {
        subNet.setIdNetwork("id");

    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroyNetwork(ClaudiaData claudiaData, NetworkInstance network, String region)
            throws InfrastructureException {

    }

    /**
     * It destroys the router.
     * 
     * @params claudiaData
     * @params router
     */
    public void destroyRouter(ClaudiaData claudiaData, RouterInstance router, String region) {
        // TODO Auto-generated method stub

    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subnet, String region)
            throws InfrastructureException {

    }

    /**
     * It loads all networks.
     * 
     * @params claudiaData
     */
    public List<NetworkInstance> loadAllNetwork(ClaudiaData claudiaData, String region) {
        return new ArrayList<NetworkInstance>();
    }

    /**
     * It obtains information about the network.
     * 
     * @params claudiaData
     * @params network
     * @return network information
     */
    public String loadNetwork(ClaudiaData claudiaData, NetworkInstance network, String region)
            throws EntityNotFoundException {
        return "";
    }

    /**
     * It add the network interface to the public router.
     */
    public void addNetworkToPublicRouter(ClaudiaData claudiaData, NetworkInstance net, String region)
            throws InfrastructureException {
        // TODO Auto-generated method stub

    }

    @Override
    public String loadSubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet, String region)
            throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteNetworkToPublicRouter(ClaudiaData claudiaData, NetworkInstance networkInstance, String region)
            throws InfrastructureException {
        // TODO Auto-generated method stub

    }


    @Override
    public NetworkInstance deployDefaultNetwork(ClaudiaData claudiaData, String region) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<NetworkInstance> loadNotSharedNetworks(ClaudiaData claudiaData, String region) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Port> listPortsFromNetwork(ClaudiaData claudiaData, String region, String networkId)
            throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

}
