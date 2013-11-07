/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OSNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.exception.VMStatusNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

/**
 * @author jesus.movilla
 */
public class ClaudiaClientOpenStackImpl implements ClaudiaClient {

    /**
     * The log.
     */

    private static Logger log = Logger.getLogger(ClaudiaClientOpenStackImpl.class);

    private OpenStackUtil openStackUtil = null;
    private final int POLLING_INTERVAL = 10000;

    public String browseService(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    public String browseVDC(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseVDC(
     * com.telefonica.euro_iaase.paasmanager.model.ClaudiaData)
     */

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseVM(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser)
     */
    public String browseVM(String org, String vdc, String service, String vm, PaasManagerUser user)
    throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseVMReplica
     * (com.telefonica.euro_iaas.paasmanager.model.ClaudiaData, java.lang.String)
     */
    public String browseVMReplica(ClaudiaData claudiaData, String tier, int replica, VM vm)
    throws ClaudiaResourceNotFoundException {

        String response = "No Content";
        try {
            response = openStackUtil.getServer(vm.getVmid(), claudiaData.getUser());
        } catch (OpenStackException e) {

            String errorMessage = "Error obtaining info from Server " + vm.getVmid();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
            // if (e.getMessage().contains("itemNotFound"))
            // break;

        }
        return response;
    }

    public String browseVMReplica(ClaudiaData claudiaData, TierInstance tierInstance)
    throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Build the payload to deploy a VM (createServer).
     * 
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVMReplica (java.lang.String,
     *      java.lang.String)
     */
    private String buildCreateServerPayload(ClaudiaData claudiaData, TierInstance tierInstance, int replica)
    throws InfrastructureException {

        if ((tierInstance.getTier().getImage() == null) || (tierInstance.getTier().getFlavour() == null) || (tierInstance.getTier().getKeypair() == null)) {
            String errorMsg = " The tier does not include a not-null information: " + "Image: " + tierInstance.getTier().getImage()
            + "Flavour: " + tierInstance.getTier().getFlavour() + "KeyPair: " + tierInstance.getTier().getKeypair();
            log.error(errorMsg);
            throw new InfrastructureException(errorMsg);
        }

        String name = claudiaData.getService() + "-" + tierInstance.getTier().getName() + "-" + replica;
        tierInstance.getTier().setName(claudiaData.getService() + "-" + tierInstance.getTier().getName() + "-" + replica);
        String payload = tierInstance.toJson();
        log.debug("Payload " + payload);

        return payload;
    }

    /**
     * Checks if a certain Server has been finally deleted from OpenStack.
     * 
     * @param tierInstance
     * @param claudiaData
     */
    private void checkDeleteServerTaskStatus(TierInstance tierInstance, ClaudiaData claudiaData)
    throws InfrastructureException {

        while (true) {
            try {
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException e) {
                    String errorMessage = "Interrupted Exception during polling";
                    log.warn(errorMessage);
                    throw new InfrastructureException(errorMessage);
                }
                String response = openStackUtil.getServer(tierInstance.getVM().getVmid(), claudiaData.getUser());
            } catch (OpenStackException e) {
                String errorMessage = "Error obtaining info from Server " + tierInstance.getVM().getVmid();
                log.error(errorMessage);

                if (e.getMessage().contains("itemNotFound")) {
                    break;
                }
                // throw new InfrastructureException(errorMessage);
            }
        }
    }

    public String createImage(ClaudiaData claudiaData, TierInstance tierInstance) throws ClaudiaRetrieveInfoException {
        return null;
    }

    public String deployService(ClaudiaData claudiaData, String ovf) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public String deployServiceFull(ClaudiaData claudiaData, String property) {
        // TODO Auto-generated method stub
        return null;
    }

    public String deployVDC(ClaudiaData claudiaData, String cpu, String mem, String disk)
    throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployVM(com
     * .telefonica.euro_iaas.paasmanager.model.ClaudiaData, java.lang.String)
     */
    public void deployVM(ClaudiaData claudiaData, TierInstance tierInstance, int replica, VM vm) throws InfrastructureException {
        // URL:http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers
        // Headers
        // X-Auth-Token: 30e2a5dd40b3453b833780657a253ec9
        // Content-Type: application/json
        // Payload
        // {"server": {"name": "jesus5",
        // "imageRef": "44dcdba3-a75d-46a3-b209-5e9035d2435e",
        // "flavorRef": "2","key_name":"jesusmovilla"},
        // "security_group": "testjesus"}

        // openStackUtilImpl = new OpenStackUtilImpl(claudiaData.getUser());

        log.debug("Deploy server " + claudiaData.getService() + " tier " + tierInstance.getName() + " replica " + replica);
        String payload = buildCreateServerPayload(claudiaData, tierInstance, replica);

        try {

            String serverId = openStackUtil.createServer(payload, claudiaData.getUser());
            if (tierInstance.getTier().getFloatingip().equals("true")) {
                String floatingIP = openStackUtil.getFloatingIP(claudiaData.getUser());
                openStackUtil.assignFloatingIP(serverId, floatingIP, claudiaData.getUser());
            }
            vm.setVmid(serverId);
        } catch (OpenStackException e) {
            String errorMessage = "Error interacting with OpenStack ";
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);

        }

    }

