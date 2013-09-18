package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;

public class ArtifactTypeDaoJpaImpl extends AbstractBaseDao<ArtifactType, Long> implements
		ArtifactTypeDao {

	@Override
	public List<ArtifactType> findAll() {
		return super.findAll(ArtifactType.class);
	}

	@Override
	public ArtifactType load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(ArtifactType.class, "id", arg0);
	}


}
