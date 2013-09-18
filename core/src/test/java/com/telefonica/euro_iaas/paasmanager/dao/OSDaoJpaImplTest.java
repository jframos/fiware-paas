package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.OS;

/**
 * Unit test for SODaoJpaImpl
 * @author Jesus M. Movilla
 *
 */
public class OSDaoJpaImplTest extends AbstractJpaDaoTest {

    private OSDao osDao;

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "OSTypeSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        OS so = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
        assertNull(so.getId());
        List<OS> oss = osDao.findAll();
        
        OS createdSO;
        if (oss.size() == 0)
        	createdSO = osDao.create(so);
        else
        	createdSO = oss.get(0);
        
        assertNotNull(createdSO.getOsType());
        assertEquals(so.getName(), createdSO.getName());
        assertEquals(so.getDescription(), createdSO.getDescription());

        OS findSo = osDao.load(createdSO.getOsType());
        assertEquals(createdSO, findSo);
    }

    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, osDao.findAll().size());
        testCreate();
        List<OS> ssoo = osDao.findAll();
        assertEquals(1, ssoo.size());
        OS os = ssoo.get(0);
        os.setName("newName");
        osDao.update(os);
        assertEquals("newName", osDao.load(os.getOsType()).getName());
        osDao.remove(os);
        assertEquals(0, osDao.findAll().size());


    }

    /**
     * @param soDao the osDao to set
     */
    public void setSoDao(OSDao osDao) {
        this.osDao = osDao;
    }


}
