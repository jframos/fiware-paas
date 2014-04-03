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

import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;

/**
 * Exception thrown when the application type is not found.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@SuppressWarnings("serial")
public class ApplicationTypeNotFoundException extends Exception {

    private ApplicationType applicationType;

    public ApplicationTypeNotFoundException() {
        super();
    }

    public ApplicationTypeNotFoundException(String msg) {
        super(msg);
    }

    public ApplicationTypeNotFoundException(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public ApplicationTypeNotFoundException(Throwable e) {
        super(e);
    }

    public ApplicationTypeNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the applicationType
     */
    public ApplicationType getApplicationType() {
        return applicationType;
    }

}
