/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
