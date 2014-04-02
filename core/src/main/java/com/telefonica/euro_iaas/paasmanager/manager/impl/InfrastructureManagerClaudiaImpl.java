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

package com.telefonica.euro_iaas.paasmanager.manager.impl;


//import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.VM_DEPLOYMENT_DELAY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
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
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class InfrastructureManagerClaudiaImpl implements InfrastructureManager {

    private static final long POLLING_INTERVAL = 10000;

    private SystemPropertiesProvider systemPropertiesProvider;
    private ClaudiaClient claudiaClient;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;

    private TierInstanceManager tierInstanceManager;
    private TierManager tierManager;
    private EnvironmentInstanceDao environmentInstanceDao;
    private NetworkInstanceManager networkInstanceManager;
    private NetworkManager networkManager;
    

    

    /** The log. */
    private static Logger log = Logger.getLogger(InfrastructureManagerClaudiaImpl.class);
    /** Max lenght of an OVF */
    private static final Integer tam_max = 90000;


    /**
     * @param taskUrl
     * @throws InfrastructureException
     */
 /*   private void checkTaskResponse(ClaudiaData claudiaData, String taskUrl) throws InfrastructureException {
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
    }*/

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

    public EnvironmentInstance createInfrasctuctureEnvironmentInstance(EnvironmentInstance environmentInstance,
            Set<Tier> tiers, ClaudiaData claudiaData) throws InfrastructureException, InvalidVappException,
            InvalidOVFException, InvalidEntityException, EntityNotFoundException {

        // Deploy MVs
        log.debug("Creating infrastructure for environment instance " + environmentInstance.getBlueprintName());

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
                log.debug("fqn " + fqn + " hostname " + hostname);
                vm.setFqn(fqn);
                vm.setHostname(hostname);
                tierInstance.setVM(vm);

                log.debug("Deploy networks if required");
                this.deployNetworks(claudiaData, tierInstance);
                log.debug("Number of networks " + tierInstance.getNetworkInstances().size() + " floatin ip "
                        + tierInstance.getTier().getFloatingip());

                try {
                    log.debug("Inserting in database ");
                    tierInstance = insertTierInstanceBD(claudiaData, environmentInstance.getEnvironment().getName(),
                            tierInstance);
                    log.debug("Return: Number of networks " + tierInstance.getNetworkInstances().size()
                            + " floating ip " + tierInstance.getTier().getFloatingip());
                    environmentInstance.addTierInstance(tierInstance);
                    environmentInstanceDao.update(environmentInstance);
                } catch (EntityNotFoundException e) {
                    log.error("Entity Not found: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                } catch (InvalidEntityException e) {
                    log.error("Invalid: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                } catch (AlreadyExistsEntityException e) {
                    log.error("AllReady found: Tier " + tierInstance.getTier().getName() + " " + e.getMessage());
                    throw new InfrastructureException(e);
                }

                try {
                    tierInstanceManager.update(claudiaData, environmentInstance.getEnvironment().getName(),
                            tierInstance);
                } catch (Exception e) {
                    log.error("Error deploying a VM: " + e.getMessage());
                    environmentInstance.setStatus(Status.ERROR);
                    throw new InfrastructureException(e.getMessage());
                }

               

                log.debug("Tier instance name " + environmentInstance.getBlueprintName() + "-" + tier.getName() + "-"
                        + numReplica);

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
                } catch (Exception e) {
                    throw new InfrastructureException(e);
                }

            }
            numberTier++;
        }
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
                claudiaClient.browseVMReplica(claudiaData, tierInstance.getName(), 1, tierInstance.getVM(),
                        tierInstance.getTier().getRegion());
            } catch (ClaudiaResourceNotFoundException e) {
                break;
            }
            claudiaClient.undeployVMReplica(claudiaData, tierInstance);
            deleteNetworksInEnv(claudiaData, envInstance, tierInstance.getTier().getRegion());
        }

    }

    private List<NetworkInstance> getNetworkInstInEnv(EnvironmentInstance envInstance) throws EntityNotFoundException,
            InvalidEntityException {
        List<NetworkInstance> netInst = new ArrayList<NetworkInstance>();
        for (TierInstance tierInstance : envInstance.getTierInstances()) {
            Set<NetworkInstance> netInts = tierInstance.cloneNetworkInt();
            tierInstance.getNetworkInstances().clear();
            tierInstanceManager.update(tierInstance);
            for (NetworkInstance net : netInts) {
                if (!netInst.contains(net)) {
                    netInst.add(net);
                }
            }
        }
        return netInst;
    }

    public void deleteNetworksInEnv(ClaudiaData claudiaData, EnvironmentInstance envInstance, String region)
            throws EntityNotFoundException, InvalidEntityException, InfrastructureException {
        log.debug("Delete the networks in env if there are not being used");
        List<NetworkInstance> netInsts = getNetworkInstInEnv(envInstance);
        for (NetworkInstance network : netInsts) {
            log.debug("Is network default? " + network.isDefaultNet());
            if (!network.isDefaultNet()) {
                networkInstanceManager.delete(claudiaData, network, region);
            }
        }
    }

    public void deleteVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        String fqn = getFQNPaas(claudiaData, tierInstance.getTier().getName(), tierInstance.getNumberReplica());
        // claudiaData.setFqn(fqn);
        // claudiaData.setReplica("");

        claudiaClient.undeployVMReplica(claudiaData, tierInstance);

    }



    public void deployVM(ClaudiaData claudiaData, TierInstance tierInstance, int replica, String vmOVF, VM vm)
            throws InfrastructureException {

        log.debug("Deploy VM for tier " + tierInstance.getTier().getName() + " with networks "
                + tierInstance.getNetworkInstances() + " and public ip " + tierInstance.getTier().getFloatingip());

        if (vmOVF == null) {
            String errorMessage = "The VEE OVF could not be procesed, " + "the OVF is null";
            log.warn(errorMessage);
            // throw new InfrastructureException(errorMessage);
        }

        claudiaClient.deployVM(claudiaData, tierInstance, replica, vm);

        String vAppReplica = null;
        // String ip = null;
        String fqn = null;
        String networks = null;
        String vmName = null;

        List<String> ips = claudiaClient.getIP(claudiaData, tierInstance.getTier().getName(), replica, vm, tierInstance
                .getTier().getRegion());
        if (ips != null) {
            for (String ip : ips) {
                log.debug("Ip " + ip);
            }
        } else {
            log.warn("ips null");
        }

        fqn = getFQNPaas(claudiaData, tierInstance.getTier().getName(), replica);
        log.info("fqn replica " + fqn);

        /*
         * vm = new VM(fqn, ip, "" + replicaNumber, null, null, vmOVF, vAppReplica);
         */
        log.info("VM ");

        vm.setIp(ips.get(0));
    }

    private String getFQNPaas(ClaudiaData claudiaData, String tierName, int replica) {
        return claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tierName + ".replicas." + replica;
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

 
    public void deployNetworks(ClaudiaData data, TierInstance tierInstance) throws InvalidEntityException,
            InfrastructureException, EntityNotFoundException {
        Tier tier = tierInstance.getTier();
        tier = tierManager.loadTierWithNetworks(tier.getName(), data.getVdc(), tier.getEnviromentName());
        // Creating networks...
        log.debug("Deploying network for tier instance " + tierInstance.getName() + " " + tier.getNetworks());
        List<Network> networkToBeDeployed = new ArrayList<Network>();
        for (Network network : tier.getNetworks()) {
            log.debug("Network to be added " + network.getNetworkName());
            if (network.getNetworkName().equals("Internet")) {

                tier.setFloatingip("true");
                tier = tierManager.update(tier);
                tierInstance.update(tier);
            } else {
                networkToBeDeployed.add(network);
            }

        }

        for (Network network : networkToBeDeployed) {
            log.debug("Network instance to be deployed: " + network.getNetworkName() + " vdc " + data.getVdc());
            network = networkManager.load(network.getNetworkName(), data.getVdc());
            NetworkInstance networkInst = network.toNetworkInstance();
            log.debug("Network instance to be deployed: " + network.getNetworkName() + " vdc " + data.getVdc());

            try {
                networkInst = networkInstanceManager.load(networkInst.getNetworkName(), data.getVdc());
                log.debug("the network inst" + networkInst.getNetworkName() + " already exists");
            } catch (EntityNotFoundException e1) {
                try {
                    networkInst = networkInstanceManager.create(data, networkInst, tierInstance.getTier().getRegion());
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
            throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException,
            InfrastructureException {

        log.debug("Inserting in database for tier instance " + tierInstance.getName() + " "
                + tierInstance.getNetworkInstances().size() + " " + tierInstance.getTier() + " "
                + tierInstance.getTier().getFloatingip());
        TierInstance tierInstanceDB = null;

       
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

    public void setClaudiaResponseAnalyser(ClaudiaResponseAnalyser claudiaResponseAnalyser) {
        this.claudiaResponseAnalyser = claudiaResponseAnalyser;
    }

    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }


    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
        this.tierInstanceManager = tierInstanceManager;
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
