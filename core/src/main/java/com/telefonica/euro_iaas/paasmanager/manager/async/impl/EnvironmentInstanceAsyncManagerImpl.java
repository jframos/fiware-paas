package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.ENVIRONMENT_INSTANCE_BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.ENVIRONMENT_BASE_URL;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TaskError;
import com.telefonica.euro_iaas.paasmanager.model.TaskReference;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

public class EnvironmentInstanceAsyncManagerImpl implements
		EnvironmentInstanceAsyncManager {
	
	private static Logger LOGGER =
		Logger.getLogger(ProductInstanceAsyncManagerImpl.class.getName());
	private ProductInstanceManager productInstanceManager;
	private EnvironmentInstanceManager environmentInstanceManager;
	private TaskManager taskManager;
	private SystemPropertiesProvider propertiesProvider;
	private TaskNotificator taskNotificator;
	private EnvironmentManager environmentManager;
	private InfrastructureManager infrastructureManager;
	
	@Override
	public void create(String vdc, Environment environment, Task task,
			String callback) {
		
		try {
		
			EnvironmentInstance environmentInstance 
				= environmentInstanceManager.create(vdc, environment);
			
			updateSuccessTask(task, environmentInstance);
			LOGGER.info("The Environment Instance "
	                + environmentInstance.getName()+ " has been CORRECTLY provisioned");
		} catch (EntityNotFoundException enf){
			String errorMsg = "The Environment "
                    + environment.getName()
                    + " is not in the System";
            updateErrorTaskOnInstall(environment, vdc, task, errorMsg, enf);
		} catch (InvalidEntityException iee) {
			String errorMsg = "The Environment "
                    + environment.getName()
                    + " is Invaid";
            updateErrorTaskOnInstall(environment, vdc, task, errorMsg, iee);	
		} catch (AlreadyExistsEntityException aee) {
			String errorMsg = "The Environment "
                    + environment.getName()
                    + " already exists";
            updateErrorTaskOnInstall(environment, vdc, task, errorMsg, aee);
		} catch (NotUniqueResultException nue) {
			String errorMsg = "There is a Product Instance already INSTALLED" +
					"in the system";
            updateErrorTaskOnInstall(environment, vdc, task, errorMsg, nue);
		}
		finally {
            notifyTask(callback, task);
        }
	}

	/*@Override
	public EnvironmentInstance load(String name) throws EntityNotFoundException {
		return environmentInstanceManager.load(name);
	}*/

	@Override
	public void destroy(EnvironmentInstance environmentInstance, Task task,
			String callback) {
		// TODO Auto-generated method stub

	}

	
    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, EnvironmentInstance environmentInstance) {
        InstallableInstance productInstance;
		String piResource = MessageFormat.format(
                propertiesProvider.getProperty(ENVIRONMENT_INSTANCE_BASE_URL),
                environmentInstance.getVdc(), 
                environmentInstance.getName()); // the vdc
        task.setResult(new TaskReference(piResource));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }
    
    /*
     * Update the error task in install operation. The installation is a bit
     * different to the other operations because the instance could exist or not
     * so this method shall do this check.
     */
    private void updateErrorTaskOnInstall(Environment environment, String vdc, 
    		Task task, String errorMsg, Throwable e) {      
    	updateErrorTask(environment, vdc, task, errorMsg, e);
    }
    
    /*
     * Update the task with necessary information when the task is wrong and the
     * product instance exists in the system.
     */
    private void updateErrorTask(Environment environment, String vdc, Task task,
            String message, Throwable t) {

    	String aiResource = MessageFormat.format(
                propertiesProvider.getProperty(ENVIRONMENT_BASE_URL),
                environment.getName());
        task.setResult(new TaskReference(aiResource));
        updateErrorTask(task, message, t);
    }

    /*
     * Update the task with necessary information when the task is wrong.
     */
    private void updateErrorTask(Task task, String message, Throwable t) {
        TaskError error = new TaskError(message);
        error.setMajorErrorCode(t.getMessage());
        error.setMinorErrorCode(t.getClass().getSimpleName());
        task.setEndTime(new Date());
        task.setStatus(TaskStates.ERROR);
        task.setError(error);
        taskManager.updateTask(task);
        LOGGER.info("An error occurs while executing an environment action. See task "
                + task.getHref() + " for more information");
    }
    
    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }
    //////////// I.O.C ////////////

    /**
     * @param productInstanceManager
     *            the productInstanceManager to set
     */
    public void setProductInstanceManager(
            ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param taskNotificator the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }
    
    /**
     * @param environmentManager
     *            the environmentManager to set
     */
    public void setEnvironmentManager(
            EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }
 
    /**
     * @param environmentInstanceManager
     *            the environmentInstanceManager to set
     */
    public void setEnvironmentInstanceManager(
            EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }

    /**
     * @param infrastructureManager
     *            the infrastructureManager to set
     */
    public void setInfrastructureManager(
            InfrastructureManager infrastructureManager) {
        this.infrastructureManager = infrastructureManager;
    }
}
