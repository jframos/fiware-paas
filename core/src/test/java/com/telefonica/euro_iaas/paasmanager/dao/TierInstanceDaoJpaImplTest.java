package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.Date;
import java.util.List;


import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;

/**
 * Unit test for TierInstanceDaoJpaImpl
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
    private TierInstanceDao tierInstanceDao;
	
    public final static String TIERINSTANCE_NAME = "tierInstanceName";
    public final static String TIERINSTANCE2_NAME = "tierInstance2Name";
    public final static String PINSTANCE_NAME = "pinstanceName";
    public final static String PINSTANCE2_NAME = "pinstance2Name";
    
       
    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
    	ProductInstanceDaoJpaImplTest productInstanceDaoJpaImplTest 
    		= new ProductInstanceDaoJpaImplTest();
    	productInstanceDaoJpaImplTest.setOsDao(osDao);
    	productInstanceDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	productInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	productInstanceDaoJpaImplTest.setProductInstanceDao(productInstanceDao);
    	productInstanceDaoJpaImplTest.testCreate1();
    
    	ProductRelease productRelease = productReleaseDao.findAll().get(0);
    
    	TierDaoJpaImplTest tierDaoJpaImplTest  = new TierDaoJpaImplTest();
    	tierDaoJpaImplTest.setOsDao(osDao);
    	tierDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	tierDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	tierDaoJpaImplTest.setServiceDao(serviceDao);
    	tierDaoJpaImplTest.setTierDao(tierDao);
    	tierDaoJpaImplTest.testCreate1();

    	Tier tier = tierDao.findAll().get(0);
    
    	List<ProductInstance> productInstances = productInstanceDao.findAll();

    	
    	TierInstance tierInstance = new TierInstance(tier, productInstances);
    	tierInstance.setName("tierInstance1");
    	tierInstance.setStatus(Status.INSTALLED);
    	
    	tierInstance = tierInstanceDao.create(tierInstance);
    	assertEquals(tierInstance, tierInstanceDao.load(tierInstance.getId()));
        //assertEquals(1, tierInstanceDao.findAll().size());
    }
    
    /**
     * Test the create and load method
     */
    public void testCreate2() throws Exception {
    	ProductInstanceDaoJpaImplTest productInstanceDaoJpaImplTest 
    		= new ProductInstanceDaoJpaImplTest();
    	productInstanceDaoJpaImplTest.setOsDao(osDao);
    	productInstanceDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	productInstanceDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	productInstanceDaoJpaImplTest.setProductInstanceDao(productInstanceDao);
    	productInstanceDaoJpaImplTest.testCreate2();
    
    	ProductRelease productRelease = productReleaseDao.findAll().get(0);
    
    	TierDaoJpaImplTest tierDaoJpaImplTest  = new TierDaoJpaImplTest();
    	tierDaoJpaImplTest.setOsDao(osDao);
    	tierDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	tierDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	tierDaoJpaImplTest.setServiceDao(serviceDao);
    	tierDaoJpaImplTest.setTierDao(tierDao);
    	tierDaoJpaImplTest.testCreate2();

    	Tier tier = tierDao.findAll().get(0);
    
    	List<ProductInstance> productInstances = productInstanceDao.findAll();

    	
    	TierInstance tierInstance = new TierInstance(tier, productInstances);
    	tierInstance.setName("tierInstance2");
    	tierInstance.setStatus(Status.INSTALLED);
    	
    	tierInstance = tierInstanceDao.create(tierInstance);
    }
    
    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, tierInstanceDao.findAll().size());
        testCreate();
        List<TierInstance> tierInstances = tierInstanceDao.findAll();
        assertEquals(1, tierInstances.size());
        TierInstance tierInstance = tierInstances.get(0);
        tierInstance.setDate(new Date(86,12,19));
        tierInstanceDao.update(tierInstance);
        assertEquals(new Date(86,12,19), 
        		tierInstanceDao.load(tierInstance.getId()).getDate());
        tierInstanceDao.remove(tierInstance);
        assertEquals(0, tierInstanceDao.findAll().size());
    }
    
    
    public void testFindByCriteria() throws Exception {  	
    	assertEquals(0, tierInstanceDao.findAll().size());
    	
    	testCreate();
    	testCreate2();
    	
    	List<TierInstance> tierInstances = tierInstanceDao.findAll();
    	assertEquals(2, tierInstanceDao.findAll().size());  	 
    	
    	TierInstance createdTierInstance1 = tierInstances.get(0);
    	TierInstance createdTierInstance2 = tierInstances.get(0);
       
        TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
        
        //find all
        tierInstances = tierInstanceDao.findByCriteria(criteria);
        assertEquals(2, tierInstances.size());
        
        //find by productInstance
        /*criteria.setProductInstance(productInstanceDao.findAll().get(0));
        tierInstances = tierInstanceDao.findByCriteria(criteria);
        assertEquals(2, tierInstances.size());*/
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
}

