package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;

public class ProductTypeDaoJpaImplTest extends AbstractJpaDaoTest implements
		ProductTypeDao {

	private String PRODUCTTYPE_NAME = "ProductType_name";
	private String PRODUCTTYPE_DESC = "ProductType_desc";
	
	private ProductTypeDao productTypeDao;
	
	@Override
	public ProductType create(ProductType productType) throws InvalidEntityException,
			AlreadyExistsEntityException {
		productType = productTypeDao.create(productType);
		assertNotNull(productType.getId());
		return productType;
	}

	@Override
	public List<ProductType> findAll() {
		return productTypeDao.findAll();
	}

	@Override
	public ProductType load(Long arg0) throws EntityNotFoundException {
		ProductType productType = 
				productTypeDao.load(productTypeDao.findAll().get(0).getId());
		assertNotNull(productType.getId());
		return productType;
	}

	@Override
	public ProductType update(ProductType arg0) throws InvalidEntityException {
		ProductType productType = productTypeDao.findAll().get(0);
		productType.setDescription("Description2");	
		
		productType = productTypeDao.update(productType);
		assertEquals(productType.getDescription(), "Description2");
		
		return productType;
	}
	
	@Override
	public void remove(ProductType arg0) {
		productTypeDao.remove(productTypeDao.findAll().get(0));

	}
	
	/**
     * @param productTypeDao the productTypeDao to set
    */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }

}
