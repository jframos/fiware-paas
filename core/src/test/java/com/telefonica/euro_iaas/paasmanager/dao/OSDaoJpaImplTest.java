package com.telefonica.euro_iaas.paasmanager.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.impl.OSDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;


/**
 * Unit test for SODaoJpaImpl
 * @author Jesus M. Movilla
 *
 */
public class OSDaoJpaImplTest extends TestCase {

    private OSDao osDao;
    private OS so1;

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "OSTypeSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    @Before
	public void setUp() throws Exception 
  
    {
    	 so1 = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
    	 List<OS> oss = new ArrayList <OS>();
    	 oss.add(so1);
    	
    	
    	
		
    }
    /**
     * Test the create and load method
     */
    
    @Test
    public void testCreate() throws Exception {
    	
    	OSDaoJpaImpl osDao = new OSDaoJpaImpl ();
    	System.out.println("Inserting OSObject in DB");  	
    	OS so = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
        assertNull(so.getId());
     //  List<OS> oss = osDao.findAll();
        
        /*OS createdSO = osDao.create(so);
        List<OS> oss = osDao.findAll();
        
        assertNotNull (createdSO);
        assertNotNull(createdSO.getOsType());
        assertEquals(so.getName(), createdSO.getName());
        assertEquals(so.getDescription(), createdSO.getDescription());

        OS findSo = osDao.load(createdSO.getOsType());
        assertEquals(createdSO, findSo);*/
    }

    /**
     * Test the create and load method
     */
    
    @Test
    public void testFindAllAndUpdate() throws Exception {
      /*  assertEquals(0, osDao.findAll().size());
        testCreate();
        List<OS> ssoo = osDao.findAll();
        assertEquals(1, ssoo.size());
        OS os = ssoo.get(0);
        os.setName("newName");
        osDao.update(os);
        assertEquals("newName", osDao.load(os.getOsType()).getName());
        osDao.remove(os);
        assertEquals(0, osDao.findAll().size());*/


    }




}
