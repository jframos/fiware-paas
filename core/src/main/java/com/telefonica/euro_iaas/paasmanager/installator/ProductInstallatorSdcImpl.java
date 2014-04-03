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

package com.telefonica.euro_iaas.paasmanager.installator;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.sdc.util.SDCUtil;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

public class ProductInstallatorSdcImpl implements ProductInstallator {

    private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private ProductReleaseManager productReleaseManager;
    private TierInstanceManager tierInstanceManager;

    private SDCUtil sDCUtil;

    private static Logger log = Logger.getLogger(ProductInstallatorSdcImpl.class);

    public ProductInstance install(ClaudiaData claudiaData, String envName, TierInstance tierInstance,
            ProductRelease productRelease, Set<Attribute> attributes) throws ProductInstallatorException {

        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        log.debug("Install software " + productRelease.getProduct() + "-" + productRelease.getVersion());
        // From Paasmanager ProductRelease To SDC ProductInstanceDto
        com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto productInstanceDto = new com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto();
        List<com.telefonica.euro_iaas.sdc.model.Attribute> attrs = new ArrayList<com.telefonica.euro_iaas.sdc.model.Attribute>();

        if (!(attributes.isEmpty())) {
            for (Attribute attrib : attributes) {
                com.telefonica.euro_iaas.sdc.model.Attribute sdcAttr = new com.telefonica.euro_iaas.sdc.model.Attribute(
                                attrib.getKey(), attrib.getValue());
                     attrs.add(sdcAttr);
            }
            productInstanceDto.setAttributes(attrs);
        }
        
        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService pIService = sDCClient
                .getProductInstanceService(sdcServerUrl, sdcMediaType);
        log.debug("sdc url " + sdcServerUrl);

        com.telefonica.euro_iaas.sdc.model.Task task = null;

        productInstanceDto.setVm(new com.telefonica.euro_iaas.sdc.model.dto.VM(tierInstance.getVM().getFqn(),
                tierInstance.getVM().getIp(), tierInstance.getVM().getHostname(), tierInstance.getVM().getDomain(),
                tierInstance.getVM().getOsType()));

        productInstanceDto.setProduct(new com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto(
                productRelease.getProduct(), productRelease.getVersion(), "product"));

        if (tierInstance.getVdc() != null)
            productInstanceDto.setVdc(tierInstance.getVdc());

        productInstanceDto.setAttributes(attrs);

        Attribute attSdcGroup = getAttribute(productRelease.getAttributes(), "sdcgroupid");
        if (attSdcGroup != null) {

            productInstanceDto.getAttributes().add(

                    new com.telefonica.euro_iaas.sdc.model.Attribute(attSdcGroup.getValue(), tierInstance.getVM()
                            .getFqn().substring(0, tierInstance.getVM().getFqn().indexOf(".vees."))));
        }

        Attribute attCoreSdcGroup = getAttribute(productRelease.getAttributes(), "sdccoregroupid");
        if (attCoreSdcGroup != null) {

            productInstanceDto.getAttributes().add(

                    new com.telefonica.euro_iaas.sdc.model.Attribute(attCoreSdcGroup.getValue(), tierInstance.getVM()
                            .getFqn().substring(0, tierInstance.getVM().getFqn().indexOf(".vees."))));
        }

        Attribute attId_web_server = getAttribute(productRelease.getAttributes(), "id_web_server");
        if (attId_web_server != null) {

            productInstanceDto.getAttributes().add(

                    new com.telefonica.euro_iaas.sdc.model.Attribute(attId_web_server.getValue(), tierInstance.getVM()
                            .getFqn().substring(0, tierInstance.getVM().getFqn().indexOf(".vees."))));
        }

        Attribute attApp_server_role = getAttribute(productRelease.getAttributes(), "app_server_role");
        if (attApp_server_role != null) {

            productInstanceDto.getAttributes().add(

                    new com.telefonica.euro_iaas.sdc.model.Attribute(attApp_server_role.getValue(), tierInstance
                            .getVM().getFqn().substring(0, tierInstance.getVM().getFqn().indexOf(".vees."))));
        }

        // Installing product with SDC
        ProductInstance productInstance = new ProductInstance();

        productInstanceDto.getVm().setFqn(tierInstance.getVM().getVmid());

        productInstance.setStatus(Status.INSTALLING);
        try {
            task = pIService.install(tierInstance.getVdc(), productInstanceDto, null);

            StringTokenizer tokens = new StringTokenizer(task.getHref(), "/");
            String id = "";

            while (tokens.hasMoreTokens()) {
                id = tokens.nextToken();
            }
            log.debug("Install software in productInstance " + productInstanceDto.getProduct().getName() + " task id "
                    + id + " " + task.getHref());

            productInstance.setTaskId(id);
            tierInstance.setTaskId(id);
            tierInstanceManager.update(claudiaData, envName, tierInstance);

            sDCUtil.checkTaskStatus(task, tierInstance.getVdc());

            com.telefonica.euro_iaas.sdc.model.ProductInstance pInstanceSDC = pIService.load(tierInstance.getVdc(),
                    productInstanceDto.getVm().getFqn() + "_" + productInstanceDto.getProduct().getName() + "_"
                            + productInstanceDto.getProduct().getVersion());
            // Set the domain
            tierInstance.getVM().setDomain(pInstanceSDC.getVm().getDomain());
            tierInstanceManager.update(claudiaData, envName, tierInstance);

        } catch (Exception e) {
            String errorMessage = " Error invokg SDC to Install Product" + productRelease.getName() + " "
                    + productRelease.getVersion() + " " + e.getMessage();
            log.error(errorMessage);
            throw new ProductInstallatorException(errorMessage);
        }

        productInstance.setName(tierInstance.getVM().getVmid() + "_" + productRelease.getProduct() + "_"
                + productRelease.getVersion());
        productInstance.setProductRelease(productRelease);
        productInstance.setVdc(tierInstance.getVdc());

        // sDCUtil.checkTaskStatus(task, productInstance.getVdc());

        productInstance.setStatus(Status.INSTALLED);

        return productInstance;

    }

