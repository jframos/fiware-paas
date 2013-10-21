/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_CPU;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_DISK;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_MEM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Template;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.monitoring.MonitoringClient;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.VappUtils;

public class InfrastructureManagerServiceClaudiaImpl implements InfrastructureManager {

    private static final long POLLING_INTERVAL = 10000;

    private SystemPropertiesProvider systemPropertiesProvider;
    private ClaudiaClient claudiaClient;
    private MonitoringClient monitoringClient;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;
    private ClaudiaUtil claudiaUtil;

    private VappUtils vappUtils;
    private OVFUtils ovfUtils;

    private TierInstanceManager tierInstanceManager;
    private EnvironmentUtils environmentUtils;

    /** The log. */
    private static Logger log = Logger.getLogger(InfrastructureManagerServiceClaudiaImpl.class);

    /** Max lenght of an OVF. */
    private static final Integer MAX_SIZE = 90000;

    /**
     * Deploy a VM from an ovf.
     * 
     * @throws InfrastructureException
     */
    private String browseService(ClaudiaData claudiaData) throws InfrastructureException {

        String browseServiceResponse = null;
        try {
            browseServiceResponse = claudiaClient.browseService(claudiaData);
            browseServiceResponse = getProcessService(claudiaData);
        } catch (ClaudiaResourceNotFoundException crnfe) {
            String errorMessage = "Resource associated to org:" + claudiaData.getOrg() + " vdc:" + claudiaData.getVdc()
                    + " service:" + claudiaData.getService() + " Error Description: " + crnfe.getMessage();
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unknown exception when retriving vapp " + " associated to org:"
                    + claudiaData.getOrg() + " vdc:" + claudiaData.getVdc() + " service:" + claudiaData.getService()
                    + " Error Description: " + e.getMessage();
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        }
        return browseServiceResponse;
    }

