/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.VIRTUAL_SERVICE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.TaskNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.VirtualServiceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.VirtualServiceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TaskError;
import com.telefonica.euro_iaas.paasmanager.model.TaskReference;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

/**
 * @author jesus.movilla
 */
public class VirtualServiceAsyncManagerImpl implements VirtualServiceAsyncManager {

    private static Logger log = Logger.getLogger(EnvironmentInstanceAsyncManagerImpl.class.getName());

    private TaskNotificator taskNotificator;
    private TaskManager taskManager;
    private SystemPropertiesProvider propertiesProvider;
    private VirtualServiceManager virtualServiceManager;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.async.VirtualServiceAsyncManager #create(java.lang.String,
     * com.telefonica.euro_iaas.paasmanager.model.Task, java.lang.String)
     */
    @Async
    public void create(String virtualServiceName, String virtualService, Task task, String callback) {

        try {

            virtualServiceManager.create(virtualServiceName, virtualService);
            updateSuccessTask(task, virtualServiceName);
            log.info("The VirtualService " + virtualServiceName + " has been CORRECTLY installed");
        } catch (TaskNotFoundException e) {
            String errorMsg = "Unable to update task: " + e.getTask().getHref() + ". Description: " + e.getMessage();
            updateErrorTaskOnInstall(virtualServiceName, task, errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Unexpected error installing virtualService : " + virtualServiceName + ". Description:"
                    + e.getMessage();
            updateErrorTaskOnInstall(virtualServiceName, task, errorMsg, e);
        } finally {
            notifyTask(callback, task);
        }

    }

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, String virtualServiceName) throws TaskNotFoundException {
        InstallableInstance productInstance;
        Task loadedTask;
        String piResource = MessageFormat.format(propertiesProvider.getProperty(VIRTUAL_SERVICE_BASE_URL),
                virtualServiceName); // the
        // virtualServiceName

        try {
            loadedTask = taskManager.load(task.getId());
        } catch (EntityNotFoundException e) {
            throw new TaskNotFoundException(e.getMessage(), task);
        }

        // Uncommented - Could not commit JPA transaction; nested exception is
        // javax.persistence.RollbackException: Transaction marked as
        // rollbackOnly
        // loadedTask.setResult(new TaskReference(piResource));
        loadedTask.setEndTime(new Date());
        loadedTask.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(loadedTask);
    }

    /*
     * Update the error task in install operation. The installation is a bit different to the other operations because
     * the instance could exist or not so this method shall do this check.
     */
    private void updateErrorTaskOnInstall(String virtualServiceName, Task task, String errorMsg, Throwable e) {
        updateErrorTask(virtualServiceName, task, errorMsg, e);
    }

    /*
     * Update the task with necessary information when the task is wrong and the product instance exists in the system.
     */
    private void updateErrorTask(String virtualServiceName, Task task, String message, Throwable t) {

        String aiResource = MessageFormat.format(propertiesProvider.getProperty(VIRTUAL_SERVICE_BASE_URL),
                virtualServiceName);
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
        log.info("An error occurs while installing Virtual Service. See task " + task.getHref()
                + " for more information");
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
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
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param taskNotificator
     *            the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

    /**
     * @param virtualServiceManager
     *            the virtualServiceManager to set
     */
    public void setVirtualServiceManager(VirtualServiceManager virtualServiceManager) {
        this.virtualServiceManager = virtualServiceManager;
    }

}
