package com.telefonica.euro_iaas.paasmanager.it.util;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.text.MessageFormat;
import java.util.List;

import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.client.ClientConstants;
import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;



public class EnvironmentInstanceUtils {

    private PaasManagerClient client = new PaasManagerClient();
    private EnvironmentInstanceService service;

    /**
     * Create the environmentInstance
     * @param environmentDto the name
     * @param productInstanceDtos
     * @return the EnvironmentInstance
     */
    public Task create(String org, String vdc, 
    		EnvironmentDto environmentDto,
    		String callback) {
        service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
      
        return service.create(org, vdc, environmentDto, callback);
    }
    
    public EnvironmentInstanceDto load (String org, String vdc, String name) { 
    	service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
                 getProperty(MIME_TYPE));

		return service.load(org, vdc, name);
    }

	public Task create(String org, String vdc, String payload, String callback) {
		service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
                getProperty(MIME_TYPE));

       return service.create(org, vdc, payload, callback);
	}
}