    /**
     * @param taskUrl
     * @throws InfrastructureException
     */
    private void checkTaskResponse(ClaudiaData claudiaData, String taskUrl) throws InfrastructureException {
        while (true) {
            String claudiaTask;
            try {
                claudiaTask = claudiaUtil.getClaudiaResource(claudiaData.getUser(), taskUrl, MediaType.WILDCARD);

                if (claudiaTask.contains("success")) {
                    try {
                        Thread.sleep(POLLING_INTERVAL);
                    } catch (InterruptedException e) {
                        String errorThread = "Thread Interrupted Exception " + "during polling";
                        log.warn(errorThread);
                        throw new InfrastructureException(errorThread);
                    }
                    break;
                } else if (claudiaTask.contains("error")) {
                    String errorMessage = "Error checking task " + taskUrl;
                    log.error(errorMessage);
                    throw new InfrastructureException(errorMessage);
                }
            } catch (ClaudiaRetrieveInfoException e1) {
                String errorMessage = "Error checking task " + taskUrl;
                log.error(errorMessage);
                throw new InfrastructureException(errorMessage);
            } catch (ClaudiaResourceNotFoundException e) {
                String errorMessage = "Error checking task " + taskUrl;
                log.error(errorMessage);
                throw new InfrastructureException(errorMessage);
            }
            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {
                String errorMessage = "Interrupted Exception during polling";
                log.warn(errorMessage);
                throw new InfrastructureException(errorMessage);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager# cloneTemplate(java.lang.String)
     */
    @Async
    public TierInstance cloneTemplate(String templateName) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<VM> createEnvironment(EnvironmentInstance envInstance, String ovf, ClaudiaData claudiaData)
            throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager# createEnvironment(java.lang.String)
     */
    public List<VM> createEnvironment(EnvironmentInstance envInstance, Tier tier, String ovf, ClaudiaData claudiaData)
            throws InfrastructureException {

        return null;
    }

    public EnvironmentInstance createInfrasctuctureEnvironmentInstance(EnvironmentInstance environmentInstance,
            List<Tier> tiers, ClaudiaData claudiaData) throws InfrastructureException, InvalidVappException,
            InvalidOVFException {
        environmentInstance.setName(claudiaData.getVdc() + "-" + environmentInstance.getEnvironment().getName());
        // claudiaData.setService(environmentInstance.getEnvironment().getName());

        // Deploy MVs
        deployVDC(claudiaData);
        deployService(claudiaData, environmentInstance.getEnvironment().getOvf());

        String vappService = this.browseService(claudiaData);

        List<TierInstance> tierInstances = null;
        try {
            tierInstances = fromVappToListTierInstance(vappService, environmentInstance.getEnvironment(), claudiaData);
        } catch (InvalidVappException e) {
            throw new InvalidVappException("Error parsing the Vapp " + e.getMessage());
        } catch (InvalidOVFException e1) {
            throw new InvalidVappException("Error obtaining the OVF for the Tier " + e1.getMessage());
        }

        for (TierInstance tierInstance : tierInstances) {

            if (tierInstance.getOvf() != null) {
                String newOvf = vappUtils.getMacroVapp(tierInstance.getOvf(), environmentInstance, tierInstance);
                tierInstance.setOvf(newOvf);
            }

            try {
                tierInstance = insertTierInstanceBD(claudiaData, environmentInstance.getEnvironment().getName(),
                        tierInstance);
            } catch (EntityNotFoundException e) {
                throw new InfrastructureException(e);
            } catch (InvalidEntityException e) {
                throw new InfrastructureException(e);
            } catch (AlreadyExistsEntityException e) {
                throw new InfrastructureException(e);
            }
            environmentInstance.addTierInstance(tierInstance);
        }
        environmentInstance.setVapp(vappService);
        return environmentInstance;
    }

    @Async
    public Template createTemplate(TierInstance tierInstance) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    public TierInstance createTierInstanceFromVapp(Tier tier, String environmentName, ClaudiaData claudiaData,
            String vapp, String vmOVF) throws InvalidVappException {
        log.info("createTierInstanceFromVapp " + tier.getName() + "  env " + environmentName + " size vapp "
                + vapp.length());
        TierInstance tierInstance = new TierInstance();
        String fqnId = null;
        try {
            fqnId = vappUtils.getFqnId(vapp);
        } catch (InvalidVappException e) {
            throw new InvalidVappException("Error to obtain the fqn from the VApp " + e.getMessage());
        }
        String vmName = vappUtils.getVMName(fqnId);

        String replica = vappUtils.getReplica(fqnId);

        VM vm = null;
        try {
            vm = getVMFromVapp(vapp);

            // claudiaData.setVm(vmName);
        } catch (InvalidVappException e) {
            throw new InvalidVappException("Error to obtain the VM from the VApp " + e.getMessage());
        }

        int numReplica = new Integer(replica).intValue();

        tierInstance.setVM(vm);
        tierInstance.setNumberReplica(numReplica);
        tierInstance.setVdc(claudiaData.getVdc());
        tierInstance.setOvf(vmOVF);
        tierInstance.setVapp(vapp);

        tierInstance.setTier(tier);

        tierInstance.setName(environmentName + "-" + tier.getName() + "-" + numReplica);

        return tierInstance;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager# deleteEnvironment
     * (com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance)
     */
    public void deleteEnvironment(ClaudiaData claudiaData, EnvironmentInstance envInstance)
            throws InfrastructureException {

        // Sacar todas la VMs del EnvironmentInstance y borrarlas
        // List<VM> vms = new ArrayList<VM>();
        List<TierInstance> tierInstances = envInstance.getTierInstances();

        if (tierInstances == null) {
            return;
        }
        for (int i = 0; i < tierInstances.size(); i++) {
            TierInstance tierInstance = tierInstances.get(i);
            // vms.add(tierInstance.getVM());
            claudiaClient.undeployVMReplica(claudiaData, tierInstance);
            monitoringClient.stopMonitoring(tierInstance.getVM().getFqn());
        }

        String fqn = envInstance.getTierInstances().get(0).getVM().getFqn();
        String service = URICreation.getService(fqn);

        // claudiaData.setService(service);
        // claudiaData.setFqn(fqn);
        // claudiaData.setReplica("1");

        claudiaClient.undeployService(claudiaData);

        // Delete all VM
        /*
         * for (int i = 0; i < vms.size(); i++) { claudiaClient.undeployVMReplica(claudiaData, tierInstance);
         * monitoringClient.stopMonitoring(vms.get(i).getFqn()); }
         */
    }

    public void deleteVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

    }

    /**
     * Cretae a Service.
     * 
     * @throws InfrastructureException
     */
    public void deployService(ClaudiaData claudiaData, String ovf) throws InfrastructureException {

        // Service
        log.debug("deployService " + ovf);
        String serviceResponse = null;
        try {
            serviceResponse = claudiaClient.browseService(claudiaData);
            log.debug("Deploy service " + serviceResponse);
        } catch (ClaudiaResourceNotFoundException e) {

            claudiaClient.deployServiceFull(claudiaData, ovf);

        }
    }

    /**
     * Create an VDC if it is not created.
     * 
     * @throws InfrastructureException
     */
    public void deployVDC(ClaudiaData claudiaData) throws InfrastructureException {
        // VDC
        log.warn("Deploy VDC");
        try {
            claudiaClient.browseVDC(claudiaData);
        } catch (ClaudiaResourceNotFoundException e) {

            String deployVDCResponse = claudiaClient.deployVDC(claudiaData,
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_CPU),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_MEM),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_DISK));
            String vdcTaskUrl = claudiaResponseAnalyser.getTaskUrl(deployVDCResponse);

            if (claudiaResponseAnalyser.getTaskStatus(deployVDCResponse).equals("error")) {
                String errorMes = "Error deploying VDC " + claudiaData.getVdc();
                log.error(errorMes);
                throw new InfrastructureException(errorMes);
            }
            if (!(claudiaResponseAnalyser.getTaskStatus(deployVDCResponse).equals("success"))) {
                checkTaskResponse(claudiaData, vdcTaskUrl);
            }
        }
    }

