package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

public class TierDaoJpaImpl extends AbstractBaseDao<Tier, Long> implements TierDao {

	@Override
	public List<Tier> findAll() {
		return super.findAll(Tier.class);
	}

	@Override
	public Tier load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(Tier.class, "id", arg0);
	}
}
