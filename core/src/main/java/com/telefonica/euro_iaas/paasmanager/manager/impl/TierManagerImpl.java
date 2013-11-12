/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.SecurityGroupManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * It is the manager for the Tier.
 * 
 * @author henar
 */
public class TierManagerImpl implements TierManager {

    private TierDao tierDao;

    private ProductReleaseManager productReleaseManager;
    private SecurityGroupManager securityGroupManager;

    private NetworkManager networkManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(TierManagerImpl.class);

    /**
     * It add teh security groups related the products.
     */
    public void addSecurityGroupToProductRelease(ClaudiaData claudiaData, Tier tier, ProductRelease productRelease)
            throws InvalidEntityException, AlreadyExistsEntityException, InfrastructureException {
        Attribute openPortsAttribute = productRelease.getAttribute("openports");
        if (openPortsAttribute != null) {
            StringTokenizer st = new StringTokenizer(openPortsAttribute.getValue());
            while (st.hasMoreTokens()) {
                Rule rule = createRulePort(st.nextToken());

                securityGroupManager.addRule(claudiaData, tier.getSecurityGroup(), rule);

            }
        }
    }

    /**
     * It creates a tier.
     */

    public Tier create(ClaudiaData claudiaData, String envName, Tier tier) throws InvalidEntityException,
            InvalidSecurityGroupRequestException, InfrastructureException {
        log.debug("Create tier name " + tier.getName() + " image " + tier.getImage() + " flavour " + tier.getFlavour()
                + " initial_number_instances " + tier.getInitialNumberInstances() + " maximum_number_instances "
                + tier.getMaximumNumberInstances() + " minimum_number_instances " + tier.getMinimumNumberInstances()
                + " floatingip " + tier.getFloatingip() + " keypair " + tier.getKeypair() + " icono " + tier.getIcono()
                + " product releases " + tier.getProductReleases() + "  vdc " + claudiaData.getVdc() + " networks "
                + tier.getNetworks());

        try {
            tier = load(tier.getName(), claudiaData.getVdc(), envName);
            return tier;
        } catch (EntityNotFoundException e) {

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")
                    && claudiaData.getVdc() != null && claudiaData.getVdc().length() > 0) {

                SecurityGroup securityGroup = createSecurityGroup(claudiaData, tier);
                tier.setSecurityGroup(securityGroup);
            }
            List<Network> networkToBeDeployed = new ArrayList<Network>();
            for (Network network : tier.getNetworks()) {
                networkToBeDeployed.add(network);
            }

           for (Network network: networkToBeDeployed) {
                log.debug("Network to be deployed: " + network.getNetworkName());

                try {
                    network = networkManager.load(network.getNetworkName());
                    log.debug("the network " + network.getNetworkName() + " already exists");
                } catch (EntityNotFoundException e1) {
                    try {
                        network = networkManager.create(network);
                    } catch (AlreadyExistsEntityException e2) {
                        throw new InvalidEntityException(network);
                    }
                }
                tier.addNetwork(network);

            }
            return tierInsertBD(tier, claudiaData);
        }
    }

    /**
     * It creates the rule port for ssh.
     * 
     * @param port
     * @return
     */

    public Rule createRulePort(String port) {
        log.debug("Generate security rule " + port);
        Rule rule = new Rule("TCP", port, port, "", "0.0.0.0/0");
        return rule;
    }

    /**
     * It creates the specified security groups.
     * 
     * @param claudiaData
     * @param tier
     * @return
     * @throws InvalidSecurityGroupRequestException
     */

    private SecurityGroup createSecurityGroup(ClaudiaData claudiaData, Tier tier)
            throws InvalidSecurityGroupRequestException {
        SecurityGroup securityGroup = generateSecurityGroup(claudiaData, tier);
        try {
            securityGroup = securityGroupManager.create(claudiaData, securityGroup);
        } catch (InvalidEntityException e) {
            log.error("It is not posssible to create the security group " + securityGroup.getName() + " "
                    + e.getMessage());
            throw new InvalidSecurityGroupRequestException("It is not posssible to create the security group "
                    + securityGroup.getName() + " " + e.getMessage(), e);
        } catch (InvalidEnvironmentRequestException e) {

            log.error("It is not posssible to create the security group " + securityGroup.getName() + " "
                    + e.getMessage());
            throw new InvalidSecurityGroupRequestException("It is not posssible to create the security group "
                    + securityGroup.getName() + " " + e.getMessage(), e);
        } catch (AlreadyExistsEntityException e) {
            log.error("It is not posssible to create the security group " + securityGroup.getName() + " "
                    + e.getMessage());
            throw new InvalidSecurityGroupRequestException("It is not posssible to create the security group "
                    + securityGroup.getName() + " " + e.getMessage(), e);
        } catch (InfrastructureException e) {
            log.error("It is not posssible to create the security group " + securityGroup.getName() + " "
                    + e.getMessage());

            throw new InvalidSecurityGroupRequestException("It is not posssible to create the security group "
                    + securityGroup.getName() + " " + e.getMessage(), e);

        }
        return securityGroup;
    }

    /**
     * It deletes the tier.
     * 
     * @param claudiaData
     * @param tier
     */
    public void delete(ClaudiaData claudiaData, Tier tier) throws EntityNotFoundException, InvalidEntityException,
            InfrastructureException {

        log.debug("Deleting tier " + tier.getName());
        try {
            tier = load(tier.getName(), claudiaData.getVdc(), claudiaData.getService());
        } catch (EntityNotFoundException e) {
            if (tier.getId() == null) {
                String mens = "It is not possible to delete the tier " + tier.getName() + " since it is not exist";
                log.error(mens);
                throw new EntityNotFoundException(Tier.class, mens, tier);
            }
        } catch (Exception e) {

            String mens = "It is not possible to delete the tier since there is an error " + e.getMessage();
            log.error(mens);
            throw new InvalidEntityException(tier, e);
        }

        if (tier.getSecurityGroup() != null) {
            SecurityGroup sec = tier.getSecurityGroup();
            log.debug("Deleting security group " + sec.getName() + " in tier " + tier.getName());
            tier.setSecurityGroup(null);
            tierDao.update(tier);
            securityGroupManager.destroy(claudiaData, sec);

        }

        log.debug("Deleting the networks");
        List<Network> netsAux = new ArrayList<Network>();
        for (Network netNet : tier.getNetworks()) {
            netsAux.add(netNet);
        }

        for (Network net : netsAux) {
            tier.deleteNetwork(net);
            tierDao.update(tier);
            log.debug("Deleting network " + net.getNetworkName());
            networkManager.delete(net);
        }


        try {
            tierDao.remove(tier);
        } catch (Exception e) {

            String mens = "It is not possible to delete the tier since it is not exist " + e.getMessage();

            log.error(mens);
            throw new InvalidEntityException(tier, e);
        }

    }

    public List<Tier> findAll() {
        return tierDao.findAll();
    }

    public List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException {
        return tierDao.findByCriteria(criteria);
    }

    public List<Tier> findByEnvironment(Environment environment) {
        return null;
    }

    public SecurityGroup generateSecurityGroup(ClaudiaData claudiaData, Tier tier) {

        SecurityGroup securityGroup = new SecurityGroup();
        securityGroup.setName("sg_" + claudiaData.getService() + "_" + claudiaData.getVdc() + "_" + tier.getName());

        log.debug("Generate security group " + "sg_" + claudiaData.getService() + "_" + claudiaData.getVdc() + "_"
                + tier.getName());

        List<Rule> rules = getDefaultRules();

        if (tier.getProductReleases() != null) {

            for (ProductRelease productRelease : tier.getProductReleases()) {
                if (productRelease.getAttributes() == null) {
                    try {

                        productRelease = productReleaseManager.load(productRelease.getProduct() + "-"
                                + productRelease.getVersion());
                    } catch (Exception e) {
                        log.warn("The product does not exists " + productRelease.getProduct() + "-"
                                + productRelease.getVersion());
                    }
                }
                Attribute openPortsAttribute = productRelease.getAttribute("openports");
                if (openPortsAttribute != null) {
                    StringTokenizer st = new StringTokenizer(openPortsAttribute.getValue());

                    while (st.hasMoreTokens()) {
                        Rule rule = createRulePort(st.nextToken());
                        rules.add(rule);
                    }
                }
            }
        }
        securityGroup.setRules(rules);
        return securityGroup;
    }

    public List<Rule> getDefaultRules() {
        List<Rule> rules = new ArrayList<Rule>();
        // 9990
        log.debug("Generate security rule " + 9990);

        Rule rule = new Rule("TCP", "9990", "9990", "", systemPropertiesProvider.getProperty("sdcIp") + "/32");

        rules.add(rule);
        Rule rule2 = new Rule("TCP", "22", "22", "", "0.0.0.0/0");
        rules.add(rule2);
        return rules;

    }

    public Tier load(String name, String vdc, String environmentName) throws EntityNotFoundException {
        try {
            return tierDao.load(name, vdc, environmentName);
        } catch (Exception e) {
            throw new EntityNotFoundException(Tier.class, "error", e.getMessage());
        }
    }

    @Override
    public Tier loadTierWithProductReleaseAndMetadata(String tierName, String environmentName, String vdc)
            throws EntityNotFoundException {
        try {
            return tierDao.loadTierWithProductReleaseAndMetadata(tierName, vdc, environmentName);
        } catch (Exception e) {
            String message = "Tier " + tierName + " not found";
            throw new EntityNotFoundException(Tier.class, message, e.getMessage());
        }
    }

    private void restore(ClaudiaData claudiaData, Tier tier) throws InvalidEntityException, InfrastructureException {
        if (tier.getSecurityGroup() != null) {
            securityGroupManager.destroy(claudiaData, tier.getSecurityGroup());

        }
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;

    }

    public void setSecurityGroupManager(SecurityGroupManager securityGroupManager) {
        this.securityGroupManager = securityGroupManager;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {

        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param tierDao
     *            the tierDao to set
     */
    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }

    /**
     * It insert the tier in the database.
     * 
     * @param tier
     * @param data
     * @return
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    public Tier tierInsertBD(Tier tier, ClaudiaData data) throws InvalidEntityException, InfrastructureException {
        List<Network> networskout = new ArrayList();
        try {
            tier = load(tier.getName(), data.getVdc(), data.getService());
            return tier;
        } catch (EntityNotFoundException e) {

            tier.setVdc(data.getVdc());
            tier.setEnviromentName(data.getService());

            List<ProductRelease> productReleases = new ArrayList();
            if (tier.getProductReleases() != null && tier.getProductReleases().size() != 0) {
                for (ProductRelease p : tier.getProductReleases()) {
                    productReleases.add(p);
                }
            } else {

                log.warn("There is not any product release associated to the tier " + tier.getName());
            }

            for (Network p : tier.getNetworks()) {
                networskout.add(p);
            }

            try {
                tier.setProductReleases(null);
                tier.setNetworks(null);
                tier = tierDao.create(tier);
            } catch (Exception e2) {
                String errorMessage = "The Tier  " + tier.getName() + "  cannot be created " + e2.getMessage();
                log.error(errorMessage);
                restore(data, tier);
                throw new InvalidEntityException(errorMessage);
            }

            if (productReleases != null && productReleases.size() != 0) {

                for (ProductRelease prod : productReleases) {

                    try {
                        prod = productReleaseManager.load(prod.getProduct() + "-" + prod.getVersion());
                        log.debug("Adding product release " + prod.getProduct() + "-" + prod.getVersion() + " to tier "
                                + tier.getName());
                        tier.addProductRelease(prod);
                        update(tier);
                    } catch (Exception e2) {
                        String errorMessage = "The ProductRelease Object " + prod.getProduct() + "-"
                                + prod.getVersion() + " is " + "NOT present in Database";
                        log.error(errorMessage);
                        throw new InvalidEntityException(e2);
                    }
                }
            }
        }

        for (Network net : networskout) {

            try {
                net = networkManager.load(net.getNetworkName());
                log.debug("Adding network " + net.getNetworkName() + "-" + " to tier " + tier.getName());
                tier.addNetwork(net);
                update(tier);
            } catch (Exception e2) {
                String errorMessage = "The Network Object " + net.getNetworkName() + " is " + "NOT present in Database";
                log.error(errorMessage);
                throw new InvalidEntityException(e2);
            }
        }

        return tier;
    }

    public Tier update(Tier tier) throws InvalidEntityException {
        log.debug("Update tier " + tier.getName());
        try {
            return tierDao.update(tier);
        } catch (InvalidEntityException e) {
            log.error("It is not possible to update the tier " + tier.getName() + " : " + e.getMessage(), e);
            throw new InvalidEntityException("It is not possible to update the tier " + tier.getName() + " : "
                    + e.getMessage());
        }

    }

}
