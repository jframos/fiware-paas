package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.Date;
import java.util.List;


import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;

/**
 * Unit test forEnvironmentInstanceDaoJpaImpl
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
    private EnvironmentTypeDao environmentTypeDao;
    private EnvironmentInstanceDao environmentInstanceDao;
    
    public final static String ENVINSTANCE_NAME = "envInstanceName";
    public final static String ENVINSTANCE2_NAME = "envInstance2Name";
     
    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
    	TierInstanceDaoJpaImplTest tierInstanceDaoJpaImplTest 
    		= new TierInstanceDaoJpaImplTest();
    	tierInstanceDaoJpaImplTest.setOsDao(osDao);
    	tierInstanceDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	tierInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	tierInstanceDaoJpaImplTest.setProductInstanceDao(productInstanceDao);
    	tierInstanceDaoJpaImplTest.setServiceDao(serviceDao);
    	tierInstanceDaoJpaImplTest.setTierDao(tierDao);
    	tierInstanceDaoJpaImplTest.setTierInstanceDao(tierInstanceDao);
    	tierInstanceDaoJpaImplTest.testCreate();
    
    	List<TierInstance> tierInstances = tierInstanceDao.findAll();
    
    	EnvironmentDaoJpaImplTest environmentDaoJpaImplTest  = new EnvironmentDaoJpaImplTest();
    	environmentDaoJpaImplTest.setOsDao(osDao);
    	environmentDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	environmentDaoJpaImplTest.setServiceDao(serviceDao);
    	environmentDaoJpaImplTest.setTierDao(tierDao);
    	environmentDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	environmentDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
    	environmentDaoJpaImplTest.setEnvironmentDao(environmentDao);
    	environmentDaoJpaImplTest.testCreate1();
    	
    	Environment environment = environmentDao.findAll().get(0);
     	
    	EnvironmentInstance environmentInstance 
    		= new EnvironmentInstance(environment, tierInstances);
    	environmentInstance.setName(ENVINSTANCE_NAME);
    	environmentInstance.setStatus(Status.INSTALLED);
    	
    	environmentInstance = environmentInstanceDao.create(environmentInstance);
        assertEquals(1, environmentInstanceDao.findAll().size());
    }
    
    /**
     * Test the create and load method
     */
    public void testCreate2() throws Exception {

    	TierInstanceDaoJpaImplTest tierInstanceDaoJpaImplTest 
		= new TierInstanceDaoJpaImplTest();
	tierInstanceDaoJpaImplTest.setOsDao(osDao);
	tierInstanceDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
	tierInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
	tierInstanceDaoJpaImplTest.setProductInstanceDao(productInstanceDao);
	tierInstanceDaoJpaImplTest.setServiceDao(serviceDao);
	tierInstanceDaoJpaImplTest.setTierDao(tierDao);
	tierInstanceDaoJpaImplTest.setTierInstanceDao(tierInstanceDao);
	tierInstanceDaoJpaImplTest.testCreate2();

	List<TierInstance> tierInstances = tierInstanceDao.findAll();

	EnvironmentDaoJpaImplTest environmentDaoJpaImplTest  = new EnvironmentDaoJpaImplTest();
	environmentDaoJpaImplTest.setOsDao(osDao);
	environmentDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
	environmentDaoJpaImplTest.setServiceDao(serviceDao);
	environmentDaoJpaImplTest.setTierDao(tierDao);
	environmentDaoJpaImplTest.setProductTypeDao(productTypeDao);
	environmentDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
	environmentDaoJpaImplTest.setEnvironmentDao(environmentDao);
	environmentDaoJpaImplTest.testCreate2();
	
	Environment environment = environmentDao.findAll().get(0);
 	
	EnvironmentInstance environmentInstance 
		= new EnvironmentInstance(environment, tierInstances);
	environmentInstance.setName(ENVINSTANCE2_NAME);
	environmentInstance.setStatus(Status.INSTALLED);
	
	environmentInstance = environmentInstanceDao.create(environmentInstance);
    //assertEquals(2, environmentInstanceDao.findAll().size());
    }
    
    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, environmentInstanceDao.findAll().size());
        testCreate();
        List<EnvironmentInstance> environmentInstances 
        	= environmentInstanceDao.findAll();
        assertEquals(1, environmentInstances.size());
        EnvironmentInstance environmentInstance = environmentInstances.get(0);
        //environmentInstance.setDate(new Date(86,12,19));
        environmentInstanceDao.update(environmentInstance);
        assertEquals(environmentInstance.getName(), 
        		environmentInstanceDao.load(environmentInstance.getName()).getName());
        environmentInstanceDao.remove(environmentInstance);
        assertEquals(0, environmentInstanceDao.findAll().size());
    }
    
    
    public void testFindByCriteria() throws Exception {  	
    	assertEquals(0, tierInstanceDao.findAll().size());
    	
    	testCreate();
    	testCreate2();
    	
    	List<EnvironmentInstance> environmentInstances = environmentInstanceDao.findAll();
    	assertEquals(2, environmentInstanceDao.findAll().size());  	 
    	
    	EnvironmentInstance createdEnvironmentInstance1 
    		= environmentInstances.get(0);
    	EnvironmentInstance createdEnvironmentInstance2 
    		= environmentInstances.get(0);
       
        TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
        
        //find all
        //environmentInstances = environmentInstanceDao.findByCriteria(criteria);
        //assertEquals(2, environmentInstances.size());       
    }
    
    /**
     * @param tierInstanceDao the tierInstanceDao to set
     */
    public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
        this.tierInstanceDao = tierInstanceDao;
    }
    
    /**
     * @param productInstanceDao the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param osDao the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }
    
    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param serviceDao the serviceDao to set
     */
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }
    /**
     * @param tierDao the tierDao to set
     */
    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }

    /**
     * @param productTypeDao the productTypeDao to set
     */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }
    
    /**
     * @param environmentDao the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param environmentInstanceDao the environmentInstanceDao to set
     */
    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }
    
    /**
     * @param environmentTypeDao the environmentTypeDao to set
     */
    public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) {
        this.environmentTypeDao = environmentTypeDao;
    }
}
