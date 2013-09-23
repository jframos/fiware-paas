package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;

public class EnvironmentTypeDaoJpaImplTest extends AbstractJpaDaoTest {

	private EnvironmentTypeDao environmentTypeDao;

	public EnvironmentType create(EnvironmentType environmentType)
			throws InvalidEntityException, AlreadyExistsEntityException {
		environmentType = environmentTypeDao.create(environmentType);
		assertNotNull(environmentType.getId());
		return environmentType;
	}

	public List<EnvironmentType> findAll() {
		return environmentTypeDao.findAll();
	}

	public EnvironmentType load(Long arg0) throws EntityNotFoundException {
		EnvironmentType environmentType = environmentTypeDao
				.load(environmentTypeDao.findAll().get(0).getName());
		assertNotNull(environmentType.getId());
		return environmentType;
	}

	public void remove(EnvironmentType environmentType) {
		environmentTypeDao.remove(environmentType);
	}

	public EnvironmentType update(EnvironmentType arg0)
			throws InvalidEntityException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param environmentTypeDao
	 *            the environmentTypeDao to set
	 */
	public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) {
		this.environmentTypeDao = environmentTypeDao;
	}

}
