package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Unit test for InstanceDaoJpaImpl
 * @author Jesus M. Movilla
 * 
 */
public class ProductInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductInstanceDao productInstanceDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ProductTypeDao productTypeDao;
    
    public final static String PINSTANCE_NAME = "instanceName";
    public final static String PINSTANCE2_NAME = "instance2Name";
    public final static String PINSTANCE_VERSION = "instanceVersion";
   

    /**
     * Test the create and load method
     */
   public void testCreate1() throws Exception {
	    ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest 
	    	= new ProductReleaseDaoJpaImplTest();
	    productReleaseDaoJpaImplTest.setOsDao(osDao);
	    productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
	    productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao);
	    productReleaseDaoJpaImplTest.testCreate1();
	    
	    ProductRelease productRelease = productReleaseDao.findAll().get(0);
	    
	    ProductInstance productInstance = new ProductInstance(productRelease, 
        		Status.INSTALLING, new VM("ip", "hostname", "domain", "fqn"),
        		"vdcTest");
        productInstance.setName(PINSTANCE_NAME);
        
        productInstance = productInstanceDao.create(productInstance);
        assertEquals(productInstance, productInstanceDao.load(productInstance.getId()));
        //assertEquals(1, productInstanceDao.findAll().size());
    }

   /**
    * Test the create and load method
    */
  public void testCreate2() throws Exception {
	    ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest 
	    	= new ProductReleaseDaoJpaImplTest();
	    productReleaseDaoJpaImplTest.setOsDao(osDao);
	    productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
	    productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao);
	    productReleaseDaoJpaImplTest.testCreate2();
	    
	    ProductRelease productRelease = productReleaseDao.findAll().get(0);
	    
	    ProductInstance productInstance = new ProductInstance(productRelease, 
       		Status.INSTALLING, new VM("ip2", "hostname2", "domain2", "fqn2"),
       		"vdcTest");
	    productInstance.setName(PINSTANCE_NAME);     
	    productInstance = productInstanceDao.create(productInstance);
   }
  
    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
    	assertEquals(0, productInstanceDao.findAll().size());
    	testCreate1();
        List<ProductInstance> productInstances = productInstanceDao.findAll();
        assertEquals(1, productInstances.size());
        ProductInstance productInstance = productInstances.get(0);
        productInstance.setStatus(Status.CONFIGURING);
        productInstanceDao.update(productInstance);
        assertEquals(Status.CONFIGURING, productInstanceDao.load(productInstance.getId()).getStatus());
        productInstanceDao.remove(productInstance);
        assertEquals(0, productInstanceDao.findAll().size());
    }
    
    public void testFindByCriteria() throws Exception {
        testCreate1();
        testCreate2();
        
        List<ProductInstance> productInstances = productInstanceDao.findAll();
        assertEquals(2, productInstances.size());
        
        ProductInstance createdProductInstance1 = productInstances.get(0);
        ProductInstance createdProductInstance2 = productInstances.get(1);
        
    	        
        ProductInstanceSearchCriteria criteria =
            new ProductInstanceSearchCriteria();
        //find all
        productInstances = productInstanceDao.findByCriteria(criteria);
        assertEquals(2, productInstances.size());
        
        //find by Status
        /*List<Status> status = new ArrayList<Status>();
        status.add(Status.INSTALLING);       
        criteria.setStatus(status);
        
        productInstances = productInstanceDao.findByCriteria(criteria);
        assertEquals(2, productInstances.size());*/
       
        //find by Host1
        criteria.setVm(new VM("ip2", "hostname2", "domain2", "fqn2"));
        productInstances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, productInstances.size());
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
     * @param productTypeDao the productTypeDao to set
     */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }
}