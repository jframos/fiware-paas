package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;

public class ApplicationInstanceResourceValidatorImpl 
	implements ApplicationInstanceResourceValidator{

	@Override
	public void validateInstall(ApplicationInstanceDto applicationInstanceDto)
			throws InvalidApplicationReleaseException,
			EnvironmentInstanceNotFoundException {
		
		if (applicationInstanceDto.getEnvironmentInstanceName() == null)
			throw new EnvironmentInstanceNotFoundException();
		
		if ((applicationInstanceDto.getApplicationName() == null ) ||
			(applicationInstanceDto.getVersion() == null))
			throw new InvalidApplicationReleaseException();
		
		if ((applicationInstanceDto.getApplicationType()!= null))
			throw new InvalidApplicationReleaseException();
	}

}
