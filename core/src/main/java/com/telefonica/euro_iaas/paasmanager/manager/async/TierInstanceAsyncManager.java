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
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

public interface TierInstanceAsyncManager {

    /**
     * Create a new Tier Instance
     * 
     * @param claudiaData
     * @param tierInstance
     * @param envInstance
     * @param task
     * @param callback
     */
    public void create(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstance, Task task,
            String callback);

    /**
     * Delete a Tier Instance
     * 
     * @param claudiaData
     * @param tierInstance
     * @param task
     * @param callback
     */
    public void delete(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstance, Task task,
            String callback);

    /**
     * Reconfigure a Tier Instance
     * 
     * @param claudiaData
     * @param tierInstance
     * @param envInstance
     * @param task
     * @param callback
     */
    public void update(ClaudiaData claudiaData, TierInstance tierInstance, EnvironmentInstance envInstance, Task task,
            String callback);

    /**
     * Delete an Instance of an Environment
     * 
     * @param environmentInstance
     * @param org
     * @param vdc
     * @param task
     * @param callback
     */
    // void destroy(EnvironmentInstance environmentInstance, String org, String
    // vdc,
    // Task task, String callback);
}
