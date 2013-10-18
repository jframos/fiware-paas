/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;


public class ApplicationInstanceDaoJpaImplTest extends AbstractJpaDaoTest {
    // implements ApplicationInstanceDao {

    /*
     * private ApplicationInstanceDao applicationInstanceDao; private
     * ApplicationReleaseDao applicationReleaseDao; private ApplicationTypeDao
     * applicationTypeDao; private EnvironmentTypeDao environmentTypeDao;
     * private ProductReleaseDao productReleaseDao; private ProductInstanceDao
     * productInstanceDao; private ServiceDao serviceDao; private ProductTypeDao
     * productTypeDao; private TierInstanceDao tierInstanceDao; private
     * EnvironmentInstanceDao environmentInstanceDao; private EnvironmentDao
     * environmentDao; private TierDao tierDao; private OSDao osDao;
     * 
     * public ApplicationInstance create(ApplicationInstance
     * applicationInstance) throws InvalidEntityException,
     * AlreadyExistsEntityException { return
     * applicationInstanceDao.create(applicationInstance); }
     * 
     * public List<ApplicationInstance> findAll() { return
     * applicationInstanceDao.findAll(); }
     * 
     * public ApplicationInstance load(String arg0) throws
     * EntityNotFoundException { return applicationInstanceDao.load(arg0); }
     * 
     * public void remove(ApplicationInstance applicationInstance) {
     * applicationInstanceDao.remove(applicationInstance);
     * 
     * }
     * 
     * public ApplicationInstance update(ApplicationInstance
     * applicationInstance) throws InvalidEntityException { return
     * applicationInstanceDao.update(applicationInstance); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate() throws Exception {
     * ApplicationReleaseDaoJpaImplTest applicationReleaseDaoJpaImplTest = new
     * ApplicationReleaseDaoJpaImplTest();
     * applicationReleaseDaoJpaImplTest.setEnvironmentTypeDao
     * (environmentTypeDao);
     * applicationReleaseDaoJpaImplTest.setApplicationTypeDao
     * (applicationTypeDao);
     * applicationReleaseDaoJpaImplTest.setApplicationReleaseDao
     * (applicationReleaseDao); applicationReleaseDaoJpaImplTest.testCreate();
     * 
     * ApplicationRelease applicationRelease =
     * applicationReleaseDao.findAll().get(0);
     * 
     * 
     * EnvironmentInstanceDaoJpaImplTest environmentInstanceDaoJpaImplTest = new
     * EnvironmentInstanceDaoJpaImplTest();
     * environmentInstanceDaoJpaImplTest.setOsDao(osDao);
     * environmentInstanceDaoJpaImplTest.setServiceDao(serviceDao);
     * environmentInstanceDaoJpaImplTest
     * .setProductReleaseDao(productReleaseDao);
     * environmentInstanceDaoJpaImplTest
     * .setProductInstanceDao(productInstanceDao);
     * environmentInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
     * environmentInstanceDaoJpaImplTest.setTierDao(tierDao);
     * environmentInstanceDaoJpaImplTest.setTierInstanceDao(tierInstanceDao);
     * environmentInstanceDaoJpaImplTest
     * .setEnvironmentTypeDao(environmentTypeDao);
     * environmentInstanceDaoJpaImplTest
     * .setEnvironmentInstanceDao(environmentInstanceDao);
     * environmentInstanceDaoJpaImplTest.setEnvironmentDao(environmentDao);
     * environmentInstanceDaoJpaImplTest.testCreate();
     * 
     * List<EnvironmentInstance> environmentInstances =
     * environmentInstanceDao.findAll();
     * 
     * ApplicationInstance applicationInstance = new ApplicationInstance(
     * applicationRelease, environmentInstances.get(0));
     * applicationInstance.setName(applicationRelease.getName() +
     * applicationRelease.getVersion() + "Instance");
     * 
     * assertEquals(0, applicationInstanceDao.findAll().size());
     * applicationInstance = applicationInstanceDao.create(applicationInstance);
     * assertEquals(applicationInstance, applicationInstanceDao.load(
     * applicationRelease.getName() + applicationRelease.getVersion() +
     * "Instance")); assertEquals(1, applicationInstanceDao.findAll().size()); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception {
     * ApplicationReleaseDaoJpaImplTest applicationReleaseDaoJpaImplTest = new
     * ApplicationReleaseDaoJpaImplTest();
     * applicationReleaseDaoJpaImplTest.setEnvironmentTypeDao
     * (environmentTypeDao);
     * applicationReleaseDaoJpaImplTest.setApplicationTypeDao
     * (applicationTypeDao);
     * applicationReleaseDaoJpaImplTest.setApplicationReleaseDao
     * (applicationReleaseDao); applicationReleaseDaoJpaImplTest.testCreate2();
     * 
     * ApplicationRelease applicationRelease =
     * applicationReleaseDao.findAll().get(0);
     * 
     * 
     * EnvironmentInstanceDaoJpaImplTest environmentInstanceDaoJpaImplTest = new
     * EnvironmentInstanceDaoJpaImplTest();
     * environmentInstanceDaoJpaImplTest.setOsDao(osDao);
     * environmentInstanceDaoJpaImplTest.setServiceDao(serviceDao);
     * environmentInstanceDaoJpaImplTest
     * .setProductReleaseDao(productReleaseDao);
     * environmentInstanceDaoJpaImplTest
     * .setProductInstanceDao(productInstanceDao);
     * environmentInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
     * environmentInstanceDaoJpaImplTest.setTierDao(tierDao);
     * environmentInstanceDaoJpaImplTest.setTierInstanceDao(tierInstanceDao);
     * environmentInstanceDaoJpaImplTest
     * .setEnvironmentTypeDao(environmentTypeDao);
     * environmentInstanceDaoJpaImplTest
     * .setEnvironmentInstanceDao(environmentInstanceDao);
     * environmentInstanceDaoJpaImplTest.setEnvironmentDao(environmentDao);
     * environmentInstanceDaoJpaImplTest.testCreate2();
     * 
     * List<EnvironmentInstance> environmentInstances =
     * environmentInstanceDao.findAll();
     * 
     * ApplicationInstance applicationInstance = new ApplicationInstance(
     * applicationRelease, environmentInstances.get(0));
     * applicationInstance.setName(applicationRelease.getName() +
     * applicationRelease.getVersion() + "Instance2");
     * 
     * //assertEquals(1, applicationInstanceDao.findAll().size());
     * applicationInstance = applicationInstanceDao.create(applicationInstance);
     * assertEquals(applicationInstance,
     * applicationInstanceDao.load(applicationInstance.getName())); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0,
     * applicationInstanceDao.findAll().size()); testCreate();
     * List<ApplicationInstance> applicationInstances =
     * applicationInstanceDao.findAll(); assertEquals(1,
     * applicationInstances.size()); ApplicationInstance applicationInstance =
     * applicationInstances.get(0);
     * applicationInstance.setStatus(Status.CONFIGURING);
     * applicationInstanceDao.update(applicationInstance);
     * assertEquals(Status.CONFIGURING, applicationInstanceDao.load(
     * applicationInstance.getName()).getStatus());
     * applicationInstanceDao.remove(applicationInstance); assertEquals(0,
     * applicationInstanceDao.findAll().size()); }
     * 
     * public void testFindByCriteria() throws Exception { testCreate();
     * testCreate2();
     * 
     * List<ApplicationInstance> applicationInstances =
     * applicationInstanceDao.findAll(); assertEquals(2,
     * applicationInstances.size());
     * 
     * ApplicationInstance createdApplicationInstance1 =
     * applicationInstances.get(0); ApplicationInstance
     * createdApplicationInstance2 = applicationInstances.get(1);
     * 
     * 
     * ApplicationInstanceSearchCriteria criteria = new
     * ApplicationInstanceSearchCriteria(); //find all applicationInstances =
     * applicationInstanceDao.findByCriteria(criteria); assertEquals(4,
     * applicationInstances.size());
     * 
     * }
     * 
     * public List<ApplicationInstance> findByCriteria(
     * ApplicationInstanceSearchCriteria criteria) { // TODO Auto-generated
     * method stub return null; }
     */

    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    /*
     * public void setApplicationInstanceDao(ApplicationInstanceDao
     * applicationInstanceDao) { this.applicationInstanceDao =
     * applicationInstanceDao; }
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
     * @param productTypeDao
     *            the productTypeDao to set
     */
    /*
     * public void setProductTypeDao(ProductTypeDao productTypeDao) {
     * this.productTypeDao = productTypeDao; }
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
     * @param serviceDao
     *            the serviceDao to set
     */
    /*
     * public void setServiceDao(ServiceDao serviceDao) { this.serviceDao =
     * serviceDao; }
     */

