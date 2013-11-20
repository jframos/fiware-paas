/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
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
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_OVFSERVICE_LOCATION;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_CPU;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_DISK;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_MEM;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.VM_DEPLOYMENT_DELAY;

public class InfrastructureManagerClaudiaImpl implements InfrastructureManager {

    private static final long POLLING_INTERVAL = 10000;

    private SystemPropertiesProvider systemPropertiesProvider;
    private ClaudiaClient claudiaClient;
    private MonitoringClient monitoringClient;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;
    private ClaudiaUtil claudiaUtil;
    private OVFUtils ovfUtils;
    private VappUtils vappUtils;
    private TierInstanceManager tierInstanceManager;
    private TierManager tierManager;
    private EnvironmentUtils environmentUtils;
    private EnvironmentInstanceDao environmentInstanceDao;
    private NetworkInstanceManager networkInstanceManager;
    private NetworkManager networkManager;

    /** The log. */
    private static Logger log = Logger.getLogger(InfrastructureManagerClaudiaImpl.class);
    /** Max lenght of an OVF */
    private static final Integer tam_max = 90000;

    /**
     * deloy a VM from an ovf
     * 
     * @param org
     * @param vdc
     * @param service
     * @throws InfrastructureException
     */
    private String browseService(ClaudiaData claudiaData) throws InfrastructureException {

        String browseServiceResponse = null;
        try {
            browseServiceResponse = claudiaClient.browseService(claudiaData);
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

    public EnvironmentInstance createInfrasctuctureEnvironmentInstance
        (EnvironmentInstance environmentInstance, List<Tier> tiers, ClaudiaData claudiaData)
        throws InvalidOVFException, InfrastructureException, InvalidEntityException, EntityNotFoundException {

        // Deploy MVs
        log.debug("Creating infrastructure for environemnt instance " + environmentInstance.getBlueprintName());
        log.debug("Deploy VDC ");
        deployVDC(claudiaData);
        log.debug("Insert service ");
        insertService(claudiaData);

        log.debug("Getting OVF singels ");
        List<String> ovfSingleVM = null;
        try {
            ovfSingleVM = ovfUtils.getOvfsSingleVM(environmentInstance.getEnvironment().getOvf());
        } catch (InvalidOVFException e) {
            String errorMessage = "Error splitting up the main ovf in single" + "VM ovfs. Description. "
                    + e.getMessage();
            log.error(errorMessage);
            // throw new InfrastructureException(errorMessage);
        }
        int numberTier = 0;
        for (Tier tier : tiers) {
            for (int numReplica = 1; numReplica <= tier.getInitialNumberInstances(); numReplica++) {
                // claudiaData.setVm(tier.getName());
                log.debug("Deploying tier instance for tier " + tier.getName());

                TierInstance tierInstance = new TierInstance();
                tierInstance.setName(environmentInstance.getBlueprintName() + "-" + tier.getName() + "-" + numReplica);
                tierInstance.setNumberReplica(numReplica);
                tierInstance.setVdc(claudiaData.getVdc());
                tierInstance.setStatus(Status.DEPLOYING);
                tierInstance.setTier(tier);
                VM vm = new VM();
                String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc()
                        + ".services." + claudiaData.getService() + ".vees." + tier.getName() + ".replicas."
                        + numReplica;
                String hostname = (claudiaData.getService() + "-" + tier.getName() + "-" + numReplica).toLowerCase();
                vm.setFqn(fqn);
                vm.setHostname(hostname);
                tierInstance.setVM(vm);
                
                this.deployNetworks(claudiaData, tierInstance);
                log.debug ("Number of networks " + tierInstance.getNetworkInstances().size() + " floatin ip " + 
                   tierInstance.getTier().getFloatingip());

                try {
                    log.debug("Inserting in database ");
                    tierInstance = insertTierInstanceBD(claudiaData, environmentInstance.getEnvironment().getName(),
                            tierInstance);
                    log.debug ("Return: Number of networks " + tierInstance.getNetworkInstances().size() + " floatin ip " + 
                            tierInstance.getTier().getFloatingip());
                    environmentInstance.addTierInstance(tierInstance);
                    environmentInstanceDao.update(environmentInstance);
                } catch (EntityNotFoundException e) {
                    log.error("Entitiy NOt found: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                } catch (InvalidEntityException e) {
                    log.error("Invalid: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                } catch (AlreadyExistsEntityException e) {
                    log.error("AllReady found: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                }
                
                

                String vAppReplica = null;
                try {
                    if (ovfSingleVM == null || ovfSingleVM.size() == 0) {
                        deployVM(claudiaData, tierInstance, numReplica, null, vm);
                    } else {
                        deployVM(claudiaData, tierInstance, numReplica, ovfSingleVM.get(numberTier), vm);
                    }
                } catch (InfrastructureException e) {
                    log.error("Error deploying a VM: " + e.getMessage());
                    environmentInstance.setStatus(Status.ERROR);
                    throw new InfrastructureException(e);
                }
                if (!systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                    try {
                        vAppReplica = claudiaClient.browseVMReplica(claudiaData, tier.getName(), numReplica, vm);
                        // ip = vappUtils.getIP(vAppReplica);
                    } catch (ClaudiaResourceNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                log.debug("Tier instance name " + environmentInstance.getBlueprintName() + "-" + tier.getName() + "-"
                        + numReplica);

                if (ovfSingleVM != null) {
                    log.debug("Setting the ovf");
                    tierInstance.setOvf(ovfSingleVM.get(numberTier));
                }
                tierInstance.setVapp(vAppReplica);
                tierInstance.setVM(vm);

                try {
                    log.debug("Inserting in database ");
                    // tierInstance = insertTierInstanceBD(tierInstance);
                    tierInstance.setStatus(Status.DEPLOYED);
                    tierInstanceManager.update(claudiaData, environmentInstance.getEnvironment().getName(),
                            tierInstance);
                } catch (EntityNotFoundException e) {
                    log.debug("Entitiy NOt found: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                } catch (InvalidEntityException e) {
                    throw new InfrastructureException(e);
                } catch (AlreadyExistsEntityException e) {
                    throw new InfrastructureException(e);
                }

            }
            numberTier++;
        }

        for (TierInstance tierInstance : environmentInstance.getTierInstances()) {
            if (tierInstance.getOvf() != null && tierInstance.getOvf().length() > 0) {
                String newOvf = vappUtils.getMacroVapp(tierInstance.getOvf(), environmentInstance, tierInstance);
                tierInstance.setOvf(newOvf);
            }
        }

        environmentInstance.setVapp(browseService(claudiaData));

        return environmentInstance;
    }

    @Async
    public Template createTemplate(TierInstance tierInstance) throws InfrastructureException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager# deleteEnvironment
     * (com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance)
     */
    public void deleteEnvironment(ClaudiaData claudiaData, EnvironmentInstance envInstance)
            throws InfrastructureException, EntityNotFoundException, InvalidEntityException {

        List<TierInstance> tierInstances = envInstance.getTierInstances();

        if (tierInstances == null)
            return;
        for (int i = 0; i < tierInstances.size(); i++) {
            TierInstance tierInstance = tierInstances.get(i);
            try {
                claudiaClient.browseVMReplica(claudiaData, tierInstance.getName(), 1, tierInstance.getVM());
            } catch (ClaudiaResourceNotFoundException e) {
                break;
            }
            claudiaClient.undeployVMReplica(claudiaData, tierInstance);
            monitoringClient.stopMonitoring(tierInstance.getVM().getFqn());
        }
        
        deleteNetworksInEnv (claudiaData, envInstance);
        
    }
    
    private List<NetworkInstance> getNetworkInstInEnv (EnvironmentInstance envInstance) throws EntityNotFoundException, InvalidEntityException {
    	List<NetworkInstance> netInst = new ArrayList<NetworkInstance>  ();
    	for (TierInstance tierInstance: envInstance.getTierInstances()) {
    		Set<NetworkInstance> netInts = tierInstance.cloneNetworkInt();
    		tierInstance.getNetworkInstances().clear();
    		tierInstanceManager.update(tierInstance);
    		for (NetworkInstance net: netInts) {
    			if (!netInst.contains(net)) {
    				netInst.add(net);
    			}
    		}
    	}
    	return netInst;
    }
    
    public void deleteNetworksInEnv (ClaudiaData claudiaData, EnvironmentInstance envInstance)
        throws EntityNotFoundException, InvalidEntityException, InfrastructureException {
    	log.debug ("Delete the networks in env if there are not being used");
    	List<NetworkInstance> netInsts = getNetworkInstInEnv (envInstance); 
    	for (NetworkInstance network: netInsts) {
    		networkInstanceManager.delete(claudiaData, network);
    	}
    }

    public void deleteVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        String fqn = getFQNPaas(claudiaData, tierInstance.getTier().getName(), tierInstance.getNumberReplica());
        // claudiaData.setFqn(fqn);
        // claudiaData.setReplica("");

        claudiaClient.undeployVMReplica(claudiaData, tierInstance);
        monitoringClient.stopMonitoring(fqn);

    }

    /**
     * Create an VDC if it is not created.
     * 
     * @param org
     * @param vdc
     * @throws InfrastructureException
     */
    private void deployVDC(ClaudiaData claudiaData) throws InfrastructureException {
        // VDC
        log.debug("Deploy VDC " + claudiaData.getOrg() + " " + claudiaData.getService());
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
            if (!(claudiaResponseAnalyser.getTaskStatus(deployVDCResponse).equals("success")))
                checkTaskResponse(claudiaData, vdcTaskUrl);
        }
    }

    /**
     * deloy a VM from an ovf
     * 
     * @param org
     * @param vdc
     * @param service
     * @param vmOVF
     * @throws InfrastructureException
     */
    private void deployVM(ClaudiaData claudiaData, TierInstance tier, int replicaNumber) throws InfrastructureException {

        // claudiaData = claudiaClient.deployVM(claudiaData, tier,
        // replicaNumber);

        // Interacting against Openstack directly
        /*
         * if (deployVMResponse.contains("Accepted")){ return; }
         */

        /*
         * String taskUrl = claudiaResponseAnalyser.getTaskUrl(deployVMResponse); if
         * (claudiaResponseAnalyser.getTaskStatus(deployVMResponse).equals( "error")) { String errorMsgVM =
         * "Error deploying VM "; log.error(errorMsgVM); throw new InfrastructureException(errorMsgVM); } if
         * (!(claudiaResponseAnalyser.getTaskStatus(deployVMResponse) .equals("success")))
         * checkTaskResponse(claudiaData, taskUrl);
         */
    }

    public void deployVM(ClaudiaData claudiaData, TierInstance tierInstance, int replica, String vmOVF, VM vm)
            throws InfrastructureException {

        log.debug("Deploy VM for tier " + tierInstance.getTier().getName() + " with networks " + tierInstance.getNetworkInstances() 
        		+ " and public ip " + tierInstance.getTier().getFloatingip());

        if (vmOVF == null) {
            String errorMessage = "The VEE OVF could not be procesed, " + "the OVF is null";
            log.warn(errorMessage);
            // throw new InfrastructureException(errorMessage);
        }

        String simpleVmOVF = vmOVF;
        try {
            if (vmOVF != null && vmOVF.length() != 0) {
                while (simpleVmOVF.contains("ovfenvelope:ProductSection")) {
                    simpleVmOVF = ovfUtils.deleteProductSection(simpleVmOVF);
                }
                simpleVmOVF = ovfUtils.changeInitialResources(simpleVmOVF);
                if (replica != 1) {
                    simpleVmOVF = ovfUtils.deleteRules(simpleVmOVF);
                }
                tierInstance.getTier().setPayload(simpleVmOVF);
            }
        } catch (Exception e) {
            log.warn("Error to modify the OVFs for the tier instance");
        }

        claudiaClient.deployVM(claudiaData, tierInstance, replica, vm);

        String vAppReplica = null;
        // String ip = null;
        String fqn = null;
        String networks = null;
        String vmName = null;

        List<String> ips = claudiaClient.getIP(claudiaData, tierInstance.getTier().getName(), replica, vm);
        if (ips != null) {
            for (String ip : ips) {
                log.debug("Ip " + ip);
            }
        } else {
            log.warn("ips null");
        }

        // To-Do
        // networks = vappUtils.getNetworks(vAppReplica);

        fqn = getFQNPaas(claudiaData, tierInstance.getTier().getName(), replica);
        log.info("fqn replica " + fqn);

        // A probar
        // vm.setFqn(vm.getVmid());

        log.info("Introducing delay ");
        // Meter un desfase configurable a eliminar despuÈs de la demo
        introduceDelay(Long.valueOf(systemPropertiesProvider.getProperty(VM_DEPLOYMENT_DELAY)));
        log.info("End delay ");
        // Inserting VApp

        log.info("Set up monitoring ");
        List<String> products = getProductsName(vmOVF);
        monitoringClient.startMonitoring(fqn, null);

        if (products != null) {
            for (String product : products) {
                monitoringClient.startMonitoring(fqn, product);
            }
        }
        /*
         * vm = new VM(fqn, ip, "" + replicaNumber, null, null, vmOVF, vAppReplica);
         */
        log.info("VM ");
        // vm = new VM(fqn, ips.get(0), environmentInstanceName+ "-" +
        // tier.getName()+"-"+replicaNumber
        // , vmName, null, vmOVF, vAppReplica);
        // vm.setVapp(vAppReplica);
        // vm.setVmOVF(vmOVF);
        // vm.setNetworks(networks);
        vm.setIp(ips.get(0));
    }

    private String getFQNPaas(ClaudiaData claudiaData, String tierName, int replica) {
        return claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tierName + ".replicas." + replica;
    }

    private List<String> getProductsName(String vmOVF) {
        if (vmOVF == null || vmOVF.length() == 0)
            return new ArrayList();
        return ovfUtils.getProductSectionName(vmOVF);
    }

    /**
     * @param ovf
     * @return
     */
    private String getVMNameFromSingleOVF(String ovf) throws InvalidOVFException {

        String vmname = null;
        log.info("ovf= " + ovf);
        try {
            Document doc = claudiaUtil.stringToDom(ovf);
            Node virtualSystem = doc.getElementsByTagName(ovfUtils.VIRTUAL_SYSTEM_TAG).item(0);

            vmname = virtualSystem.getAttributes().getNamedItem(ovfUtils.VIRTUAL_SYSTEM_ID).getTextContent();
        } catch (SAXException e) {
            throw new InvalidOVFException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new InvalidOVFException(e.getMessage());
        } catch (IOException e) {
            throw new InvalidOVFException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidOVFException(e.getMessage());
        }

        return vmname;
    }

    public String ImageScalability(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {
        log.debug("Image scalability ");

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

    /*
     * public String updateVmOvf(String ovf, String imageName) { if (imageName != null) { String[] part_inicio =
     * ovf.split("<References>", 2); String[] part_final = part_inicio[1].split("</References>", 2); String[]
     * part_middle = part_final[0].split(" "); part_middle[2] = "ovf:href=\"" + imageName + "\""; String middle = "";
     * for (int i = 0; part_middle.length > i; i++) { middle = middle + " " + part_middle[i]; } ovf = part_inicio[0] +
     * "<References>" + middle + "</References>" + part_final[1]; // cambiamos el operating sistem Section String[]
     * part_inicio2 = ovf.split("<ovf:OperatingSystemSection", 2); // Tenemos la prat_inicio[0] bien String[]
     * part_final2 = part_inicio2[1].split( "</ovf:OperatingSystemSection>", 2); String[] part_middle2 =
     * part_final2[0].split("<Description>", 2); String middle2 = part_middle2[0] + "<Description>" + imageName +
     * "</Description>"; ovf = part_inicio2[0] + "<ovf:OperatingSystemSection" + middle2 +
     * "</ovf:OperatingSystemSection>" + part_final2[1]; // Ahora hay que cambiar los valores de la VM } String ovfFinal
     * = changeInitialResources(ovf); while (ovfFinal.contains("ovfenvelope:ProductSection")) ovfFinal =
     * deleteProductSection(ovfFinal); while (ovfFinal.contains("rsrvr:GovernanceRuleSection")) ovfFinal =
     * deleteRules(ovfFinal); return ovfFinal; } private String deleteRules(String ovf) { String[] part_inicio =
     * ovf.split("<rsrvr:GovernanceRuleSection", 2); String[] part_final = part_inicio[1].split(
     * "</rsrvr:GovernanceRuleSection>", 2); String ovfNew = part_inicio[0] + part_final[1]; return ovfNew; } public
     * String deleteProductSection(String ovf) { String[] part_inicio = ovf.split("<ovfenvelope:ProductSection", 2);
     * String[] part_final = part_inicio[1].split( "</ovfenvelope:ProductSection>", 2); String ovfNew = part_inicio[0] +
     * part_final[1]; return ovfNew; }
     */
    /*
     * public String changeInitialResources(String ovf) { String[] part_inicio = ovf.split("<ovf:VirtualSystem ovf:id=",
     * 2); String[] part_final = part_inicio[1].split(">", 2); // Modificamos part final[0] String[] part_middle =
     * part_final[0].split(" ", 2);// por un lado lo que // es, y por otro a // cambiar String balancer =""; String
     * middle =""; if (ovf.indexOf("rsrvr:balanced=")!=-1) { balancer = ovf.substring(ovf.indexOf("rsrvr:balanced="),
     * ovf.indexOf(">")); middle = part_middle[0] +balancer+ "  rsrvr:initial=\"1\" rsrvr:max=\"1\" rsrvr:min=\"1\">"; }
     * else middle = part_middle[0] + "  rsrvr:initial=\"1\" rsrvr:max=\"1\" rsrvr:min=\"1\">"; String ovfChanged =
     * part_inicio[0] + "<ovf:VirtualSystem ovf:id=" + middle + part_final[1]; return ovfChanged; }
     */

    /**
     * Cretae a Service
     * 
     * @param org
     * @param vdc
     * @param service
     * @throws InfrastructureException
     */
    private void insertService(ClaudiaData claudiaData) throws InfrastructureException {

        // Service
        log.debug("Insert service VDC " + claudiaData.getOrg() + " " + claudiaData.getVdc() + "  "
                + claudiaData.getService());
        String serviceResponse;
        try {
            serviceResponse = claudiaClient.browseService(claudiaData);
        } catch (ClaudiaResourceNotFoundException e) {

            String deployServiceResponse = claudiaClient.deployService(claudiaData,
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_OVFSERVICE_LOCATION));

            /*
             * String serviceTaskUrl = claudiaResponseAnalyser .getTaskUrl(deployServiceResponse); if
             * (claudiaResponseAnalyser.getTaskStatus(deployServiceResponse) .equals("error")) { String errorMesServ =
             * "Error deploying Service " + claudiaData.getService(); log.error(errorMesServ); throw new
             * InfrastructureException(errorMesServ); } if
             * (!(claudiaResponseAnalyser.getTaskStatus(deployServiceResponse) .equals("success")))
             * checkTaskResponse(claudiaData, serviceTaskUrl);
             */
        }
    }
    
    public void deployNetworks (ClaudiaData data, TierInstance tierInstance)
        throws InvalidEntityException, InfrastructureException, EntityNotFoundException {
    	// Creating networks...
    	log.debug ("Deploying network for tier instance " + tierInstance.getName() + " " + tierInstance.getTier().getNetworks());
        List<Network> networkToBeDeployed = new ArrayList<Network>();
        for (Network network: tierInstance.getTier().getNetworks()) {
        	log.debug("Network to be added " + network.getNetworkName());
        	if (network.getNetworkName().equals("Internet")) {
        		Tier tier = tierInstance.getTier();
        		tier = tierManager.load(tier.getName(), data.getVdc(), data.getService());
        		tier.setFloatingip("true");
        		tier = tierManager.update(tier);
        		tierInstance.update(tier);
        	}
        	else {
        		 networkToBeDeployed.add(network);
        	}
           
        }
        
        for (Network network: networkToBeDeployed) {
            log.debug("Network instance to be deployed: " + network.getNetworkName());
            network = networkManager.load(network.getNetworkName());
            NetworkInstance networkInst = network.toNetworkInstance();

            try {
            	networkInst = networkInstanceManager.load(networkInst.getNetworkName());
                log.debug("the network inst" + networkInst.getNetworkName() + " already exists");
            } catch (EntityNotFoundException e1) {
                try {
                	networkInst = networkInstanceManager.create(data, networkInst);
                } catch (AlreadyExistsEntityException e2) {
                    throw new InvalidEntityException(network);
                } catch (InfrastructureException e) { 
                	String mens = "Error to deploy a network " + network.getNetworkName() + " :" + e.getMessage();
                	throw new InfrastructureException(mens);
				}
            }
            log.debug("Adding network to tier isntance " + networkInst.getNetworkName());
            tierInstance.addNetworkInstance(networkInst);
        }
    }

    private TierInstance insertTierInstanceBD(ClaudiaData claudiaData, String envName, TierInstance tierInstance)
            throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException, InfrastructureException {

        log.debug("Inserting in database for tier instance " + tierInstance.getName() + " " + tierInstance.getNetworkInstances().size()
        + " " + tierInstance.getTier().getFloatingip());
        TierInstance tierInstanceDB = null;

        if (tierInstance.getOvf() != null && tierInstance.getOvf().length() > tam_max) {
            String vmOVF = tierInstance.getOvf();
            while (vmOVF.contains("ovfenvelope:ProductSection"))
                vmOVF = environmentUtils.deleteProductSection(vmOVF);
            tierInstance.setOvf(vmOVF);
        }
        try {
            tierInstanceDB = tierInstanceManager.load(tierInstance.getName());
            log.warn("the tier already exists");
        } catch (EntityNotFoundException e) {
            tierInstanceDB = tierInstanceManager.create(claudiaData, envName, tierInstance);

        }
        return tierInstanceDB;
    }

    /**
     * Introducing some delay after vm deployment (only for old claudia)
     * 
     * @param delay
     * @throws InfrastructureException
     */
    private void introduceDelay(Long delay) throws InfrastructureException {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            String errorThread = "Thread Interrupted Exception " + "during delay after vm deployment";
            log.warn(errorThread);
            throw new InfrastructureException(errorThread);
        }
    }

    /**
     * @param claudiaClient
     *            the claudiaClient to set
     */

    public void setClaudiaClient(ClaudiaClient claudiaClient) {
        this.claudiaClient = claudiaClient;
    }

    /**
     * @param ClaudiaResponseAnalyser
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

    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }

    public void setEnvironmentUtils(EnvironmentUtils environmentUtils) {
        this.environmentUtils = environmentUtils;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     * @param monitoringClient
     *            the monitoringClient to set
     */
    public void setMonitoringClient(MonitoringClient monitoringClient) {
        this.monitoringClient = monitoringClient;
    }

    /**
     * @param OVFUtils
     *            the OVFUtils to set
     */
    public void setOvfUtils(OVFUtils ovfUtils) {
        this.ovfUtils = ovfUtils;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
        this.tierInstanceManager = tierInstanceManager;
    }

    /**
     * @param vappUtils
     *            the Vapputils to set
     */
    public void setVappUtils(VappUtils vappUtils) {
        this.vappUtils = vappUtils;
    }

    public String StartStopScalability(ClaudiaData claudiaData, boolean b) throws InfrastructureException {
        String scalalility = claudiaClient.onOffScalability(claudiaData, claudiaData.getService(), b);
        return scalalility;
    }
    
    public void setNetworkInstanceManager(NetworkInstanceManager networkInstanceManager) {
        this.networkInstanceManager = networkInstanceManager;
    }
    
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }
    
    
    

}
