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

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;

public class ApplicationInstanceResourceValidatorImpl implements ApplicationInstanceResourceValidator {

    private ClaudiaUtil claudiaUtil;
    private ApplicationInstanceManager applicationInstanceManager;
    /** The log. */
    private static Logger log = Logger.getLogger(ApplicationInstanceResourceValidatorImpl.class);

    public void validateInstall(String vdc, String environmentInstance, ApplicationReleaseDto applicationReleaseDto)
            throws InvalidApplicationReleaseException, ApplicationInstanceNotFoundException {

        if ((applicationReleaseDto.getApplicationName() == null) || (applicationReleaseDto.getVersion() == null))
            throw new InvalidApplicationReleaseException("Application Name is not provided");

        // Check that the application is not installed already
        try {
            applicationInstanceManager
                    .load(vdc, applicationReleaseDto.getApplicationName() + "-" + environmentInstance);
            throw new InvalidApplicationReleaseException("Application already installed");
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block

        }

        // if ((applicationInstanceDto.getApplicationType()== null))
        // throw new InvalidApplicationReleaseException();
    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.rest.validation.
     * ApplicationInstanceResourceValidator#validateInstall(java.lang.String)
     */
    public void validateInstall(String extendedovf) throws InvalidOVFException {
        // Validate if the extendedovf includes an application/product
        // specification
    }

    /**
     * @param claudiaUtil
     *            the claudiaUtil to set
     */
    public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
        this.claudiaUtil = claudiaUtil;
    }

    public void setApplicationInstanceManager(ApplicationInstanceManager applicationInstanceManager) {
        this.applicationInstanceManager = applicationInstanceManager;
    }

}
