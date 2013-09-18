package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;

public class ApplicationReleaseDaoJpaImpl 
	extends AbstractBaseDao<ApplicationRelease, String>  implements ApplicationReleaseDao {

	@Override
	public List<ApplicationRelease> findAll() {
		return super.findAll(ApplicationRelease.class);
	}

	@Override
	public ApplicationRelease load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ApplicationRelease.class, "id", arg0);
	}

}
