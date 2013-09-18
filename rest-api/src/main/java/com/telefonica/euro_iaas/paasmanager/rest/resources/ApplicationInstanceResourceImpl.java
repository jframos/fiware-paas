package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.paasmanager.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.validation.ApplicationInstanceResourceValidator;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;

public class ApplicationInstanceResourceImpl implements
		ApplicationInstanceResource {

	private static Logger LOGGER =
			Logger.getLogger(ApplicationInstanceResourceImpl.class.getName());
		
    @InjectParam("applicationInstanceAsyncManager")
    private ApplicationInstanceAsyncManager applicationInstanceAsyncManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;
    
    private ApplicationInstanceResourceValidator validator;
    
	@Override
	public Task install(String vdc,
		ApplicationInstanceDto applicationInstanceDto, String callback) 
			throws InvalidApplicationReleaseException, 
			EnvironmentInstanceNotFoundException, 
			ProductReleaseNotFoundException {
		
		validator.validateInstall(applicationInstanceDto);
		
		String environmentInstanceName = null;
		ApplicationRelease applicationRelease = new ApplicationRelease();
		
		if (applicationInstanceDto.getEnvironmentInstanceName() != null){
			environmentInstanceName = 
				applicationInstanceDto.getEnvironmentInstanceName();
		}
		
		if (applicationInstanceDto.getApplicationName()!= null)
			applicationRelease.setName(applicationInstanceDto.getApplicationName());
			
		if (applicationInstanceDto.getAttributes() != null)
			applicationRelease.setAttributes(applicationInstanceDto.getAttributes());
		
		if (applicationInstanceDto.getArtifacts() != null)
			applicationRelease.setArtifacts(applicationInstanceDto.getArtifacts());
		
		if (applicationInstanceDto.getVersion() != null)
			applicationRelease.setVersion(applicationInstanceDto.getVersion());
		
		if (applicationInstanceDto.getApplicationType() != null){
			ApplicationType appType = new ApplicationType();
			appType.setName(applicationInstanceDto.getApplicationType());
			applicationRelease.setApplicationType(appType);
		}
			
    	Task task = createTask(MessageFormat.format(
                "Deploy  applicationInstance ",
                applicationInstanceDto.getApplicationName() + " on " +
                applicationInstanceDto.getEnvironmentInstanceName() ), vdc);
                
		 applicationInstanceAsyncManager. install(vdc, 
				environmentInstanceName, applicationRelease, task, callback);
		 
		 return task;
	}

	@Override
	public List<ApplicationInstance> findAll(String hostname, String domain,
			String ip, String fqn, Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status, String vdc,
			String applicationName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInstance load(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task uninstall(String vdc, Long id, String callback) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Task createTask(String description, String vdc) {
		Task task = new Task(TaskStates.RUNNING);
	    task.setDescription(description);
	    task.setVdc(vdc);
	    return taskManager.createTask(task);
	}
	
    /**
     * @param validator the validator to set
     */
    public void setValidator(ApplicationInstanceResourceValidator validator) {
        this.validator = validator;
    }

}
