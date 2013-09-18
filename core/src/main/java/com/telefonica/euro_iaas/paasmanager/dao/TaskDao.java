package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TaskSearchCriteria;
/**
 * Default the persistence operations for Task elements.
 * @author Jesus M. Movilla
 *
 */
public interface TaskDao extends BaseDAO<Task, Long> {

    List<Task> findByCriteria(TaskSearchCriteria criteria);

}
