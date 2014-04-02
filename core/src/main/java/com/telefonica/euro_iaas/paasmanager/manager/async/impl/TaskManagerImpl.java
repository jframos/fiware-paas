/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.PAAS_MANAGER_URL;
import static com.telefonica.euro_iaas.paasmanager.util.Configuration.TASK_PATH;

import java.text.MessageFormat;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TaskDao;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Default TaskManager implementation.
 * 
 * @author Jesus M.Movilla
 */
public class TaskManagerImpl implements TaskManager {

    private TaskDao taskDao;
    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    public Task createTask(Task task) {
        try {
            task = taskDao.create(task);            
            task.setHref(getTaskUrl (task));
            return task;
        } catch (AlreadyExistsEntityException e) {
            throw new PaasManagerServerRuntimeException(e);
        }
    }
    
    private String getTaskUrl (Task task) {
        String path = MessageFormat.format(TASK_PATH,
                Long.valueOf(task.getId())
                .toString(), task.getVdc()); // th
        
        return propertiesProvider.getProperty(PAAS_MANAGER_URL) + path;
    }

    /**
     * {@inheritDoc}
     */
    public Task updateTask(Task task) {
        try {
            task = taskDao.update(task);
            task.setHref(getTaskUrl (task));
            return task;
        } catch (Exception e) {
            throw new PaasManagerServerRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Task load(Long id) throws EntityNotFoundException {
        Task task = taskDao.load(id);
        task.setHref(getTaskUrl (task));
        return task;
    }

    /**
     * {@inheritDoc}
     */
    public List<Task> findByCriteria(TaskSearchCriteria criteria) {
        List<Task> tasks = taskDao.findByCriteria(criteria);
        for (Task task : tasks) {
            task.setHref(getTaskUrl (task));
        }
        return tasks;
    }

    // //////// I.O.C ////////

    /**
     * @param taskDao
     *            the taskDao to set
     */
    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
