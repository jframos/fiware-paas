package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.model.Task;

/**
 * In charge to notify when the requested action is finished providing the result of that.
 * 
 * @author Jesus M. Movilla
 */
public interface TaskNotificator {

    /**
     * Send the notification to provided url
     * 
     * @param url
     *            the url
     * @param task
     *            the task containing information about the action
     */
    void notify(String url, Task task);
}
