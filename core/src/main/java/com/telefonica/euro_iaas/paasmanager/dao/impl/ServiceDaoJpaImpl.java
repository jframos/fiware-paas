package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ServiceDao;
import com.telefonica.euro_iaas.paasmanager.model.Service;

public class ServiceDaoJpaImpl extends AbstractBaseDao<Service, String> implements ServiceDao {

    public List<Service> findAll() {
        return super.findAll(Service.class);
    }

    public Service load(String name) throws EntityNotFoundException {
        return super.loadByField(Service.class, "name", name);
    }

}
