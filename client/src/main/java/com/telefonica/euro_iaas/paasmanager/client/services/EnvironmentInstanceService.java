/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.client.services;

import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;

public interface EnvironmentInstanceService {

    /**
     * Create the selected EnvironmentInstance . If the Environment already exists, then add the new Release.
     * 
     * @param EnvironmentDto
     *            <ol>
     *            <li>The EnvironmentDto: contains the information about the environment instance</li>
     *            </ol>
     * @param vdc
     *            the vdc to which it belongs
     * @param callback
     */

    Task create(String org, String vdc, EnvironmentDto environmentDto, String callback);

    /**
     * Create the selected EnvironmentInstance from payload
     * 
     * @param vdc
     * @param payload
     * @param callback
     * @return
     */
    Task create(String org, String vdc, String payload, String callback);

    /**
     * Return an environmentInstanceDto
     * 
     * @param vdc
     * @param name
     * @return
     */
    EnvironmentInstanceDto load(String org, String vdc, String name);
}
