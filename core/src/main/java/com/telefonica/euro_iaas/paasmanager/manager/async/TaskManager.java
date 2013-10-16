package com.telefonica.euro_iaas.paasmanager.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TaskSearchCriteria;

/**
 * Provides the methods to work with tasks.
 * 
 * @author Sergio Arroyo
 */
public interface TaskManager {

    /**
     * Creates the task and persist it.
     * 
     * @param task
     * @return
     */
    Task createTask(Task task);

    /**
     * Update the task.
     * 
     * @param task
     * @return the updated task.
     */
    Task updateTask(Task task);

    /**
     * Retrieve the task by id.
     * 
     * @param id
     *            the id
     * @return the task.
     * @throws EntityNotFoundException
     */
    Task load(Long id) throws EntityNotFoundException;

    /**
     * Find all tasks that match with the given criteria
     * 
     * @param criteria
     *            the search criteria
     * @return the tasks.
     */
    List<Task> findByCriteria(TaskSearchCriteria criteria);
    /**
     * Retrive a tasks' list from a tierInstance
     * 
     * @param vdc
     * @param envName
     * @param tierName
     * @return
     */
}
