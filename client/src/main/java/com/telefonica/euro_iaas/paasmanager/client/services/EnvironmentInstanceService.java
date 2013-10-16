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
