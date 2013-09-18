package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;

public class AppicationTypeDaoJpaImplTest extends AbstractJpaDaoTest implements
		ApplicationTypeDao {

	private ApplicationTypeDao applicationTypeDao;
	
	@Override
	public ApplicationType create(ApplicationType applicationType)
			throws InvalidEntityException, AlreadyExistsEntityException {
			
		applicationType 
				= applicationTypeDao.create(applicationType);
		assertNotNull(applicationType.getId());
		return applicationType;
			
	}

	@Override
	public List<ApplicationType> findAll() {
		return applicationTypeDao.findAll();
	}

	@Override
	public ApplicationType load(Long arg0) throws EntityNotFoundException {
		ApplicationType applicationType = applicationTypeDao.load(arg0);
		
		return applicationType;
		
	}

	@Override
	public void remove(ApplicationType applicationType) {
		applicationTypeDao.remove(applicationType);

	}

	@Override
	public ApplicationType update(ApplicationType applicationType)
			throws InvalidEntityException {
		return applicationTypeDao.update(applicationType);
	}

	/**
	 * @param applicationTypeDao the applicationTypeDao to set
	 */
	public void setApplicationTypeDao(ApplicationTypeDao applicationTypeDao) {
		this.applicationTypeDao = applicationTypeDao;
	}

}
