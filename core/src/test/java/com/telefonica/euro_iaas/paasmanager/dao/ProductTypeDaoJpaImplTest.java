/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;

public class ProductTypeDaoJpaImplTest extends AbstractJpaDaoTest {

    private final String PRODUCTTYPE_NAME = "ProductType_name";
    private final String PRODUCTTYPE_DESC = "ProductType_desc";

    private ProductTypeDao productTypeDao;

    public ProductType create(ProductType productType) throws InvalidEntityException, AlreadyExistsEntityException {
        System.out.println("Inserting ProductTypeObject in DB");
        productType = productTypeDao.create(productType);
        assertNotNull(productType.getId());
        return productType;
    }

    public List<ProductType> findAll() {
        return productTypeDao.findAll();
    }

    public ProductType load(Long arg0) throws EntityNotFoundException {
        ProductType productType = productTypeDao.load(productTypeDao.findAll().get(0).getName());
        assertNotNull(productType.getId());
        return productType;
    }

    public void remove(ProductType arg0) {
        productTypeDao.remove(productTypeDao.findAll().get(0));

    }

    /**
     * @param productTypeDao
     *            the productTypeDao to set
     */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }

    public ProductType update(ProductType arg0) throws InvalidEntityException {
        ProductType productType = productTypeDao.findAll().get(0);
        productType.setDescription("Description2");

        productType = productTypeDao.update(productType);
        assertEquals(productType.getDescription(), "Description2");

        return productType;
    }

}