    public void installArtifact(ProductInstance productInstance, Artifact artifact) throws ProductInstallatorException {
        log.debug ("Install artifact " + artifact.getName() + " in product " + artifact.getProductRelease().getProduct() + " for productinstance " 
                + productInstance.getName());
        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service = sDCClient
                .getProductInstanceService(sdcServerUrl, sdcMediaType);

        List<com.telefonica.euro_iaas.sdc.model.Attribute> atts = new ArrayList();

        for (Attribute att : artifact.getAttributes()) {
            com.telefonica.euro_iaas.sdc.model.Attribute attsdc = new com.telefonica.euro_iaas.sdc.model.Attribute(
                    att.getKey(), att.getValue(), att.getDescription());
            atts.add(attsdc);
        }

        com.telefonica.euro_iaas.sdc.model.Artifact sdcArtifact = new com.telefonica.euro_iaas.sdc.model.Artifact(
                artifact.getName(), atts);

        // Installing product with SDC
        productInstance.setStatus(Status.DEPLOYING_ARTEFACT);
        
        com.telefonica.euro_iaas.sdc.model.Task task = service.installArtifact(productInstance.getVdc(),
                productInstance.getName(), sdcArtifact, null);
        log.debug("Deploying artefact " + artifact.getName() + "with href task " + task.getHref());


        StringTokenizer tokens = new StringTokenizer(task.getHref(), "/");
        String id = "";

        while (tokens.hasMoreTokens()) {
            id = tokens.nextToken();
        }
        log.debug("Install artifact in productInstance " + productInstance.getProductRelease().getProduct() + " task id "
                + id + " " + task.getHref());

        productInstance.setTaskId(id);


        sDCUtil.checkTaskStatus(task, productInstance.getVdc());

        /* How to catch an productInstallation error */
        if (task.getStatus() == com.telefonica.euro_iaas.sdc.model.Task.TaskStates.ERROR)
            throw new ProductInstallatorException("Error installing artefact " + artifact.getName()
                    + " in product instance " + productInstance.getProductRelease().getProduct() + ". Description: "
                    + task.getError());

        productInstance.setStatus(Status.ARTEFACT_DEPLOYED);

    }

    public void uninstallArtifact(ProductInstance productInstance, Artifact artifact)
            throws ProductInstallatorException {
        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service = sDCClient
                .getProductInstanceService(sdcServerUrl, sdcMediaType);

        List<com.telefonica.euro_iaas.sdc.model.Attribute> atts = new ArrayList();

        for (Attribute att : artifact.getAttributes()) {
            com.telefonica.euro_iaas.sdc.model.Attribute attsdc = new com.telefonica.euro_iaas.sdc.model.Attribute(
                    att.getKey(), att.getValue(), att.getDescription());
            atts.add(attsdc);
        }

        com.telefonica.euro_iaas.sdc.model.Artifact sdcArtifact = new com.telefonica.euro_iaas.sdc.model.Artifact(
                artifact.getName(), atts);

        // Installing product with SDC
        productInstance.setStatus(Status.UNDEPLOYING_ARTEFACT);
        com.telefonica.euro_iaas.sdc.model.Task task = service.uninstallArtifact(productInstance.getVdc(),
                productInstance.getName(), sdcArtifact, null);
        /* How to catch an productInstallation error */
        if (task.getStatus() == com.telefonica.euro_iaas.sdc.model.Task.TaskStates.ERROR)
            throw new ProductInstallatorException("Error uninstalling artefact " + artifact.getName()
                    + " in product instance " + productInstance.getProductRelease().getProduct() + ". Description: "
                    + task.getError());

        productInstance.setStatus(Status.ARTEFACT_UNDEPLOYED);

    }

    public void uninstall(ProductInstance productInstance) throws ProductInstallatorException {

        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService productService = sDCClient
                .getProductInstanceService(sdcServerUrl, sdcMediaType);

        // TODO check if the product to be uninstalled supports a current
        // application instance:
        /*
         * List<InstallableInstanceDto> applicationInstances = supportedApplicationInstanceInstalled(fqn, instance); if
         * (applicationInstances.size() > 0) throw new
         * SupportedApplicationInstanceInstalledException(applicationInstances, instance); else
         */

        try {
            productService.uninstall(productInstance.getVdc(), productInstance.getName(), null);
        } catch (Exception e) {
            String errorMessage = " Error invokg SDC to UnInstall Product" + productInstance.getName();
            log.error(errorMessage);
            throw new ProductInstallatorException(errorMessage);
        }

        // productService.uninstall(productInstance.getVdc(),
        // productInstance.getId(), null);

    }

