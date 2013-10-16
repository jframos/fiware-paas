package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;

public class ApplicationTypeDaoJpaImpl extends AbstractBaseDao<ApplicationType, String> implements ApplicationTypeDao {

    public List<ApplicationType> findAll() {
        return super.findAll(ApplicationType.class);
    }

    public ApplicationType load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ApplicationType.class, "name", arg0);
    }

}
