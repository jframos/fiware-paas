/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.TaskNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TaskError;
import com.telefonica.euro_iaas.paasmanager.model.TaskReference;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;
import org.apache.commons.lang.StringUtils;


import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.APPLICATION_RELEASE_BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.APPLICATION_TYPE_BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.ENVIRONMENT_INSTANCE_BASE_URL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.PRODUCT_RELEASE_BASE_URL;

/**
 * Application Instance Manager operations: install
 * 
 * @author Jesus M. Movilla
 */
public class ApplicationInstanceAsyncManagerImpl implements ApplicationInstanceAsyncManager {

    private static Logger LOGGER = Logger.getLogger(ProductInstanceAsyncManagerImpl.class.getName());
    private ApplicationInstanceManager applicationInstanceManager;
    private TaskManager taskManager;
    private SystemPropertiesProvider propertiesProvider;
    private TaskNotificator taskNotificator;
    private EnvironmentInstanceDao environmentInstanceDao;

    /**
     * Install an applicationRelease on an already existent EnvironmentInstance
     * 
     * @param vdc
     *            the vdc where the instance will be installed
     * @param environmentInstanceName
     *            on which applicationRelease is going to be deployed
     * @param applicationRelease
     *            to be deployed
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the execution will be sent
     */
    public void install(String org, String vdc, String environmentInstanceName, ApplicationRelease applicationRelease,
            Task task, String callback) {

        try {
            EnvironmentInstance environmentInstance = environmentInstanceDao.load(environmentInstanceName);

            ApplicationInstance applicationInstance = applicationInstanceManager.install(org, vdc, environmentInstance,
                    applicationRelease);
            updateSuccessTask(task, environmentInstance);
            LOGGER.info("Application " + applicationRelease.getName() + '-' + applicationRelease.getVersion()
                    + " installed successfully " + " on Environment " + environmentInstanceName);
        } catch (EntityNotFoundException e) {
            String errorMsg = e.getMessage();
            updateErrorTask(environmentInstanceName, ENVIRONMENT_INSTANCE_BASE_URL, task, errorMsg, e);
        } catch (ProductReleaseNotFoundException prNFE) {
            String errorMsg = prNFE.getMessage();
            updateErrorTask(prNFE.getProductRelease().getName(), PRODUCT_RELEASE_BASE_URL, task, errorMsg, prNFE);
        } catch (ApplicationTypeNotFoundException atNFE) {
            String errorMsg = atNFE.getMessage();
            updateErrorTask(atNFE.getApplicationType().getName(), APPLICATION_TYPE_BASE_URL, task, errorMsg, atNFE);
        } catch (InvalidEntityException iee) {
            String errorMsg = iee.getMessage();
            updateErrorTask(applicationRelease.getName(), APPLICATION_RELEASE_BASE_URL, task, errorMsg, iee);
        } catch (AlreadyExistsEntityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProductInstallatorException e) {
            String errorMsg = "Error installing an application. Description:" + e.getMessage();
            updateErrorTask("Error installing an application", PRODUCT_RELEASE_BASE_URL, task, errorMsg, e);
        } catch (TaskNotFoundException e) {
            String errorMsg = "Error installing an application. Description:" + e.getMessage();
            updateErrorTask("Error installing an application", PRODUCT_RELEASE_BASE_URL, task, errorMsg, e);
        } finally {
            notifyTask(callback, task);
        }

    }

    public void uninstall(String org, String vdc, String environmentInstanceName, String applicationName, Task task,
            String callback) {

        ApplicationInstance applicationInstance = null;
        try {
            applicationInstance = applicationInstanceManager.load(vdc, applicationName);
            EnvironmentInstance environmentInstance = environmentInstanceDao.load(environmentInstanceName);
            applicationInstanceManager.uninstall(org, vdc, environmentInstance, applicationInstance);
            updateSuccessTask(task, environmentInstance);
            LOGGER.info("Application " + applicationInstance.getName() + '-' + " uninstalled successfully "
                    + " on Environment " + environmentInstanceName);
        } catch (EntityNotFoundException e) {
            String errorMsg = e.getMessage();
            updateErrorTask(environmentInstanceName, ENVIRONMENT_INSTANCE_BASE_URL, task, errorMsg, e);
        } catch (ProductInstallatorException e) {
            String errorMsg = e.getMessage();
            updateErrorTask(environmentInstanceName, ENVIRONMENT_INSTANCE_BASE_URL, task, errorMsg, e);
        } catch (TaskNotFoundException e) {
            String errorMsg = e.getMessage();
            updateErrorTask(environmentInstanceName, ENVIRONMENT_INSTANCE_BASE_URL, task, errorMsg, e);
        }

    }

    private void updateSuccessTask(Task task, EnvironmentInstance environmentInstance) throws TaskNotFoundException {
        InstallableInstance productInstance;
        Task loadedTask;
        String piResource = MessageFormat.format(propertiesProvider.getProperty(ENVIRONMENT_INSTANCE_BASE_URL),
                environmentInstance.getVdc(), environmentInstance.getName()); // the
        // vdc

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

    /**
     * Update the task with necessary information when the task is wrong because the environmentInstance does not exist
     * in the system.
     */
    private void updateErrorTask(String resourceName, String baseUrl, Task task, String message, Throwable t) {
        String piResource = MessageFormat.format(propertiesProvider.getProperty(baseUrl), resourceName); // the name
        task.setResult(new TaskReference(piResource));
        updateErrorTask(task, message, t);
    }

    /**
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
        LOGGER.info("An error occurs while installing an application release on a" + " environment. See task "
                + task.getHref() + " for more information");
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    /**
     * @param applicationInstanceManager
     *            the productInstanceManager to set
     */
    public void setApplicationInstanceManager(ApplicationInstanceManager applicationInstanceManager) {
        this.applicationInstanceManager = applicationInstanceManager;
    }

    /**
     * @param environmentInstanceDao
     *            the environmentInstanceDao to set
     */
    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
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

}