    public void configure(ClaudiaData claudiaData, ProductInstance productInstance, List<Attribute> properties)
            throws ProductInstallatorException {
        log.info("Configure product " + productInstance.getName() + " "
                + productInstance.getProductRelease().getProduct());
        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService pIService = sDCClient
                .getProductInstanceService(sdcServerUrl, sdcMediaType);

        List<com.telefonica.euro_iaas.sdc.model.Attribute> arguments = new ArrayList();

        if (properties != null && properties.size() != 0) {
            for (Attribute attri : properties) {

                com.telefonica.euro_iaas.sdc.model.Attribute att = new com.telefonica.euro_iaas.sdc.model.Attribute();
                att.setKey(attri.getKey());
                att.setKey(attri.getValue());
                arguments.add(att);
            }
        }

        com.telefonica.euro_iaas.sdc.model.Task task = null;

        String name = getProductInstanceName(claudiaData, productInstance);

        // FIWARE.customers.60b4125450fc4a109f50357894ba2e28.services.deploytm.vees.contextbrokr.replicas.1_mongos_2.2.3
        // deploytm-contextbrokr-1_mongos_2.2.3
        try {
            task = pIService.configure(productInstance.getVdc(), name, null, arguments);
        } catch (Exception e) {
            String errorMessage = " Error invokg SDC to configure Product" + productInstance.getName() + " "
                    + e.getMessage();
            log.error(errorMessage);
            throw new ProductInstallatorException(errorMessage);
        }

        sDCUtil.checkTaskStatus(task, productInstance.getVdc());

        return;

    }

    // Borrado del nodo en el ChefServer
    public void deleteNode(String vdc, String sdcNodeName) throws ProductInstallatorException {

        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        ChefClientService chefClientService = sDCClient.getChefClientService(sdcServerUrl, sdcMediaType);

        com.telefonica.euro_iaas.sdc.model.Task task = null;

        // Borrado del Nodo en el chef Server
        try {
            task = chefClientService.delete(vdc, sdcNodeName);
        } catch (Exception e) {
            String errorMessage = " Error invokg SDC to delete Chef Server Node " + sdcNodeName + " " + e.getMessage();
            log.error(errorMessage);
            throw new ProductInstallatorException(errorMessage);
        }

        sDCUtil.checkTaskStatus(task, vdc);

        return;
    }

    // Load a node from the nodename
    public ChefClient loadNode(String vdc, String hostname) throws ProductInstallatorException, EntityNotFoundException {

        String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
        String sdcMediaType = systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);

        // SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ChefClientService chefClientService = sDCClient
                .getChefClientService(sdcServerUrl, sdcMediaType);

        com.telefonica.euro_iaas.sdc.model.Task task = null;

        // Borrado del Nodo en el chef Server
        try {
            return chefClientService.loadByHostname(vdc, hostname);
        } catch (ResourceNotFoundException rnfe) {
            throw new EntityNotFoundException(ChefClient.class, rnfe.getMessage(), rnfe);
        } catch (Exception e) {
            String errorMessage = " Error invokg SDC to delete Chef Server Node " + hostname + " " + e.getMessage();
            log.error(errorMessage);
            throw new ProductInstallatorException(errorMessage);
        }
    }

    public Attribute getAttribute(Set<Attribute> attributes, String key) {
        if (attributes == null)
            return null;
        for (Attribute attribute : attributes) {
            if (attribute.getKey().equals(key))
                return attribute;
        }
        return null;
    }

    // //////////// I.O.C /////////////
    /**
     * @param sDCClient
     *            the sDCClient to set
     */
    public void setSDCClient(SDCClient sDCClient) {
        this.sDCClient = sDCClient;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;
    }

    public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
        this.tierInstanceManager = tierInstanceManager;
    }

    public void setSDCUtil(SDCUtil sDCUtil) {
        this.sDCUtil = sDCUtil;
    }

    public String getProductInstanceName(ClaudiaData claudiaData, ProductInstance productInstance) {
        String tierName = "";
        String productName = "";
        // Installing product with SDC
        StringTokenizer st = new StringTokenizer(productInstance.getName(), "-");

        while (st.hasMoreTokens()) {

            st.nextToken();
            tierName = st.nextToken();

            productName = st.nextToken();
        }
        String name = claudiaData.getOrg() + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tierName + ".replicas." + productName;
        return name;

    }

    /*
     * private Map<String, String> getHeaders(ClaudiaData claudiaData) { Map<String, String> headers = new
     * HashMap<String, String>(); headers.put("X-Auth-Token", claudiaData.getUser().getToken());
     * headers.put("Tenant-ID", claudiaData.getUser().getTenantId()); }
     */

}
