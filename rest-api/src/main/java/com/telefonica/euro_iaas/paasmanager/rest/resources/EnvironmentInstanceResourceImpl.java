package com.telefonica.euro_iaas.paasmanager.rest.resources;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.ENVIRONMENT_INSTANCE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.impl.ProductInstanceAsyncManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.TaskError;
import com.telefonica.euro_iaas.paasmanager.model.TaskReference;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Default EnvironmentInstanceResource implementation.
 *
 * @author Jesus M. Movilla
 *
 */
@Path("/vdc/{vdc}/environmentInstance")
@Component
@Scope("request")
public class EnvironmentInstanceResourceImpl implements
		EnvironmentInstanceResource {
	 
	private static Logger LOGGER =
			Logger.getLogger(EnvironmentInstanceResourceImpl.class.getName());
		
    @InjectParam("environmentInstanceAsyncManager")
    private EnvironmentInstanceAsyncManager environmentInstanceAsyncManager;
    @InjectParam("environmentInstanceManager")
    private EnvironmentInstanceManager environmentInstanceManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;
	
    @Override
	public Task create(String vdc, EnvironmentDto environmentDto,
			String callback) throws EntityNotFoundException, 
			InvalidEntityException, AlreadyExistsEntityException {
		
   	
    	//Validaciones
    	Environment environment = new Environment();
        
        if (environmentDto.getEnvironmentType() != null)
        	environment.setEnvironmentType(environmentDto.getEnvironmentType());
        
        environment.setTiers(environmentDto.getTiers());
        environment.setEnvironmentType(environmentDto.getEnvironmentType());
        environment.setName(environmentDto.getName());
        
    	Task task = createTask(MessageFormat.format(
                "Create environment {0}",
                environmentDto.getName()), vdc);
        
    	environmentInstanceAsyncManager.create(vdc, environment, task, callback);
		
    	return task;
	}

	@Override
	public List<EnvironmentInstance> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status, String vdc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnvironmentInstance load(String name) {
        try {
            return environmentInstanceManager.load(name);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
	}

	@Override
	public Task destroy(String vdc, Long id, String callback) {
		// TODO Auto-generated method stub
		return null;
	}

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }

}
