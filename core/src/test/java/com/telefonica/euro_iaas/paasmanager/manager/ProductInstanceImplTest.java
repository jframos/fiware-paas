/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.manager.impl.InfrastructureManagerClaudiaImpl;
import com.telefonica.euro_iaas.paasmanager.manager.impl.ProductInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.manager.impl.TierInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;

/**
 * @author jesus.movilla
 *
 */
public class ProductInstanceImplTest extends TestCase{



	private ProductInstanceDao productInstanceDao;
	
	private ProductReleaseManager productReleaseManager;
	private ProductInstallator productInstallator;
		
	private ProductInstanceManagerImpl manager = null;
	
	private ProductRelease productRelease = null;
	private TierInstance tierInstance = null;
	

	
	@Before
	public void setUp() throws Exception{
		
		productInstanceDao = mock (ProductInstanceDao.class);
		
		productReleaseManager = mock (ProductReleaseManager.class);
		productInstallator = mock (ProductInstallator.class);
			
		manager = new ProductInstanceManagerImpl();
	    
		manager.setProductInstallator(productInstallator);
		manager.setProductInstanceDao(productInstanceDao);
		manager.setProductReleaseManager(productReleaseManager);

		productRelease = new ProductRelease ("product", "2.0");
		
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		productReleases.add(productRelease);
		
		VM host = new VM(null, "hostname", "domain");
		
		Tier tier = new Tier();
		tier.setInitial_number_instances(new Integer(1));
		tier.setMaximum_number_instances(new Integer(5));
		tier.setMinimum_number_instances(new Integer(1));
		tier.setName("tierName");
		tier.setProductReleases(productReleases);
		
		tierInstance = new TierInstance (tier, "tierInsatnce", "nametierInstance", host);	
		ProductInstance productInstance = new ProductInstance (productRelease, Status.INSTALLING, "vdc");
		
		when(productReleaseManager.load(any(String.class))).thenReturn(productRelease);
		when(productInstanceDao.load(any(String.class))).thenReturn(productInstance);
		when(productInstallator.install(any(TierInstance.class), any(ProductRelease.class))).thenReturn(productInstance);
	}
	
	@Test
	public void testCreateProductInstance() throws Exception{

		ProductInstance productInstance = new ProductInstance (productRelease, Status.INSTALLING, "vdc");
		ProductInstance productInstanceCreated = manager.create(productInstance);  
		assertEquals(productInstance.getName(), productInstanceCreated.getName());

	}
	
	@Test
	public void testInstallProductInstanceNoAttributes() throws Exception{

		ProductInstance productInstance = new ProductInstance (productRelease, Status.INSTALLING, "vdc");
		ProductInstance productInstanceCreated = manager.install(tierInstance, "vdc", productRelease, null);  
		assertEquals(productInstance.getName(), productInstanceCreated.getName());

	}
	
	
	
	
}
