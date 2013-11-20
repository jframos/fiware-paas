/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_BASEURL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_IP;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_OVFSERVICE_LOCATION;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_PORT;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDCTEMPLATE_LOCATION;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_CPU;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_DISK;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_MEM;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.claudia.client.OvfInjector;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtilImpl;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OSNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.VMStatusNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.monitoring.MonitoringClient;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ClaudiaClientImpl implements ClaudiaClient {

    private static final long POLLING_INTERVAL = 10000;
    private static final String RESOURCE_NOTFOUND_PATTERN = "ErrorSet";

    private ClaudiaUtil claudiaUtil;
    private VappUtils vappUtils;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;
    private SystemPropertiesProvider systemPropertiesProvider;
    private MonitoringClient monitoringClient;

    private static final String type = MediaType.WILDCARD;

    /** The log. */
    private static Logger log = Logger.getLogger(ClaudiaClientImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseVDC( java.lang.String, java.lang.String)
     */
    public String browseVDC(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {

        String vdcUrl = null;
        String vdcResponse = null;
        List<String> parameters = new ArrayList<String>();
        parameters.add(claudiaData.getOrg());
        parameters.add(claudiaData.getVdc());

        try {
            vdcUrl = claudiaUtil.getUrl(parameters);
            log.debug("browseVDC " + vdcUrl);
        } catch (URLNotRetrievedException e) {
            String errorMessage = "URL Not Provided for org " + claudiaData.getOrg() + " and vdc "
                    + claudiaData.getVdc();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }

        try {
            vdcResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), vdcUrl, type);
            log.debug("vdcResponse " + vdcResponse);
            if (vdcResponse.contains(RESOURCE_NOTFOUND_PATTERN)) {
                String errorMessage = "VDC " + claudiaData.getVdc() + " is not present in the" + " system";
                log.error(errorMessage);
                throw new ClaudiaResourceNotFoundException(errorMessage);
            }

        } catch (ClaudiaRetrieveInfoException e) {
            String errorMessage = "Could not perform GET operation on Claudia";
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }
        return vdcResponse;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseService (java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String browseService(ClaudiaData claudiaData) throws ClaudiaResourceNotFoundException {

        String serviceUrl = null;
        String serviceResponse = null;
        List<String> parameters = new ArrayList<String>();
        parameters.add(claudiaData.getOrg());
        parameters.add(claudiaData.getVdc());
        parameters.add(claudiaData.getService());

        try {
            serviceUrl = claudiaUtil.getUrl(parameters);
            log.debug("browseService " + serviceUrl);

        } catch (URLNotRetrievedException e) {
            String errorMessage = "URL Not Provided for org " + claudiaData.getOrg() + "  vdc " + claudiaData.getVdc()
                    + " service " + claudiaData.getService();
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }

        try {
            serviceResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), serviceUrl, type);

            log.debug("serviceResponse " + serviceResponse);
            if (serviceResponse.contains(RESOURCE_NOTFOUND_PATTERN)) {
                String errorMessage = "Service " + claudiaData.getService() + " is not present in the" + " system org "
                        + claudiaData.getOrg() + " vdc " + claudiaData.getVdc();
                log.error(errorMessage);
                throw new ClaudiaResourceNotFoundException(errorMessage);
            }
        } catch (ClaudiaRetrieveInfoException e) {
            String errorMessage = "Could not perform GET operation on Claudia";
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }
        return serviceResponse;
    }

    public void undeployService(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();
        // String actionUri = URICreation.getURIVapp(claudiaData.getFqn());
        String actionUri = URICreation.getURIService(tierInstance.getVM().getFqn());
        String serviceUrl = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);

        log.info("serviceUrl: " + serviceUrl);
        try {
            claudiaUtil.deleteClaudiaResource(claudiaData.getUser(), serviceUrl);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorUndeployVM = "An error ocurred undeploying VM " + tierInstance.getVM().getFqn();
            log.error(errorUndeployVM);
            throw new InfrastructureException(errorUndeployVM, e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#browseVM(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String browseVM(String org, String vdc, String service, String vm, PaasManagerUser user)
            throws ClaudiaResourceNotFoundException {

        String vmUrl = null;
        String vmResponse = null;
        List<String> parameters = new ArrayList<String>();
        parameters.add(org);
        parameters.add(vdc);
        parameters.add(service);
        parameters.add(vm);

        try {
            vmUrl = claudiaUtil.getUrl(parameters);
        } catch (URLNotRetrievedException e) {
            String errorMessage = "URL Not Provided for org " + org + "  vdc " + vdc + " service " + service + " vm "
                    + vm;
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }

        try {
            vmResponse = claudiaUtil.getClaudiaResource(user, vmUrl, type);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorMessage = "Could not perform GET operation on Claudia";
            log.error(errorMessage);
            throw new ClaudiaResourceNotFoundException(errorMessage, e);
        }
        return vmResponse;
    }

    public String browseVMReplica(ClaudiaData claudiaData, String tierName, int replica, VM vm, String region)
            throws ClaudiaResourceNotFoundException {

        int cont = 0;
        String replicaResponse, fqnReplica, actionUri, url = null;
        List<String> parameters = new ArrayList<String>();

        // Different url to get the VApp
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {

            parameters.add(claudiaData.getOrg());
            parameters.add(claudiaData.getVdc());
            parameters.add(claudiaData.getService());
            parameters.add(tierName);

            try {
                url = claudiaUtil.getUrl(parameters);
            } catch (URLNotRetrievedException e) {
                String errorMessage = "URL Not Provided for org " + claudiaData.getOrg() + "  vdc "
                        + claudiaData.getVdc() + " service " + claudiaData.getService() + " vm " + tierName;

                log.error(errorMessage);
                throw new ClaudiaResourceNotFoundException(errorMessage, e);
            }

        } else {

            String vapp = URICreation.getFQN(claudiaData.getOrg(), claudiaData.getVdc(), claudiaData.getService());
            fqnReplica = URICreation.getFQN(claudiaData.getOrg(), claudiaData.getVdc(), claudiaData.getService(),
                    tierName + "_" + replica);
            actionUri = URICreation.getURIService(vapp) + "/" + tierName + "_" + replica;

            url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                    systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        }

        try {

            replicaResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), url, type);

            while (!(replicaResponse.contains(VappUtils.IPADDRESS_TAG))) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    log.warn("Interrupted Exception during polling");
                }
                replicaResponse = claudiaUtil.getClaudiaResource(claudiaData.getUser(), url, type);
                cont = cont + 1;
                if (cont >= 25)
                    break;
            }

        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVDC = "An browsering the replica: " + claudiaData.getService() + " " + tierName + ":"
                    + e.getMessage();
            log.error(errorDeployVDC + " " + e.getMessage());
            throw new ClaudiaResourceNotFoundException(errorDeployVDC);
        }
        return replicaResponse;
    }

    @Override
    public List<String> findAllVMs(ClaudiaData claudiaData, String region) throws ClaudiaResourceNotFoundException {
        return null;  // To change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getIP(ClaudiaData claudiaData, String tierName, int replica, VM vm, String region)
            throws InfrastructureException {

        List<String> ips = new ArrayList<String>();
        try {
            String vAppReplica = browseVMReplica(claudiaData, tierName, replica, vm, region);
            ips.add(vappUtils.getIP(vAppReplica));
            log.debug("IP replica " + ips.get(0));
        } catch (ClaudiaResourceNotFoundException e) {
            throw new InfrastructureException(e.getMessage());
        } catch (ProductInstallatorException pie) {
            throw new InfrastructureException(pie.getMessage());
        }
        return ips;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVM (java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public void undeployVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();
        String actionUri = URICreation.getURIVapp(tierInstance.getVM().getFqn());
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri)
                + "/" + tierInstance.getNumberReplica();
        log.debug("url: " + url);
        try {
            claudiaUtil.deleteClaudiaResource(claudiaData.getUser(), url);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorUndeployVM = "An error ocurred undeploying VM " + tierInstance.getVM().getFqn();
            log.error(errorUndeployVM);
            throw new InfrastructureException(errorUndeployVM);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployVDC( java.lang.String, java.lang.String,
     * int, int, int)
     */
    public String deployVDC(ClaudiaData claudiaData, String cpu, String mem, String disk)
            throws InfrastructureException {
        ClientResponse response = null;

        String actionUri = URICreation.getURIOrg(URICreation.getFQN(claudiaData.getOrg()))
                + URICreation.URI_VDC_ADD_MODIFIER;
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);

        try {
            String payload = readFile(systemPropertiesProvider.getProperty(NEOCLAUDIA_VDCTEMPLATE_LOCATION))
                    .replace("${vdcName}", claudiaData.getVdc()).replace("${vdcDescription}", "vdcDescription")
                    .replace("${cpuLimit}", systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_CPU))
                    .replace("${memLimit}", systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_MEM))
                    .replace("${diskLimit}", systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_DISK));

            response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), url, payload);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVDC = "An error ocurred deploying VDC: " + claudiaData.getVdc();
            log.error(errorDeployVDC);
            throw new InfrastructureException(errorDeployVDC, e);
        } catch (IOException e) {
            String errorDeployVDC = "Error obtaining VDC Template "
                    + systemPropertiesProvider.getProperty(NEOCLAUDIA_VDCTEMPLATE_LOCATION);
            log.error(errorDeployVDC);
            throw new InfrastructureException(errorDeployVDC, e);
        }
        return response.getEntity(String.class);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployService (java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String deployService(ClaudiaData claudiaData, String ovfUrl) throws InfrastructureException {

        ClientResponse response = null;
        String payload = null;
        String actionUri = URICreation.getURIServiceAdd(URICreation.getFQN(claudiaData.getOrg(), claudiaData.getVdc()));
        log.debug("actionUri: " + actionUri);
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        String emptyServiceUrl = systemPropertiesProvider.getProperty(NEOCLAUDIA_OVFSERVICE_LOCATION);

        try {
            payload = new OvfInjector().injectNameOnOvf(claudiaData.getService(), emptyServiceUrl,
                    "VirtualSystemCollection");
            response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), url, payload);
        } catch (TransformerException e1) {
            String errorDeployService = "An error obtaining/parsing " + "serviceOVfFile: " + "Error Description: "
                    + e1.getMessage();
            log.error(errorDeployService);
            throw new InfrastructureException(errorDeployService, e1);

        } catch (ParserConfigurationException e1) {
            String errorDeployService = "An error obtaining/parsing " + "serviceOVfFile: " + "Error Description: "
                    + e1.getMessage();
            log.error(errorDeployService);
            throw new InfrastructureException(errorDeployService, e1);
        } catch (SAXException e1) {
            String errorDeployService = "An error obtaining/parsing " + "serviceOVfFile: " + "Error Description: "
                    + e1.getMessage();
            log.error(errorDeployService);
            throw new InfrastructureException(errorDeployService, e1);
        } catch (IOException e) {
            String errorDeployService = "An error obtaining/parsing " + "serviceOVfFile: " + "Error Description: "
                    + e.getMessage();
            log.error(errorDeployService);
            throw new InfrastructureException(errorDeployService, e);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployService = "An error ocurred deploying Service: " + claudiaData.getService();
            log.error(errorDeployService);
            throw new InfrastructureException(errorDeployService, e);
        }

        return response.getEntity(String.class);
    }

    /*
     * public String deployServiceFull(ClaudiaData claudiaData, String ovfs) throws InfrastructureException {
     * ClientResponse response = null; String payload = null; String actionUri = URICreation
     * .getURIServiceAdd(URICreation.getFQN( claudiaData.getOrg(),claudiaData.getVdc())); log.debug("actionUri: " +
     * actionUri); String url = MessageFormat.format( systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
     * systemPropertiesProvider.getProperty(NEOCLAUDIA_IP), systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT),
     * actionUri); try { response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), url, ovfs); } catch
     * (ClaudiaRetrieveInfoException e) { String errorDeployService = "An error ocurred deploying Service: " +
     * claudiaData.getService(); log.error(errorDeployService); throw new InfrastructureException(errorDeployService); }
     * return response.getEntity(String.class); }
     */

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployVM(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String deployVM(String org, String vdc, String service, String vmName, PaasManagerUser user, String vmUrl)
            throws InfrastructureException {
        String payload = null;
        String vmResponse = null;
        ClientResponse response = null;
        String actionUri = URICreation.getURIVEEReplicaAdd(URICreation.getFQN(org, vdc, service));
        log.debug("actionUri: " + actionUri);
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        try {
            payload = new OvfInjector().injectNameOnOvf(vmName, vmUrl, "VirtualSystem");
            response = claudiaUtil.postClaudiaResource(user, url, payload);
            vmResponse = response.getEntity(String.class);
        } catch (TransformerException e1) {
            String errorDeployVM = "An error ocurred obtaining/parsing vmOvfFile: " + vmUrl;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e1);
        } catch (ParserConfigurationException e1) {
            String errorDeployVM = "An error ocurred obtaining/parsing vmOvfFile: " + vmUrl;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e1);
        } catch (SAXException e1) {
            String errorDeployVM = "An error ocurred obtaining/parsing vmOvfFile: " + vmUrl;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e1);
        } catch (IOException e) {
            String errorDeployVM = "An error ocurred obtaining/parsing vmOvfFile: " + vmUrl;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVM = "An error ocurred deploying VM: " + vmName;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e);
        }

        return vmResponse;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#deployVM(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void deployVM(ClaudiaData claudiaData, TierInstance tierInstance, int replica, VM vm)
            throws InfrastructureException {

        Tier tier = tierInstance.getTier();
        String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + replica;
        String hostname = claudiaData.getService() + "-" + tier.getName() + "-" + replica;

        vm.setFqn(fqn);
        vm.setHostname(hostname);

        String vmResponse = null;
        ClientResponse response = null;
        String actionUri = URICreation.getURIVEEReplicaAdd(URICreation.getFQN(claudiaData.getOrg(),
                claudiaData.getVdc(), claudiaData.getService()));
        log.info("actionUri: " + actionUri);
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        log.info("url: " + url);
        try {
            response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), url, tier.getPayload());
            vmResponse = response.getEntity(String.class);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVM = "An error ocurred deploying VM ";
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e);
        }

        String taskUrl = claudiaResponseAnalyser.getTaskUrl(vmResponse);

        if (claudiaResponseAnalyser.getTaskStatus(vmResponse).equals("error")) {
            String errorMsgVM = "Error deploying VM ";
            log.error(errorMsgVM);
            throw new InfrastructureException(errorMsgVM);
        }

        if (!(claudiaResponseAnalyser.getTaskStatus(vmResponse).equals("success")))

            checkTaskResponse(claudiaData, taskUrl);

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#undeployVM (java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public void undeployVM(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException {

        ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();

        String actionUri = URICreation.getURIVapp(tierInstance.getVM().getFqn());

        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);

        log.info("url: " + url);

        try {
            claudiaUtil.deleteClaudiaResource(claudiaData.getUser(), url);
            monitoringClient.stopMonitoring(tierInstance.getVM().getFqn());
        } catch (ClaudiaRetrieveInfoException e) {
            String errorUndeployVM = "An error ocurred undeploying VM " + tierInstance.getVM().getFqn();
            log.error(errorUndeployVM);
            throw new InfrastructureException(errorUndeployVM, e);
            /*
             * } catch (MonitoringException e) { String errorStoppingMonitoring = "An error ocurred deleting VM " + fqn
             * + "entry from Monitoring System"; log.error(errorStoppingMonitoring); throw new
             * InfrastructureException(errorStoppingMonitoring);
             */
        }

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainIPFromFqn (java.lang.String)
     */
    public String obtainIPFromFqn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws IPNotRetrievedException, ClaudiaResourceNotFoundException, NetworkNotRetrievedException {
        int cont = 0;

        String vmResponse = browseVM(org, vdc, service, vmName, user);

        while (!(vmResponse.contains(ClaudiaUtil.IPADDRESS_NODENAME))) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.warn("Interrupted Exception during polling");
            }
            vmResponse = browseVM(org, vdc, service, vmName, user);
            cont = cont + 1;
            if (cont >= 10)
                break;
        }
        String network = getPublicNetworkFromDocument(vmResponse);
        return getIPFromDocument(vmResponse, network);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainOS(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String obtainOS(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws OSNotRetrievedException, ClaudiaResourceNotFoundException {

        int cont = 0;

        String vmResponse = browseVM(org, vdc, service, vmName, user);

        while (!(vmResponse.contains(ClaudiaUtil.IPADDRESS_NODENAME))) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.warn("Interrupted Exception during polling");
            }
            vmResponse = browseVM(org, vdc, service, vmName, user);
            cont = cont + 1;
            if (cont >= 10)
                break;
        }

        return getOSFromDocument(vmResponse);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainOS(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String getVApp(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws OSNotRetrievedException, ClaudiaResourceNotFoundException {

        int cont = 0;

        String vmResponse = browseVM(org, vdc, service, vmName, user);

        while (!(vmResponse.contains(ClaudiaUtil.IPADDRESS_NODENAME))) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.warn("Interrupted Exception during polling");
            }
            vmResponse = browseVM(org, vdc, service, vmName, user);
            cont = cont + 1;
            if (cont >= 10)
                break;
        }

        return vmResponse;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainOS(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String obtainVMStatus(String vapp) throws VMStatusNotRetrievedException {

        String status = null;
        Document doc;
        try {
            doc = claudiaUtil.stringToDom(vapp);
            NodeList vappList = doc.getElementsByTagName(ClaudiaUtil.VAPP_NODENAME);
            for (int j = 0; j < vappList.getLength(); j++) {
                Element soElement = (Element) vappList.item(j);
                status = soElement.getAttribute(ClaudiaUtil.VM_STATUS_ATTRIBUTE);
            }
            if (status == null)
                throw new VMStatusNotRetrievedException("VM Status is null");

        } catch (SAXException e) {
            String errorMessage = "SAXException when obtaining VM Status. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new VMStatusNotRetrievedException(errorMessage, e);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException when obtaining " + "VM Status. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new VMStatusNotRetrievedException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = "IOException when obtaining " + "VM Status. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new VMStatusNotRetrievedException(errorMessage, e);
        }

        return status;
    }

    public String switchVMOn(String org, String vdc, String service, String vmName, PaasManagerUser user)
            throws InfrastructureException {

        String vmResponse = null;
        ClientResponse response = null;
        String actionUri = URICreation.getURIVapp(URICreation.getFQN(org, vdc, service, vmName))
                + "/power/action/powerOn";
        log.info("actionUri: " + actionUri);
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        log.info("switchVMOn url: " + url);
        try {
            response = claudiaUtil.postClaudiaResource(user, url, "");
            vmResponse = response.getEntity(String.class);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVM = "An error ocurred switching On VM " + vmName;
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM, e);
        }
        return vmResponse;
    }

    /**
     * @param xmlResource
     * @return
     * @throws IOException
     */
    private static String readFile(String xmlResource) throws IOException {
        InputStream is = new FileInputStream(xmlResource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }

        return ruleFile.toString();
    }

    /**
     * @param xmlSource
     * @param networkName
     * @return
     * @throws IPNotRetrievedException
     */
    private String getIPFromDocument(String xmlSource, String networkName) throws IPNotRetrievedException {
        String ipv4 = null;
        Document doc;
        try {
            doc = claudiaUtil.stringToDom(xmlSource);
            NodeList ovItemList = doc.getElementsByTagName(ClaudiaUtil.OVFITEM_NODENAME);

            for (int j = 0; j < ovItemList.getLength(); j++) {
                Node ovfItem = ovItemList.item(j);

                NodeList ovfElements = ovfItem.getChildNodes();

                for (int k = 0; k < ovfElements.getLength(); k++) {
                    Node ovfElement = ovfElements.item(k);

                    if ((ovfElement.getNodeName().equals(ClaudiaUtil.OVFITEM_CONNECTION_NODENAME))
                            && ovfElement.getTextContent().trim().equals(networkName))
                        ipv4 = getIPFromItemElement(ovfItem);
                }
            }

            if (ipv4 == null)
                throw new IPNotRetrievedException();
        } catch (SAXException e) {
            throw new IPNotRetrievedException();
        } catch (ParserConfigurationException e) {
            throw new IPNotRetrievedException();
        } catch (IOException e) {
            throw new IPNotRetrievedException();
        }
        return ipv4;
    }

    private String getIPFromItemElement(Node ovfItem) {

        String ipv4 = null;
        NodeList ovfElements = ovfItem.getChildNodes();

        for (int j = 0; j < ovfElements.getLength(); j++) {
            Node ovfElement = ovfElements.item(j);

            if (ovfElement.getNodeName().equals(ClaudiaUtil.IPADDRESS_NODENAME))
                ipv4 = ovfElement.getTextContent();
        }
        return ipv4;
    }

    private String getOSFromDocument(String xmlSource) throws OSNotRetrievedException {
        String so = null;
        Document doc;
        try {
            doc = claudiaUtil.stringToDom(xmlSource);
            NodeList soList = doc.getElementsByTagName(ClaudiaUtil.OS_NODENAME);
            for (int j = 0; j < soList.getLength(); j++) {
                Element soElement = (Element) soList.item(j);
                so = soElement.getAttribute(ClaudiaUtil.OS_ID_ATTRIBUTENAME);
            }
            if (so == null) {
                throw new OSNotRetrievedException("OS is null");
            }
        } catch (SAXException e) {
            String errorMessage = "SAXException when obtaining OS. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new OSNotRetrievedException(errorMessage, e);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException when obtaining " + "OS. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new OSNotRetrievedException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = "IOException when obtaining " + "OS. Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new OSNotRetrievedException(errorMessage, e);
        }

        return so;
    }

    /**
     * @param xmlSource
     * @return
     * @throws NetworkNotRetrievedException
     */
    private String getPublicNetworkFromDocument(String xmlSource) throws NetworkNotRetrievedException {
        String networkName = null;
        Document doc;
        try {
            doc = claudiaUtil.stringToDom(xmlSource);

            NodeList networkList = doc.getElementsByTagName(ClaudiaUtil.NETWORK_NODENAME);
            for (int j = 0; j < networkList.getLength(); j++) {
                Element networkElement = (Element) networkList.item(j);
                networkName = networkElement.getAttribute(ClaudiaUtil.NETWORK_NAME_ATTRIBUTE);
            }
            if (networkName == null) {
                String errorMessage = "Network Name Not Found in VM xml";
                log.error(errorMessage);
                throw new NetworkNotRetrievedException(errorMessage);
            }

        } catch (SAXException e) {
            String errorMessage = "SAXException obtaining NetworkName.  Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new NetworkNotRetrievedException(errorMessage, e);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException obtaining " + "NetworkName.  Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new NetworkNotRetrievedException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = "IOException obtaining " + "NetworkName.  Desc: " + e.getMessage();
            log.error(errorMessage);
            throw new NetworkNotRetrievedException(errorMessage, e);
        }

        return networkName;
    }

    public String onOffScalability(ClaudiaData claudiaData, String environmentName, boolean b)
            throws InfrastructureException {

        String vmResponse = null;
        ClientResponse response = null;

        // URI del service
        // /api/org/org-id/vdc/vdc-id/vapp/service/action/scale/enable

        String state = (b) ? "enable" : "disable";
        String actionUri = getURIServiceAdd(claudiaData.getOrg(), claudiaData.getVdc(), environmentName, state);

        log.info("actionUri: " + actionUri);
        String uri = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);

        log.info("url: " + uri);

        try {
            response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), uri);
            vmResponse = response.getEntity(String.class);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorManageScalability = "An error ocurred manageScalability ";
            log.error(errorManageScalability);
            throw new InfrastructureException(errorManageScalability, e);
        }

        return vmResponse;
    }

    private static String getURIServiceAdd(String org, String vdc, String service, String state) {
        String Scalability_URI = "/api/org/" + org + "/vdc/" + vdc + "/vapp/" + service + "/action/scale/" + state;
        return Scalability_URI;
    }

    public String createImage(ClaudiaData claudiaData, TierInstance tierInstance) throws ClaudiaRetrieveInfoException {

        String actionUri = getURIScalabilityAdd(claudiaData.getOrg(), // org
                claudiaData.getVdc(), // vdc
                claudiaData.getService(), // servicio (vapp)
                tierInstance.getTier().getName(), // vee
                "1"); // nombre del virtual machine

        String uri = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);
        log.debug("url: " + uri);

        String ImageName = getTemplateName(claudiaData, tierInstance.getVM());
        String payload = "<Name>" + ImageName + "</Name>";
        try {
            ClientResponse response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), uri, payload);
            if (response.getStatus() == 500) {
                ImageName = null;
            }
        } catch (ClaudiaRetrieveInfoException e) {
            ImageName = null;
            String errorCreateImage = "An error ocurred creating" + "the imagen.";
            log.error(errorCreateImage);
            throw new ClaudiaRetrieveInfoException(errorCreateImage, e);
        }

        return ImageName;
    }

    private String getTemplateName(ClaudiaData claudiaData, VM vm) {
        String fqn = vm.getFqn();
        String[] imageNameCust = fqn.split("customers.", 2);
        String[] imageNameServ = imageNameCust[1].split("services.", 2);
        String[] imageNameVees = imageNameServ[1].split("vees.", 2);
        String[] imageNameRep = imageNameVees[1].split(".replicas", 2);
        String imagName = imageNameCust[0] + imageNameServ[0] + imageNameVees[0] + imageNameRep[0];
        // String imagName = claudiaData.getOrg() + "." +
        // 4caast.customers.test9.services.casoUso4.vees.tomcat.replicas.1
        return imagName;
    }

    // 4caast.test9.casoUsoSAP135.tomcat
    private static String getURIScalabilityAdd(String org, String vdc, String vapp, String vee, String vmName) {

        String Scalability_URI = "/api/org/" + org + "/vdc/" + vdc + "/vapp/" + vapp + "/" + vee + "/" + vmName
                + "/action/templatize";
        return Scalability_URI;
    }

    /**
     * @param taskUrl
     * @throws InfrastructureException
     */
    private void checkTaskResponse(ClaudiaData claudiaData, String taskUrl) throws InfrastructureException {
        log.debug("checkTaskResponse");
        while (true) {
            String claudiaTask;
            try {
                claudiaTask = claudiaUtil.getClaudiaResource(claudiaData.getUser(), taskUrl, MediaType.WILDCARD);
                log.debug("claudiaTask" + claudiaTask);

                if (claudiaTask.contains("success")) {
                    try {
                        Thread.sleep(POLLING_INTERVAL);
                    } catch (InterruptedException e) {
                        String errorThread = "Thread Interrupted Exception " + "during polling";
                        log.warn(errorThread);
                        throw new InfrastructureException(errorThread, e);
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
                throw new InfrastructureException(errorMessage, e1);
            } catch (ClaudiaResourceNotFoundException e) {
                String errorMessage = "Error checking task " + taskUrl;
                log.error(errorMessage);
                throw new InfrastructureException(errorMessage, e);
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

    /**
     * @param claudiaUtil
     *            the claudiaUtil to set
     */
    public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
        this.claudiaUtil = claudiaUtil;
    }

    /**
     * @param vappUtils
     *            the claudiaUtil to set
     */
    public void setVappUtils(VappUtils vappUtils) {
        this.vappUtils = vappUtils;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setClaudiaResponseAnalyser(ClaudiaResponseAnalyser claudiaResponseAnalyser) {
        this.claudiaResponseAnalyser = claudiaResponseAnalyser;
    }

    public String deployServiceFull(ClaudiaData claudiaData, String ovf) throws InfrastructureException {
        String serviceResponse = null;
        ClientResponse response = null;
        String actionUri = URICreation.getURIServiceAdd(URICreation.getFQN(claudiaData.getOrg(), claudiaData.getVdc()));
        log.debug("actionUri: " + actionUri);
        String url = MessageFormat.format(systemPropertiesProvider.getProperty(NEOCLAUDIA_BASEURL),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_IP),
                systemPropertiesProvider.getProperty(NEOCLAUDIA_PORT), actionUri);

        try {
            response = claudiaUtil.postClaudiaResource(claudiaData.getUser(), url, ovf);
            serviceResponse = response.getEntity(String.class);
        } catch (ClaudiaRetrieveInfoException e) {
            String errorDeployVM = "An error ocurred deploying VM ";
            log.error(errorDeployVM);
            throw new InfrastructureException(errorDeployVM);
        }
        log.debug("serviceResponse: " + serviceResponse);

        String taskUrl = claudiaResponseAnalyser.getTaskUrl(serviceResponse);
        log.debug("taskUrl: " + taskUrl);

        if (claudiaResponseAnalyser.getTaskStatus(serviceResponse).equals("error")) {
            String errorMsgVM = "Error deploying VM ";
            log.error(errorMsgVM);
            throw new InfrastructureException(errorMsgVM);
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            log.warn("Interrupted Exception during polling");
        }

        if (!(claudiaResponseAnalyser.getTaskStatus(serviceResponse).equals("success")))

            checkTaskResponse(claudiaData, taskUrl);

        return "OK";

    }

    public void undeployService(ClaudiaData claudiaData) throws InfrastructureException {
        // TODO Auto-generated method stub

    }

}
