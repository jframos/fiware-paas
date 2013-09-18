package com.telefonica.euro_iaas.paasmanager.client.services;

import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

public interface EnvironmentInstanceService {

    /**
     * Create the selected EnvironmentInstance . 
     * If the Environment
     * already exists, then add the new Release. 
     *
     * @param EnvironmentDto
     * <ol>
     * <li>The EnvironmentDto: contains the information about the 
	 * environment instance </li>
     * </ol>
     * @param vdc the vdc to which it belongs
     * @param callback
     */

    Task create(String vdc, EnvironmentDto environmentDto, String callback);

   
}

