/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.keystone.impl;

import java.sql.Connection;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.TokenDao;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.keystone.Token;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import org.apache.log4j.Logger;

/**
 * @author jesus.movilla
 */
public class TokenDaoIdmImpl implements TokenDao {

    private static Logger log = Logger.getLogger(TokenDaoIdmImpl.class);
    private SystemPropertiesProvider systemPropertiesProvider;

    /*
     * @PersistenceContext(unitName = "keystone") private EntityManager entityManagerFactoryKeystone;
     */

    /**
     * Find Last Valid Token from user
     * 
     * @param name
     * @return
     * @throws EntityNotFoundException
     * @throws OpenStackException
     */
    public Token findLastTokenFromUser(Connection connection, String tenantId) throws EntityNotFoundException,
            OpenStackException {

        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL) + "tokens";
        log.debug("actionUri: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);

        String payload = "{\"auth\":{\"passwordCredentials\":{\"username\": \"admin\", \"password\": \"openstack\"}, \"tenantName\": "
                + "\"" + tenantId + "\"}}";

        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);
        builder = builder.header("Content-type", "application/json");
        builder = builder.header("Accept", "application/xml");

        response = builder.post(ClientResponse.class);

        if (response.getStatus() != 200) {
            String message = "Error calling OpenStack to an valid token. " + "Status " + response.getStatus();
            throw new OpenStackException(message);
        }
        String resp = response.getEntity(String.class);
        System.out.print(resp);
        Token token = extractToken(resp);
        token.setTenantId(tenantId);
        return token;
    }

    private Token extractToken(String payload) {
        int i = payload.indexOf("id");
        int j = payload.indexOf(",\"tenant\"", i);

        String tokenstring = payload.substring(i - 1, j);
        log.info(tokenstring);

        tokenstring = tokenstring.replaceAll("\"", "");
        tokenstring = tokenstring.substring("id:".length());
        log.info(tokenstring);

        log.debug("Token  " + tokenstring);

        Token token = new Token();
        token.setId(tokenstring);
        token.setExpires("");
        token.setExtra("");
        return token;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
