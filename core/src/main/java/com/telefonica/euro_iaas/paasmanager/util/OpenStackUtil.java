/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

// import org.openstack.docs.compute.api.v1.Server;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public interface OpenStackUtil {

    /**
     * pool name in nova *
     */
    // public static final String IPFLOATING_POOL_NAME = "fiprt1";
    /**
     * name of the json type.
     */
    String APPLICATION_JSON = "application/json";
    /**
     * name of the xml type.
     */
    String APPLICATION_XML = "application/xml";
    /**
     * name of the accept header.
     */
    String ACCEPT = "Accept";
    /**
     * name of the content-Type header.
     */
    String CONTENT_TYPE = "Content-Type";
    /**
     * name of the Authentication header.
     */
    String X_AUTH_TOKEN = "X-Auth-Token";
    /**
     * name of the resource Images.
     */
    String RESOURCE_IMAGES = "images/";
    /**
     * name of the resource Flavors.
     */
    String RESOURCE_FLAVORS = "flavors/";
    /**
     * name of the resource Networks.
     */
    String RESOURCE_NETWORKS = "networks";
    
    /**
     * name of the resource Networks.
     */
    String RESOURCE_PORTS = "ports";
    /**
     * name of the resource Subnets.
     */
    String RESOURCE_SUBNETS = "subnets";
    /**
     * name of the resource Subnets.
     */
    String RESOURCE_ROUTERS = "routers";
    /**
     * name of the resource Servers.
     */
    String RESOURCE_SERVERS = "servers";
    /**
     * path for a detailed resource .
     */
    String RESOURCE_DETAIL = "detail";
    /**
     * path for actions.
     */
    String RESOURCE_ACTION = "action";
    /**
     * path for add interfaces to routers.
     */
    String RESOURCE_ADD_INTERFACE = "add_router_interface";
    /**
     * path for remove interfaces to routers.
     */
    String RESOURCE_REMOVE_INTERFACE = "remove_router_interface";

    /**
     * path for floatingIPS.
     */
    String RESOURCE_FLOATINGIP = "os-floating-ips";
    /**
     * name of OpenStack constant name: org.openstack.compute.storage.root. Amount of root Disk.
     */
    String OPENSTACK_COMPUTE_STORAGE_ROOT = "org.openstack.compute.storage.root";
    /**
     * name of OpenStack constant name: org.openstack.compute.storage.root. Amount of root Disk.
     */
    String OPENSTACK_COMPUTE_STORAGE_EPHEMERAL = "org.openstack.compute.storage.ephemeral";
    /**
     * name of OpenStack constant name: org.openstack.compute.swap. Amount of swap.
     */
    String OPENSTACK_COMPUTE_SWAP = "org.openstack.compute.swap";
    /**
     * path for a detailed resource.
     */

    String ERROR_AUTHENTICATION_HEADERS = "Authentication Token, Tenant ID and User must be initialized...";

    /**
     * It adds an network interface to the public router.
     */
    String addInterfaceToPublicRouter(PaasManagerUser user, NetworkInstance net, String region)
            throws OpenStackException;

    /**
     * It adds an interface to the router.
     */
    String addInterface(String idRouter, NetworkInstance net, String region, String token, String vdc)
            throws OpenStackException;

    /**
     * Method to add an interface to the router.
     */
    String addRouterInterface(String routerId, String subNetId, String region, String token, String vdc)
            throws OpenStackException;

    /**
     * Assign FloatingIP to a serverId.
     */
    String assignFloatingIP(String serverId, String floatingIP, String region, String token, String vdc)
            throws OpenStackException;

    /**
     * Method to create a new network.
     * @param payload
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String createNetwork(String payload, String region, String token, String vdc)
        throws OpenStackException;

    /**
     * Method to create a new router.
     */
    String createRouter(RouterInstance router, String region, String token, String vdc) throws OpenStackException;

    /**
     * Deploys a VM inOpenStack.
     */
    String createServer(String payload, String region, String token, String vdc) throws OpenStackException;

    /**
     * Method to create a new subnet.
     */
    String createSubNet(SubNetworkInstance subNet, String region, String token, String vdc) throws OpenStackException;

    /**
     * Method to delete a network.
     */
    String deleteNetwork(String networkId, String region, String token, String vdc) throws OpenStackException;

    /**
     * Undeploys a router in Openstack.
     */
    String deleteRouter(String routerId, String region, String token, String vdc) throws OpenStackException;

    /**
     * Undeploys a VM in Openstack.
     */
    String deleteServer(String serverId, String region, String token, String vdc) throws OpenStackException;

    /**
     * It deletes the subnetwork.
     */
    void deleteSubNetwork(String idSubNet, String region, String vdc, String token) throws OpenStackException;

    /**
     * Obtain the floating IP's of a certain tenantID.
     */
    String getFloatingIP(String region, String token, String vdc) throws OpenStackException;

    /**
     * Method to get a single Network.
     */
    String getNetworkDetails(String networkId, String region, String token, String vdc) throws OpenStackException;

    /**
     * Method to get a list of Networks.
     */
    String getNetworks(String region, String token, String vdc) throws OpenStackException;

    /**
     * Get the Server details.
     */

    String getServer(String serverId, String region, String token, String vdc) throws OpenStackException;

    /**
     * List all servers in OpenStack.
     */
    String listServers(String region, String token, String vdc) throws OpenStackException;
    
    /**
     * List all ports in OpenStack.
     */
    String listPorts(PaasManagerUser user, String region) throws OpenStackException;

    /**
     * It removes the interface of the network in the router.
     */
    String removeInterface(RouterInstance router, String net, String region, String token, String vdc)
            throws OpenStackException;

    /**
     * It return all absolute limit values by tenantId.
     */
    String getAbsoluteLimits(PaasManagerUser user, String region) throws OpenStackException;

    /**
     * It gets the subnetwork details.
     */
    String getSubNetworkDetails(String subNetworkId, String region, String token, String vdc) throws OpenStackException;

    /**
     * It gets the network from the user.
     */

    String listNetworks(PaasManagerUser user, String region) throws OpenStackException;

    /**
     * It deletes the interface in the public router.
     */
    String deleteInterfaceToPublicRouter(PaasManagerUser user, NetworkInstance netInstance, String region)
            throws OpenStackException;

}
