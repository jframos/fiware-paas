package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;

public class EnvironmentTypeDaoJpaImpl extends
		AbstractBaseDao<EnvironmentType, String> implements EnvironmentTypeDao {

	public List<EnvironmentType> findAll() {
		return super.findAll(EnvironmentType.class);
	}

	public EnvironmentType load(String arg0) throws EntityNotFoundException {
		return super.loadByField(EnvironmentType.class, "name", arg0);
	}

}