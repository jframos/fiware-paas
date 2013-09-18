package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ServiceDao;
import com.telefonica.euro_iaas.paasmanager.model.Service;

public class ServiceDaoJpaImpl 
	extends AbstractBaseDao<Service, Long>  implements ServiceDao {

	@Override
	public List<Service> findAll() {
		return super.findAll(Service.class);
	}

	@Override
	public Service load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(Service.class, "id", arg0);
	}

}
