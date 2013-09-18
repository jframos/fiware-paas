package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;

/**
 * Unit test for ProductReleaseDaoJpaImpl
 * @author Jesus M. Movilla
 *
 */
public class ProductReleaseDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    private ProductTypeDao productTypeDao;

    public final static String PR_NAME = "ProductReleaseName";
    public final static String PR_VERSION = "ProductReleaseVersion";
    public final static String PR_DESCRIPTION = "ProductReleaseDescription";
    
    public final static String PR2_NAME = "Productrelease2Name";
    public final static String PR2_VERSION = "ProductRelease2Version";
    
    /**
     * Test the create and load method
     */
    public void testCreate1() throws Exception{
        OSDaoJpaImplTest osDaoJpaImplTest = new OSDaoJpaImplTest();
        osDaoJpaImplTest.setSoDao(osDao);
        osDaoJpaImplTest.testCreate();
        
        ProductTypeDaoJpaImplTest productTypeDaoJpaImplTest = new ProductTypeDaoJpaImplTest();
        productTypeDaoJpaImplTest.setProductTypeDao(productTypeDao);
        List<ProductType> productTypes = productTypeDao.findAll();
        
        ProductType productType;
        if (productTypes.size() == 0)
        	productType= 
        		productTypeDaoJpaImplTest.create(new ProductType("PRODUCT_TYPE", "PRODUCT_DESCRIPTION"));
        else
        	productType = productTypes.get(0);
        
        List<OS> supportedOOSS = osDao.findAll();
        
    	ProductRelease productRelease = new ProductRelease(PR_NAME, PR_VERSION);
    			
    	assertNotNull(productRelease.getId());
    	
    	productRelease.setDescription(PR_DESCRIPTION);
    	productRelease.setSupportedOOSS(supportedOOSS);
    	productRelease.setProductType(productType);
    	
    	List<ProductRelease> productReleases = productReleaseDao.findAll();
    	
    	ProductRelease createdProductRelease;
    	if (productReleases.size() == 0 )	
    		createdProductRelease = productReleaseDao.create(productRelease);
    	else
    		createdProductRelease = productReleases.get(0);
    	
    	assertNotNull(createdProductRelease.getId());
        assertEquals(productRelease.getId(), createdProductRelease.getId());  
    }
    
    /**
     * Test the create and load method
     */
    public void testCreate2() throws Exception {
        
        List<OS> supportedOOSS = osDao.findAll();       
    	ProductRelease productRelease = new ProductRelease(PR2_NAME, PR2_VERSION);   			
    	assertNotNull(productRelease.getId());
    	
    	ProductTypeDaoJpaImplTest productTypeDaoJpaImplTest = new ProductTypeDaoJpaImplTest();
        productTypeDaoJpaImplTest.setProductTypeDao(productTypeDao);
        
        ProductType productType;
        if (productTypeDaoJpaImplTest.findAll().size() == 0)
        	productType = 
        			productTypeDaoJpaImplTest.create(new ProductType("PRODUCT_TYPE", "PRODUCT_DESCRIPTION"));
        else
        	productType = productTypeDaoJpaImplTest.findAll().get(0);
        
    	productRelease.setDescription(PR_DESCRIPTION);
    	productRelease.setProductType(productType);
    	productRelease.setSupportedOOSS(supportedOOSS);  	
    	
    	List<ProductRelease> productReleases = productReleaseDao.findAll();
    	
    	ProductRelease createdProductRelease;
    	if (productReleases.size() == 0 )	
    		createdProductRelease = productReleaseDao.create(productRelease);
    	else
    		createdProductRelease = productReleases.get(0);
    	
    	assertNotNull(createdProductRelease.getId());
   }
    
    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, productReleaseDao.findAll().size());
        testCreate1();
        List<ProductRelease> productReleases = productReleaseDao.findAll();
        assertEquals(1, productReleases.size());
        ProductRelease productRelease = productReleases.get(0);
        productRelease.setDescription("newDescription");
        productReleaseDao.update(productRelease);
        assertEquals("newDescription", productReleaseDao.load(productRelease.getName()).getDescription());
        productReleaseDao.remove(productRelease);
        assertEquals(0, productReleaseDao.findAll().size());
    }

    public void testFindByCriteria() throws Exception {
    	
    	testCreate1();  	
    	testCreate2();
    	
        ProductReleaseSearchCriteria criteria =
            new ProductReleaseSearchCriteria();
        //find all
        List<ProductRelease> productReleases =
            productReleaseDao.findByCriteria(criteria);
        
        assertEquals(1, productReleases.size());
        
        //find by OSType
        criteria.setOSType(productReleases.get(0).getSupportedOOSS().get(0).getOsType());
        productReleases = productReleaseDao.findByCriteria(criteria);
        assertEquals(1, productReleases.size());
        
        //find by productName
        criteria.setProduct(PR_NAME);
        productReleases = productReleaseDao.findByCriteria(criteria);
        assertEquals(1, productReleases.size());
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
     * @param productTypeDao the productTypeDao to set
    */
    public void setProductTypeDao(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }

}
