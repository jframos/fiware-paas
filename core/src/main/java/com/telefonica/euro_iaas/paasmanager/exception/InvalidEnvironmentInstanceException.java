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

package com.telefonica.euro_iaas.paasmanager.exception;

import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

/**
 * Exception thrown when trying to insert a ProductRelease that does not have the right information
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidEnvironmentInstanceException extends Exception {

    private EnvironmentInstance environment;

    public InvalidEnvironmentInstanceException() {
        super();
    }

    public InvalidEnvironmentInstanceException(EnvironmentInstance environment) {
        this.environment = environment;
    }

    public InvalidEnvironmentInstanceException(String msg) {
        super(msg);
    }

    public InvalidEnvironmentInstanceException(String msg, Throwable e) {
        super(msg, e);
    }

    public InvalidEnvironmentInstanceException(Throwable e) {
        super(e);
    }

    /**
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironment() {
        return environment;
    }

    /**
     * @param environmentInstance
     *            the environmentInstance to set
     */
    public void setEnvironment(EnvironmentInstance environment) {
        this.environment = environment;
    }
}
