/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.model.Service;

/**
 * Unit test for ServiceDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ServiceDaoJpaImplTest {
    // implements ServiceDao {

    @Autowired
    private ServiceDao serviceDao;

    public final static String SERVICE_NAME = "ServiceName";
    public final static String SERVICE_VERSION = "ServiceVersion";
    public final static String SERVICE_DESCRIPTION = "ServiceDescription";

    public final static String SERVICE2_NAME = "Service2Name";
    public final static String SERVICE2_DESCRIPTION = "Service2Description";

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        Service service = new Service();
        service.setName(SERVICE_NAME);
        service.setDescription(SERVICE_DESCRIPTION);
        assertNull(service.getId());

        Service createdService = serviceDao.create(service);

        assertNotNull(createdService.getId());
        assertEquals(service.getId(), createdService.getId());

    }

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception { Service service = new Service(); service.setName(SERVICE2_NAME);
     * service.setDescription(SERVICE2_DESCRIPTION); assertNull(service.getId()); Service createdService =
     * serviceDao.create(service); }
     */

    /**
     * Test the create and load method
     */
    @Test
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, serviceDao.findAll().size());
        testCreate();
        List<Service> services = serviceDao.findAll();
        assertEquals(1, services.size());
        Service service = services.get(0);
        service.setDescription("newDescription");
        serviceDao.update(service);
        assertEquals("newDescription", serviceDao.load(service.getName()).getDescription());
        serviceDao.remove(service);
        assertEquals(0, serviceDao.findAll().size());
    }

}
