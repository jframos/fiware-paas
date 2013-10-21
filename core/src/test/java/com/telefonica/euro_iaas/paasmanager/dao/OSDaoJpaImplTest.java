/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.impl.OSDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.OS;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */
public class OSDaoJpaImplTest {

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
        List<OS> oss = new ArrayList<OS>();
        oss.add(so1);

    }

    @Test
    public void testCreate() throws Exception {

        OSDaoJpaImpl osDao = new OSDaoJpaImpl();
        OS so = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
        assertNull(so.getId());

    }

}
