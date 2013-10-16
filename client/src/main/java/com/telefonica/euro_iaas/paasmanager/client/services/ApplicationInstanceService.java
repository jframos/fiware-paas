/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.client.services;

import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;

/**
 * @author jesus.movilla
 */
public interface ApplicationInstanceService {

    /**
     * Install an Application Release into an Environment Instance
     * 
     * @param vdc
     * @param applicationInstanceDto
     * @param callback
     * @return Task
     */
    Task install(String vdc, ApplicationInstanceDto applicationInstanceDto, String callback);

}
