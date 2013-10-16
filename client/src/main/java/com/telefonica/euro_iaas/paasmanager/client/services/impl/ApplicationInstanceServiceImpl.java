/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.client.services.impl;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.paasmanager.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.client.services.ApplicationInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;

/**
 * @author jesus.movilla
 */
public class ApplicationInstanceServiceImpl extends AbstractBaseService implements ApplicationInstanceService {

    public ApplicationInstanceServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    /**
     * Client to install an application Release on a Environment Instance
     * 
     * @param vdc
     * @param applicationInstanceDto
     * @param callback
     * @param Task
     */
    public Task install(String vdc, ApplicationInstanceDto applicationInstanceDto, String callback) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_APPLICATION_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(applicationInstanceDto);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    protected Builder addCallback(Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }

}
