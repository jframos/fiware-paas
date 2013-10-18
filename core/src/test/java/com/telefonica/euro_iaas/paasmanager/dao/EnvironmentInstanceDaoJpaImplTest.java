/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.impl.EnvironmentInstanceDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

/**
 * Unit test forEnvironmentInstanceDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentInstanceDaoJpaImplTest extends AbstractJpaDaoTest {


    private ProductInstanceDao productInstanceDao;
    private ProductTypeDao productTypeDao;
    private TierDao tierDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ServiceDao serviceDao;
    private TierInstanceDao tierInstanceDao;
    private EnvironmentDao environmentDao;
    private EnvironmentInstanceDaoJpaImpl environmentInstanceDao;

    public final static String ENVINSTANCE_NAME = "envInstanceName";
    public final static String ENVINSTANCE2_NAME = "envInstance2Name";

    /**
     * Test the create and load method
     */


    @Test
    public void testCreate() throws Exception {
        environmentInstanceDao = new EnvironmentInstanceDaoJpaImpl();



        // Environment environment = environmentDao.findAll().get(0);

        EnvironmentInstance environmentInstance = new
        EnvironmentInstance(ENVINSTANCE_NAME, "Description");
        environmentInstance.setName(ENVINSTANCE_NAME);
        environmentInstance.setStatus(Status.INSTALLED);

        // environmentInstance = environmentInstanceDao.create(environmentInstance);
        // assertEquals(1, environmentInstanceDao.findAll().size());
    }

    /** Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception {
     * 
     * TierInstanceDaoJpaImplTest tierInstanceDaoJpaImplTest = new
     * TierInstanceDaoJpaImplTest();
     * 
     * 
     * 
     * List<TierInstance> tierInstances = tierInstanceDao.findAll();
     * 
     * EnvironmentDaoJpaImplTest environmentDaoJpaImplTest = new
     * EnvironmentDaoJpaImplTest(); environmentDaoJpaImplTest.setOsDao(osDao);
     * environmentDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
     * environmentDaoJpaImplTest.setServiceDao(serviceDao);
     * environmentDaoJpaImplTest.setTierDao(tierDao);
     * environmentDaoJpaImplTest.setProductTypeDao(productTypeDao);
     * environmentDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
     * environmentDaoJpaImplTest.setEnvironmentDao(environmentDao);
     * environmentDaoJpaImplTest.testCreate2();
     * 
     * Environment environment = environmentDao.findAll().get(0);
     * 
     * EnvironmentInstance environmentInstance = new
     * EnvironmentInstance(environment, tierInstances);
     * environmentInstance.setName(ENVINSTANCE2_NAME);
     * environmentInstance.setStatus(Status.INSTALLED);
     * 
     * environmentInstance = environmentInstanceDao.create(environmentInstance);
     * //assertEquals(2, environmentInstanceDao.findAll().size()); }
     * 
     * /** Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0,
     * environmentInstanceDao.findAll().size()); testCreate();
     * List<EnvironmentInstance> environmentInstances =
     * environmentInstanceDao.findAll(); assertEquals(1,
     * environmentInstances.size()); EnvironmentInstance environmentInstance =
     * environmentInstances.get(0); //environmentInstance.setDate(new
     * Date(86,12,19)); environmentInstanceDao.update(environmentInstance);
     * assertEquals(environmentInstance.getName(),
     * environmentInstanceDao.load(environmentInstance.getName()).getName());
     * environmentInstanceDao.remove(environmentInstance); assertEquals(0,
     * environmentInstanceDao.findAll().size()); }
     * 
     * 
     * public void testFindByCriteria() throws Exception { assertEquals(0,
     * tierInstanceDao.findAll().size());
     * 
     * testCreate(); testCreate2();
     * 
     * List<EnvironmentInstance> environmentInstances =
     * environmentInstanceDao.findAll(); assertEquals(2,
     * environmentInstanceDao.findAll().size());
     * 
     * EnvironmentInstance createdEnvironmentInstance1 =
     * environmentInstances.get(0); EnvironmentInstance
     * createdEnvironmentInstance2 = environmentInstances.get(0);
     * 
     * TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
     * 
     * ////find all //environmentInstances =
     * environmentInstanceDao.findByCriteria(criteria); //assertEquals(2,
     * environmentInstances.size()); }
     * 
     * public void testLoadByName() throws Exception {
     * 
     * testCreate(); List<EnvironmentInstance> environmentInstances =
     * environmentInstanceDao.findAll(); assertEquals(1,
     * environmentInstanceDao.findAll().size());
     * System.out.println("EvironmetName 1 " +
     * environmentInstances.get(0).getName());
     * 
     * EnvironmentInstance environentInstance =
     * environmentInstanceDao.load(ENVINSTANCE_NAME);
     * assertEquals(ENVINSTANCE_NAME, environentInstance.getName());
     * 
     * assertEquals(2, environentInstance.getTierInstances().size());
     * System.out.println("TierInstance (0) : " +
     * environentInstance.getTierInstances().get(0).getName());
     * System.out.println("TierInstance (1) : " +
     * environentInstance.getTierInstances().get(1).getName());
     * 
     * }
     */

}
