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

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.paasmanager.claudia.KeystoneClient;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author henar mu�oz
 */
public class KeystoneImpl implements KeystoneClient {

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(KeystoneImpl.class);

    public String obtainToken(String tenantId) throws OpenStackException {
        String url = systemPropertiesProvider.getProperty("keystone.nova.url")
                + systemPropertiesProvider.getProperty("openstack.version") + "/tokens";
        log.debug("actionUri: " + url);

        Client client = new Client();
        ClientResponse response = null;

        WebResource wr = client.resource(url);

        String payload = "{\"auth\":{\"passwordCredentials\":{\"username\": \"admin\", \"password\": \"openstack\"}, \"tenantName\": "
                + "\"" + tenantId + "\"}}";

        Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).entity(payload);
        builder = builder.header("Content-type", "application/json");

        response = builder.post(ClientResponse.class);

        if (response.getStatus() != 200) {
            String message = "Error calling OpenStack to an valid token. " + "Status " + response.getStatus();
            throw new OpenStackException(message);
        }
        int i = payload.indexOf("token");
        int j = payload.indexOf(">", i);
        String respone = response.getEntity(String.class);
        String token = respone.substring(i - 1, j + 1);

        // token = "<token expires=\"2012-11-13T15:01:51Z\" id=\"783bec9d7d734f1e943986485a90966d\">";
        // Regular Expression <\s*token\s*(issued_at=\".*?\"\s*)?expires=\"(.*?)(\"\s*id=\")(.*)\"\/*>
        // as a Java string "<\\s*token\\s*(issued_at=\\\".*?\\\"\\s*)?expires=\\\"(.*?)(\\\"\\s*id=\\\")(.*)\\\"\\/*>"
        String pattern1 = "<\\s*token\\s*(issued_at=\\\".*?\\\"\\s*)?expires=\\\"(.*?)(\\\"\\s*id=\\\")(.*)\\\"\\/*>";

        if (token.matches(pattern1)) {

            token = token.replaceAll(pattern1, "$4");
        }

        log.debug("Token  " + token);
        return token;

    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
