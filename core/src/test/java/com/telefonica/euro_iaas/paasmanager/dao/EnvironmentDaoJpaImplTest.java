package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

/**
 * Unit test for EnvironmentDaoJpaImplTest
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    private TierDao tierDao;
    private ProductTypeDao productTypeDao;
    private ServiceDao serviceDao;
    private EnvironmentDao environmentDao;
    private EnvironmentTypeDao environmentTypeDao;

  
    public final static String ENVIRONMENT_NAME = "EnvironmentName";
    public final static String ENVIRONMENT2_NAME = "Environment2Name";

    /**
     * Test the create and load method
     */
    public void testCreate1() throws Exception {
    	TierDaoJpaImplTest tierDaoJpaImplTest  = new TierDaoJpaImplTest();
    	tierDaoJpaImplTest.setOsDao(osDao);
    	tierDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	tierDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	tierDaoJpaImplTest.setServiceDao(serviceDao);
    	tierDaoJpaImplTest.setTierDao(tierDao);
    	tierDaoJpaImplTest.testCreate1();

    	Tier tier = tierDao.findAll().get(0);
    	List <Tier> tiers = new ArrayList<Tier> ();
    	
    	EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest  
    		= new EnvironmentTypeDaoJpaImplTest();
    	environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
    	
    	List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();
    	
    	EnvironmentType environmentType;
    	if (environmentTypes.size() == 0)
    		environmentType 
    		= environmentTypeDaoJpaImplTest.create(new EnvironmentType ("Env1Name", "Env1Desc"));
    	else
    		environmentType = environmentTypes.get(0);
    	
    	Environment environment = new Environment (ENVIRONMENT_NAME
    			, environmentType
    			, tiers);
    	
    	Environment createdEnvironment = environmentDao.create(environment);    	
    	assertNotNull(createdEnvironment.getName());
        assertEquals(createdEnvironment.getName(), environment.getName());     
    }
    
    public void testCreate2() throws Exception {
    	TierDaoJpaImplTest tierDaoJpaImplTest  = new TierDaoJpaImplTest();
    	tierDaoJpaImplTest.setOsDao(osDao);
    	tierDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
    	tierDaoJpaImplTest.setProductTypeDao(productTypeDao);
    	tierDaoJpaImplTest.setServiceDao(serviceDao);
    	tierDaoJpaImplTest.setTierDao(tierDao);
    	tierDaoJpaImplTest.testCreate2();

    	Tier tier = tierDao.findAll().get(0);
    	List <Tier> tiers = new ArrayList<Tier> ();
    	
    	EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest  
    		= new EnvironmentTypeDaoJpaImplTest();
    	environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
    	
    	List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();
    	
    	EnvironmentType environmentType;
    	if (environmentTypes.size() < 2)
    		environmentType 
    		= environmentTypeDaoJpaImplTest.create(new EnvironmentType ("Env3Name", "Env3Desc"));
    	else
    		environmentType = environmentTypes.get(1);
    	
    	Environment environment = new Environment (ENVIRONMENT2_NAME
    			, environmentType
    			, tiers);
    	
    	Environment createdEnvironment = environmentDao.create(environment);    	
    	assertNotNull(createdEnvironment.getName());
        assertEquals(createdEnvironment.getName(), environment.getName());  
    }

    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, environmentDao.findAll().size());
        testCreate1();
        List<Environment> environments = environmentDao.findAll();
        assertEquals(1, environments.size());
        Environment environment = environments.get(0);
        environmentDao.update(environment);
        environmentDao.remove(environment);
        assertEquals(0, environmentDao.findAll().size());
    }

    public void testFindByCriteria() throws Exception {
    	testCreate1();
    	testCreate2();
    	
        List<Environment> environments = environmentDao.findAll();
        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
        
        //findByCriteria
        environments = environmentDao.findByCriteria(criteria);
        assertEquals(2, environments.size());
        
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
     * @param environmentDao the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }
    
    /**
     * @param environmentTypeDao the environmentTypeDao to set
     */
    public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) {
        this.environmentTypeDao = environmentTypeDao;
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