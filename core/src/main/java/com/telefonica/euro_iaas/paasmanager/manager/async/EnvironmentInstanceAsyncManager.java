/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
