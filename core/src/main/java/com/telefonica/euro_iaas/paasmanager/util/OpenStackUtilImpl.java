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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.openstack.docs.compute.api.v1.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.claudia.util.JAXBUtils;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public class OpenStackUtilImpl implements OpenStackUtil {

    /**
     * The log.
     */

    private static Logger log = Logger.getLogger(OpenStackUtilImpl.class);
    /**
     * the properties configuration.
     */
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

    private OpenStackRegion openStackRegion;

    /**
     * The constructor.
     */
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

    public String addInterface(String idRouter, NetworkInstance net, String region, String token, String vdc)
            throws OpenStackException {
        // PUT /v2.0/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e/add_router_interface
        // Accept: application/json

        log.debug("Adding an interface from network " + net.getNetworkName() + " to router " + idRouter);
        String response = null;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating router in " + idRouter + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating router " + idRouter + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;

    }

    /**
     * It adds an interface to the router.
     */
    public String addInterfaceToPublicRouter(PaasManagerUser user, NetworkInstance net, String region)
            throws OpenStackException {

        String idRouter = systemPropertiesProvider.getProperty(SystemPropertiesProvider.PUBLIC_ROUTER_ID);
        PaasManagerUser adminUser = this.getAdminUser(user);

        log.debug("tenantid " + adminUser.getTenantId());
        log.debug("token " + adminUser.getToken());
        log.debug("user name " + adminUser.getUserName());

        log.debug("Adding an interface from network " + net.getNetworkName() + " to router " + idRouter);
        String response;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, adminUser.getToken(),
                    adminUser.getTenantId());
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating router in " + idRouter + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating router " + idRouter + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;

    }

    /**
     * It add a network interface to the router.
     */
    public String addRouterInterface(String routerId, String subNetId, String region, String token, String vdc)
            throws OpenStackException {
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
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);
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
     * Add a floatingIP to a full ip pool.
     * 
     * @param payload
     * @param region
     * @return
     * @throws OpenStackException
     */
    public String allocateFloatingIP(String payload, String region, String token, String vdc) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createNovaPostRequest("/" + RESOURCE_FLOATINGIP, payload, APPLICATION_XML,
                    APPLICATION_JSON, region, token, vdc);

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
     * Assign certain floatingIP to a serverId.
     * 
     * @param serverId
     * @param floatingIP
     * @return
     * @throws OpenStackException
     */
    public String assignFloatingIP(String serverId, String floatingIP, String region, String token, String vdc)
            throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/json"
        // -d "{"reboot" : {"type" : "SOFT" }}"
        // -X POST
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/6570eca2-21e2-4942-bede-f556c57af2b4/action"

        String response;
        // TaskResult deletion = new TaskResult();
        String payload = buildPayloadFloatingIP(floatingIP);
        try {
            HttpUriRequest request = createNovaPostRequest(RESOURCE_SERVERS + "/" + serverId + "/" + RESOURCE_ACTION,
                    payload, APPLICATION_XML, APPLICATION_JSON, region, token, vdc);

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
     * Checks if metadatas (authToken and tenant) were initialized.
     * 
     * @throws OpenStackException
     */
    private void checkParam(String tenantId, String token) throws OpenStackException {
        if (token == null || tenantId == null) {
            throw new OpenStackException(ERROR_AUTHENTICATION_HEADERS);
        }
    }

    /**
     * It obtains the credentials to invoke as a admin user.
     * 
     * @return
     * @throws OpenStackException
     */
    public PaasManagerUser getAdminUser(PaasManagerUser user) throws OpenStackException {
        HttpPost postRequest = createKeystonePostRequest();
        ArrayList<Object> response = executePostRequest(postRequest);
        return extractData(response, user);
    }

    /**
     * It obtains the request for invoking Openstack keystone with admin credentials.
     * 
     * @return
     * @throws OpenStackException
     */
    private HttpPost createKeystonePostRequest() throws OpenStackException {
        // curl -d '{"auth": {"tenantName": "demo", "passwordCredentials":
        // {"username": "admin", "password": "temporal"}}}'
        // -H "Content-type: application/json"
        // -H "Accept: application/xml"�
        // http://10.95.171.115:35357/v2.0/tokens

        String keystoneURL = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL);
        String adminUser = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER);
        String adminPass = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS);
        String adminTenant = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);

        HttpEntity entity = null;
        HttpPost postRequest = new HttpPost(keystoneURL + "tokens");
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setHeader("Accept", "application/xml");

        String msg = "{\"auth\": {\"tenantName\": \"" + adminTenant + "\", \""
                + "passwordCredentials\":{\"username\": \"" + adminUser + "\"," + " \"password\": \"" + adminPass
                + "\"}}}";

        try {
            entity = new StringEntity(msg);
        } catch (UnsupportedEncodingException ex) {
            log.error("Unsupported encoding exception");
            throw new OpenStackException("Unsupported encoding exception " + ex.getMessage());
        }
        postRequest.setEntity(entity);
        return postRequest;
    }

    private ArrayList<Object> executePostRequest(HttpPost postRequest) throws OpenStackException {
        HttpResponse response;
        CloseableHttpClient httpClient = getHttpClient();
        ArrayList<Object> message = new ArrayList();

        Date localDate = null;
        String aux;
        try {
            response = httpClient.execute(postRequest);
            localDate = new Date();
            if ((response.getStatusLine().getStatusCode() != 201) && (response.getStatusLine().getStatusCode() != 200)) {
                log.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String temp = "";

            while ((aux = br.readLine()) != null) {
                temp += aux;
            }

            message.add(temp);

            String aux1 = response.getHeaders("Date")[0].getValue();
            log.info("Date recibido: " + aux1);
            message.add(response.getHeaders("Date")[0].getValue());
            HttpEntity ent = response.getEntity();
            if (ent != null) {
                EntityUtils.consume(ent);
            }

        } catch (ClientProtocolException ex) {
            log.error("Client protocol exception");
            throw new OpenStackException("Client protocol exception " + ex.getMessage());
        } catch (IOException ex) {
            log.error("I/O exception of some sort has occurred");
            throw new OpenStackException("I/O exception of some sort has occurred " + ex.getMessage());
        }
        return message;
    }

    protected PaasManagerUser extractData(ArrayList<Object> response, PaasManagerUser user) {
        String payload = (String) response.get(0);

        int i = payload.indexOf("token");
        int j = payload.indexOf(">", i);
        String token = payload.substring(i - 1, j + 1);
        String tenantId = "";

        // token = "<token expires=\"2012-11-13T15:01:51Z\" id=\"783bec9d7d734f1e943986485a90966d\">";
        // Regular Expression <\s*token\s*(issued_at=\".*?\"\s*)?expires=\"(.*?)(\"\s*id=\")(.*)\"\/*>
        // as a Java string "<\\s*token\\s*(issued_at=\\\".*?\\\"\\s*)?expires=\\\"(.*?)(\\\"\\s*id=\\\")(.*)\\\"\\/*>"
        String pattern1 = "<\\s*token\\s*(issued_at=\\\".*?\\\"\\s*)?expires=\\\"(.*?)(\\\"\\s*id=\\\")(.*)\\\"\\/*>";

        if (token.matches(pattern1)) {

            token = token.replaceAll(pattern1, "$4");
            log.info("token id: " + token);
        } else {
            log.error("Token format unknown: " + token);

            throw new RuntimeException("Token format unknown:\n " + token);
        }

        i = payload.indexOf("tenant");
        j = payload.indexOf(">", i);
        tenantId = payload.substring(i - 1, j + 1);

        // Regular Expression (<\s*tenant\s*.*)("\s*id=")(.*?)("\s*.*/*>)
        // as a Java string "(<\\s*tenant\\s*.*)(\"\\s*id=\")(.*?)(\"\\s*.*/*>)"
        pattern1 = "(<\\s*tenant\\s*.*)(\"\\s*id=\")(.*?)(\"\\s*.*/*>)";

        if (tenantId.matches(pattern1)) {
            tenantId = tenantId.replaceAll(pattern1, "$3");
        } else {
            log.error("Tenant format unknown:\n " + tenantId);

            throw new RuntimeException("Tenant format unknown:\n " + tenantId);
        }
        PaasManagerUser user2 = new PaasManagerUser(tenantId, token, user.getAuthorities());
        return user2;
    }

    public String createNetwork(String payload, String region, String token, String vdc) throws OpenStackException {

        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/xml"
        // -X POST "http://10.95.171.115:9696/v2/networks"
        // -d '{"network" : {"name" : "testNetwork", "admin_state_up": false}}'
        String response = null;

        try {

            log.debug("Payload " + payload);

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_NETWORKS, payload, APPLICATION_JSON, region,
                    token, vdc);
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error creating network " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error creating network from OpenStack: " + e;
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
    private HttpUriRequest createNovaDeleteRequest(String resource, String region, String token, String vdc)
            throws OpenStackException {
        HttpUriRequest request;

        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String novaUrl = openStackRegion.getNovaEndPoint(region, token);
        request = new HttpDelete(novaUrl + vdc + "/" + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE,
        // OpenStackConstants.APPLICATION_JSON);
        log.debug("NOVA DELETE url " + request.getURI().toString());
        log.debug("NOVA token " + token);
        request.setHeader(ACCEPT, APPLICATION_JSON);
        request.setHeader(X_AUTH_TOKEN, token);

        return request;
    }

    /**
     * Returns a request for a NOVA GET petition.
     */
    private HttpUriRequest createNovaGetRequest(String resource, String accept, String region, String token, String vdc)
            throws OpenStackException {
        HttpUriRequest request;

        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String novaUrl = openStackRegion.getNovaEndPoint(region, token);
        request = new HttpGet(novaUrl + vdc + "/" + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE,
        // OpenStackConstants.APPLICATION_XML);
        log.debug("NOVA GET url " + request.getURI().toString());
        log.debug("NOVA token " + token);
        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, token);

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
            String region, String token, String vdc) throws OpenStackException {
        HttpPost request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        String novaUrl = openStackRegion.getNovaEndPoint(region, token);

        request = new HttpPost(novaUrl + vdc + "/" + resource);

        try {
            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        log.debug("NOVA POST url " + request.getURI().toString());
        log.debug("NOVA token " + token);

        request.setHeader(CONTENT_TYPE, content);
        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, token);

        return request;
    }

    /**
     * Returns a request for a QUANTUM DELETE petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpUriRequest createQuantumDeleteRequest(String resource, String region, String vdc, String token)
            throws OpenStackException {
        HttpUriRequest request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String quantumUrl = null;
        quantumUrl = openStackRegion.getQuantumEndPoint(region, token);
        request = new HttpDelete(quantumUrl + resource);

        // request.setHeader(OpenStackConstants.CONTENT_TYPE, OpenStackConstants.APPLICATION_JSON);
        request.setHeader(ACCEPT, APPLICATION_JSON);
        log.debug(X_AUTH_TOKEN + " " + token);
        request.setHeader(X_AUTH_TOKEN, token);

        return request;
    }

    /**
     * Returns a request for a Quantum GET petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpUriRequest createQuantumGetRequest(String resource, String accept, String region, String token,
            String vdc) throws OpenStackException {
        HttpUriRequest request;

        // Check that the auth token, tenant and user was initialized previously.
        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String quantumUrl = openStackRegion.getQuantumEndPoint(region, token);
        request = new HttpGet(quantumUrl + resource);

        request.setHeader(ACCEPT, accept);
        request.setHeader(X_AUTH_TOKEN, token);

        return request;
    }

    /**
     * Returns a request for a Quantum POST petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpPost createQuantumPostRequest(String resource, String payload, String content, String region,
            String token, String vdc) throws OpenStackException {
        log.debug("createQuantumPostRequest " + resource);
        HttpPost request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        String quantumUrl = openStackRegion.getQuantumEndPoint(region, token);
        request = new HttpPost(quantumUrl + resource);

        try {

            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        request.setHeader(ACCEPT, APPLICATION_JSON);

        request.setHeader(CONTENT_TYPE, content);
        log.debug("Content " + content);

        request.setHeader(X_AUTH_TOKEN, token);
        log.debug("user.getToken() " + token);

        return request;
    }

    /**
     * Returns a request for a Quantum POST petition.
     * 
     * @param resource
     *            the target resource
     * @return HttpUriRequest the request
     */
    private HttpPut createQuantumPutRequest(String resource, String payload, String content, String region,
            String token, String vdc) throws OpenStackException {
        HttpPut request;

        // Check that the authtoken, tenant and user was initialized
        // previously.
        try {
            checkParam(vdc, token);
        } catch (OpenStackException ex) {
            java.util.logging.Logger.getLogger(OpenStackUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Payload " + payload);

        String quantumUrl = openStackRegion.getQuantumEndPoint(region, token);
        request = new HttpPut(quantumUrl + resource);

        try {

            request.setEntity(new StringEntity(payload));

        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        request.setHeader(ACCEPT, APPLICATION_JSON);

        request.setHeader(CONTENT_TYPE, content);

        request.setHeader(X_AUTH_TOKEN, token);

        return request;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.claudia.smi.OpenStackClient#createRouter(java.lang.String,
     * com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser )
     */
    public String createRouter(RouterInstance router, String region, String token, String vdc)
            throws OpenStackException {
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

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_ROUTERS, payload, APPLICATION_JSON, region,
                    token, vdc);
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
    public String createServer(String payload, String region, String token, String vdc) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/images/88y6ga216ad4s33ra6asd5fgrg7"
        log.debug("create server " + token + " vdc " + vdc);
        HttpUriRequest request;
        String response = null;
        Server server = null;
        String getResponse = "";

        try {
            request = createNovaPostRequest(RESOURCE_SERVERS, payload, APPLICATION_JSON, APPLICATION_XML, region,
                    token, vdc);
        } catch (OpenStackException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        try {
            response = executeNovaRequest(request);
            // String id = response.split(",")[1];
            server = JAXBUtils.unmarshall(response, false, Server.class);

            // Mecanismo de sondeo.
            while (!(getResponse.contains("ACTIVE") || getResponse.contains("ERROR"))) {
                getResponse = getServer(server.getId(), region, token, vdc);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            String errorMessage = "Error creating server: " + e.getMessage();
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        if (getResponse.contains("ERROR")) {

            // parse error
            try {
                server = JAXBUtils.unmarshall(getResponse, false, Server.class);
            } catch (IOException e) {
                throw new OpenStackException("Error in parse response after deploy the VM " + server.getId());
            }
            throw new OpenStackException("Fault (" + server.getFault().getCode() + "):"
                    + server.getFault().getMessage());

        }
        return server.getId();
    }

    /**
     * It creates a subnet in Openstack.
     */
    public String createSubNet(SubNetworkInstance subNet, String region, String token, String vdc)
            throws OpenStackException {
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

            HttpUriRequest request = createQuantumPostRequest(RESOURCE_SUBNETS, payload, APPLICATION_JSON, region,
                    token, vdc);
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
    public String deleteNetwork(String networkId, String region, String token, String vdc) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757...'
        // -H "Accept: application/json"
        // -X DELETE "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"

        HttpUriRequest request = createQuantumDeleteRequest(RESOURCE_NETWORKS + "/" + networkId, region, vdc, token);

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

    public String deleteRouter(String routerId, String region, String token, String vdc) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757...'
        // -H "Accept: application/json"
        // -X DELETE "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumDeleteRequest(RESOURCE_ROUTERS + "/" + routerId, region, token, vdc);

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

    public String deleteServer(String serverId, String region, String token, String vdc) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/json" -H "Accept: application/json"
        // -d "{"reboot" : {"type" : "SOFT" }}"
        // -X POST
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/
        // 6570eca2-21e2-4942-bede-f556c57af2b4/action"

        String response = null;

        try {
            HttpUriRequest request = createNovaDeleteRequest(RESOURCE_SERVERS + "/" + serverId, region, token, vdc);

            response = executeNovaRequest(request);

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

    public void deleteSubNetwork(String idSubNet, String region, String vdc, String token) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createQuantumDeleteRequest(RESOURCE_SUBNETS + "/" + idSubNet, region, vdc, token);

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting subNet " + idSubNet + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting subNet " + idSubNet + " from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

    }

    /**
     * Method to execute a request and get the response from NOVA.
     * 
     * @param request
     *            the request to be executed
     * @return HttpUriRequest the response from server
     * @throws OpenStackException
     */
    private String executeNovaRequest(HttpUriRequest request) throws OpenStackException {
        log.debug("executeNovaRequest " + request.getURI().toString());
        String[] newHeaders = null;
        // Where the response is located. 0 for json, 1 for XML (it depends on
        // the \n)
        int responseLocation = 0;

        CloseableHttpClient httpClient = getHttpClient();

        if (request.containsHeader(ACCEPT) && request.getFirstHeader(ACCEPT).getValue().equals(APPLICATION_XML)) {
            responseLocation = 1;
        }
        HttpResponse response = null;

        try {
            response = httpClient.execute(request);
            log.debug("Status : " + response.getStatusLine().getStatusCode());
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
                    if (result.indexOf("badRequest") != -1) {
                        String error = result.substring(result.indexOf("<message>") + 9, result.indexOf("</message>"));
                        log.debug("Error in the request " + error);
                        throw new OpenStackException(error);
                    }

                    throw new OpenStackException(result);
                }

                response.getEntity().getContent().close();
            } else {
                return response.getStatusLine().getReasonPhrase();
            }

        } catch (Exception e) {
            log.warn("Error to execute the request " + e.getMessage());
            if (response.getStatusLine().getStatusCode() == http_code_accepted) {
                return response.getStatusLine().getReasonPhrase();
            } else {
                throw new OpenStackException(e.getMessage());
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

    public String getFloatingIP(String region, String token, String vdc) throws OpenStackException {
        String floatingIP = null;
        // Get FloatingIPS fron tenant
        String getFloatingIPsResponse = getFloatingIPs(region, token, vdc);

        if (isAnyFloatingIPFreeToBeAssigned(getFloatingIPsResponse)) {
            floatingIP = getFloatingIPFree(getFloatingIPsResponse);
        } else {
            floatingIP = allocateFloatingIP(
                    buildAllocateFloatingIPPayload(systemPropertiesProvider
                            .getProperty(SystemPropertiesProvider.NOVA_IPFLOATING_POOLNAME)),
                    region, token, vdc);
            getFloatingIPsResponse = getFloatingIPs(region, token, vdc);
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
    private String getFloatingIPs(String region, String token, String vdc) throws OpenStackException {
        String response = null;

        try {
            HttpUriRequest request = createNovaGetRequest("/" + RESOURCE_FLOATINGIP, APPLICATION_XML, region, token,
                    vdc);

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
    public String getNetworkDetails(String networkId, String region, String token, String vdc)
            throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Accept: application/xml"
        // -X GET "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumGetRequest(RESOURCE_NETWORKS + "/" + networkId, APPLICATION_XML, region,
                token, vdc);

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

    /**
     * It obtains subnetwork details.
     */
    public String getSubNetworkDetails(String subNetworkId, String region, String token, String vdc)
            throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Accept: application/xml"
        // -X GET "http://10.95.171.115:9696/v2/networks/5867b6bd-ba18-4ae3-a34f-dd0f2e189eb6"
        HttpUriRequest request = createQuantumGetRequest(RESOURCE_SUBNETS + "/" + subNetworkId, APPLICATION_XML,
                region, token, vdc);

        String response;

        try {

            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error getting network " + subNetworkId + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error getting network " + subNetworkId + " from OpenStack: " + e;
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
    public String getNetworks(String region, String token, String vdc) throws OpenStackException {
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET "http://10.95.171.115:9696/v2/networks"
        HttpUriRequest request = createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_XML, region, token, vdc);

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

    public String getServer(String serverId, String region, String token, String vdc) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers/88y6ga216ad4s33ra6asd5fgrg7"

        HttpUriRequest request = createNovaGetRequest(RESOURCE_SERVERS + "/" + serverId, APPLICATION_XML, region,
                token, vdc);

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

    public String listServers(String region, String token, String vdc) throws OpenStackException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // I need to know X-Auth-Token, orgID-Tennat, IP and Port
        // curl -v -H 'X-Auth-Token: a92287ea7c2243d78a7180ef3f7a5757'
        // -H "Content-Type: application/xml" -H "Accept: application/json"
        // -X GET
        // "http://10.95.171.115:8774/v2/30c60771b6d144d2861b21e442f0bef9/servers"

        HttpUriRequest request = createNovaGetRequest(RESOURCE_SERVERS, APPLICATION_JSON, region, token, vdc);

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
    public String removeInterface(RouterInstance router, String net, String region, String token, String vdc)
            throws OpenStackException {

        // PUT /v2.0/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e/remove_router_interface
        // Accept: application/json

        // {"subnet_id": "a2f1f29d-571b-4533-907f-5803ab96ead1"}

        log.debug("Removing an interface from network " + net + " to router " + router.getName());
        String response = null;

        try {
            String payload = "{\"subnet_id\": \"" + net + "\"}";
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + router.getIdRouter() + "/"
                    + RESOURCE_REMOVE_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);
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
     */
    public String getAbsoluteLimits(PaasManagerUser user, String region) throws OpenStackException {
        PaasManagerUser user2 = this.getAdminUser(user);

        log.debug("tenantid " + user.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = createNovaGetRequest("limits", APPLICATION_JSON, region, user.getToken(),
                user.getTenantId());

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

    public String listNetworks(PaasManagerUser user, String region) throws OpenStackException {
        log.debug("List networks from user " + user.getUserName());

        PaasManagerUser user2 = this.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_JSON, region, user2.getToken(),
                user2.getUserName());

        String response = null;

        try {
            response = executeNovaRequest(request);
            log.debug("List network response");
            log.debug(response);

        } catch (Exception e) {
            String errorMessage = "Error getting list of networks from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }
        return response;

    }

    public String listPorts(PaasManagerUser user, String region) throws OpenStackException {
        log.debug("List ports from user " + user.getUserName());
        PaasManagerUser user2 = this.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = createQuantumGetRequest(RESOURCE_PORTS, APPLICATION_JSON, region, user2.getToken(),
                user2.getUserName());

        String response = null;

        try {
            response = executeNovaRequest(request);
            log.debug("List port response");
            log.debug(response);

        } catch (Exception e) {
            String errorMessage = "Error getting list of networks from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }
        return response;

    }

    public String deleteInterfaceToPublicRouter(PaasManagerUser user, NetworkInstance net, String region)
            throws OpenStackException {
        log.debug("Delete interface in public router");
        String idRouter = systemPropertiesProvider.getProperty(SystemPropertiesProvider.PUBLIC_ROUTER_ID);
        PaasManagerUser user2 = this.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        log.debug("Deleting an interface from network " + net.getNetworkName() + " to router " + idRouter);
        String response = null;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_REMOVE_INTERFACE, payload, APPLICATION_JSON, region, user2.getToken(),
                    user2.getTenantId());
            response = executeNovaRequest(request);

        } catch (OpenStackException e) {
            String errorMessage = "Error deleting interface in  public router " + idRouter + ": " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error deleting interface in  public router from OpenStack: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return response;
    }

    public OpenStackRegion getOpenStackRegion() {
        return openStackRegion;
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }
}
