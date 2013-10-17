package com.telefonica.euro_iaas.paasmanager.async;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.impl.EnvironmentInstanceAsyncManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

/**
 * Unit test suite for EnvironmentInstanceAsyncManagerImpl
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentInstanceAsyncManagerImplTest {

	/*
	 * private EnvironmentManager environmentManager; private
	 * EnvironmentInstanceManager environmentInstanceManager; private
	 * TierInstanceManager tierInstanceManager; private ProductInstanceManager
	 * productInstanceManager; private InfrastructureManager
	 * infrastructureManager; private TaskNotificator taskNotificator; private
	 * TaskManager taskManager; private SystemPropertiesProvider
	 * propertiesProvider;
	 * 
	 * private Environment expectedEnvironment; private VM host = new
	 * VM("fqn","ip","hostname", "domain"); private VM host2 = new VM
	 * ("fqn2","ip2","hostname2", "domain2"); private String vdc = "VDC";
	 * private String extendedOVF; private String callback = "callback"; private
	 * Task task; private OS os;
	 * 
	 * private List<VM> vmtestings;
	 * 
	 * @Before public void setUp() throws Exception {
	 * 
	 * InputStream is = ClassLoader.getSystemClassLoader()
	 * .getResourceAsStream("4caastovfexample_attributes.xml"); BufferedReader
	 * reader = new BufferedReader(new InputStreamReader(is)); StringBuffer
	 * ruleFile = new StringBuffer(); String actualString;
	 * 
	 * while ((actualString = reader.readLine()) != null) {
	 * ruleFile.append(actualString).append("\n"); }
	 * 
	 * extendedOVF = ruleFile.toString();
	 * 
	 * task= mock(Task.class);
	 * 
	 * taskNotificator = mock (TaskNotificator.class); taskManager = mock
	 * (TaskManager.class);
	 * when(taskManager.updateTask(any(Task.class))).thenReturn(task);
	 * 
	 * 
	 * vmtestings = new ArrayList<VM>(); vmtestings.add(host);
	 * 
	 * List<VM> vmproductions = new ArrayList<VM>(); vmproductions.add(host);
	 * vmproductions.add(host2);
	 * 
	 * infrastructureManager = mock(InfrastructureManager.class);
	 * //when(infrastructureManager.getVMs(vdc, new
	 * Integer(1))).thenReturn(vmtestings);
	 * //when(infrastructureManager.getVMs(vdc, new
	 * Integer(2))).thenReturn(vmproductions);
	 * 
	 * os = new OS("os1", "1", "os1 description", "v1"); List<OS> supportedOOSS
	 * = new ArrayList<OS>();
	 * 
	 * List<Tier> tiers = new ArrayList<Tier>(); List<ProductRelease>
	 * productReleases = new ArrayList<ProductRelease> ();
	 * 
	 * ProductRelease productRelease = new ProductRelease();
	 * 
	 * productRelease.setDescription("desc"); productRelease.setProductType(new
	 * ProductType("name","desc")); productRelease.setVersion("1.0");
	 * productRelease.setName("name");
	 * productRelease.setSupportedOOSS(supportedOOSS);
	 * 
	 * productReleases.add(productRelease);
	 * 
	 * Tier tier = new Tier(); tier.setName("tiername");
	 * tier.setInitial_number_instances(1); tier.setMaximum_number_instances(1);
	 * tier.setMinimum_number_instances(4);
	 * tier.setProductReleases(productReleases); tiers.add(tier);
	 * 
	 * expectedEnvironment = new Environment();
	 * 
	 * expectedEnvironment.setName("name");
	 * expectedEnvironment.setEnvironmentType(new EnvironmentType("name",
	 * "description")); expectedEnvironment.setTiers(tiers);
	 * 
	 * List<Attribute> attributes = new ArrayList<Attribute>();
	 * 
	 * ProductInstance expectProductInstance = new ProductInstance();
	 * //expectProductInstance.setName("name");
	 * expectProductInstance.setProductRelease(productRelease);
	 * expectProductInstance.setStatus(Status.INSTALLED);
	 * expectProductInstance.setVm(host);
	 * 
	 * List<ProductInstance> productInstances = new
	 * ArrayList<ProductInstance>();
	 * productInstances.add(expectProductInstance);
	 * 
	 * TierInstance expectedTierInstance = new TierInstance (tier,
	 * productInstances);
	 * 
	 * productInstanceManager = mock (ProductInstanceManager.class);
	 * when(productInstanceManager.install(any(VM.class), any(String.class),
	 * any(ProductRelease.class), (List<Attribute>)
	 * any(Object.class))).thenReturn(expectProductInstance);
	 * 
	 * 
	 * environmentManager = mock(EnvironmentManager.class);
	 * when(environmentManager
	 * .load(any(String.class))).thenReturn(expectedEnvironment);
	 * 
	 * 
	 * tierInstanceManager = mock (TierInstanceManager.class);
	 * when(tierInstanceManager.update(any(TierInstance.class)))
	 * .thenReturn(expectedTierInstance);
	 * 
	 * List<TierInstance> tierInstances = new ArrayList<TierInstance>();
	 * tierInstances.add(expectedTierInstance);
	 * 
	 * environmentInstanceManager = mock(EnvironmentInstanceManager.class);
	 * when(environmentInstanceManager.create(any(String.class),
	 * any(String.class),any(String.class),
	 * any(Environment.class))).thenReturn(new
	 * EnvironmentInstance(expectedEnvironment, tierInstances));
	 * 
	 * propertiesProvider = mock(SystemPropertiesProvider.class);
	 * when(propertiesProvider
	 * .getProperty(any(String.class))).thenReturn("blablablablba");
	 * 
	 * }
	 * 
	 * @Test public void CreateWhenEverithingIsOk() throws Exception {
	 * EnvironmentInstanceAsyncManagerImpl manager = new
	 * EnvironmentInstanceAsyncManagerImpl();
	 * manager.setEnvironmentInstanceManager(environmentInstanceManager);
	 * manager.setTaskNotificator(taskNotificator);
	 * manager.setPropertiesProvider(propertiesProvider);
	 * manager.setTaskManager(taskManager);
	 * 
	 * manager.create("org", vdc, "vmtestings", expectedEnvironment, task,
	 * callback);
	 * 
	 * 
	 * // make verifications
	 * 
	 * }
	 */
}
