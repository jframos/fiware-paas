/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.it.util;

import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.ApplicationInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;


import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

/**
 * @author jesus.movilla
 */
public class ApplicationInstanceUtils {

    private PaasManagerClient client = new PaasManagerClient();
    private ApplicationInstanceService service;

    /**
     * Create the environmentInstance
     * 
     * @param environmentDto
     *            the name
     * @param productInstanceDtos
     * @return the EnvironmentInstance
     */
    public Task install(String vdc, ApplicationInstanceDto applicationInstanceDto, String callback) {

        service = client.getApplicationInstanceService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.install(vdc, applicationInstanceDto, callback);
    }

}
