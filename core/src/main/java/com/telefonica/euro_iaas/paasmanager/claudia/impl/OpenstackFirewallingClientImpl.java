/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * @author henar mu�oz
 */
public class OpenstackFirewallingClientImpl implements FirewallingClient {

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(OpenstackFirewallingClientImpl.class);

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient#loadAllSecurityGroups(com.telefonica.euro_iaas
     * .paasmanager.model.ClaudiaData)
     */
    public List<SecurityGroup> loadAllSecurityGroups(ClaudiaData claudiaData) throws OpenStackException {

        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)
                + systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY)
                + claudiaData.getVdc() + "/os-security-groups";
        log.debug("actionUri: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);
        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

        Map<String, String> header = getHeaders(claudiaData.getUser());
        for (String key : header.keySet()) {
            builder = builder.header(key, header.get(key));
        }
        response = builder.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            String message = "Error calling OpenStack to recover all secGroups. " + "Status " + response.getStatus();
            throw new OpenStackException(message);
        }
        String stringAllSecurityGroup = response.getEntity(String.class);
        log.debug("Status " + response.getStatus());

        JSONObject jsonNode = JSONObject.fromObject(stringAllSecurityGroup);
        List<SecurityGroup> securityGroups = fromStringToSecGroups(jsonNode);

        return securityGroups;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient#load(java.lang.String)
     */
    public SecurityGroup loadSecurityGroup(ClaudiaData claudiaData, String securityGroupId)
            throws EntityNotFoundException {

        log.debug("Loading security group " + securityGroupId);
        String url = systemPropertiesProvider.getProperty("openstack.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + claudiaData.getVdc()
                + "/os-security-groups/" + securityGroupId;
        log.debug("actionUri: " + url);

        try {

            Client client = new Client();
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(claudiaData.getUser());
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
            /*
             * if (response.getStatus() == 200 || response.getStatus() == 201 || response.getStatus() == 204) {
             * log.debug("Operation ok result " + result); System.out.println("Result " + result); String id =
             * result.substring(result.indexOf(", \"id\":") + ", \"id\":".length() + 1, result
             * .indexOf(", \"name\": ")); log.debug("Operation ok id " + id); return id; } else {
             * log.error("Error to create a security group " + result); throw new InfrastructureException(
             * "Error to create a security group " + result); }
             */
            SecurityGroup secGroup = new SecurityGroup();
            secGroup.fromJson(jsonSecGroupRaw);
            return secGroup;

        } catch (Exception e) {
            String errorMessage = "Error loading SecurityGroup : " + securityGroupId + "  " + e.getMessage();
            log.error(errorMessage);
            throw new EntityNotFoundException(SecurityGroup.class, securityGroupId, errorMessage);
        }
    }

    public String deploySecurityGroup(ClaudiaData claudiaData, SecurityGroup securityGroup)
            throws InfrastructureException {

        String id = createSecurityGroup(claudiaData, securityGroup);
        return id;

    }

    public String deployRule(ClaudiaData claudiaData, Rule rule) throws InfrastructureException {
        return createRule(claudiaData, rule);

    }

    public String createSecurityGroup(ClaudiaData claudiaData, SecurityGroup securityGroup)
            throws InfrastructureException {

        log.debug("Creating security group " + securityGroup.getName());
        String url = systemPropertiesProvider.getProperty("openstack.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + claudiaData.getVdc()
                + "/os-security-groups";
        log.debug("actionUri: " + url);

        String payload = securityGroup.toJSON();
        log.debug(payload);

        try {

            Client client = new Client();
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);

            Map<String, String> header = getHeaders(claudiaData.getUser());
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }
            response = builder.post(ClientResponse.class);

            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());

            if (response.getStatus() == 200 || response.getStatus() == 201 || response.getStatus() == 204) {

                log.debug("Operation ok result " + result);
                System.out.println("Result " + result);
                org.json.JSONObject network = new org.json.JSONObject(result);
                String id;

                if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_OPENSTACK_DISTRIBUTION).equals(
                        "essex")) {
                    id = network.getJSONObject("security_group").getString("id");
                } else {
                    id = "" + network.getJSONObject("security_group").getInt("id");
                }
                // String id = result.substring(result.indexOf(", \"id\":")
                // + ", \"id\":".length() + 1, result
                // .indexOf(", \"name\": "));
                log.debug("Operation ok id " + id);

                // if (id.startsWith("\"") && id.endsWith("\"")) {
                // id = id.substring(1, id.length()-1);
                // }

                return id;
            } else {
                log.error("Error to create a security group " + result);
                throw new InfrastructureException("Error to create a security group " + result);
            }

        } catch (Exception e) {

            String errorMessage = "Error performing post on the resource: " + url + " with payload: " + payload + " "
                    + e.getMessage();
            log.error(errorMessage);

            throw new InfrastructureException(errorMessage);
        }

    }

    public String createRule(ClaudiaData claudiaData, Rule rule) throws InfrastructureException {

        log.debug("Creating security rule " + rule.getFromPort());
        String url = systemPropertiesProvider.getProperty("openstack.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + claudiaData.getVdc()
                + "/os-security-group-rules";
        log.debug("actionUri: " + url);

        String payload = rule.toJSON();
        log.debug(payload);

        try {

            Client client = new Client();

            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);

            Map<String, String> header = getHeaders(claudiaData.getUser());
            for (String key : header.keySet()) {
                builder = builder.header(key, header.get(key));
            }

            response = builder.post(ClientResponse.class);

            String result = response.getEntity(String.class);

            log.debug("Status " + response.getStatus());
            if (response.getStatus() == 200 || response.getStatus() == 201 || response.getStatus() == 204) {

                log.debug("Operation ok result " + result);

                // String id = result.substring(result.indexOf(", \"id\":")
                // + ", \"id\":".length() + 1, result.indexOf("}}"));

                org.json.JSONObject network = new org.json.JSONObject(result);
                String id;

                if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_OPENSTACK_DISTRIBUTION).equals(
                        "essex")) {
                    id = network.getJSONObject("security_group_rule").getString("id");
                } else {
                    id = "" + network.getJSONObject("security_group_rule").getInt("id");
                }
                log.debug("Operation ok result " + id);

                // if (id.startsWith("\"") && id.endsWith("\"")) {
                // id = id.substring(1, id.length()-1);
                // }

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
            e.printStackTrace();

            throw new InfrastructureException(errorMessage);
        }

    }

    public void destroySecurityGroup(ClaudiaData claudiaData, SecurityGroup securityGroup)
            throws InfrastructureException {
        log.debug("Destroy security group " + securityGroup.getName());
        String url = systemPropertiesProvider.getProperty("openstack.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + claudiaData.getVdc()
                + "/os-security-groups/" + securityGroup.getIdSecurityGroup();
        log.debug("actionUri: " + url);

        try {

            Client client = new Client();

            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(claudiaData.getUser());
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
                throw new InfrastructureException("Error to delete a security group " + securityGroup + " : " + result);
            }

        } catch (Exception e) {
            String errorMessage = "Error performing delete on the resource: " + url;
            e.printStackTrace();

            throw new InfrastructureException(errorMessage);
        }

    }

    public void destroyRule(ClaudiaData claudiaData, Rule rule) throws InfrastructureException {
        log.debug("Destroy security rule " + rule.getFromPort());
        String url = systemPropertiesProvider.getProperty("openstack.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + claudiaData.getVdc()
                + "/os-security-group-rules/" + rule.getIdRule();
        log.debug("actionUri: " + url);

        try {

            Client client = new Client();
            log.debug("url: " + url);
            ClientResponse response = null;

            WebResource wr = client.resource(url);
            Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            Map<String, String> header = getHeaders(claudiaData.getUser());
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
                throw new InfrastructureException("Error to delete a security rule " + rule.getIdRule() + ": " + result);
            }

        } catch (Exception e) {
            String errorMessage = "Error performing delete on the resource: " + url;
            e.printStackTrace();

            throw new InfrastructureException(errorMessage);
        }

    }

    private Map<String, String> getHeaders(PaasManagerUser claudiaData) {

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("X-Auth-Token", claudiaData.getToken());
        headers.put("X-Auth-Project-Id", claudiaData.getTenantId());
        return headers;

    }

    /**
     * Converting from a string (list of secGrous in json) to a list of SecurityGroups
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

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
