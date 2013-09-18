package com.telefonica.euro_iaas.paasmanager.client.services.impl;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.paasmanager.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.Task;

public class EnvironmentInstanceServiceImpl extends AbstractBaseService
		implements EnvironmentInstanceService {

	public EnvironmentInstanceServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
	}
	
	
	@Override
	public Task create(String vdc, 
			EnvironmentDto environmentDto, String callback) {
        
        String url = getBaseHost() + MessageFormat.format(
                ClientConstants.BASE_ENVIRONMENT_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(environmentDto);
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