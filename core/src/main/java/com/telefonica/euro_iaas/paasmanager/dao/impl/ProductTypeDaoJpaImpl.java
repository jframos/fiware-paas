package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;

public class ProductTypeDaoJpaImpl extends AbstractBaseDao<ProductType, Long> implements
ProductTypeDao {

	@Override
	public List<ProductType> findAll() {
		return super.findAll(ProductType.class);
	}

	@Override
	public ProductType load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(ProductType.class, "id", arg0);
	}
}
