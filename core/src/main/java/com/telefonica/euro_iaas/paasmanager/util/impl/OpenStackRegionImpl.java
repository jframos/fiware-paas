/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackRegion;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * This class implements OpenStackRegion interface.<br>
 * {@inheritDoc}
 */
public class OpenStackRegionImpl implements OpenStackRegion {

    private Client client;

    private static Logger log = Logger.getLogger(OpenStackRegionImpl.class);

    /**
     * Default constructor. It creates a jersey client.
     */
    public OpenStackRegionImpl() {
        client = Client.create();
    }

    /**
     * the properties configuration.
     */
    private SystemPropertiesProvider systemPropertiesProvider;

    @Override
    public String getEndPointByNameAndRegionName(String name, String regionName, String token)
            throws OpenStackException {

        String responseJSON = callToKeystone(token);

        String result = parseEndpoint(responseJSON, name, regionName);
        if (result == null) {
            throw new OpenStackException("region not found");
        }
        return result;
    }

    @Override
    public String getNovaEndPoint(String regionName, String token) throws OpenStackException {
        String url = getEndPointByNameAndRegionName("nova", regionName, token);

        Integer index = url.lastIndexOf("/");
        url = url.substring(0, index + 1);

        return url;

    }

    @Override
    public String getQuantumEndPoint(String regionName, String token) throws OpenStackException {
        String url = getEndPointByNameAndRegionName("quantum", regionName, token);
        Integer index = url.lastIndexOf("/v");
        if (index == -1) {
            url = url + "v2.0/";
        }
        return url;
    }

    @Override
    public List<String> getRegionNames(String token) throws OpenStackException {

        String responseJSON = callToKeystone(token);
        return parseRegionName(responseJSON, "nova");

    }

    private String callToKeystone(String token) throws OpenStackException {
        ClientResponse response = getJSONWithEndpoints(token);
        return response.getEntity(String.class);

    }

    private ClientResponse getJSONWithEndpoints(String token) throws OpenStackException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL) + "tokens/" + token
                + "/endpoints";

        WebResource webResource = client.resource(url);

        WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
        builder.header("X-Auth-Token", token);

        ClientResponse response = builder.get(ClientResponse.class);

        int code = response.getStatus();

        if (code != 200) {
            String message = "Failed : HTTP (url:" + url + ") error code : " + code + "body: "
                    + response.getEntity(String.class);

            if (code == 501) {
                log.warn(message);
                response = getEndPointsThroughTokenRequest();
            } else {
                log.error(message);
                throw new OpenStackException(message);
            }

        }
        return response;
    }

    private String parseEndpoint(String response, String name, String regionName) {

        JSONObject jsonObject = JSONObject.fromObject(response);

        String url = null;
        Map<String, String> urlMap = new HashMap<String, String>();
        if (jsonObject.containsKey("endpoints")) {

            JSONArray endpointsArray = jsonObject.getJSONArray("endpoints");

            boolean notFound = true;
            Iterator it = endpointsArray.iterator();
            JSONObject endpointJson = null;
            while (notFound && it.hasNext()) {

                endpointJson = JSONObject.fromObject(it.next());
                String name1 = endpointJson.get("name").toString();
                String regionName1 = endpointJson.get("region").toString();

                if (name.equals(name1)) {
                    url = endpointJson.get("publicURL").toString();
                    urlMap.put(regionName1, url);
                    if (regionName.equals(regionName1)) {
                        notFound = false;
                    }
                }
            }
            if (!notFound) {
                return url;
            }
            // return default regionName

        } else {
            if (jsonObject.containsKey("access")) {
                JSONArray servicesArray = jsonObject.getJSONObject("access").getJSONArray("serviceCatalog");

                boolean notFound = true;
                Iterator it = servicesArray.iterator();
                JSONObject serviceJSON;
                while (notFound && it.hasNext()) {

                    serviceJSON = JSONObject.fromObject(it.next());
                    String name1 = serviceJSON.get("name").toString();

                    if (name.equals(name1)) {
                        JSONArray endpointsArray = serviceJSON.getJSONArray("endpoints");
                        Iterator it2 = endpointsArray.iterator();

                        while (notFound && it2.hasNext()) {
                            JSONObject endpointJson = JSONObject.fromObject(it2.next());

                            String regionName1 = endpointJson.get("region").toString();
                            url = endpointJson.get("publicURL").toString();
                            if (regionName.equals(regionName1)) {
                                notFound = false;
                            }
                            urlMap.put(regionName1, url);

                        }

                    }
                }
                if (!notFound) {
                    return url;
                }

            }

        }
        return urlMap.get(systemPropertiesProvider.getProperty(SystemPropertiesProvider.DEFAULT_REGION_NAME));
    }

    /**
     * Parse region name, with compatibility with essex,grizzly.
     * 
     * @param response
     * @param name
     * @return
     */
    private List<String> parseRegionName(String response, String name) {

        List<String> names = new ArrayList<String>(2);

        JSONObject jsonObject = JSONObject.fromObject(response);

        if (jsonObject.containsKey("endpoints")) {

            JSONArray endpointsArray = jsonObject.getJSONArray("endpoints");

            Iterator it = endpointsArray.iterator();
            JSONObject endpointJson;
            while (it.hasNext()) {

                endpointJson = JSONObject.fromObject(it.next());
                String name1 = endpointJson.get("name").toString();
                String regionName1 = endpointJson.get("region").toString();

                if (name.equals(name1)) {
                    if (!names.contains(regionName1)) {
                        names.add(regionName1);
                    }
                }
            }
        } else {
            if (jsonObject.containsKey("access")) {

                JSONArray servicesArray = jsonObject.getJSONObject("access").getJSONArray("serviceCatalog");

                boolean notFound = true;
                Iterator it = servicesArray.iterator();
                JSONObject serviceJSON;
                while (notFound && it.hasNext()) {

                    serviceJSON = JSONObject.fromObject(it.next());
                    String name1 = serviceJSON.get("name").toString();

                    if (name.equals(name1)) {
                        JSONArray endpointsArray = serviceJSON.getJSONArray("endpoints");

                        Iterator it2 = endpointsArray.iterator();
                        while (it2.hasNext()) {
                            JSONObject endpointJson = JSONObject.fromObject(it2.next());

                            String regionName1 = endpointJson.get("region").toString();
                            if (!names.contains(regionName1)) {
                                names.add(regionName1);
                            }

                        }
                        notFound = false;

                    }
                }
            }
        }
        return names;
    }

    public ClientResponse getEndPointsThroughTokenRequest() throws OpenStackException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL) + "tokens";
        String tenantId = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);
        log.debug("actionUri: " + url);

        ClientResponse response;

        WebResource wr = client.resource(url);

        String payload = "{\"auth\":{\"passwordCredentials\":{\"username\": \"admin\", \"password\": \"openstack\"}, \"tenantName\": "
                + "\"" + tenantId + "\"}}";

        WebResource.Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                .entity(payload);
        builder = builder.header("Content-type", MediaType.APPLICATION_JSON);

        response = builder.post(ClientResponse.class);

        int httpCode = response.getStatus();
        if (httpCode != 200) {
            String message = "Error calling OpenStack to an valid token. " + "Status " + httpCode;
            log.warn(message);
            throw new OpenStackException(message);
        }

        return response;

    }

    public SystemPropertiesProvider getSystemPropertiesProvider() {
        return systemPropertiesProvider;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
