/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.sql.Connection;

import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import org.junit.Test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynchronizationBoostrapTest {

    @Test
    public void testContextInitialized() {

        String url = "jdbc:mysql://account.lab.fi-ware.eu/fi-ware-idm_production";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "nacho";
        String password = "nachotid123";
        Connection conn = null;

        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);

        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.OPENSTACK_SYNCHRONIZATION_POLLING_PERIOD))
                .thenReturn("1");
        when(systemPropertiesProvider.getProperty("keystone.nova.url")).thenReturn(
                "http://cloud.lab.fi-ware.eu:4731/v2.0");
        when(systemPropertiesProvider.getProperty("openstack.nova.url"))
                .thenReturn("http://cloud.lab.fi-ware.eu:8774/");
        when(systemPropertiesProvider.getProperty("openstack.version")).thenReturn("v2/");

        /*
         * EnvironmentInstanceDaoJpaImpl environmentInstanceDao= new EnvironmentInstanceDaoJpaImpl ();
         * TierInstanceDaoJpaImpl tierInstanceDao= new TierInstanceDaoJpaImpl (); TierDaoJpaImpl tierDao= new
         * TierDaoJpaImpl (); RuleDaoJpaImpl ruleDao = new RuleDaoJpaImpl (); SecurityGroupDaoJpaImpl securityGroupDao =
         * new SecurityGroupDaoJpaImpl (); UserDaoImplIdm userDao = new UserDaoTest (); TokenDaoKeystoneImpl tokenDao =
         * new TokenDaoKeystoneImpl (); tokenDao.setSystemPropertiesProvider(systemPropertiesProvider); try { //Lineas
         * comentadas por errores al arrancar el paasManager Class.forName(driver).newInstance(); conn =
         * DriverManager.getConnection(url, userName, password); OpenStackSyncImpl openStackSync =new
         * OpenStackSyncImpl(conn, false, tierDao, tierInstanceDao, null, null, systemPropertiesProvider,
         * environmentInstanceDao, ruleDao, securityGroupDao, userDao, tokenDao); openStackSync.syncronize(conn, true);
         * Thread myThread = new Thread(openStackSync); myThread.start(); } catch (Exception e) { System.out.println
         * (e.getMessage()); } }
         */
    }

}
