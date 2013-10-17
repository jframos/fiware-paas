package com.telefonica.euro_iaas.paasmanager.bootstrap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;


import com.telefonica.euro_iaas.paasmanager.dao.impl.EnvironmentInstanceDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.dao.impl.RuleDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.dao.impl.SecurityGroupDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.dao.impl.TierDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.dao.impl.TierInstanceDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.UserDaoTest;
import com.telefonica.euro_iaas.paasmanager.dao.keystone.impl.TokenDaoKeystoneImpl;




import com.telefonica.euro_iaas.paasmanager.util.OpenStackSyncImpl;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


public class SynchronizationBoostrapTest {

	@Test
	public void testContextInitialized() {
		
		String url =  "jdbc:mysql://account.lab.fi-ware.eu/fi-ware-idm_production" ;
		String driver = "com.mysql.jdbc.Driver";
		String userName = "nacho";
		String password = "nachotid123";
		Connection conn = null;
	
		SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class );
		
		when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.OPENSTACK_SYNCHRONIZATION_POLLING_PERIOD)).thenReturn("1");
		when(systemPropertiesProvider.getProperty("keystone.nova.url")).thenReturn("http://cloud.lab.fi-ware.eu:4731/v2.0");
		when(systemPropertiesProvider.getProperty("openstack.nova.url")).thenReturn("http://cloud.lab.fi-ware.eu:8774/");
				when(systemPropertiesProvider.getProperty("openstack.version")).thenReturn("v2/");
	
		
	/*	EnvironmentInstanceDaoJpaImpl environmentInstanceDao= new EnvironmentInstanceDaoJpaImpl ();
		TierInstanceDaoJpaImpl tierInstanceDao= new TierInstanceDaoJpaImpl ();
		TierDaoJpaImpl tierDao= new TierDaoJpaImpl ();
		RuleDaoJpaImpl ruleDao = new RuleDaoJpaImpl ();
		SecurityGroupDaoJpaImpl securityGroupDao = new SecurityGroupDaoJpaImpl ();
		UserDaoImplIdm userDao = new UserDaoTest ();
	
		TokenDaoKeystoneImpl tokenDao = new TokenDaoKeystoneImpl ();
		
		tokenDao.setSystemPropertiesProvider(systemPropertiesProvider);
         
        try {
        	//Lineas comentadas por errores al arrancar el paasManager
        	
        	Class.forName(driver).newInstance();
        	conn = DriverManager.getConnection(url, userName, password);
        	 
        	OpenStackSyncImpl openStackSync 
        		=new OpenStackSyncImpl(conn, false, tierDao, 
        				tierInstanceDao, null, null, 
						systemPropertiesProvider, environmentInstanceDao, 
						ruleDao, securityGroupDao, userDao, tokenDao);
        	openStackSync.syncronize(conn, true);
			Thread myThread = new Thread(openStackSync);
			myThread.start();
	} catch (Exception e)
	{
		System.out.println (e.getMessage());
	}
	}*/
	}

}
