package com.telefonica.euro_iaas.paasmanager.manager.async;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;

public interface EnvironmentInstanceAsyncManager {

	/**
     * Create a Instance of an Environment
     * @param vdc the vdc where the application will be installed.
     * @param environment the environment to install containing the tiers 
     * and productInstances
     * @param Attributes of the EnvironmentInstances
     * @param task the task which contains the information about the async execution
     * @param callback if not null, contains the url where the system shall
     * notify when the task is done
     * @return the task referencing the installed application.
     */

    void create(String vdc, EnvironmentInstance environment,
    		 Task task, String callback) throws EntityNotFoundException;
    
   
    /**
     * Retrieve the selected environment instance.
     * @param id the instance id
     * @return the environment instance
     */
    EnvironmentInstance load(Long id) throws EntityNotFoundException;
    
    /**
     * Destroy an Instance of an Environment.
     *
     * @param environmentInstance to be removed
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     * @return the task.
     */
    void destroy(EnvironmentInstance environmentInstance, 
    		Task task, String callback);
}
