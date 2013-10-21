/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import com.telefonica.euro_iaas.paasmanager.dao.impl.ProductInstanceDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;

/**
 * Unit test for InstanceDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */
public class ProductInstanceDaoJpaImplTest extends TestCase {

    private ProductInstanceDao productInstanceDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ProductTypeDao productTypeDao;
    private ProductRelease productRelease;

    public final static String PINSTANCE_NAME = "instanceName";
    public final static String PINSTANCE2_NAME = "instance2Name";
    public final static String PINSTANCE_VERSION = "instanceVersion";

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /*
     * public void testFindByCriteria() throws Exception { createProductRelease(); VM host = new VM(null, "hostname",
     * "domain"); VM host2 = new VM("fqn"); ProductRelease release = productReleaseDao.findAll().get(0); ProductInstance
     * pi1 = new ProductInstance(release, Status.INSTALLED, host, "vdc"); ProductInstance pi2 = new
     * ProductInstance(release, Status.UNINSTALLED, host2, "vdc"); pi1 = productInstanceDao.create(pi1); pi2 =
     * productInstanceDao.create(pi2); ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria(); //
     * find all List<ProductInstance> instances = productInstanceDao .findByCriteria(criteria); assertEquals(2,
     * instances.size()); // find by Host1 criteria.setVM(host); instances =
     * productInstanceDao.findByCriteria(criteria); assertEquals(1, instances.size()); assertEquals(pi1,
     * instances.get(0)); // find by Host2 criteria.setVM(host2); instances =
     * productInstanceDao.findByCriteria(criteria); assertEquals(1, instances.size()); assertEquals(pi2,
     * instances.get(0)); // find by Status criteria.setVM(null); criteria.setStatus(Status.INSTALLED); instances =
     * productInstanceDao.findByCriteria(criteria); assertEquals(1, instances.size()); assertEquals(pi1,
     * instances.get(0)); criteria.setVm(host); criteria.setProductRelease(release); instances =
     * productInstanceDao.findByCriteria(criteria); assertEquals(1, instances.size()); assertEquals(pi1,
     * instances.get(0)); criteria.setVm(new VM(null, "hostname", "domain")); instances =
     * productInstanceDao.findByCriteria(criteria); assertEquals(1, instances.size()); assertEquals(pi1,
     * instances.get(0)); criteria.setProductName("asd"); instances = productInstanceDao.findByCriteria(criteria);
     * assertEquals(0, instances.size()); try { productInstanceDao.findUniqueByCriteria(criteria);
     * fail("NotUniqueResultException expected"); } catch (NotUniqueResultException e) { // it's ok, exception expected
     * } criteria.setProductName(PRODUCT_NAME); instances = productInstanceDao.findByCriteria(criteria); assertEquals(1,
     * instances.size()); assertEquals(pi1, instances.get(0)); assertEquals(pi1,
     * productInstanceDao.findUniqueByCriteria(criteria)); }
     */
    /**
     * Test the create and load method
     */
    /*
     * @Test public void testCreate1() throws Exception { //productReleaseDaoJpaImplTest.testCreate1();
     * //productDaoTest.testCreateAndFindByCriteria(); createProductRelease(); ProductRelease productRelease =
     * productReleaseDao.findAll().get(0); ProductInstance productInstance = new ProductInstance(productRelease,
     * Status.INSTALLING, new VM("ip", "hostname", "domain", "fqn"), "vdcTest");
     * productInstance.setName(PINSTANCE_NAME); productInstance = productInstanceDao.create(productInstance);
     * assertEquals(productInstance, productInstanceDao.load(productInstance.getId())); //assertEquals(1,
     * productInstanceDao.findAll().size()); }
     */
    /**
     * Test the create and load method
     */
    /*
     * @Test public void testCreate2() throws Exception { ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest =
     * new ProductReleaseDaoJpaImplTest(); productReleaseDaoJpaImplTest.setOsDao(osDao);
     * productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
     * productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao); productReleaseDaoJpaImplTest.testCreate2();
     * ProductRelease productRelease = productReleaseDao.findAll().get(0); ProductInstance productInstance = new
     * ProductInstance(productRelease, Status.INSTALLING, new VM("ip2", "hostname2", "domain2", "fqn2"), "vdcTest");
     * productInstance.setName(PINSTANCE_NAME); productInstance = productInstanceDao.create(productInstance); }
     */
    /**
     * Test the create and load method
     */
    /*
     * @Test public void testFindAllAndUpdate() throws Exception { assertEquals(0, productInstanceDao.findAll().size());
     * testCreate1(); List<ProductInstance> productInstances = productInstanceDao.findAll(); assertEquals(1,
     * productInstances.size()); ProductInstance productInstance = productInstances.get(0);
     * productInstance.setStatus(Status.CONFIGURING); productInstanceDao.update(productInstance);
     * assertEquals(Status.CONFIGURING, productInstanceDao.load(productInstance.getId()).getStatus());
     * productInstanceDao.remove(productInstance); assertEquals(0, productInstanceDao.findAll().size()); }
     */

    /*
     * public void testFindByCriteria() throws Exception { testCreate1(); testCreate2(); List<ProductInstance>
     * productInstances = productInstanceDao.findAll(); assertEquals(2, productInstances.size()); ProductInstance
     * createdProductInstance1 = productInstances.get(0); ProductInstance createdProductInstance2 =
     * productInstances.get(1); ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria(); //find all
     * productInstances = productInstanceDao.findByCriteria(criteria); assertEquals(2, productInstances.size()); //find
     * by Status //List<Status> status = new ArrayList<Status>(); //status.add(Status.INSTALLING);
     * //criteria.setStatus(status); //productInstances = productInstanceDao.findByCriteria(criteria); //assertEquals(2,
     * productInstances.size()); //find by Host1 criteria.setVm(new VM("ip2", "hostname2", "domain2", "fqn2"));
     * productInstances = productInstanceDao.findByCriteria(criteria); assertEquals(1, productInstances.size()); }
     */

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param productTypeDao
     *            the productTypeDao to set
     */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }

    @Override
    @Before
    public void setUp() {
        productReleaseDao = mock(ProductReleaseDao.class);

        productRelease = new ProductRelease("product", "version");

    }

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        /*
         * ProductRelease release = productReleaseDao.findAll().get(0); ProductInstance instance = new
         * ProductInstance(release, Status.INSTALLED, new VM("fqn", "ip", "hostname", "domain"), "vdc"); assertEquals(0,
         * productInstanceDao.findAll().size()); instance = productInstanceDao.create(instance); assertEquals(instance,
         * productInstanceDao.load(instance.getId())); assertEquals(1, productInstanceDao.findAll().size());
         */

        // ProductRelease productRelease = productReleaseDao.findAll().get(0);
        TierInstance tierinstance = new TierInstance();
        ProductInstanceDaoJpaImpl productInstanceDao = new ProductInstanceDaoJpaImpl();
        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdcTest");
        productInstance.setName(PINSTANCE_NAME);

        // productInstance = productInstanceDao.create(productInstance);
        // assertEquals(productInstance,
        // productInstanceDao.load(productInstance.getId()));

    }
}
