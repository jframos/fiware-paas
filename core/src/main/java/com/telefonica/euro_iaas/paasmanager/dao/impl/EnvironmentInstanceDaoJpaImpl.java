package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

public class EnvironmentInstanceDaoJpaImpl 
	extends AbstractBaseDao<EnvironmentInstance, String> implements EnvironmentInstanceDao {

	
	@Override
	public List<EnvironmentInstance> findAll() {
		return super.findAll(EnvironmentInstance.class);
	}

	@Override
	public EnvironmentInstance load(String  arg0) throws EntityNotFoundException {
        return super.loadByField(EnvironmentInstance.class, "name", arg0);
	}

}
