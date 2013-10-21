/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.impl.ProductReleaseDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import junit.framework.TestCase;
import org.junit.Before;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for ProductReleaseDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */
public class ProductReleaseDaoJpaImplTest extends TestCase {

    private OSDao osDao;
    private ProductTypeDao productTypeDao;

    public final static String PR_PRODUCT = "ProductReleaseProduct";
    public final static String PR_VERSION = "ProductReleaseVersion";
    public final static String PR_DESCRIPTION = "ProductReleaseDescription";

    public final static String PR2_PRODUCT = "Productrelease2Product";
    public final static String PR2_VERSION = "ProductRelease2Version";

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "OSTypeSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    @Override
    @Before
    public void setUp() throws Exception

    {

        productTypeDao = mock(ProductTypeDao.class);

        List<ProductType> productTypes = new ArrayList<ProductType>();
        productTypes.add(new ProductType());
        when(productTypeDao.findAll()).thenReturn(productTypes);
        osDao = mock(OSDao.class);

    }

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate1() throws Exception{ System.out.println("Inserting ProductReleaseObject1 in DB");
     * ProductReleaseDaoJpaImpl productReleaseDao = new ProductReleaseDaoJpaImpl (); List<ProductType> productTypes =
     * new ArrayList <ProductType>(); ProductType productType = new ProductType("PRODUCT_TYPE", "PRODUCT_DESCRIPTION");
     * List<OS> supportedOOSS = new ArrayList <OS> (); supportedOOSS.add(new OS (SO_OSTYPE, SO_NAME, SO_DESCRIPTION,
     * SO_VERSION)); ProductRelease productRelease = new ProductRelease(PR_PRODUCT, PR_VERSION);
     * assertNotNull(productRelease.getName()); productRelease.setDescription(PR_DESCRIPTION);
     * productRelease.setSupportedOOSS(supportedOOSS); productRelease.setProductType(productType); List<ProductRelease>
     * productReleases = productReleaseDao.findAll(); ProductRelease createdProductRelease; /* if
     * (productReleases.size() == 0 ) createdProductRelease = productReleaseDao.create(productRelease); else
     * createdProductRelease = productReleases.get(0); assertNotNull(createdProductRelease.getId());
     * assertEquals(productRelease.getId(), createdProductRelease.getId());
     */
    // }

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception { System.out.println("Inserting ProductReleaseObject2 in DB");
     * ProductReleaseDaoJpaImpl productReleaseDao = new ProductReleaseDaoJpaImpl (); List<OS> supportedOOSS =
     * osDao.findAll(); ProductRelease productRelease = new ProductRelease(PR2_PRODUCT, PR2_VERSION);
     * assertNotNull(productRelease.getName()); ProductTypeDaoJpaImplTest productTypeDaoJpaImplTest = new
     * ProductTypeDaoJpaImplTest(); productTypeDaoJpaImplTest.setProductTypeDao(productTypeDao); ProductType
     * productType; if (productTypeDaoJpaImplTest.findAll().size() == 0) productType =
     * productTypeDaoJpaImplTest.create(new ProductType("PRODUCT_TYPE", "PRODUCT_DESCRIPTION")); else productType =
     * productTypeDaoJpaImplTest.findAll().get(0); productRelease.setDescription(PR_DESCRIPTION);
     * productRelease.setProductType(productType); productRelease.setSupportedOOSS(supportedOOSS); List<ProductRelease>
     * productReleases = null; try { productReleaseDao.findAll(); } catch (Exception e) { e.printStackTrace(); } } /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        ProductReleaseDaoJpaImpl productReleaseDao = new ProductReleaseDaoJpaImpl();
        // assertEquals(0, productReleaseDao.findAll().size());
        // testCreate1();
        /*
         * List<ProductRelease> productReleases = productReleaseDao.findAll(); assertEquals(1, productReleases.size());
         * ProductRelease productRelease = productReleases.get(0); productRelease.setDescription("newDescription");
         * productReleaseDao.update(productRelease); assertEquals("newDescription",
         * productReleaseDao.load(productRelease.getName()).getDescription()); productReleaseDao.remove(productRelease);
         * assertEquals(0, productReleaseDao.findAll().size());
         */
    }

    public void testFindByCriteria() throws Exception {
        /*
         * ProductReleaseDaoJpaImpl productReleaseDao = new ProductReleaseDaoJpaImpl (); // testCreate1();
         * //testCreate2(); ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria(); //find all
         * List<ProductRelease> productReleases = productReleaseDao.findByCriteria(criteria); assertEquals(1,
         * productReleases.size()); //find by OSType criteria.setOSType(productReleases.get(0).getSupportedOOSS
         * ().get(0).getOsType()); productReleases = productReleaseDao.findByCriteria(criteria); assertEquals(1,
         * productReleases.size()); //find by productName /*criteria.setProduct(PR_PRODUCT); productReleases =
         * productReleaseDao.findByCriteria(criteria); assertEquals(1, productReleases.size());
         */
    }

}
