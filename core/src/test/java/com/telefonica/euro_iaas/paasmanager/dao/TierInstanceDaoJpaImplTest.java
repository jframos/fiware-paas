/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;


/**
 * Unit test for TierInstanceDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 * 
 */
public class TierInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductInstanceDao productInstanceDao;
    private ProductTypeDao productTypeDao;
    private TierDao tierDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ServiceDao serviceDao;

    public final static String TIERINSTANCE_NAME = "tierInstanceName";
    public final static String TIERINSTANCE2_NAME = "tierInstance2Name";
    public final static String PINSTANCE_NAME = "pinstanceName";
    public final static String PINSTANCE2_NAME = "pinstance2Name";

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate() throws Exception {
     * 
     * TierInstanceDaoJpaImpl tierInstanceDao = new TierInstanceDaoJpaImpl();
     * 
     * EntityManager entityManager = mock (EntityManager.class);
     * tierInstanceDao.setEntityManager(entityManager);
     * 
     * 
     * 
     * Tier tier = new Tier ();
     * 
     * List<ProductInstance> productInstances = new ArrayList<ProductInstance>
     * (); productInstances.add(new ProductInstance ());
     * 
     * TierInstance tierInstance = new TierInstance();
     * tierInstance.setName("tierInstance1");
     * tierInstance.setStatus(Status.INSTALLED); tierInstance.setTier(tier);
     * tierInstance.setProductInstances(productInstances);
     * 
     * tierInstance = tierInstanceDao.create(tierInstance);
     * assertEquals(tierInstance, tierInstanceDao.load(tierInstance.getName()));
     * //assertEquals(1, tierInstanceDao.findAll().size()); }
     * 
     * /** Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception { TierInstanceDaoJpaImpl
     * tierInstanceDao = new TierInstanceDaoJpaImpl();
     * 
     * ProductRelease productRelease = productReleaseDao.findAll().get(0);
     * 
     * 
     * 
     * Tier tier = tierDao.findAll().get(0);
     * 
     * List<ProductInstance> productInstances = productInstanceDao.findAll();
     * 
     * 
     * TierInstance tierInstance = new TierInstance();
     * tierInstance.setName("tierInstance2");
     * tierInstance.setStatus(Status.INSTALLED); tierInstance.setTier(tier);
     * tierInstance.setProductInstances(productInstances); tierInstance =
     * tierInstanceDao.create(tierInstance); }
     * 
     * /** Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception {
     * TierInstanceDaoJpaImpl tierInstanceDao = new TierInstanceDaoJpaImpl();
     * assertEquals(0, tierInstanceDao.findAll().size()); testCreate();
     * List<TierInstance> tierInstances = tierInstanceDao.findAll();
     * assertEquals(1, tierInstances.size()); TierInstance tierInstance =
     * tierInstances.get(0); tierInstance.setDate(new Date(86,12,19));
     * tierInstanceDao.update(tierInstance); assertEquals(new Date(86,12,19),
     * tierInstanceDao.load(tierInstance.getName()).getDate());
     * tierInstanceDao.remove(tierInstance); assertEquals(0,
     * tierInstanceDao.findAll().size()); }
     * 
     * 
     * public void testFindByCriteria() throws Exception {
     * TierInstanceDaoJpaImpl tierInstanceDao = new TierInstanceDaoJpaImpl();
     * assertEquals(0, tierInstanceDao.findAll().size());
     * 
     * testCreate(); testCreate2();
     * 
     * List<TierInstance> tierInstances = tierInstanceDao.findAll();
     * assertEquals(2, tierInstanceDao.findAll().size());
     * 
     * TierInstance createdTierInstance1 = tierInstances.get(0); TierInstance
     * createdTierInstance2 = tierInstances.get(0);
     * 
     * TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
     * 
     * //find all tierInstances = tierInstanceDao.findByCriteria(criteria);
     * assertEquals(2, tierInstances.size());
     * 
     * }
     * 
     * /**
     * 
     * @param tierInstanceDao the tierInstanceDao to set
     */
    /*
     * public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
     * this.tierInstanceDao = tierInstanceDao; }
     */

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    /*
     * public void setProductInstanceDao(ProductInstanceDao productInstanceDao)
     * { this.productInstanceDao = productInstanceDao; }
     */

    /**
     * @param osDao
     *            the osDao to set
     */
    /*
     * public void setOsDao(OSDao osDao) { this.osDao = osDao; }
     */

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    /*
     * public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
     * this.productReleaseDao = productReleaseDao; }
     */

    /**
     * @param serviceDao
     *            the serviceDao to set
     */
    /*
     * public void setServiceDao(ServiceDao serviceDao) { this.serviceDao =
     * serviceDao; }
     */
    /**
     * @param tierDao
     *            the tierDao to set
     */
    /*
     * public void setTierDao(TierDao tierDao) { this.tierDao = tierDao; }
     */

    /**
     * @param productTypeDao
     *            the productTypeDao to set
     */
    /*
     * public void setProductTypeDao(ProductTypeDao productTypeDao) {
     * this.productTypeDao = productTypeDao; }
     */
}
