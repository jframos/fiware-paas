package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
