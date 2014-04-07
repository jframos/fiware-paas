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


import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

/**
 * @author jesus.movilla
 */
public class OpenStackConfigUtilImpl implements OpenStackConfigUtil {

    /**
     * The log.
     */

    private static Logger log = Logger.getLogger(OpenStackConfigUtilImpl.class);

    private HttpClientConnectionManager connectionManager;

    private OpenStackRegion openStackRegion;
    
    private OpenOperationUtil openOperationUtil;

    /**
     * The constructor.
     */
    public OpenStackConfigUtilImpl() {
        connectionManager = new PoolingHttpClientConnectionManager();
    }

    public HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

 
    
    /**
     * It gets the public admin network
     */
    public String getPublicAdminNetwork(PaasManagerUser user,  String region)
            throws OpenStackException {
        log.debug("Obtain public admin network");
        RegionCache regionCache = new RegionCache();
        String networkId = regionCache.getUrl("network", "net");
        if (networkId != null) {
        	return networkId;
        }

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
                	regionCache.putUrl("network", "net", net.getIdNetwork());
                	return net.getIdNetwork();
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
    
    public String getPublicFloatingPool(PaasManagerUser user,  String region)
    	throws OpenStackException {
        log.debug("Obtain public admin network");
        RegionCache regionCache = new RegionCache();
        String networkId = regionCache.getUrl("floating", "net");
        if (networkId != null) {
        	return networkId;
        }

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
	    			regionCache.putUrl("floating", "net", net.getNetworkName());
	    			return net.getNetworkName();
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
    public String getPublicRouter(PaasManagerUser user,  String region, String publicNetworkId)
            throws OpenStackException {
        log.debug("Obtain public router for external netwrk ");
        RegionCache regionCache = new RegionCache();
        String routerId = regionCache.getUrl("router", "net");
        if (routerId != null) {
        	return routerId;
        }

     //   String publicNetworkId = this.getPublicAdminNetwork(user, region).getIdNetwork();

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
                	regionCache.putUrl("router", "net", router.getIdRouter());
                	return router.getIdRouter();
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
