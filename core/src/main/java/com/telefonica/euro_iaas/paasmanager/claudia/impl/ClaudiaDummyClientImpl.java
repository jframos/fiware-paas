/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OSNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.VMStatusNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.FileUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ClaudiaDummyClientImpl implements ClaudiaClient {

    private SystemPropertiesProvider systemPropertiesProvider;
    private FileUtils fileUtils = null;

    public String onOffScalability(ClaudiaData claudiaData, String environmentName, boolean b)
            throws InfrastructureException {
        return null;
    }

    public String browseService(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        String payload = null;
        try {
            payload = fileUtils.readFile(systemPropertiesProvider.getProperty("vappTestServiceLocation"));
        } catch (FileUtilsException e) {
            throw new ClaudiaResourceNotFoundException("Error in the Claudia Dummy Utils " + e.getMessage());
        }
        return payload;
    }

    public String browseVDC(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return "OK";
    }

    public String browseVM(String org, String vdc, String service, String vm, PaasManagerUser user)
            throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    public String browseVMReplica(ClaudiaData claudiaData, String tierName, int replica, VM vm)
            throws ClaudiaResourceNotFoundException {

        String payload = null;
        String ip = systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");

        try {
            payload = fileUtils.readFile(systemPropertiesProvider.getProperty("neoclaudiaVappVMLocation"))
                    .replace("{org}", claudiaData.getOrg()).replace("{vdc}", claudiaData.getVdc())
                    .replace("{service}", claudiaData.getService()).replace("{vm}", tierName).replace("{replica}", "1")
                    .replace("{IP}", ip);

        } catch (FileUtilsException e) {
            throw new ClaudiaResourceNotFoundException("Error in the Claudia Dummy Utils " + e.getMessage());
        }
        return payload;
    }

    public String deployService(ClaudiaData claudiaData, String ovf) throws InfrastructureException {
        // TODO Auto-generated method stub

        return "<task href=\"http://localhost:8081/paasmanager/rest/vdc/hola/task/65\" startTime=\"2013-02-11T11:29:44.713+01:00\" status=\"SUCESS\"> "
                + "<description>Create environment testtomcatsap8</description> " + "<vdc>hola</vdc> " + "</task>";
    }

    public void undeployService(ClaudiaData claudiaData) throws InfrastructureException {
        return;
    }

    public String deployVDC(ClaudiaData claudiaData, String cpu, String mem, String disk)
            throws InfrastructureException {
        // TODO Auto-generated method stub
        return "NO used";
    }

    public String deployVM(String org, String vdc, String service, String vmName, PaasManagerUser user, String vmPath)
            throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public void deployVM(ClaudiaData claudiaData, Tier tier, int replica, VM vm) throws InfrastructureException {
        // TODO Auto-generated method stub
        return;
        // return
        // "<task href=\"http://130.206.80.112:8080/paasmanager/rest/vdc/test1/task/35\" startTime=\"2012-11-22T10:29:20.746+01:00\" status=\"success\">"+
        // " <description>Create environment testtomcatsap5</description>"+
        // " <vdc>test1</vdc>    </task>";
    }

    public String getVApp(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException,
            OSNotRetrievedException {

        FileUtils fileUtils = null;
        String payload = null;
        String ip = systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");

        try {
            payload = fileUtils.readFile("VappTemplate.xml", "./src/main/resources").replace("{org}", org)
                    .replace("{vdc}", vdc).replace("{service}", service).replace("{vm}", vmName)
                    .replace("{replica}", "1").replace("{IP}", ip);

        } catch (FileUtilsException e) {

            throw new IPNotRetrievedException("Error in the Claudia Dummy Utils " + e.getMessage());
        }
        return payload;
    }

    public List<String> getIP(ClaudiaData claudiaData, String tierName, int replica, VM vm)
            throws InfrastructureException {

        List<String> ips = new ArrayList<String>();
        ips.add(systemPropertiesProvider.getProperty("IP_VM_DummyClaudia"));
        return ips;
    }

    public String obtainIPFromFqn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException {

        String ip = systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");
        return ip;
    }

    public String obtainOS(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws OSNotRetrievedException, ClaudiaResourceNotFoundException {

        return "95";
    }

    public void undeployVM(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {
        return;

    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public SystemPropertiesProvider getSystemPropertiesProvider() {
        return this.systemPropertiesProvider;
    }

    public void setFileUtils(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public FileUtils getFileUtils() {
        return this.fileUtils;
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
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#switchVMOn (java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String switchVMOn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public String deployServiceFull(ClaudiaData claudiaData, String property) {
        // TODO Auto-generated method stub
        return null;
    }

    public String createImage(ClaudiaData claudiaData, TierInstance tierInstance) throws ClaudiaRetrieveInfoException {
        // TODO Auto-generated method stub
        return null;
    }

    public void undeployVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#findAllVMs(com.telefonica.euro_iaas.paasmanager.model
     * .ClaudiaData)
     */
    public List<String> findAllVMs(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
