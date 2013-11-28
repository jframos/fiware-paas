/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.model.OS;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class OSDaoJpaImplTest {

    @Autowired
    private OSDao osDao;

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "li";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    @Test
    public void testCreate() throws Exception {

        OS os = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
        OS osCreated = osDao.create(os);
        assertNotNull(osCreated.getId());

    }

}
