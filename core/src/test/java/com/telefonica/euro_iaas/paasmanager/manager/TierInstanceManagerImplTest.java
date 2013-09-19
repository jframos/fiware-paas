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


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.TierInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * @author jesus.movilla
 *
 */
public class TierInstanceManagerImplTest extends TestCase{



	private TierInstanceDao tierInstanceDao;
	private InfrastructureManager infrastructureManager;
	private ProductReleaseManager productReleaseManager;
	private ProductInstanceManager productInstanceManager;
	private TierManager tierManager;
	private EnvironmentInstanceManager environmentInstanceManager;
	
	private Tier tier = null;
	private TierInstance tierInstanceResult = null;
	private TierInstance tierInstanceProductResult = null;
	private EnvironmentInstance envInstance = null;
	private TierInstanceManagerImpl manager = null;
	
	private PaasManagerUser user;
	private ClaudiaData claudiaData;
	
	@Before
	public void setUp() throws Exception{
		
		claudiaData = new ClaudiaData("org", "vdc", "service");
		user = new PaasManagerUser("user", "password", 
				(Collection<? extends GrantedAuthority>) 
				new ArrayList<GrantedAuthority>() );
		claudiaData.setUser(user);
		
		tierInstanceDao = mock (TierInstanceDao.class);
		infrastructureManager = mock (InfrastructureManager.class);
		productReleaseManager = mock (ProductReleaseManager.class);
		productInstanceManager = mock (ProductInstanceManager.class);
		environmentInstanceManager = mock (EnvironmentInstanceManager.class);
		tierManager = mock (TierManager.class);
		
		manager = new TierInstanceManagerImpl();
	    
		manager.setInfrastructureManager(infrastructureManager);
		manager.setProductInstanceManager(productInstanceManager);
		manager.setTierInstanceDao(tierInstanceDao);
		manager.setTierManager(tierManager);
		manager.setEnvironmentInstanceManager(environmentInstanceManager);
		
		ProductRelease productRelease = new ProductRelease ("product", "2.0");
		
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		productReleases.add(productRelease);
		
		VM host = new VM(null, "hostname", "domain");
		
		tier = new Tier();
		tier.setInitial_number_instances(new Integer(1));
		tier.setMaximum_number_instances(new Integer(5));
		tier.setMinimum_number_instances(new Integer(1));
		tier.setName("tierName");
		tier.setProductReleases(productReleases);
		
		tierInstanceResult = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);	
		ProductInstance productInstance = new ProductInstance (productRelease, Status.INSTALLING, "vdc");
		tierInstanceProductResult = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);	
		tierInstanceProductResult.setId(new Long (1));
		tierInstanceProductResult.addProductInstance(productInstance);
		when(tierManager.load(any(String.class))).thenReturn(tier);		
		when(tierInstanceDao.create(any(TierInstance.class))).thenReturn(tierInstanceResult);
		when(tierInstanceDao.update(any(TierInstance.class))).thenReturn(tierInstanceProductResult);
		
		when(tierInstanceDao.load(any(String.class))).thenThrow(new EntityNotFoundException(TierInstance.class, "dD", null) );
		
		when(productInstanceManager.create(any(ProductInstance.class))).thenReturn(productInstance);
		
		
	}
	
	@Test
	public void testCreateTierInstance() throws Exception{
		
		VM host = new VM(null, "hostname", "domain");
		TierInstance tierInstance = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);		
		
		   
		TierInstance tierInstanceCreated = manager.create(tierInstance);  
		assertEquals(tierInstanceCreated.getName(), tierInstanceCreated.getName());
	//	assertNotNull(environmentCreated.getId());
	}
	
	@Test
	public void testAddProductInstances() throws Exception{
		
		VM host = new VM(null, "hostname", "domain");
		TierInstance tierInstance = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);		
		
	
		
			TierInstance tierInstanceCreated = manager.create(tierInstance);   
		 
		  
		 
		assertEquals(tierInstanceCreated.getName(), tierInstanceCreated.getName());
		
		ProductRelease productRelease = new ProductRelease ("product", "2.0");
		
		
		ProductInstance productInstance = new ProductInstance (productRelease, Status.INSTALLING, "vdc");
		tierInstanceCreated.addProductInstance(productInstance);
		TierInstance tierInstanceUpdate = manager.update(tierInstance);  
		
		verify(tierInstanceDao, times(2)).load(any(String.class));
		verify(tierInstanceDao, times(0)).update(any(TierInstance.class));
		verify(tierInstanceDao, times(2)).create(any(TierInstance.class));
		assertEquals(tierInstanceUpdate.getProductInstances().size(), 1);
	}
	
	@Test
	public void testRemoveTierInstance() throws Exception{
		
		VM host = new VM(null, "hostname", "domain");
		TierInstance tierInstance = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);		
		
		TierInstance tierInstanceCreated = manager.create(tierInstance);  
		manager.remove(tierInstanceCreated);
	}
	
	@Test
	public void testScalePaaSInstance() throws Exception{
		
		VM host = new VM(null, "hostname", "domain");
		EnvironmentInstance envInst = new EnvironmentInstance();
		TierInstance tierInstance = new TierInstance (tier, "tierInsatnce", "nametierInstance-tier-1", host);		
		TierInstance tierInstanceCreated = manager.create(tierInstance); 
		manager.create(claudiaData, tierInstanceCreated, envInst);
	
	}
	
}
