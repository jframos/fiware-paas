package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

public class EnvironmentManagerImpl implements EnvironmentManager {

	
	private EnvironmentDao environmentDao;
	
	@Override
	public Environment create(String name, List<Tier> tiers,
			EnvironmentType environmentType) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy(Environment environment) {
		// TODO Auto-generated method stub

	}

	@Override
	public Environment load(String name) throws EntityNotFoundException {
		return environmentDao.load(name);
	}

	@Override
	public List<Environment> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
    /**
     * @param environmentDao
     *            the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

}
