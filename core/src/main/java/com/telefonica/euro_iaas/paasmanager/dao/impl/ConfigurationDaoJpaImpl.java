package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ConfigurationDao;
import com.telefonica.euro_iaas.paasmanager.model.Configuration;

public class ConfigurationDaoJpaImpl extends AbstractBaseDao<Configuration, Long> implements
		ConfigurationDao {

	@Override
	public Configuration create(Configuration configuration)
			throws InvalidEntityException, AlreadyExistsEntityException {
		return super.create(configuration);
		//return null;
	}

	@Override
	public List<Configuration> findAll() {
		return super.findAll(Configuration.class);
		//return null;
	}

	@Override
	public Configuration load(Long id) throws EntityNotFoundException {
		return super.loadByField(Configuration.class, "id", id);
		//return null;
	}

	@Override
	public void remove(Configuration configuration) {
		super.remove(configuration);
	}

	@Override
	public Configuration update(Configuration configuration)
			throws InvalidEntityException {
		return super.update(configuration);
		//return null;
	}
	
	/*public String suma(String a, String b) {
		//return a + b;
		//return "ab";
		return null;
	}*/

}
