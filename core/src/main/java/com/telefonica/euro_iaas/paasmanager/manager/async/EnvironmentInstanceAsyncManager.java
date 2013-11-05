/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.async;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;

public interface EnvironmentInstanceAsyncManager {

    /**
     * @param org
     * @param vdc
     * @param envInstanceName
     *            =service (Claudia)
     * @param environment
     * @param task
     * @param callback
     */
    /*
     * void create (String org, String vdc, PaasManagerUser user, String envInstanceName, Environment environment, Task
     * task, String callback);
     */

    void create(ClaudiaData claudiaData, EnvironmentInstance environmentInstance, Task task, String callback);

    /**
     * Retrieve the selected environment instance.
     * 
     * @param name
     *            the instance name
     * @return the environment instance
     */
    // EnvironmentInstance load(String name) throws EntityNotFoundException;

    /**
     * Delete an Instance of an Environment
     * 
     * @param environmentInstance
     * @param org
     * @param vdc
     * @param task
     * @param callback
     */
    void destroy(ClaudiaData claudiaData, EnvironmentInstance environmentInstance, Task task, String callback);
}
