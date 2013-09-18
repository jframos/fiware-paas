package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Service;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * Unit test for TierDaoJpaImplTest
 * @author Jesus M. Movilla
 *
 */
public class TierDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    private TierDao tierDao;
    private ServiceDao serviceDao;
    private ProductTypeDao productTypeDao;
    
    public final static String TIER_NAME = "TierName";
    public final static Integer MAXIMUM_INSTANCES = 8;
    public final static Integer MINIMUM_INSTANCES = 1;
    public final static Integer INITIAL_INSTANCES = 1;
    
    
    /**
     * Test the create and load method
     */
    public void testCreate1() throws Exception {
        
    	ServiceDaoJpaImplTest serviceDaoJpaImplTest= new ServiceDaoJpaImplTest();
    	serviceDaoJpaImplTest.setServiceDao(serviceDao);
    	
    	List<Service> services = serviceDao.findAll();
    	
    	Service service;
    	if (services.size()==0)
    		service =
    			serviceDaoJpaImplTest.create (new Service("ServiceName", "ServiceDesc", null));
    	else
    		service = serviceDao.findAll().get(0);
    	
    	ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest = new ProductReleaseDaoJpaImplTest();
    	productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	productReleaseDaoJpaImplTest.setOsDao(osDao);
    	productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	productReleaseDaoJpaImplTest.testCreate1();
    	ProductRelease productRelease = productReleaseDao.findAll().get(0);
    	
    	Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES,
    			MINIMUM_INSTANCES, INITIAL_INSTANCES,
    			service, productRelease);
    			
    	Tier createdTier = tierDao.create(tier);
    	
    	assertNotNull(createdTier.getName());
        assertEquals(createdTier.getName(), tier.getName());
       
    }
    
    /**
     * Test the create and load method
     */
    public void testCreate2() throws Exception {
        
    	ServiceDaoJpaImplTest serviceDaoJpaImplTest= new ServiceDaoJpaImplTest();
    	serviceDaoJpaImplTest.setServiceDao(serviceDao);

    	List<Service> services = serviceDao.findAll();
    	
    	Service service;
    	if (services.size()==0)
    		service =
    			serviceDaoJpaImplTest.create (new Service("ServiceName2", "ServiceDesc2", null));
    	else
    		service = serviceDao.findAll().get(0);
    	
    	ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest = new ProductReleaseDaoJpaImplTest();
    	productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	productReleaseDaoJpaImplTest.setOsDao(osDao);
    	productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	productReleaseDaoJpaImplTest.testCreate2();
    	ProductRelease productRelease2 = productReleaseDao.findAll().get(0);
    	
    	Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES,
    			MINIMUM_INSTANCES, INITIAL_INSTANCES,
    			service, productRelease2);
    			
    	Tier createdTier = tierDao.create(tier);
    	
    	assertNotNull(createdTier.getName());
        assertEquals(createdTier.getName(), tier.getName());
       
    }
    
    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
    	Integer initial_number_instances = 3;
        assertEquals(0, tierDao.findAll().size());
        testCreate1();
        List<Tier> tiers = tierDao.findAll();
        assertEquals(1, tiers.size());
        Tier tier = tiers.get(0);
        tier.setInitial_number_instances(3);
        tierDao.update(tier);
        assertEquals(initial_number_instances, tierDao.load(tier.getId()).getInitial_number_instances());
        tierDao.remove(tier);
        assertEquals(0, tierDao.findAll().size());
    }
    
    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
    
    /**
     * @param soDao the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param tierDao the tierDao to set
     */
    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }
    
    /**
     * @param serviceDao the serviceDao to set
     */
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    /**
     * @param productTypeDao the productTypeDao to set
     */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }
}
