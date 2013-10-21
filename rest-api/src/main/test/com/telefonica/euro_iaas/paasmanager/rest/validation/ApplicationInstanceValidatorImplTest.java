/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
