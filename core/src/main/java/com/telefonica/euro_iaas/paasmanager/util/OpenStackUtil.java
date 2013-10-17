/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

// import org.openstack.docs.compute.api.v1.Server;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
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
    public static final String APPLICATION_JSON = "application/json";
    /**
     * name of the xml type.
     */
    public static final String APPLICATION_XML = "application/xml";
    /**
     * name of the accept header.
     */
    public static final String ACCEPT = "Accept";
    /**
     * name of the content-Type header.
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * name of the Authentication header.
     */
    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    /**
     * name of the resource Images.
     */
    public static final String RESOURCE_IMAGES = "images/";
    /**
     * name of the resource Flavors.
     */
    public static final String RESOURCE_FLAVORS = "flavors/";
    /**
     * name of the resource Networks.
     */
    public static final String RESOURCE_NETWORKS = "networks";
    /**
     * name of the resource Subnets.
     */
    public static final String RESOURCE_SUBNETS = "subnets";
    /**
     * name of the resource Subnets.
     */
    public static final String RESOURCE_ROUTERS = "routers";
    /**
     * name of the resource Servers.
     */
    public static final String RESOURCE_SERVERS = "servers";
    /**
     * path for a detailed resource .
     */
    public static final String RESOURCE_DETAIL = "detail";
    /**
     * path for actions.
     */
    public static final String RESOURCE_ACTION = "action";
    /**
     * path for add interfaces to routers.
     */
    public static final String RESOURCE_ADD_INTERFACE = "add_router_interface";
    /**
     * path for floatingIPS.
     */
    public static final String RESOURCE_FLOATINGIP = "os-floating-ips";
    /**
     * name of OpenStack constant name: org.openstack.compute.storage.root. Amount of root Disk.
     */
    public static final String OPENSTACK_COMPUTE_STORAGE_ROOT = "org.openstack.compute.storage.root";
    /**
     * name of OpenStack constant name: org.openstack.compute.storage.root. Amount of root Disk.
     */
    public static final String OPENSTACK_COMPUTE_STORAGE_EPHEMERAL = "org.openstack.compute.storage.ephemeral";
    /**
     * name of OpenStack constant name: org.openstack.compute.swap. Amount of swap.
     */
    public static final String OPENSTACK_COMPUTE_SWAP = "org.openstack.compute.swap";
    /**
     * path for a detailed resource
     */
    public static final String ERROR_AUTHENTICATION_HEADERS = "Authentication Token, Tenant ID and User must be initialized...";

    /**
     * Get the Server details
     * 
     * @param serverId
     * @return
     * @throws OpenStackException
     */
    String getServer(String serverId, PaasManagerUser user) throws OpenStackException;

    /**
     * List all servers in OpenStack
     * 
     * @return
     * @throws OpenStackException
     */
    String listServers(PaasManagerUser user) throws OpenStackException;

    /**
     * Deploys a VM inOpenStack
     * 
     * @param payload
     * @return serverId
     * @throws OpenStackException
     */
    String createServer(String payload, PaasManagerUser user) throws OpenStackException;

    /**
     * Undeploys a VM in Openstack
     * 
     * @param serverId
     *            the VM to be undeployed
     * @param the
     *            user
     */
    String deleteServer(String serverId, PaasManagerUser user) throws OpenStackException;

    /**
     * Obtain the floating IP's of a certain tenantID
     */
    String getFloatingIP(PaasManagerUser user) throws OpenStackException;

    /**
     * Allocate a new FloatingIP to a tenant
     * 
     * @return
     * @throws OpenStackException
     */
    // public String allocateFloatingIP(String payload) throws
    // OpenStackException;
    /**
     * Assign FloatingIP to a serverId
     * 
     * @param serverId
     * @param floatingIP
     * @return
     * @throws OpenStackException
     */
    String assignFloatingIP(String serverId, String floatingIP, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to create a new network.
     * 
     * @param name
     *            the name of the network
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String createNetwork(String name, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to delete a network.
     * 
     * @param networkId
     *            the Id of the network
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String deleteNetwork(String networkId, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to get a single Network.
     * 
     * @param networkId
     *            the Id of the network
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String getNetworkDetails(String networkId, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to get a list of Networks.
     * 
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String getNetworks(PaasManagerUser user) throws OpenStackException;

    /**
     * Method to create a new router.
     * 
     * @param name
     *            the name of the router
     * @param networkId
     *            the id of the external network to associate with
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String createRouter(String name, String networkId, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to create a new subnet.
     * 
     * @param name
     *            the name of the subnet
     * @param networkId
     *            the id of the external network to associate with
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String createSubNet(String name, String networkId, PaasManagerUser user) throws OpenStackException;

    /**
     * Method to add an interface to the router.
     * 
     * @param routerId
     *            the id of the router
     * @param subNetId
     *            the id of the subNet to associate with
     * @param user
     *            the user
     * @return the result
     * @throws OpenStackException
     *             OpenStackException
     */
    String addRouterInterface(String routerId, String subNetId, PaasManagerUser user) throws OpenStackException;
}
