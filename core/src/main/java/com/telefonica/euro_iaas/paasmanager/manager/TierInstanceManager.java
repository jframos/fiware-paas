package com.telefonica.euro_iaas.paasmanager.manager;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

public interface TierInstanceManager {

	/**
     * Update an tierInstance
     * @param tierInstance
     * @return the tierInstance created
     */
	TierInstance update(TierInstance tierInstance) 
    		throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException ;
}
