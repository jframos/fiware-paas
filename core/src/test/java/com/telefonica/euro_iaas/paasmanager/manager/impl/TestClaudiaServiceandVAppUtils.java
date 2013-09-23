package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtilsDomImpl;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.VappUtilsImpl;

public class TestClaudiaServiceandVAppUtils extends TestCase {

	ClaudiaData claudiaData = new ClaudiaData("org", "vdc", "service");
	Environment envResult = null;
	ProductInstance productInstance = null;
	InfrastructureManagerServiceClaudiaImpl manager = null;

	@Before 
	public void setUp ()
	{
		 manager = new InfrastructureManagerServiceClaudiaImpl();
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
		when(
				systemPropertiesProvider
						.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM))
				.thenReturn("ddFIWARE");

		vappUtils.setSystemPropertiesProvider(systemPropertiesProvider);
		manager.setVappUtils(vappUtils);
		OVFUtilsDomImpl ovfUtils = new OVFUtilsDomImpl();
		manager.setOvfUtils(ovfUtils);

		
		envResult = new Environment();
		envResult.setName("environemntName");
		envResult.setEnvironmentType(new EnvironmentType("Generic", "Generic"));
		String ovfname = "src/test/resources/SAP83scal.xml";
		String ovfService = null;
		try {
			ovfService = getFile(ovfname);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		envResult.setOvf(ovfService);
		Tier tier = new Tier();
		tier.setInitialNumberInstances(new Integer(1));
		tier.setMaximumNumberInstances(new Integer(5));
		tier.setMinimumNumberInstances(new Integer(1));
		tier.setName("FlexVM1");
		Tier tier2 = new Tier();
		tier2.setInitialNumberInstances(new Integer(1));
		tier2.setMaximumNumberInstances(new Integer(5));
		tier2.setMinimumNumberInstances(new Integer(1));
		tier2.setName("haproxy");
		Tier tier3 = new Tier();
		tier3.setInitialNumberInstances(new Integer(1));
		tier3.setMaximumNumberInstances(new Integer(5));
		tier3.setMinimumNumberInstances(new Integer(1));
		tier3.setName("FlexVM2");
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		ProductRelease productRelease = new ProductRelease("product", "2.0");
		productReleases.add(productRelease);

		productInstance = new ProductInstance();
		productInstance.setName("productInstance");
		productInstance.setProductRelease(productRelease);
		productReleases.add(productRelease);
		tier.setProductReleases(productReleases);
		tier2.setProductReleases(productReleases);
		tier3.setProductReleases(productReleases);

		List<Tier> tiers = new ArrayList<Tier>();
		tiers.add(tier);
		tiers.add(tier2);
		tiers.add(tier3);
		envResult.setTiers(tiers);
	}
	@Test
	public void testClaudiaServiceandVAppUtils() {

		

		EnvironmentInstance environmentInstance = new EnvironmentInstance();
		environmentInstance.setEnvironment(envResult);

		environmentInstance.setName(claudiaData.getVdc() + "-"
				+ envResult.getName());

		String vappname = "src/test/resources/vappsap83.xml";
		String vappService = null;
		try {
			vappService = getFile(vappname);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		InfrastructureManagerServiceClaudiaImpl manager = new InfrastructureManagerServiceClaudiaImpl();
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
		when(
				systemPropertiesProvider
						.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM))
				.thenReturn("ddFIWARE");

		vappUtils.setSystemPropertiesProvider(systemPropertiesProvider);
		manager.setVappUtils(vappUtils);
		OVFUtilsDomImpl ovfUtils = new OVFUtilsDomImpl();
		manager.setOvfUtils(ovfUtils);

		List<TierInstance> tierInstances = null;

		try {
			tierInstances = manager.fromVappToListTierInstance(vappService,
					envResult, claudiaData);
		} catch (InvalidVappException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidOVFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (TierInstance tierInstance: tierInstances)
		{
			if (tierInstance.getTier().equals("haproxy"))
			{
				assertEquals (tierInstance.getVM().getIp(), "109.231.73.170");
			}
			else if (tierInstance.getTier().equals("FlexVM1"))
			{
				assertEquals (tierInstance.getVM().getIp(), "109.231.80.84");
			}
			else if (tierInstance.getTier().equals("FlexVM2"))
			{
				assertEquals (tierInstance.getVM().getIp(), "109.231.73.171");
			}
			
		}

		for (TierInstance tierInstance : tierInstances) {
			environmentInstance.addTierInstance(tierInstance);
		}
		environmentInstance.setVapp(vappService);

		EnvironmentInstanceManagerImpl environmentInstanceManager = new EnvironmentInstanceManagerImpl();

		ProductInstanceManager productInstanceManager = mock(ProductInstanceManager.class);
		try {
			when(
					productInstanceManager.install(any(TierInstance.class),any(ClaudiaData.class),
							any(String.class), any(ProductRelease.class),
							any(List.class))).thenReturn(productInstance);
		} catch (ProductInstallatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductInstanceRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotUniqueResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		environmentInstanceManager
				.setProductInstanceManager(productInstanceManager);
		InfrastructureManager infrastructureManager = mock(InfrastructureManager.class);
		try {
			when(
					infrastructureManager.ImageScalability(
							any(ClaudiaData.class), any(TierInstance.class)))
					.thenReturn("dd");
		} catch (InfrastructureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EnvironmentUtils environmentUtils = mock(EnvironmentUtils.class);
		when(environmentUtils.updateVmOvf(any(String.class), any(String.class)))
				.thenReturn("dd");

		environmentInstanceManager
				.setInfrastructureManager(infrastructureManager);
		environmentInstanceManager.setEnvironmentUtils(environmentUtils);
		
		TierInstanceDao tierInstanceDao = mock(TierInstanceDao.class);
		environmentInstanceManager.setTierInstanceDao(tierInstanceDao);
		
		try {
			boolean bScalableEnvironment = environmentInstanceManager
					.installSoftwareInEnvironmentInstance(claudiaData,
							environmentInstance);
		} catch (ProductInstallatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductInstanceRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotUniqueResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testSeveralNetworksClaudiaServiceandVAppUtils() {

		

		EnvironmentInstance environmentInstance = new EnvironmentInstance();
		environmentInstance.setEnvironment(envResult);

		environmentInstance.setName(claudiaData.getVdc() + "-"
				+ envResult.getName());

		String vappname = "src/test/resources/vappsap84.xml";
		String vappService = null;
		try {
			vappService = getFile(vappname);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		List<TierInstance> tierInstances = null;

		try {
			tierInstances = manager.fromVappToListTierInstance(vappService,
					envResult, claudiaData);
		} catch (InvalidVappException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidOVFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (TierInstance tierInstance: tierInstances)
		{
			if (tierInstance.getTier().equals("haproxy"))
			{
				assertEquals (tierInstance.getVM().getNetworks().get("public"), "109.231.73.170");
				assertEquals (tierInstance.getVM().getNetworks().get("private"), "1.231.73.170");
				assertEquals (tierInstance.getVM().getNetworks().get("other"), "2.231.73.170");
			}
			else if (tierInstance.getTier().equals("FlexVM1"))
			{
				assertEquals (tierInstance.getVM().getNetworks().get("public"), "109.231.80.84");
				assertEquals (tierInstance.getVM().getNetworks().get("private"), "1.231.80.84");
				assertEquals (tierInstance.getVM().getNetworks().get("other"), "2.231.80.84");
			}
			else if (tierInstance.getTier().equals("FlexVM2"))
			{
				assertEquals (tierInstance.getVM().getNetworks().get("public"), "109.231.73.171");
				assertEquals (tierInstance.getVM().getNetworks().get("private"), "1.231.73.171");
				assertEquals (tierInstance.getVM().getNetworks().get("other"), "2.231.73.171");
			}
			
		}

		for (TierInstance tierInstance : tierInstances) {
			environmentInstance.addTierInstance(tierInstance);
		}
		environmentInstance.setVapp(vappService);

		EnvironmentInstanceManagerImpl environmentInstanceManager = new EnvironmentInstanceManagerImpl();

		ProductInstanceManager productInstanceManager = mock(ProductInstanceManager.class);
		try {
			when(
					productInstanceManager.install(any(TierInstance.class),any(ClaudiaData.class),
							any(String.class), any(ProductRelease.class),
							any(List.class))).thenReturn(productInstance);
		} catch (ProductInstallatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductInstanceRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotUniqueResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		environmentInstanceManager
				.setProductInstanceManager(productInstanceManager);
		InfrastructureManager infrastructureManager = mock(InfrastructureManager.class);
		try {
			when(
					infrastructureManager.ImageScalability(
							any(ClaudiaData.class), any(TierInstance.class)))
					.thenReturn("dd");
		} catch (InfrastructureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EnvironmentUtils environmentUtils = mock(EnvironmentUtils.class);
		when(environmentUtils.updateVmOvf(any(String.class), any(String.class)))
				.thenReturn("dd");

		environmentInstanceManager
				.setInfrastructureManager(infrastructureManager);
		environmentInstanceManager.setEnvironmentUtils(environmentUtils);
		
		TierInstanceDao tierInstanceDao = mock(TierInstanceDao.class);
		environmentInstanceManager.setTierInstanceDao(tierInstanceDao);
		
		try {
			boolean bScalableEnvironment = environmentInstanceManager
					.installSoftwareInEnvironmentInstance(claudiaData,
							environmentInstance);
		} catch (ProductInstallatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductInstanceRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotUniqueResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String getFile(String file) throws IOException {
		File f = new File(file);
		System.out.println(f.isFile() + " " + f.getAbsolutePath());
		InputStream is = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(file);
		InputStream dd = new FileInputStream(f);

		BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
		StringBuffer ruleFile = new StringBuffer();
		String actualString;

		while ((actualString = reader.readLine()) != null) {
			ruleFile.append(actualString).append("\n");
		}
		return ruleFile.toString();
	}

}