    public String deployVM(String org, String vdc, String service, String vmName, PaasManagerUser user, String vmPath)
    throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> findAllVMs(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        List<String> vmIds = new ArrayList<String>();

        String response = "No Content";
        try {
            response = openStackUtil.listServers(claudiaData.getUser());
            vmIds = fromJson(response);
        } catch (OpenStackException e) {
            String errorMessage = "Error obtaining list of Servers for tenant-ID "
                + claudiaData.getUser().getTenantId();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
            // if (e.getMessage().contains("itemNotFound"))
            // break;
        }
        return vmIds;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVMReplica (java.lang.String,
     * java.lang.String)
     */

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainOS(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser)
     */

    private List<String> fromJson(String listServersResponse) {
        List<String> names = new ArrayList<String>();
        net.sf.json.JSONObject jsonNodeServers = net.sf.json.JSONObject.fromObject(listServersResponse);
        JSONArray jsonServersList = jsonNodeServers.getJSONArray("servers");

        for (Object o : jsonServersList) {
            SecurityGroup secGroup = new SecurityGroup();
            net.sf.json.JSONObject jsonSecGroup = (net.sf.json.JSONObject) o;
            String vmId = jsonSecGroup.getString("name");
            names.add(vmId);
        }
        return names;
    }

    public List<String> getIP(ClaudiaData claudiaData, String tierName, int replica, VM vm)
    throws InfrastructureException {
        List<String> ips = new ArrayList<String>();

        try {
            String response = openStackUtil.getServer(vm.getVmid(), claudiaData.getUser());
            String[] ipsResponse = response.split("addr=");

            for (int i = 1; i < ipsResponse.length; i++) {
                String ip = ipsResponse[i].split("\"")[1];
                ips.add(ip);
            }

        } catch (OpenStackException oes) {
            String errorMessage = "Error interacting getting ip from OpenStack ";
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        }
        return ips;
    }

    public String getVApp(String org, String vdc, String service, String vmName, PaasManagerUser user)
    throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException,
    OSNotRetrievedException {
        // TODO Auto-generated method stub
        return null;
    }

    public String obtainIPFromFqn(String org, String vdc, String service, String vmName, PaasManagerUser user)
    throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException {
        // TODO Auto-generated method stub
        return null;
    }

    public String obtainOS(String org, String vdc, String service, String vmName, PaasManagerUser user)
    throws OSNotRetrievedException, ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainVMStatus (java.lang.String)
     */
    public String obtainVMStatus(String vapp) throws VMStatusNotRetrievedException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#OnOffScalability
     * (com.telefonica.euro_iaas.paasmanager.model.ClaudiaData, java.lang.String, boolean)
     */
    public String onOffScalability(ClaudiaData claudiaData, String environmentName, boolean b)
    throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param openStackUtil
     *            the openStackUtil to set
     */
    public void setOpenStackUtil(OpenStackUtil openStackUtil) {
        this.openStackUtil = openStackUtil;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#switchVMOn (java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser)
     */
    public String switchVMOn(String org, String vdc, String service, String vmName, PaasManagerUser user)
    throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public void undeployService(ClaudiaData claudiaData) throws InfrastructureException {
    }

    /*
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVM (java.lang.String)
     */
    public void undeployVM(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

    }

    public void undeployVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        try {
            openStackUtil.deleteServer(tierInstance.getVM().getVmid(), claudiaData.getUser());
            checkDeleteServerTaskStatus(tierInstance, claudiaData);
        } catch (OpenStackException oes) {
            String errorMessage = "Error deleting serverId: " + tierInstance.getVM().getVmid();
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        }
    }
}
