/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;

/**
 * @author jesus.movilla
 * 
 */
public class ApplicationReleaseManagerImpl implements ApplicationReleaseManager {

	private ApplicationReleaseDao applicationReleaseDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager
	 * #load(java.lang.String)
	 */
	public ApplicationRelease load(String name) throws EntityNotFoundException {
		return applicationReleaseDao.load(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager
	 * #findAll()
	 */
	public List<ApplicationRelease> findAll() {
		return applicationReleaseDao.findAll();
	}

	public List<ApplicationRelease> findByCriteria(
			ApplicationReleaseSearchCriteria criteria) {
		return applicationReleaseDao.findByCriteria(criteria);
	}

	/**
	 * @param productDao
	 *            the applicationDao to set
	 */
	public void setApplicationReleaseDao(
			ApplicationReleaseDao applicationReleaseDao) {
		this.applicationReleaseDao = applicationReleaseDao;
	}

}
