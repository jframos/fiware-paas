/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackRegion;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author henar munoz
 */
public class OpenstackFirewallingClientImpl implements FirewallingClient {

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(OpenstackFirewallingClientImpl.class);

    private OpenStackRegion openStackRegion;

    /**
     * Deploy a rule in the security group.
     * 
     * @param region
     * @param token
     * @param vdc
     * @param rule
     */
    public String deployRule(String region, String token, String vdc, Rule rule) throws InfrastructureException {
        log.debug("Creating security rule " + rule.getFromPort());
        String novaUrl = getNovaEndPoint(region, token);
        String url = novaUrl + vdc + "/os-security-group-rules";
        log.debug("actionUri: " + url);

        String payload = rule.toJSON();
        log.debug(payload);

        try {

            Client client = new Client();

            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);

            Map<String, String> header = getHeaders(token, vdc);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }

            response = builder.post(ClientResponse.class);

            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());
            if (response.getStatus() == 200 || response.getStatus() == 201 || response.getStatus() == 204) {

                log.debug("Operation ok result " + result);
                org.json.JSONObject network = new org.json.JSONObject(result);
                String id = network.getJSONObject("security_group_rule").getString("id");

                log.debug("Operation ok result " + id);
                rule.setIdRule(id);
                return id;

            } else {
                log.error("Error to create a security rule " + result);
                throw new InfrastructureException("Error to create a security rule " + result);
            }

        } catch (Exception e) {
            String errorMessage = "Error performing post on the resource: " + url + " with payload: " + payload + " "
                    + e.getMessage();
            log.error(errorMessage);

            throw new InfrastructureException(errorMessage);
        }
    }

    private String getNovaEndPoint(String region, String token) throws InfrastructureException {
        String novaUrl;
        try {
            novaUrl = openStackRegion.getNovaEndPoint(region, token);
        } catch (OpenStackException e) {
            log.warn("Can not obtain nova url " + e);
            throw new InfrastructureException("Can not obtain nova url " + e);
        }
        return novaUrl;
    }

    /**
     * Creating the security group in OpenStack.
     */
    public String deploySecurityGroup(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InfrastructureException {

        log.debug("Creating security group " + securityGroup.getName());
        String url = getNovaEndPoint(region, token) + vdc + "/os-security-groups";
        log.debug("actionUri: " + url);

        String payload = securityGroup.toJSON();
        log.debug(payload);

        try {

            Client client = new Client();
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);

            Map<String, String> header = getHeaders(token, vdc);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }
            response = builder.post(ClientResponse.class);

            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());

            if (response.getStatus() == 200 || response.getStatus() == 201 || response.getStatus() == 204) {

                log.debug("Operation ok result " + result);
                org.json.JSONObject network = new org.json.JSONObject(result);
                String id;

                id = network.getJSONObject("security_group").getString("id");
                log.debug("Operation ok id " + id);

                return id;
            } else {
                log.error("Error to create a security group " + result);
                throw new InfrastructureException(result);
            }

        } catch (Exception e) {

            String errorMessage = "Error performing post on the resource: " + url + " with payload: " + payload + " "
                    + e.getMessage();
            log.error(errorMessage);

            throw new InfrastructureException(errorMessage);
        }

    }

    /**
     * Destroy the rule in the security group.
     */
    public void destroyRule(String region, String token, String vdc, Rule rule) throws InfrastructureException {

        log.debug("Destroy security rule " + rule.getFromPort());
        String url = getNovaEndPoint(region, token) + vdc + "/os-security-group-rules/" + rule.getIdRule();
        log.debug("actionUri: " + url);

        try {

            Client client = new Client();
            log.debug("url: " + url);
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(token, vdc);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }

            response = builder.delete(ClientResponse.class);
            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());

            if (response.getStatus() == 200 || response.getStatus() == 202 || response.getStatus() == 201
                    || response.getStatus() == 204) {
                log.debug("Operation ok result " + result);
            } else {
                log.error("Error to delete a security rule " + rule.getIdRule() + ": " + result);
                throw new InfrastructureException(result);
            }

        } catch (Exception e) {
            String errorMessage = "Error performing delete on the resource: " + url + " " + e.getMessage();

            throw new InfrastructureException(errorMessage);
        }

    }

    /**
     * It destroys a security group.
     */
    public void destroySecurityGroup(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InfrastructureException {

        log.debug("Destroy security group " + securityGroup.getName());
        String url = getNovaEndPoint(region, token) + vdc + "/os-security-groups/" + securityGroup.getIdSecurityGroup();
        log.debug("actionUri: " + url);

        try {

            Client client = new Client();

            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(token, vdc);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }

            response = builder.delete(ClientResponse.class);

            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());

            if (response.getStatus() == 200 || response.getStatus() == 202 || response.getStatus() == 201
                    || response.getStatus() == 204) {

                log.debug("Operation ok result " + result);

            } else {
                log.error("Error to delete a security group " + securityGroup + " : " + result);
                throw new InfrastructureException(result);
            }

        } catch (Exception e) {
            String errorMessage = "Error performing delete on the resource: " + url + " " + e;
            e.printStackTrace();

            throw new InfrastructureException(errorMessage);
        }

    }

    /**
     * Converting from a string (list of secGrous in json) to a list of SecurityGroups.
     * 
     * @param jsonSecGroups
     * @return
     */
    private List<SecurityGroup> fromStringToSecGroups(JSONObject jsonSecGroups) {
        List<SecurityGroup> secGroups = new ArrayList<SecurityGroup>();
        JSONArray jsonSecGroupsList = jsonSecGroups.getJSONArray("security_groups");

        for (Object o : jsonSecGroupsList) {
            SecurityGroup secGroup = new SecurityGroup();
            JSONObject jsonSecGroup = (JSONObject) o;
            secGroup.fromJson(jsonSecGroup);
            secGroups.add(secGroup);
        }
        return secGroups;
    }

    private Map<String, String> getHeaders(String token, String vdc) {

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("X-Auth-Token", token);
        headers.put("X-Auth-Project-Id", vdc);
        return headers;

    }

    /**
     * It loads all the security groups.
     */
    public List<SecurityGroup> loadAllSecurityGroups(String region, String token, String vdc)
            throws InfrastructureException {

        String url = null;
        url = getNovaEndPoint(region, token) + vdc + "/os-security-groups";
        log.debug("actionUri: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);
        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

        Map<String, String> header = getHeaders(token, vdc);
        for (String key : header.keySet()) {
            builder = builder.header(key, header.get(key));
        }
        response = builder.get(ClientResponse.class);
        String responseEntity = response.getEntity(String.class);

        if (response.getStatus() != 200) {
            String message = "Error calling OpenStack to recover all secGroups. " + "Status " + response.getStatus()
                    + " " + responseEntity;
            throw new InfrastructureException(responseEntity);
        }
        log.debug("Status " + response.getStatus());

        JSONObject jsonNode = JSONObject.fromObject(responseEntity);
        List<SecurityGroup> securityGroups = fromStringToSecGroups(jsonNode);

        return securityGroups;

    }

    /**
     * Load a security group.
     */

    public SecurityGroup loadSecurityGroup(String region, String token, String vdc, String securityGroupId)
            throws EntityNotFoundException, InfrastructureException {

        log.debug("Loading security group " + securityGroupId);
        String url = null;
        url = getNovaEndPoint(region, token) + vdc + "/os-security-groups/" + securityGroupId;

        log.debug("actionUri: " + url);

        try {

            Client client = new Client();
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(token, vdc);
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }
            response = builder.get(ClientResponse.class);
            String stringSecurityGroup = response.getEntity(String.class);
            JSONObject jsonsecurityGroup = JSONObject.fromObject(stringSecurityGroup);

            String jsonSecGroup = jsonsecurityGroup.getString("security_group");
            JSONObject jsonSecGroupRaw = JSONObject.fromObject(jsonSecGroup);

            log.debug("Status " + response.getStatus());

            if (response.getStatus() == 404) {
                String errorMessage = "Error loading SecurityGroup : " + securityGroupId;
                log.info(errorMessage);
                throw new EntityNotFoundException(SecurityGroup.class, securityGroupId, errorMessage);
            }

            SecurityGroup secGroup = new SecurityGroup();
            secGroup.fromJson(jsonSecGroupRaw);
            return secGroup;

        } catch (Exception e) {
            String errorMessage = "Error loading SecurityGroup : " + securityGroupId + "  " + e.getMessage();
            log.error(errorMessage);
            throw new EntityNotFoundException(SecurityGroup.class, securityGroupId, errorMessage);
        }
    }

    /**
     * @param systemPropertiesProvider
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public OpenStackRegion getOpenStackRegion() {
        return openStackRegion;
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }
}