    public void deployVM(ClaudiaData claudiaData, Tier tier, int replica, String vmOVF, VM vm)
            throws InfrastructureException {
        return;
    }

    public List<TierInstance> fromVappToListTierInstance(String vappService, Environment environment,
            ClaudiaData claudiaData) throws InvalidVappException, InvalidOVFException {
        log.info("fromVappToListTierInstance");

        List<TierInstance> tierInstances = new ArrayList<TierInstance>();
        List<String> vappSingleVM = null;
        try {
            vappSingleVM = vappUtils.getVappsSingleVM(claudiaData, vappService);
        } catch (InvalidVappException e) {
            String errorMessage = "Error splitting up the main ovf in single" + "VM ovfs. Description. "
                    + e.getMessage();
            log.error(errorMessage);

        }

        List<String> ovfSingleVM;
        try {
            ovfSingleVM = ovfUtils.getOvfsSingleVM(environment.getOvf());
        } catch (InvalidOVFException e) {
            String errorMessage = "Error splitting up the main ovf in single" + "VM ovfs. Description. "
                    + e.getMessage();
            log.error(errorMessage);
            throw new InvalidOVFException(errorMessage);
        }

        for (Tier tier : environment.getTiers()) {

            String xVapp = "";
            String ovf = "";
            for (int i = 0; i < vappSingleVM.size(); i++) {
                String vapptext = getVirtualSystemName(vappSingleVM.get(i));
                log.debug(vapptext + " " + tier.getName());
                if (vapptext.equals(tier.getName())) {
                    xVapp = vappSingleVM.get(i);

                }
            }

            if (xVapp.equals("")) {
                log.warn("It is not possible to obtain the IP from the VApp");
                throw new InvalidVappException("It is not possible to obtain the IP from the VApp");
            }
            for (int i = 0; i < ovfSingleVM.size(); i++) {
                if (ovfSingleVM.get(i).indexOf("ovf:VirtualSystem ovf:id=\"" + tier.getName()) != -1) {
                    ovf = ovfSingleVM.get(i);
                }
            }
            if (ovf.equals("")) {
                log.warn("It is not possible to obtain the ovf adecuated");
                throw new InvalidVappException("It is not possible to obtain the ovf adecuated");
            }

            TierInstance tierInstance = null;
            try {
                tierInstance = createTierInstanceFromVapp(tier, environment.getName(), claudiaData, xVapp, ovf);

                tierInstances.add(tierInstance);
            } catch (InvalidVappException e) {
                throw new InvalidVappException("Error to parse the VApp" + e.getMessage());
            }

        }
        return tierInstances;
    }

