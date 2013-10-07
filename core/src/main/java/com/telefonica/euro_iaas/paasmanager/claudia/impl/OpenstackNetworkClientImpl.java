package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

/**
 * @author henar.munoz
 *
 */
public class OpenstackNetworkClientImpl implements NetworkClient {

    private OpenStackUtil openStackUtil = null;
    private static Logger log = Logger.getLogger(OpenstackNetworkClientImpl.class);
    /**
     * It adds the network to the router.
     * @params claudiaData
     * @params router
     * @params network
     * @return network information
     * @throws InfrastructureException
     */
    public void addNetworkToRouter(ClaudiaData claudiaData, Router router, Network network) throws InfrastructureException {
        try {
            openStackUtil.addRouterInterface(router.getIdRouter(),network.getSubNets().get(0).getIdSubNet(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to add the network " + network.getNetworkName() + "to the router " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException( msm, e);
        }

    }

    /**
     * The deploy the network in Openstack.
     * @params claudiaData
     * @params network
     */
    public void deployNetwork(ClaudiaData claudiaData, Network network) throws InfrastructureException {
        log.info("Deploy network " + network.getNetworkName() + " for user " + claudiaData.getUser().getTenantName());
        String response;
        try {
            response = openStackUtil.createNetwork(network, claudiaData.getUser());
            // "network-" + claudiaData.getUser().getTenantName()
            JSONObject networkString = new JSONObject(response);
            String id = networkString.getJSONObject("network").getString("id");
            log.debug("Network id " + id);
            network.setIdNetwork(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + network.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + network.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * The deploy the network in Openstack.
     * @params claudiaData
     * @params network
     */
    public void deployRouter(ClaudiaData claudiaData, Router router) throws InfrastructureException {
        log.info("Deploy router " + router.getName() + " for user " + claudiaData.getUser().getTenantName());

        try {
            String response = openStackUtil.createRouter(router.getName(), router.getIdNetwork(),
                    claudiaData.getUser());

            JSONObject networkString = new JSONObject(response);
            String id = networkString.getJSONObject("router").getString("id");
            log.debug("Router id " + id);
            router.setIdRouter(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * The deploy the subnet in Openstack.
     * @params claudiaData
     * @params subNet
     */
    public void deploySubNetwork(ClaudiaData claudiaData, SubNetwork subNet) throws InfrastructureException {
        log.info("Deploy network " + subNet.getName() + " for user " + claudiaData.getUser().getTenantName());
        String response;
        try {
            response = openStackUtil.createSubNet(subNet.getName(), subNet.getIdNetwork(), subNet.getCidr(), claudiaData.getUser());
            // "network-" + claudiaData.getUser().getTenantName()
            JSONObject networkString = new JSONObject(response);
            String id = networkString.getJSONObject("subnet").getString("id");
            log.debug("Network id " + id);
            subNet.setIdSubNet(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + subNet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + subNet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }
    /**
     * It destroys the network.
     * @params claudiaData
     * @params network
     */
    public void destroyNetwork(ClaudiaData claudiaData, Network network) throws InfrastructureException {

        try {
            openStackUtil.deleteNetwork(network.getIdNetwork(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to delete the network " + network.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    public void destroyRouter(ClaudiaData claudiaData, Router router) {
        // TODO Auto-generated method stub

    }

    /**
     * It destroys the network.
     * @params claudiaData
     * @params network
     */
    public void destroySubNetwork(ClaudiaData claudiaData, SubNetwork subnet) throws InfrastructureException {

        try {
            openStackUtil.deleteNetwork(subnet.getIdSubNet(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to delete the network " + subnet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * It loads all networks.
     * @params claudiaData
     */
    public List<Network> loadAllNetwork(ClaudiaData claudiaData) throws OpenStackException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * It obtains information about the network.
     * @params claudiaData
     * @params network
     * @return network information
     */
    public String loadNetwork(ClaudiaData claudiaData, Network network) throws EntityNotFoundException {
        String response = "";
        try {
            response = openStackUtil.getNetworkDetails(network.getIdNetwork(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to obtain the network infromation " + network.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new EntityNotFoundException(Network.class, msm, e);
        }
        return response;
    }

    /**
     * Set the variable.
     * @params openStackUtil
     */
    public void setOpenStackUtil(OpenStackUtil openStackUtil) {
        this.openStackUtil = openStackUtil;
    }

}
