package com.telefonica.euro_iaas.paasmanager.manager.async;


import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

public interface EnvironmentInstanceAsyncManager {


	/**
	 * 
	 * @param org
	 * @param vdc
	 * @param envInstanceName=service (Claudia)
	 * @param environment
	 * @param task
	 * @param callback
	 */
	/*void create (String org, String vdc, PaasManagerUser user, 
			String envInstanceName,
			Environment environment, Task task, String callback);*/
    
	void create (ClaudiaData claudiaData, Environment environment, 
			Task task, String callback);
    
    /**
     * Retrieve the selected environment instance.
     * @param name the instance name
     * @return the environment instance
     */
    //EnvironmentInstance load(String name) throws EntityNotFoundException;
    
   /**
    * Delete an Instance of an Environment
    * @param environmentInstance
    * @param org
    * @param vdc
    * @param task
    * @param callback
    */
    void destroy(ClaudiaData claudiaData, 
    		EnvironmentInstance environmentInstance, Task task, String callback);
}
