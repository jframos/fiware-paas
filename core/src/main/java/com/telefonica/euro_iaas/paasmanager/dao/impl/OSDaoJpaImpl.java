package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.OSDao;
import com.telefonica.euro_iaas.paasmanager.model.OS;

public class OSDaoJpaImpl extends AbstractBaseDao<OS, String> implements OSDao {

	public List<OS> findAll() {
		return super.findAll(OS.class);
	}

	/** {@inheritDoc} */
	public OS load(String osType) throws EntityNotFoundException {
		return super.loadByField(OS.class, "osType", osType);
	}

}
