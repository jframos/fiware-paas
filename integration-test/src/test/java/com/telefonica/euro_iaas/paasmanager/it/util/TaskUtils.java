/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.it.util;

import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.TaskService;
import com.telefonica.euro_iaas.paasmanager.model.Task;


import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

/**
 * @author jesus.movilla
 */
public class TaskUtils {

    private PaasManagerClient client = new PaasManagerClient();
    private TaskService service;

    public Task load(String vdc, Long id) {
        service = client.getTaskService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        return service.load(vdc, id);
    }

}
