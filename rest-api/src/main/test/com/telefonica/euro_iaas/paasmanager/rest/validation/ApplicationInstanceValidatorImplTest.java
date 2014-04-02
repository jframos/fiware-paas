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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;

public class ApplicationInstanceValidatorImplTest {

    private ApplicationInstanceResourceValidator applicationInstanceResourceValidator;
    private ApplicationInstanceDto applicationInstanceDto;

    @Before
    public void setUp() throws Exception {

        applicationInstanceResourceValidator = new ApplicationInstanceResourceValidator();

        applicationInstanceDto = new ApplicationInstanceDto();
        applicationInstanceDto.setEnvironmentInstanceName("name");
        applicationInstanceDto.setApplicationName("applicationReleaseName");
        applicationInstanceDto.setVersion("version");

    }

    @Test
    public void testInstallValidateKO() throws Exception {
        applicationInstanceResourceValidator.validateInstall(applicationInstanceDto);
    }

    @Test
    public void testInstallValidateInvalidApplicationRelease() throws Exception {
        try {
            applicationInstanceDto.setVersion(null);
            applicationInstanceResourceValidator.validateInstall(applicationInstanceDto);
            Assert.fail();
        } catch (InvalidApplicationReleaseException e) {
            // Expected Exception
        }

        try {
            applicationInstanceDto.setApplicationName(null);
            applicationInstanceResourceValidator.validateInstall(applicationInstanceDto);
            Assert.fail();
        } catch (InvalidApplicationReleaseException e) {
            // Expected Exception
        }

        try {
            applicationInstanceDto.setApplicationType(null);
            applicationInstanceResourceValidator.validateInstall(applicationInstanceDto);
            Assert.fail();
        } catch (InvalidApplicationReleaseException e) {
            // Expected Exception
        }
    }

    @Test
    public void testInstallValidateNFEnvironmentInstance() throws Exception {

        try {
            applicationInstanceDto.setApplicationName(null);
            applicationInstanceResourceValidator.validateInstall(applicationInstanceDto);
            Assert.fail();
        } catch (EnvironmentInstanceNotFoundException e) {
            // Expected Exception
        }
    }

}
