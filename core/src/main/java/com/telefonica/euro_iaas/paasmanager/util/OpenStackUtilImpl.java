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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    
    private OpenOperationUtil openOperationUtil;

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

            HttpUriRequest request = openOperationUtil.createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);

            response = openOperationUtil.executeNovaRequest(request);

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

        String idRouter = this.getPublicRouter(user, region).getIdRouter();
        PaasManagerUser adminUser = openOperationUtil.getAdminUser(user);

        log.debug("tenantid " + adminUser.getTenantId());
        log.debug("token " + adminUser.getToken());
        log.debug("user name " + adminUser.getUserName());

        log.debug("Adding an interface from network " + net.getNetworkName() + " to router " + idRouter);
        String response;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = openOperationUtil.createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, adminUser.getToken(),
                    adminUser.getTenantId());
            response = openOperationUtil.executeNovaRequest(request);

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
     * It gets the public admin network
     */
    public NetworkInstance getPublicAdminNetwork(PaasManagerUser user,  String region)
            throws OpenStackException {
        log.debug("Obtain public admin network");

        PaasManagerUser adminUser = openOperationUtil.getAdminUser(user);
        
        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_XML, region, adminUser.getToken(), adminUser.getTenantId());

        String response = null;

        try {
            response = openOperationUtil.executeNovaRequest(request);
            JSONObject lNetworkString = new JSONObject(response);
            JSONArray jsonNetworks = lNetworkString.getJSONArray("networks");
            
            for (int i = 0; i< jsonNetworks.length(); i++) {
                
                JSONObject jsonNet = jsonNetworks.getJSONObject(i);
                NetworkInstance net = isPublicNetwork (jsonNet, adminUser.getTenantId());
                if (net != null)
                {
                    return net;
                }

            }

        } catch (OpenStackException e) {
            String errorMessage = "Error getting networks for obtaining the public network: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error getting networks from OpenStack for obtaining the public network: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return null;
    }
    
    /**
     * It gets the public router network
     */
    public RouterInstance getPublicRouter(PaasManagerUser user,  String region)
            throws OpenStackException {
        log.debug("Obtain public router for external netwrk ");
        
        String publicNetworkId = this.getPublicAdminNetwork(user, region).getIdNetwork();

        PaasManagerUser adminUser = openOperationUtil.getAdminUser(user);
        
        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_ROUTERS, APPLICATION_XML, region, adminUser.getToken(), adminUser.getTenantId());

        String response = null;

        try {
            response = openOperationUtil.executeNovaRequest(request);
            JSONObject lRouterString = new JSONObject(response);
            JSONArray jsonRouters = lRouterString.getJSONArray("routers");
            
            for (int i = 0; i< jsonRouters.length(); i++) {
                
                JSONObject jsonNet = jsonRouters.getJSONObject(i);
                RouterInstance router = isPublicRouter (jsonNet, adminUser.getTenantId(),publicNetworkId);
                if (router != null)
                {
                    return router;
                }

            }

        } catch (OpenStackException e) {
            String errorMessage = "Error getting router for obtaining the public router: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Error getting router from OpenStack for obtaining the public router: " + e;
            log.error(errorMessage);
            throw new OpenStackException(errorMessage);
        }

        return null;



    }
    
    private NetworkInstance isPublicNetwork(JSONObject jsonNet, String vdc)  {

        NetworkInstance netInst;
        try {
            netInst = NetworkInstance.fromJson(jsonNet);
        } catch (JSONException e) {
            log.warn("Error to parser the json for the network");
            return null;
        }

        if (!netInst.getExternal()) {
            return null;
        }
        
        if (!vdc.equals(netInst.getTenantId())) {
            return null;
        }
        return netInst;
    }
    
    private RouterInstance isPublicRouter(JSONObject jsonRouter, String vdc, String networkPublic)  {

        RouterInstance routerInst;
        try {
            routerInst = RouterInstance.fromJson(jsonRouter);
        } catch (JSONException e) {
            log.warn("Error to parser the json for the router");
            return null;
        }
        if (!vdc.equals(routerInst.getTenantId())) {
            return null;
        }

        if (!routerInst.getNetworkId().equals(networkPublic)) {
            return null;
        }

        return routerInst;
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

            HttpUriRequest request = openOperationUtil.createQuantumPutRequest(RESOURCE_ROUTERS + "/" + routerId + "/"
                    + RESOURCE_ADD_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);
            response = openOperationUtil.executeNovaRequest(request);

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
            HttpUriRequest request = openOperationUtil.createNovaPostRequest("/" + RESOURCE_FLOATINGIP, payload, APPLICATION_XML,
                    APPLICATION_JSON, region, token, vdc);

            response = openOperationUtil.executeNovaRequest(request);
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
            HttpUriRequest request = openOperationUtil.createNovaPostRequest(RESOURCE_SERVERS + "/" + serverId + "/" + RESOURCE_ACTION,
                    payload, APPLICATION_XML, APPLICATION_JSON, region, token, vdc);

            response = openOperationUtil.executeNovaRequest(request);
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

            HttpUriRequest request = openOperationUtil.createQuantumPostRequest(RESOURCE_NETWORKS, payload, APPLICATION_JSON, region,
                    token, vdc);
            response = openOperationUtil.executeNovaRequest(request);

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

            HttpUriRequest request = openOperationUtil.createQuantumPostRequest(RESOURCE_ROUTERS, payload, APPLICATION_JSON, region,
                    token, vdc);
            response = openOperationUtil.executeNovaRequest(request);

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
            request = openOperationUtil.createNovaPostRequest(RESOURCE_SERVERS, payload, APPLICATION_JSON, APPLICATION_XML, region,
                    token, vdc);
        } catch (OpenStackException ex) {
            throw new OpenStackException(ex.getMessage());
        }

        try {
            response = openOperationUtil.executeNovaRequest(request);
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

            HttpUriRequest request = openOperationUtil.createQuantumPostRequest(RESOURCE_SUBNETS, payload, APPLICATION_JSON, region,
                    token, vdc);
            response = openOperationUtil.executeNovaRequest(request);

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

        HttpUriRequest request = openOperationUtil.createQuantumDeleteRequest(RESOURCE_NETWORKS + "/" + networkId, region, vdc, token);

        String response = null;

        try {

            response = openOperationUtil.executeNovaRequest(request);

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
        HttpUriRequest request = openOperationUtil.createQuantumDeleteRequest(RESOURCE_ROUTERS + "/" + routerId, region, token, vdc);

        String response = null;

        try {

            response = openOperationUtil.executeNovaRequest(request);

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
            HttpUriRequest request = openOperationUtil.createNovaDeleteRequest(RESOURCE_SERVERS + "/" + serverId, region, token, vdc);

            response = openOperationUtil.executeNovaRequest(request);

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
            HttpUriRequest request = openOperationUtil.createQuantumDeleteRequest(RESOURCE_SUBNETS + "/" + idSubNet, region, vdc, token);

            response = openOperationUtil.executeNovaRequest(request);

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

    public String getFloatingIP(PaasManagerUser user, String region) throws OpenStackException {
        String floatingIP = null;
        
        String getFloatingIPsResponse = getFloatingIPs(region, user.getToken(), user.getTenantId());
        String floatingIpPool = this.getPublicAdminNetwork(user, region).getNetworkName();

        if (isAnyFloatingIPFreeToBeAssigned(getFloatingIPsResponse)) {
            floatingIP = getFloatingIPFree(getFloatingIPsResponse);
        } else {
            floatingIP = allocateFloatingIP(
                    buildAllocateFloatingIPPayload(floatingIpPool),
                    region, user.getToken(), user.getTenantId());
            getFloatingIPsResponse = getFloatingIPs(region, user.getToken(), user.getTenantId());
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
            HttpUriRequest request = openOperationUtil.createNovaGetRequest("/" + RESOURCE_FLOATINGIP, APPLICATION_XML, region, token,
                    vdc);

            response = openOperationUtil.executeNovaRequest(request);

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
        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_NETWORKS + "/" + networkId, APPLICATION_XML, region,
                token, vdc);

        String response = null;

        try {

            response = openOperationUtil.executeNovaRequest(request);

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
        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_SUBNETS + "/" + subNetworkId, APPLICATION_XML,
                region, token, vdc);

        String response;

        try {

            response = openOperationUtil.executeNovaRequest(request);

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
        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_XML, region, token, vdc);

        String response = null;

        try {

            response = openOperationUtil.executeNovaRequest(request);

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

        HttpUriRequest request = openOperationUtil.createNovaGetRequest(RESOURCE_SERVERS + "/" + serverId, APPLICATION_XML, region,
                token, vdc);

        String response = null;
        // TaskResult server = new TaskResult();

        try {
            response = openOperationUtil.executeNovaRequest(request);
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

        HttpUriRequest request = openOperationUtil.createNovaGetRequest(RESOURCE_SERVERS, APPLICATION_JSON, region, token, vdc);

        String response = null;
        // TaskResult server = new TaskResult();

        try {
            response = openOperationUtil.executeNovaRequest(request);
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

            HttpUriRequest request = openOperationUtil.createQuantumPutRequest(RESOURCE_ROUTERS + "/" + router.getIdRouter() + "/"
                    + RESOURCE_REMOVE_INTERFACE, payload, APPLICATION_JSON, region, token, vdc);
            response = openOperationUtil.executeNovaRequest(request);

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
        PaasManagerUser user2 = openOperationUtil.getAdminUser(user);

        log.debug("tenantid " + user.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = openOperationUtil.createNovaGetRequest("limits", APPLICATION_JSON, region, user.getToken(),
                user.getTenantId());

        String response = openOperationUtil.executeNovaRequest(request);

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

        PaasManagerUser user2 = openOperationUtil.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_NETWORKS, APPLICATION_JSON, region, user2.getToken(),
                user2.getUserName());

        String response = null;

        try {
            response = openOperationUtil.executeNovaRequest(request);
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
        PaasManagerUser user2 = openOperationUtil.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        HttpUriRequest request = openOperationUtil.createQuantumGetRequest(RESOURCE_PORTS, APPLICATION_JSON, region, user2.getToken(),
                user2.getUserName());

        String response = null;

        try {
            response = openOperationUtil.executeNovaRequest(request);
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
        String idRouter = this.getPublicRouter(user, region).getIdRouter();
        PaasManagerUser user2 = openOperationUtil.getAdminUser(user);

        log.debug("tenantid " + user2.getTenantId());
        log.debug("token " + user2.getToken());
        log.debug("user name " + user2.getUserName());

        log.debug("Deleting an interface from network " + net.getNetworkName() + " to router " + idRouter);
        String response = null;

        try {
            String payload = net.toAddInterfaceJson();
            log.debug(payload);

            HttpUriRequest request = openOperationUtil.createQuantumPutRequest(RESOURCE_ROUTERS + "/" + idRouter + "/"
                    + RESOURCE_REMOVE_INTERFACE, payload, APPLICATION_JSON, region, user2.getToken(),
                    user2.getTenantId());
            response = openOperationUtil.executeNovaRequest(request);

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
    
    public void setOpenOperationUtil(OpenOperationUtil openOperationUtil) {
        this.openOperationUtil = openOperationUtil;
    }

   
}
