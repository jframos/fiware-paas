/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.client.services.impl;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.paasmanager.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;

public class EnvironmentInstanceServiceImpl extends AbstractBaseService implements EnvironmentInstanceService {

    public EnvironmentInstanceServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    public Task create(String org, String vdc, EnvironmentDto environmentDto, String callback) {

        String url = getBaseHost() + "/envInst"
                + MessageFormat.format(ClientConstants.BASE_ENVIRONMENT_INSTANCE_PATH, org, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(environmentDto);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService#create(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public Task create(String org, String vdc, String payload, String callback) {
        String url = getBaseHost() + "/ovf"
                + MessageFormat.format(ClientConstants.BASE_ENVIRONMENT_INSTANCE_PATH, org, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(payload);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    public EnvironmentInstanceDto load(String org, String vdc, String name) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.ENVIRONMENT_INSTANCE_PATH, org, vdc, name);
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).get(EnvironmentInstanceDto.class);
    }

    protected Builder addCallback(Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }
}
