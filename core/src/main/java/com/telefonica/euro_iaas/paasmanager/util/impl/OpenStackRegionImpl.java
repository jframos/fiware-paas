/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util.impl;

import java.util.Iterator;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

    /**
     * the properties configuration.
     */
    private SystemPropertiesProvider systemPropertiesProvider;

    @Override
    public String getEndPointByNameAndRegionName(String name, String regionName, String token)
            throws OpenStackException {

        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_KEYSTONE_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) + "/tokens/" + token
                + "/endpoints";

        WebResource webResource = client.resource(url);

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String result = parseEndpoint(response.getEntity(String.class), name, regionName);
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
        return getEndPointByNameAndRegionName("quantum", regionName, token);
    }

    private String parseEndpoint(String response, String name, String regionName) {

        JSONObject jsonObject = JSONObject.fromObject(response);

        JSONArray endpointsArray = jsonObject.getJSONArray("endpoints");

        boolean notFound = true;
        Iterator it = endpointsArray.iterator();
        JSONObject endpointJson = null;
        while (notFound && it.hasNext()) {

            endpointJson = JSONObject.fromObject(it.next());
            String name1 = endpointJson.get("name").toString();
            String regionName1 = endpointJson.get("region").toString();

            if (name.equals(name1) && regionName.equals(regionName1)) {
                notFound = false;
            }
        }
        if (!notFound) {
            String url = endpointJson.get("publicURL").toString();
            return url;
        }

        return null;
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
