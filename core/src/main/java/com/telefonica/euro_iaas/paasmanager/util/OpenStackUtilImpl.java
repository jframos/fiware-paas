/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.openstack.docs.compute.api.v1.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.claudia.util.JAXBUtils;
import com.telefonica.euro_iaas.paasmanager.claudia.impl.ClaudiaClientImpl;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public class OpenStackUtilImpl implements OpenStackUtil {

    /**
     * The log.
     */
    private static Logger log = Logger.getLogger(ClaudiaClientImpl.class);

    private SystemPropertiesProvider systemPropertiesProvider;
    /**
     * HTTP code for accepted requests.
     */
    private static int http_code_accepted = 202;
    /**
     * HTTP code for accepted requests.
     */
    private static int http_code_ok = 200;
    /**
     * HTTP code for created requests.
     */
    private static int http_code_created = 201;
    /**
     * HTTP code for no content response.
     */
    private static int http_code_deleted = 204;

    /**
     * authToken to be used.
     */
    private String authToken;
    /**
     * tenant to be used.
     */
    private String tenant;

    /**
     * user to be used.
     */
    private String user;

    private HttpClientConnectionManager connectionManager;

    public OpenStackUtilImpl() {

        connectionManager = new PoolingHttpClientConnectionManager();
    }

    public HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Returns an InputStream as String.
     * 
     * @param is
     *            InputStream from response
     * @return Compute Compute
     * @throws OpenStackException
     *             OCCIException
     */
    private static String convertStreamToString(InputStream is) throws OpenStackException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw new OpenStackException(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new OpenStackException(e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     * It adds an interface to the router.
     */
    public String addInterface(Router router, Network net, PaasManagerUser user) throws OpenStackException {

        // PUT /v2.0/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e/add_router_interface
        // Accept: application/json

        log.debug("Adding an interface from network " + net.getNetworkName() + " to router " + router.getName());
        String response = null;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + router.getIdRouter() + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating router in " + router.getName() + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating router " + router.getName() + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;

    }

    /**
     * It add a network interface to the router.
     */
    public String addRouterInterface(String routerId, String subNetId, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/xml"
        // -X POST "http://10.95.171.115:9696/v2.0/subnets"
        // -d '{"network" : {"name" : "testNetwork", "admin_state_up": false}}'

        String response = null;

        try {
            String payload = "{\"subnet_id\": \"" + subNetId + "\"}";

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + routerId + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error adding interface " + subNetId + " to router " + routerId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error adding interface " + subNetId + " to router " + routerId + " from OpenStack: "
                    + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * Add a floatingIP to a full ip pool
     * 
     * @param payload
     * @return
     * @throws OpenStackException
     */
    public String allocateFloatingIP(String payload, PaasManagerUser user) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createNovaPostRequest("/" + RESOURCE_FLOATINGIP, payload, APPLICATION_XML,
                    APPLICATION_JSON, user);

            response = executeNovaRequest(request);
            // deletion.setMessage(response);

        } catch (OpenStackException e) {
            String errorMessage = "Error Getting Floating IPs " + " from tenant " + tenant + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error Getting Floating IPs " + " from tenant " + tenant + ": " + " from OpenStack: "
                    + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * Deploys a VM in Openstack
     */
    /*
     * public String deleteServer(String serverId, PaasManagerUser user) throws OpenStackException { // throw new
     * UnsupportedOperationException("Not supported yet."); // I need to know X-Auth-Token, orgID-Tennat, IP and Port //
     * curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757' // -H "Content-Type: application/json" -H
     * "Accept: application/json" // -d "{"reboot" : {"type" : "SOFT" }}" // -X POST
     * "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/6570eca2-21e2-4942-bede-f556c57af2b4/action"
     * String response = null; //TaskResult deletion = new TaskResult(); try { HttpUriRequest request =
     * createNovaDeleteRequest(OpenStackConstants.RESOURCE_SERVERS + "/" + serverId, user); response =
     * executeNovaRequest(request); // deletion.setMessage(response); } catch (InfrastructureException e) { String
     * errorMessage = "Error deleting server " + serverId + ": " + e; log.error(errorMessage); throw new
     * OpenStackException(errorMessage); //deletion.setMessage(e.getMessage()); //deletion.setSuccess(false); } catch
     * (Exception e) { String errorMessage = "Error deleting server " + serverId + " from OpenStack: " + e;
     * log.error(errorMessage); throw new OpenStackException(errorMessage); //deletion.setMessage(e.getMessage());
     * //deletion.setSuccess(false); } return response; }
     */
    /**
     * Assign certain floatingIP to a serverId
     * 
     * @param serverId
     * @param floatingIP
     * @return
     * @throws OpenStackException
     */
    public String assignFloatingIP(String serverId, String floatingIP, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/json"
        // -d "{"reboot" : {"type" : "SOFT" }}"
        // -X POST
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/6570eca2-21e2-4942-bede-f556c57af2b4/action"

        String response = null;
        // TaskResult deletion = new TaskResult();
        String payload = buildPayloadFloatingIP(floatingIP);
        try {
            HttpUriRequest request = createNovaPostRequest(RESOURCE_SERVERS + "/" + serverId + "/" + RESOURCE_ACTION,
                    payload, APPLICATION_XML, APPLICATION_JSON, user);

            response = executeNovaRequest(request);
            // deletion.setMessage(response);

        } catch (OpenStackException e) {
            String errorMessage = "Error Assigning Floating IP " + floatingIP + " to server " + serverId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error Assigning Floating IP " + floatingIP + " to server " + serverId
                    + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    private String buildAllocateFloatingIPPayload(String floatingIPPool) {
        return "<?xml version='1.0' encoding='UTF-8'?>" + "<pool>" + floatingIPPool + "</pool>";

    }

    /**
     * Building the payload to assign FloatingIP.
     * 
     * @param floatigIP
     * @return
     */
    private String buildPayloadFloatingIP(String floatigIP) {
        return "<addFloatingIp>\n<address>" + floatigIP + "</address>\n</addFloatingIp>";
    }

    /**
     * Checks if metadatas (authToken, tenant and user) were initialized.
     * 
     * @throws InfrastructureException
     */
    private void checkParam(PaasManagerUser user) throws OpenStackException {
        if (user.getToken() == null || user.getTenantId() == null || user.getUsername() == null) {
            throw new OpenStackException(ERROR_AUTHENTICATION_HEADERS);
        }
    }

    public String createNetwork(Network net, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/xml"
        // -X POST "http://10.95.171.115:9696/v2/networks"
        // -d '{"network" : {"name" : "testNetwork", "admin_state_up": false}}'

        String response = null;

        try {
            String payload = net.toJson();

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_NETWORKS, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating network " + net.getNetworkName() + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating network " + net.getNetworkName() + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * Returns a request for a NOVA DELETE petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpUriRequest createNovaDeleteRequest(String resource, PaasManagerUser user) {
        HttpUriRequest request;

        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        request = new HttpDelete(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) + user.getTenantId()
                + "/" + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE,
        // OpenStackConstants.APPLICATION_JSON);
        request.setHeader(ACCEPT, APPLICATION_JSON);
        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a NOVA GET petition.
     * 
     * @param resource
     * @param accept
     * @param user
     * @return
     */
    private HttpUriRequest createNovaGetRequest(String resource, String accept, PaasManagerUser user) {
        HttpUriRequest request;

        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        request = new HttpGet(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) + user.getTenantId()
                + "/" + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE,
        // OpenStackConstants.APPLICATION_XML);
        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a NOVA POST petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpPost createNovaPostRequest(String resource, String payload, String content, String accept,
            PaasManagerUser user) throws OpenStackException {
        HttpPost request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        request = new HttpPost(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) + user.getTenantId()
                + "/" + resource);

        try {
            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        request.setHeader(CONTENT_TYPE, content);
        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a QUANTUM DELETE petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpUriRequest createQuantumDeleteRequest(String resource, PaasManagerUser user) {
        HttpUriRequest request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        request = new HttpDelete(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_VERSION) + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE, OpenStackConstants.APPLICATION_JSON);
        request.setHeader(ACCEPT, APPLICATION_JSON);
        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a Quantum GET petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpUriRequest createQuantumGetRequest(String resource, String accept, PaasManagerUser user) {
        HttpUriRequest request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        request = new HttpGet(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_VERSION) + resource);

        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a Quantum POST petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpPost createQuantumPostRequest(String resource, String payload, String content, PaasManagerUser user)
            throws OpenStackException {
        HttpPost request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        request = new HttpPost(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_VERSION) + resource);

        try {

            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        request.setHeader(ACCEPT, APPLICATION_JSON);

        request.setHeader(CONTENT_TYPE, content);

        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /**
     * Returns a request for a Quantum POST petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpPut createQuantumPutRequest(String resource, String payload, String content, PaasManagerUser user)
            throws OpenStackException {
        HttpPut request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(user);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        request = new HttpPut(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_QUANTUM_VERSION) + resource);

        try {

            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        request.setHeader(ACCEPT, APPLICATION_JSON);

        request.setHeader(CONTENT_TYPE, content);

        request.setHeader(X_AUTH_TOKEN, user.getToken());

        return request;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.claudia.smi.OpenStackClient#createRouter(java.lang.String,
     * com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser )
     */
    public String createRouter(Router router, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/xml"
        // -X POST "http://10.95.171.115:9696/v2.0/subnets"

        // -d '{"network" : {"name" : "testNetwork", "admin_state_up": false}}'
        log.debug("Creating a router " + router.getName());

        String response = null;

        try {
            String payload = router.toJson();
            log.debug(payload);

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_ROUTERS, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {

            String errorMessage = "Error creating router in " + router.getName() + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating router " + router.getName() + " from OpenStack: " + e;
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * It creates a vm in Openstack.
     */
    public String createServer(String payload, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/images/88y6ga216ad4s33ra6asd5fgrg7"
        HttpUriRequest request;
        String response = null;
        Server server = null;
        String getResponse = "getResponse";

        try {
            request = createNovaPostRequest(RESOURCE_SERVERS, payload, APPLICATION_JSON, APPLICATION_XML, user);
        } catch (OpenStackException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        try {
            response = executeNovaRequest(request);

            // String id = response.split(",")[1];
            server = JAXBUtils.unmarshall(response, false, Server.class);

            // Mecanismo de sondeo.
            while (!(getResponse.contains("ACTIVE") || getResponse.contains("ERROR"))) {
                getResponse = getServer(server.getId(), user);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            String errorMessage = "Error creating server: " + e.getMessage();
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        if (getResponse.contains("ERROR")) {
            throw new OpenStackException("Error to deploy the VM " + server.getId());
        }
        return server.getId();
    }

    /**
     * It creates a subnet in Openstack.
     */
    public String createSubNet(SubNetwork subNet, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/xml"
        // -X POST "http://10.95.171.115:9696/v2.0/subnets"
        // -d '{"network" : {"name" : "testNetwork", "admin_state_up": false}}'

        log.debug("Creating sub net " + subNet.getName());
        String response = null;

        try {
            String payload = subNet.toJson();
            log.info("Payload " + payload);

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_SUBNETS, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating subNetwork in " + subNet.getIdNetwork() + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating subNetwork " + subNet.getIdNetwork() + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * It deletes a network.
     */
    public String deleteNetwork(String networkId, PaasManagerUser user) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757...'
        // -H "Accept: application/json"
        // -X DELETE "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumDeleteRequest(RESOURCE_NETWORKS + "/" + networkId, user);

        String response = null;

        try {

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting network " + networkId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting network " + networkId + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    public String deleteRouter(String routerId, PaasManagerUser user) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757...'
        // -H "Accept: application/json"
        // -X DELETE "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumDeleteRequest(RESOURCE_ROUTERS + "/" + routerId, user);

        String response = null;

        try {

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting router " + routerId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting router " + routerId + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * It deletes a server.
     */

    public String deleteServer(String serverId, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/json"
        // -d "{"reboot" : {"type" : "SOFT" }}"
        // -X POST
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/
        // 6570eca2-21e2-4942-bede-f556c57af2b4/action"

        String response = null;
        // TaskResult deletion = new TaskResult();

        try {
            HttpUriRequest request = createNovaDeleteRequest(RESOURCE_SERVERS + "/" + serverId, user);

            response = executeNovaRequest(request);
            // deletion.setMessage(response);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting server " + serverId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting server " + serverId + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    public void deleteSubNetwork(String idSubNet, PaasManagerUser user) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createNovaDeleteRequest(RESOURCE_SERVERS + "/" + idSubNet, user);

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting server " + idSubNet + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting server " + idSubNet + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.claudia.smi.OpenStackClient#getNetworkDetails(java.lang.String,
     * com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser)
     */

    /**
     * Method to execute a request and get the response from NOVA.
     * 
     * @param request
     *            the request to be executed
     * @return HttpUriRequest the response from server
     * @throws OpenStackException
     */
    private String executeNovaRequest(HttpUriRequest request) throws OpenStackException {
        String[] newHeaders = null;
        // Where the response is located. 0 for json, 1 for XML (it depends on
        // the \n)
        int responseLocation = 0;

        CloseableHttpClient httpClient = getHttpClient();

        if (request.containsHeader(ACCEPT) & request.getFirstHeader(ACCEPT).getValue().equals(APPLICATION_XML)) {
            responseLocation = 1;
        }
        HttpResponse response = null;

        try {
            response = httpClient.execute(request);
            // if (response.getEntity() != null) {
            if ((response.getStatusLine().getStatusCode() != http_code_deleted)) {

                InputStream is = response.getEntity().getContent();
                String result = convertStreamToString(is);
                log.debug("Result " + result);
                is.close();

                if ((response.getStatusLine().getStatusCode() == http_code_ok)
                        || (response.getStatusLine().getStatusCode() == http_code_accepted)
                        || (response.getStatusLine().getStatusCode() == http_code_created)) {

                    newHeaders = result.split("\n");
                } else {
                    log.debug(" HttpResponse " + response.getStatusLine().getStatusCode());
                    throw new OpenStackException(result);
                }

                response.getEntity().getContent().close();
            } else {
                return response.getStatusLine().getReasonPhrase();
            }

        } catch (Exception e) {
            if (response.getStatusLine().getStatusCode() == http_code_accepted) {
                return response.getStatusLine().getReasonPhrase();
            } else {
                throw new OpenStackException(e.getMessage());
            }
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.warn("Error in close httpclient");
            }
        }

        if (response.containsHeader("Location")
                && response.getFirstHeader("Location").getValue().contains(RESOURCE_IMAGES)) {

            return response.getFirstHeader("Location").getValue();
        }

        return newHeaders[responseLocation];
    }

    protected CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    /**
     * Obtains the attribute value from a node.
     * 
     * @param node
     * @param attribute
     * @return
     */
    private String findAttributeValueInNode(Node node, String attribute) {
        return node.getAttributes().getNamedItem(attribute).getTextContent();
    }

    /*
     * Obtains the list of nodes whose tag is nodeListTag
     */
    private NodeList findNodeList(String xmlDoc, String nodeListTag) throws OpenStackException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        NodeList nodeList = null;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlDoc)));

            nodeList = doc.getElementsByTagName(nodeListTag);

        } catch (SAXException e) {
            String errorMessage = "SAXException when obtaining nodeList." + " Desc: " + e.getMessage();
            log.warn(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (ParserConfigurationException e) {
            String errorMessage = "ParserConfigurationException when obtaining " + "NodelIst. Desc: " + e.getMessage();
            log.warn(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (IOException e) {
            String errorMessage = "IOException when obtaining " + "NodeList. Desc: " + e.getMessage();
            log.warn(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unexpected exception : " + e.getMessage();
            log.warn(errorMessage);
            throw new OpenStackException(errorMessage);
        }
        return nodeList;
    }

    public String getFloatingIP(PaasManagerUser user) throws OpenStackException {
        String floatingIP = null;
        // Get FloatingIPS fron tenant
        String getFloatingIPsResponse = getFloatingIPs(user);

        if (isAnyFloatingIPFreeToBeAssigned(getFloatingIPsResponse)) {
            floatingIP = getFloatingIPFree(getFloatingIPsResponse);
        } else {
            floatingIP = allocateFloatingIP(
                    buildAllocateFloatingIPPayload(systemPropertiesProvider
                            .getProperty(SystemPropertiesProvider.NOVA_IPFLOATING_POOLNAME)),
                    user);
            getFloatingIPsResponse = getFloatingIPs(user);
            floatingIP = getFloatingIPFree(getFloatingIPsResponse);
        }

        return floatingIP;
    }

    /**
     * Get a Free FloatingIP.
     * 
     * @param xmlDoc
     * @return
     * @throws Exception
     */
    private String getFloatingIPFree(String xmlDoc) throws OpenStackException {
        String floatingIP = null;
        NodeList floatingIPs = findNodeList(xmlDoc, "floating_ip");
        for (int i = 0; i < floatingIPs.getLength(); i++) {
            Node floatingIPNode = floatingIPs.item(i);
            if (findAttributeValueInNode(floatingIPNode, "instance_id").equals("None")) {
                floatingIP = findAttributeValueInNode(floatingIPNode, "ip");
            }
        }
        return floatingIP;
    }

    /**
     * Obtain the xml including all FloatingIPs of a certain tenant.
     * 
     * @return
     * @throws OpenStackException
     */
    private String getFloatingIPs(PaasManagerUser user) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createNovaGetRequest("/" + RESOURCE_FLOATINGIP, APPLICATION_XML, user);

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error Getting Floating IPs " + " from tenant " + tenant + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error Getting Floating IPs " + " from tenant " + tenant + ": " + " from OpenStack: "
                    + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /**
     * It obtains network details.
     */
    public String getNetworkDetails(String networkId, PaasManagerUser user) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Accept: application/xml"
        // -X GET "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumGetRequest(RESOURCE_NETWORKS + "/" + networkId, APPLICATION_XML, user);

        String response = null;

        try {

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error getting network " + networkId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error getting network " + networkId + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.claudia.smi.OpenStackClient#deleteNetwork(java.lang.String,
     * com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser)
     */

    /**
     * It obtains network list.
     */
    public String getNetworks(PaasManagerUser user) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET "http://10.95.171.115:9696/v2/networks"
        HttpUriRequest request = createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_XML, user);

        String response = null;

        try {

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error getting networks: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error getting networks from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    public String getServer(String serverId, PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/88y6ga216ad4s33ra6asd5fgrg7"

        HttpUriRequest request = createNovaGetRequest(RESOURCE_SERVERS + "/" + serverId, APPLICATION_XML, user);

        String response = null;
        // TaskResult server = new TaskResult();

        try {
            response = executeNovaRequest(request);
            // server.setMessage(response);

        } catch (Exception e) {
            String errorMessage = "Error getting server " + serverId + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
            // server.setMessage(e.getMessage());
            // server.setSuccess(false);
        }
        return response;
    }

    /**
     * Is Any FloatingIP Free.
     * 
     * @param xmlDoc
     * @return
     * @throws Exception
     */
    private boolean isAnyFloatingIPFreeToBeAssigned(String xmlDoc) throws OpenStackException {

        NodeList floatingIPs = findNodeList(xmlDoc, "floating_ip");
        for (int i = 0; i < floatingIPs.getLength(); i++) {
            Node floatingIPNode = floatingIPs.item(i);
            if (findAttributeValueInNode(floatingIPNode, "instance_id").equals("None")) {
                return true;
            }
        }
        return false;
    }

    public String listServers(PaasManagerUser user) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers"

        HttpUriRequest request = createNovaGetRequest(RESOURCE_SERVERS, APPLICATION_JSON, user);

        String response = null;
        // TaskResult server = new TaskResult();

        try {
            response = executeNovaRequest(request);
            // server.setMessage(response);

        } catch (Exception e) {
            String errorMessage = "Error getting lis of servers " + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
            // server.setMessage(e.getMessage());
            // server.setSuccess(false);
        }
        return response;
    }

    /**
     * It adds an interface to the router
     */
    public String removeInterface(Router router, String net, PaasManagerUser user) throws OpenStackException {

        // PUT /v2.0/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e/remove_router_interface
        // Accept: application/json

        // {"subnet_id": "a2f1f29d-571b-4533-907f-5803ab96ead1"}

        log.debug("Removing an interface from network " + net + " to router " + router.getName());
        String response = null;

        try {
            String payload = "{\"subnet_id\": \"" + net + "\"}";
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + router.getIdRouter() + "/"
                    + RESOURCE_REMOVE_INTERFACE, payload, APPLICATION_JSON, user);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting interface in router " + router.getName() + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting interface in router " + router.getName() + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;

    }

    /**
     * Return a string with absolute limits values by tenantId.
     * 
     * <pre>
     * GET http://host:port/v2/tenantId/limits
     * Accept: application/json
     * X-Auth-Token: ea90309ce14b4da490fe035c618515db
     * </pre>
     * 
     * @param paasManagerUser
     *            parameter to rest client
     * @return a string with data
     * @throws OpenStackException
     */
    @Override
    public String getAbsoluteLimits(PaasManagerUser paasManagerUser) throws OpenStackException {

        HttpUriRequest request = createNovaGetRequest("limits", APPLICATION_JSON, paasManagerUser);

        String response = executeNovaRequest(request);

        return response;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
