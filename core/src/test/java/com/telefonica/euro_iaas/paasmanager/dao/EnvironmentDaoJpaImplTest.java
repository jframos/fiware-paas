package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
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
  /*  public void testCreate1() throws Exception {
    	

    	Tier tier = tierDao.findAll().get(0);
    	List <Tier> tiers = new ArrayList<Tier> ();
    	
    	
    	
    	List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();
    	
    	EnvironmentType environmentType;
    	
    	
    	Environment environment = new Environment (ENVIRONMENT_NAME
    			, environmentType
    			, tiers);
    	
    	Environment createdEnvironment = environmentDao.create(environment);    	
    	assertNotNull(createdEnvironment.getName());
        assertEquals(createdEnvironment.getName(), environment.getName());     
    }
    
    public void testCreate2() throws Exception {
    	
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
 /*   public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, environmentDao.findAll().size());
        testCreate1();
        List<Environment> environments = environmentDao.findAll();
        assertEquals(1, environments.size());
        Environment environment = environments.get(0);
        environmentDao.update(environment);
        environmentDao.remove(environment);
        assertEquals(0, environmentDao.findAll().size());
    }

    /**
     * Test the recovery of a full environment object
     */
   /* public void testRecoverFullEnvironmentObject() throws Exception {
        System.out.println("**********testRecoverFullEnvironmentObject.START***");
    	testCreate1();
        //Environment environment = environmentDao.load(ENVIRONMENT_NAME);
        Environment environment = environmentDao.findAll().get(0);
    	List<Tier> tiers = environment.getTiers();
        //assertEquals(1, tiers.size());
        
        for (int i=0; i < tiers.size(); i++) {
        	List<ProductRelease> productReleases = tiers.get(i).getProductReleases();
        	assertEquals(1, productReleases.size());
            System.out.println(" tier[" + i + "]" + tiers.get(i).getName());
    		
        	for (int j=0; j < productReleases.size(); j++) {
        		ProductRelease pRelease = productReleases.get(j);
        		System.out.println(" pRelease[" + j + "]" + pRelease.getProduct());
        		List<OS> supportedOOSS = 
        				pRelease.getSupportedOOSS();    		
        		assertEquals(1, supportedOOSS.size());
                
        		for (int k=0; k < supportedOOSS.size(); k++) {
        			System.out.println(" OS[" + k + "]" + supportedOOSS.get(k).getName());
        		}
            	
        	}
        }
        assertEquals(1, environmentDao.findAll().size());
        System.out.println("**********testRecoverFullEnvironmentObject.FINISH***");
    	
    }
    
    public void testFindByCriteria() throws Exception {
    	testCreate1();
    	testCreate2();
    	
        List<Environment> environments = environmentDao.findAll();
        EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
        
        //findByCriteria
        environments = environmentDao.findByCriteria(criteria);
        assertEquals(2, environments.size());
        
    }*/
 
   
}