    /**
     * @param applicationReleaseDao
     *            the applicationReleaseDao to set
     */
    /*
     * public void setApplicationReleaseDao(ApplicationReleaseDao
     * applicationReleaseDao) { this.applicationReleaseDao =
     * applicationReleaseDao; }
     */

    /**
     * @param applicationTypeDao
     *            the applicationTypeDao to set
     */
    /*
     * public void setApplicationTypeDao(ApplicationTypeDao applicationTypeDao)
     * { this.applicationTypeDao = applicationTypeDao; }
     */

    /**
     * @param osDao
     *            the osDao to set
     */
    /*
     * public void setOsDao(OSDao osDao) { this.osDao = osDao; }
     */

    /**
     * @param tierInstanceDao
     *            the tierInstanceDao to set
     */
    /*
     * public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
     * this.tierInstanceDao = tierInstanceDao; }
     */

    /**
     * @param environmentTypeDao
     *            the environmentTypeDao to set
     */
    /*
     * public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao)
     * { this.environmentTypeDao = environmentTypeDao; }
     */

    /**
     * @param environmentDao
     *            the environmentDao to set
     */
    /*
     * public void setEnvironmentDao(EnvironmentDao environmentDao) {
     * this.environmentDao = environmentDao; }
     */

    /**
     * @param environmentInstanceDao
     *            the environmentInstanceDao to set
     */
    /*
     * public void setEnvironmentInstanceDao(EnvironmentInstanceDao
     * environmentInstanceDao) { this.environmentInstanceDao =
     * environmentInstanceDao; }
     */

    /**
     * @param tierDao
     *            the tierDao to set
     */
    /*
     * public void setTierDao(TierDao tierDao) { this.tierDao = tierDao; }
     */

}
