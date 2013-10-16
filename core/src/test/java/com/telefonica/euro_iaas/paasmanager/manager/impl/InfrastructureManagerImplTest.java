/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.monitoring.MonitoringClient;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class InfrastructureManagerImplTest extends TestCase {

    private String vdc, org;
    private int number_vms;
    private SystemPropertiesProvider propertiesProvider;
    private ClaudiaClient claudiaClient;
    private ClaudiaUtil claudiaUtil;
    private MonitoringClient monitoringClient;
    private OVFUtils ovfUtils;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;

    private String vdcResponse = "vdcResponse";

    private String vdcNotFoundResponse = "ElementNotFound";

    private String serviceResponse = "serviceResponse";
    private EnvironmentInstanceDao environmentInstanceDao;

    // private String ovfname = "Case01-01-initial-vapp-creation.xml";
    private String ovfname = "4caastovfexample.xml";
    private String ovf = "ovf";
    private PaasManagerUser user;
    private ClaudiaData claudiaData;
    private TierInstanceManager tierInstanceManager;
    private InfrastructureManagerClaudiaImpl manager;

    @Before
    public void setUp() throws Exception {

        propertiesProvider = mock(SystemPropertiesProvider.class);
        claudiaClient = mock(ClaudiaClient.class);
        claudiaUtil = mock(ClaudiaUtil.class);
        claudiaResponseAnalyser = mock(ClaudiaResponseAnalyser.class);
        monitoringClient = mock(MonitoringClient.class);
        tierInstanceManager = mock(TierInstanceManager.class);
        ovfUtils = mock(OVFUtils.class);
        environmentInstanceDao = mock(EnvironmentInstanceDao.class);
        manager = new InfrastructureManagerClaudiaImpl();
        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);
        manager.setMonitoringClient(monitoringClient);
        manager.setOvfUtils(ovfUtils);
        manager.setTierInstanceManager(tierInstanceManager);
        manager.setEnvironmentInstanceDao(environmentInstanceDao);
        claudiaData = new ClaudiaData("org", "vdc", "service");

        /*
		 * vdc = "paasmanagerVDC"; org = "ORG"; number_vms = 2; claudiaData =
		 * new ClaudiaData(org, vdc);
		 * 
		 * //Taking ovf from a file InputStream is =
		 * ClassLoader.getSystemClassLoader().getResourceAsStream(ovfname);
		 * BufferedReader reader = new BufferedReader(new
		 * InputStreamReader(is)); StringBuffer ruleFile = new StringBuffer();
		 * String actualString;
		 * 
		 * while ((actualString = reader.readLine()) != null) {
		 * ruleFile.append(actualString).append("\n"); }
		 * 
		 * // user = new PaasManagerUser("user", "paasword", null);
		 * //claudiaData.setUser(user);
		 * 
		 * // ovf = ruleFile.toString(); // System.out.println("ovf: " + ovf);
		 * 
		 * // vdcResponseTask = new Task();
		 * vdcResponseTask.setState(TaskStates.SUCCESS);
		 * vdcResponseTask.setResource
		 * ("http://10.95.171.89:8080/rest-api-management/", "resourceType");
		 * serviceResponseTask = new Task();
		 * serviceResponseTask.setState(TaskStates.SUCCESS);
		 * serviceResponseTask.
		 * setResource("http://10.95.171.89:8080/rest-api-management/",
		 * "resourceType");
		 * 
		 * vmResponseTask = new Task();
		 * vmResponseTask.setState(TaskStates.SUCCESS);
		 * vmResponseTask.setResource
		 * ("http://10.95.171.89:8080/rest-api-management/", "resourceType");
		 * 
		 * propertiesProvider = mock(SystemPropertiesProvider.class);
		 * when(propertiesProvider
		 * .getProperty(NEOCLAUDIA_SERVICE)).thenReturn("paasmanagerService");
		 * when
		 * (propertiesProvider.getProperty(NEOCLAUDIA_VDC_CPU)).thenReturn("12"
		 * );
		 * when(propertiesProvider.getProperty(NEOCLAUDIA_VDC_MEM)).thenReturn
		 * ("14");
		 * when(propertiesProvider.getProperty(NEOCLAUDIA_VDC_DISK)).thenReturn
		 * ("16");
		 * when(propertiesProvider.getProperty(NEOCLAUDIA_ORG)).thenReturn
		 * ("EUROPIAAS-VC1");
		 * when(propertiesProvider.getProperty(NEOCLAUDIA_OVFSERVICE_LOCATION
		 * )).thenReturn("empty.ovf");
		 * when(propertiesProvider.getProperty(VM_NAME_PREFIX
		 * )).thenReturn("paasManagerVM");
		 * 
		 * 
		 * claudiaClient = mock(ClaudiaClient.class);
		 * //when(claudiaClient.browseVDC(any(String.class), any(String.class),
		 * user)) //.thenReturn(vdcResponse);
		 * when(claudiaClient.browseVDC(any(ClaudiaData
		 * .class))).thenReturn(vdcResponse);
		 * when(claudiaClient.deployVDC(any(ClaudiaData.class),
		 * any(String.class), any(String.class),
		 * any(String.class))).thenReturn("OK");
		 * when(claudiaClient.browseService
		 * (any(ClaudiaData.class))).thenReturn(vdcResponse);
		 * when(claudiaClient.deployService(any(ClaudiaData.class),
		 * any(String.class))) .thenReturn("OK");
		 * when(claudiaClient.deployVM(any(String.class), any(String.class),
		 * any(String.class), any(String.class), user, any(String.class)))
		 * .thenReturn("OK");
		 * 
		 * when(claudiaClient.deployVM(any(ClaudiaData.class),any(Tier.class)))
		 * .thenReturn(claudiaData);
		 * 
		 * when(claudiaClient.obtainIPFromFqn(any(String.class),
		 * any(String.class), any(String.class),any(String.class), user))
		 * .thenReturn("10.95.171.34");
		 * 
		 * claudiaUtil = mock (ClaudiaUtil.class);
		 * 
		 * claudiaResponseAnalyser = mock(ClaudiaResponseAnalyser.class);
		 * when(claudiaResponseAnalyser.getTaskUrl(any(String.class)))
		 * .thenReturn("OK");
		 * when(claudiaResponseAnalyser.getTaskStatus(any(String.class)))
		 * .thenReturn("success");
         */

    }

    @Test
    public void testDeployVMNoFIWARE() throws Exception {
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");
        when(propertiesProvider.getProperty(any(String.class))).thenReturn("1");
        List<String> ips = new ArrayList();
        ips.add("IP");

        String hostname = claudiaData.getService() + "-" + tier.getName() + "-" + 1;
        String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + 1;

        VM vm = new VM();

        vm.setFqn(fqn);
        vm.setHostname(hostname);

        when(claudiaClient.getIP(any(ClaudiaData.class), any(String.class), Matchers.anyInt(), any(VM.class)))
                .thenReturn(ips);
        Mockito.doNothing().when(claudiaClient)
                .deployVM(any(ClaudiaData.class), any(Tier.class), Matchers.anyInt(), any(VM.class));
        when(ovfUtils.changeInitialResources(any(String.class))).thenReturn("ovf");
        Mockito.doNothing().when(monitoringClient).startMonitoring(any(String.class), any(String.class));

        manager.deployVM(claudiaData, tier, 1, "ovf", vm);
        assertEquals(vm.getDomain(), "");
        /*
         * assertEquals(vm.getFqn(), claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() +
         * ".services." + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + 1);
         */
        assertEquals(vm.getHostname(), hostname);
        assertEquals(vm.getIp(), "IP");

    }

    @Test
    public void testCreateInfrasctuctureEnvironmentInstance() throws Exception {

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);

        List<Tier> lTier = new ArrayList();
        lTier.add(tier);
        Environment env = new Environment("name", lTier, "description");
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", env);
        when(propertiesProvider.getProperty("openstack-tcloud.cloudSystem")).thenReturn("4caast");
        when(propertiesProvider.getProperty("vmDeploymentDelay")).thenReturn("1");
        List<String> ips = new ArrayList<String>();
        ips.add("IP");
        List<String> ovfs = new ArrayList<String>();
        ovfs.add(null);

        String hostname = claudiaData.getService() + "-" + tier.getName() + "-" + 1;
        String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + 1;

        VM vm = new VM();

        vm.setFqn(fqn);
        vm.setHostname(hostname);
        vm.setIp("IP");

        tierInstance.setVM(vm);

        String ovf = null;

        tierInstance.setOvf(ovf);
        tierInstance.setVapp("vapp");

        when(claudiaClient.getIP(any(ClaudiaData.class), any(String.class), Matchers.anyInt(), any(VM.class)))
                .thenReturn(ips);
        Mockito.doNothing().when(claudiaClient)
                .deployVM(any(ClaudiaData.class), any(Tier.class), Matchers.anyInt(), any(VM.class));
        when(ovfUtils.changeInitialResources(any(String.class))).thenReturn(null);
        when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);
        when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("vdc");
        when(claudiaClient.browseService(any(ClaudiaData.class))).thenReturn("vapp");
        when(environmentInstanceDao.update(any(EnvironmentInstance.class))).thenReturn(envInst);
        when(claudiaClient.browseVMReplica(any(ClaudiaData.class), any(String.class), anyInt(), any(VM.class)))
                .thenReturn("vapp");

        Mockito.doThrow(new EntityNotFoundException(TierInstance.class, "test", tierInstance))
                .when(tierInstanceManager).load(any(String.class));
        when(tierInstanceManager.create(any(ClaudiaData.class), any(String.class), any(TierInstance.class)))
                .thenReturn(tierInstance);

        Mockito.doNothing().when(monitoringClient).startMonitoring(any(String.class), any(String.class));

        EnvironmentInstance envInst2 = manager.createInfrasctuctureEnvironmentInstance(envInst, envInst
                .getEnvironment().getTiers(), claudiaData);
        assertEquals(envInst2.getBlueprintName(), "blue");
        // assertEquals(envInst2.getName(),
        // claudiaData.getVdc()+"-"+tier.getName());
        assertEquals(envInst2.getTierInstances().size(), 1);
        assertEquals(envInst2.getTierInstances().get(0).getTier().getName(), tier.getName());
        assertEquals(envInst2.getTierInstances().get(0).getVApp(), "vapp");

        assertEquals(envInst2.getTierInstances().get(0).getVM().getIp(), "IP");
        assertEquals(envInst2.getTierInstances().get(0).getVM().getHostname(), hostname);
        verify(tierInstanceManager, times(1)).create(claudiaData, "name", tierInstance);

    }

    private String getFile(String file) throws IOException {
        File f = new File(file);
        System.out.println(f.isFile() + " " + f.getAbsolutePath());
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();

    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetVMsIsOk() throws Exception {
        InfrastructureManagerClaudiaImpl manager = new InfrastructureManagerClaudiaImpl();

        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);

        // List<VM> vms = manager.getVMs(vdc, number_vms);
        // assertEquals(number_vms, vms.size());
    }

    @Test
    public void testGetVMsIsOkCreateVDC() throws Exception {
        InfrastructureManagerClaudiaImpl manager = new InfrastructureManagerClaudiaImpl();

        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);

        // List<VM> vms = manager.getVMs(vdc, number_vms);
        // assertEquals(number_vms, vms.size());
    }

    @Test
    public void testCreateEnvironment() throws Exception {

        // List<VM> vms = manager.createEnvironment(ovf, org, vdc);

    }

}
