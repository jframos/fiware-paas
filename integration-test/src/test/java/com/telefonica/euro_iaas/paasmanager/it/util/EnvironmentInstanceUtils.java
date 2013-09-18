package com.telefonica.euro_iaas.paasmanager.it.util;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;


public class EnvironmentInstanceUtils {

    private PaasManagerClient client = new PaasManagerClient();
    private EnvironmentInstanceService service;

    /**
     * Create the environmentInstance
     * @param environmentDto the name
     * @param productInstanceDtos
     * @return the EnvironmentInstance
     */
    public Task create(String vdc, 
    		EnvironmentDto environmentDto,
    		String callback) {
        service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
      
        return service.create(vdc, environmentDto, callback);
    }
}