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

    public String browseVMReplica(ClaudiaData claudiaData, String tier, int replica, VM vm, String region)
            throws ClaudiaResourceNotFoundException {

        String response = "No Content";
        try {
            String token = claudiaData.getUser().getToken();
            String vdc = claudiaData.getVdc();
            response = openStackUtil.getServer(vm.getVmid(), region, token, vdc);
        } catch (OpenStackException e) {

            String errorMessage = "Error obtaining info from Server " + vm.getVmid();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);

        }
        return response;
    }

    /**
     * Build the payload to deploy a VM (createServer).
     * 
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVMReplica (java.lang.String,
     *      java.lang.String)
     */
    private String buildCreateServerPayload(ClaudiaData claudiaData, TierInstance tierInstance, int replica)
            throws InfrastructureException {

        if ((tierInstance.getTier().getImage() == null) || (tierInstance.getTier().getFlavour() == null)
                || (tierInstance.getTier().getKeypair() == null)) {
            String errorMsg = " The tier does not include a not-null information: " + "Image: "
                    + tierInstance.getTier().getImage() + "Flavour: " + tierInstance.getTier().getFlavour()
                    + "KeyPair: " + tierInstance.getTier().getKeypair();
            log.error(errorMsg);
            throw new InfrastructureException(errorMsg);
        }

        String name = claudiaData.getService() + "-" + tierInstance.getTier().getName() + "-" + replica;
        tierInstance.getTier().setName(
                claudiaData.getService() + "-" + tierInstance.getTier().getName() + "-" + replica);
        String payload = tierInstance.toJson();
        log.debug("Payload " + payload);
        log.debug("Floating ip " + tierInstance.getTier().getFloatingip());

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
                String response = openStackUtil.getServer(tierInstance.getVM().getVmid(), tierInstance.getTier()
                        .getRegion(), claudiaData.getUser().getToken(), claudiaData.getUser().getToken());
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

    /*
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployVM(com
     * .telefonica.euro_iaas.paasmanager.model.ClaudiaData, java.lang.String)
     */
    public void deployVM(ClaudiaData claudiaData, TierInstance tierInstance, int replica, VM vm)
            throws InfrastructureException {
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

        log.debug("Deploy server " + claudiaData.getService() + " tier " + tierInstance.getName() + " replica "
                + replica + " with networks " + tierInstance.getNetworkInstances());

        String payload = buildCreateServerPayload(claudiaData, tierInstance, replica);

        try {

            String region = tierInstance.getTier().getRegion();
            String token = claudiaData.getUser().getToken();
            String vdc = tierInstance.getTier().getVdc();
            String serverId = openStackUtil.createServer(payload, region, token, vdc);
            if (tierInstance.getTier().getFloatingip().equals("true")) {
                String floatingIP = openStackUtil.getFloatingIP(region, token, vdc);
                openStackUtil.assignFloatingIP(serverId, floatingIP, region, token, vdc);
            }
            vm.setVmid(serverId);
        } catch (OpenStackException e) {
            String errorMessage = "Error interacting with OpenStack ";
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);

        }

    }

    @Override
    public void undeployVM(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void undeployService(ClaudiaData claudiaData) throws InfrastructureException {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String obtainIPFromFqn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String obtainOS(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws OSNotRetrievedException, ClaudiaResourceNotFoundException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getVApp(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException,
            OSNotRetrievedException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String onOffScalability(ClaudiaData claudiaData, String environmentName, boolean b)
            throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String createImage(ClaudiaData claudiaData, TierInstance tierInstance) throws ClaudiaRetrieveInfoException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String switchVMOn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String obtainVMStatus(String vapp) throws VMStatusNotRetrievedException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> findAllVMs(ClaudiaData claudiaData, String region) throws ClaudiaResourceNotFoundException {
        List<String> vmIds;

        String response = "No Content";
        try {
            String token = claudiaData.getUser().getToken();
            String vdc = claudiaData.getVdc();
            response = openStackUtil.listServers(region, token, vdc);
            vmIds = fromJson(response);
        } catch (OpenStackException e) {
            String errorMessage = "Error obtaining list of Servers for tenant-ID "
                    + claudiaData.getUser().getTenantId();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }
        return vmIds;
    }

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

    public List<String> getIP(ClaudiaData claudiaData, String tierName, int replica, VM vm, String region)
            throws InfrastructureException {
        List<String> ips = new ArrayList<String>();

        try {
            String token = claudiaData.getUser().getToken();
            String vdc = claudiaData.getVdc();
            String response = openStackUtil.getServer(vm.getVmid(), region, token, vdc);
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

    @Override
    public String deployServiceFull(ClaudiaData claudiaData, String ovf) throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param openStackUtil
     *            the openStackUtil to set
     */
    public void setOpenStackUtil(OpenStackUtil openStackUtil) {
        this.openStackUtil = openStackUtil;
    }

    @Override
    public String browseVDC(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String browseService(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String browseVM(String org, String vdc, String service, String vm, PaasManagerUser user)
            throws ClaudiaResourceNotFoundException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String deployService(ClaudiaData claudiaData, String ovf) throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String deployVM(String org, String vdc, String service, String vmName, PaasManagerUser user, String vmPath)
            throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    public void undeployVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        try {

            String region = tierInstance.getTier().getRegion();
            String token = claudiaData.getUser().getToken();
            String vdc = tierInstance.getTier().getVdc();
            openStackUtil.deleteServer(tierInstance.getVM().getVmid(), region, token, vdc);
            checkDeleteServerTaskStatus(tierInstance, claudiaData);
        } catch (OpenStackException oes) {
            String errorMessage = "Error deleting serverId: " + tierInstance.getVM().getVmid();
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        }
    }

    @Override
    public String deployVDC(ClaudiaData claudiaData, String cpu, String mem, String disk)
            throws InfrastructureException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }
}
