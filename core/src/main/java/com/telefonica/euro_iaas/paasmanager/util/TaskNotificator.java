/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

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