    public String getProcessService(ClaudiaData claudiaData) throws InfrastructureException {

        String serviceUrl = null;
        String serviceResponse = null;

        List<String> parameters = new ArrayList<String>();
        parameters.add(claudiaData.getOrg());
        parameters.add(claudiaData.getVdc());
        parameters.add(claudiaData.getService());

        try {
            serviceUrl = claudiaUtil.getUrl(parameters);
        } catch (URLNotRetrievedException e1) {
            throw new InfrastructureException("It is not possible to build the url for get service");
        }

        try {
            serviceResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), serviceUrl, MediaType.WILDCARD);
        } catch (ClaudiaRetrieveInfoException e1) {

            throw new InfrastructureException("It is not possible to get the url for get service " + serviceUrl);
        } catch (ClaudiaResourceNotFoundException e1) {
            throw new InfrastructureException("It is not possible to get the url for get service " + serviceUrl);
        }

        int cont = 0;

        while (cont <= 30) {
            try {
                getProcessVapp(claudiaData, serviceResponse);
                return serviceResponse;

            } catch (InvalidVappException e) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e2) {
                    log.warn("Interrupted Exception during polling");
                }
                try {
                    serviceResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), serviceUrl,
                            MediaType.WILDCARD);
                } catch (ClaudiaRetrieveInfoException e1) {

                    throw new InfrastructureException("It is not possible to get the url for get service " + serviceUrl);
                } catch (ClaudiaResourceNotFoundException e1) {
                    throw new InfrastructureException("It is not possible to get the url for get service " + serviceUrl);
                }
            }

            cont = cont + 1;
        }

        if (cont >= 30) {
            log.error("Error to deploy the service. It is not possible to get its information");
            throw new InfrastructureException(
                    "It is not possible to get the information about the service. It is not deployed " + serviceUrl);
        }
        return null;
    }

    public void getProcessVapp(ClaudiaData claudiaData, String vappService) throws InvalidVappException {
        List<String> vappSingleVM = null;
        try {
            vappSingleVM = vappUtils.getVappsSingleVM(claudiaData, vappService);
        } catch (InvalidVappException e) {
            throw new InvalidVappException("");

        }

        for (String vapp : vappSingleVM) {
            try {
                vappUtils.getIP(vapp);
            } catch (InvalidVappException e) {
                throw new InvalidVappException("");
            }
        }

    }

    private String getVirtualSystemName(String vapp) throws InvalidVappException {
        log.info("getVirtualSystemName ");
        String fqnId = null;
        try {
            fqnId = vappUtils.getFqnId(vapp);
        } catch (InvalidVappException e) {
            throw new InvalidVappException("Error to obtain the fqn from the VApp " + e.getMessage());
        }
        log.info("getVirtualSystemName " + fqnId);
        String vmName = vappUtils.getVMName(fqnId);
        log.info("getVirtualSystemName " + vmName);
        return vmName;
    }

    public VM getVMFromVapp(String vapp) throws InvalidVappException {

        log.info("getVMFromVapp ");
        HashMap<String, String> ips = vappUtils.getNetworkAndIP(vapp);
        VM vm = new VM();

        Iterator it = ips.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            vm.addNetwork((String) e.getKey(), (String) e.getValue());
            vm.setIp((String) e.getValue());
        }

        String fqnId = vappUtils.getFqnId(vapp);

        String vmName = vappUtils.getVMName(fqnId);

        log.info("fqn replica " + fqnId);

        vm.setFqn(fqnId);

        vm.setHostname(vmName);

        return vm;
    }

    public String ImageScalability(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        String scaleResponse;
        try {
            scaleResponse = claudiaClient.createImage(claudiaData, tierInstance);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorMessage = "Error creating teh image of the VM with the " + "fqn: "
                    + tierInstance.getVM().getFqn() + ". Descrption. " + e.getMessage();
            log.error(errorMessage);
            throw new InfrastructureException(errorMessage);
        }
        return scaleResponse;
    }

    private TierInstance insertTierInstanceBD(ClaudiaData claudiaData, String envName, TierInstance tierInstance)
            throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException {

        TierInstance tierInstanceDB = null;
        if (tierInstance.getOvf() != null && tierInstance.getOvf().length() > MAX_SIZE) {
            String vmOVF = tierInstance.getOvf();
            while (vmOVF.contains("ovfenvelope:ProductSection"))
                vmOVF = environmentUtils.deleteProductSection(vmOVF);
            tierInstance.setOvf(vmOVF);
        }
        try {
            tierInstanceDB = tierInstanceManager.load(tierInstance.getName());
        } catch (EntityNotFoundException e) {
            tierInstanceDB = tierInstanceManager.create(claudiaData, envName, tierInstance);
            // El ovf no puede superar un m�ximo de caracteres
        }
        return tierInstanceDB;
    }

    /**
     * @param claudiaClient
     *            the claudiaClient to set
     */

    public void setClaudiaClient(ClaudiaClient claudiaClient) {
        this.claudiaClient = claudiaClient;
    }

    /**
     * @param claudiaResponseAnalyser
     *            the ClaudiaResponseAnalyser to set
     */
    public void setClaudiaResponseAnalyser(ClaudiaResponseAnalyser claudiaResponseAnalyser) {
        this.claudiaResponseAnalyser = claudiaResponseAnalyser;
    }

    /**
     * @param claudiaUtil
     *            the claudiaUtil to set
     */
    public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
        this.claudiaUtil = claudiaUtil;
    }

    /**
     * @param environmentUtils
     *            the EnvironmentUtils to set
     */
    public void setEnvironmentUtils(EnvironmentUtils environmentUtils) {
        this.environmentUtils = environmentUtils;
    }

    /**
     * @param monitoringClient
     *            the monitoringClient to set
     */
    public void setMonitoringClient(MonitoringClient monitoringClient) {
        this.monitoringClient = monitoringClient;
    }

    /**
     * @param ovfUtils
     *            the OVFUtils to set
     */
    public void setOvfUtils(OVFUtils ovfUtils) {
        this.ovfUtils = ovfUtils;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param tierInstanceManager
     *            the TierInstanceManager to set
     */
    public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
        this.tierInstanceManager = tierInstanceManager;
    }

    /**
     * @param vappUtils
     *            the VappUtils to set
     */
    public void setVappUtils(VappUtils vappUtils) {
        this.vappUtils = vappUtils;
    }

    public String StartStopScalability(ClaudiaData claudiaData, boolean b) throws InfrastructureException {
        String scalalility = claudiaClient.onOffScalability(claudiaData, claudiaData.getService(), b);
        return scalalility;
    }

}
