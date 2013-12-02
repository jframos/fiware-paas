/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
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
public class InfrastructureManagerImplTest {

    private SystemPropertiesProvider propertiesProvider;
    private ClaudiaClient claudiaClient;
    private ClaudiaUtil claudiaUtil;
    private MonitoringClient monitoringClient;
    private OVFUtils ovfUtils;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;

    private EnvironmentInstanceDao environmentInstanceDao;
    private NetworkInstanceManager networkInstanceManager;

    // private String ovfname = "Case01-01-initial-vapp-creation.xml";
    private PaasManagerUser user;
    private ClaudiaData claudiaData;
    private TierInstanceManager tierInstanceManager;
    private TierManager tierManager;
    private InfrastructureManagerClaudiaImpl manager;
    private NetworkManager networkManager;
    private Tier tier;
    private VM vm;

    @Before
    public void setUp() throws Exception {

        propertiesProvider = mock(SystemPropertiesProvider.class);
        claudiaClient = mock(ClaudiaClient.class);
        claudiaUtil = mock(ClaudiaUtil.class);
        claudiaResponseAnalyser = mock(ClaudiaResponseAnalyser.class);
        monitoringClient = mock(MonitoringClient.class);
        tierInstanceManager = mock(TierInstanceManager.class);
        tierManager = mock(TierManager.class);
        ovfUtils = mock(OVFUtils.class);
        networkManager = mock(NetworkManager.class);
        environmentInstanceDao = mock(EnvironmentInstanceDao.class);
        networkInstanceManager = mock(NetworkInstanceManager.class);
        manager = new InfrastructureManagerClaudiaImpl();
        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);
        manager.setMonitoringClient(monitoringClient);
        manager.setOvfUtils(ovfUtils);
        manager.setTierInstanceManager(tierInstanceManager);
        manager.setEnvironmentInstanceDao(environmentInstanceDao);
        manager.setNetworkInstanceManager(networkInstanceManager);
        manager.setNetworkManager(networkManager);
        manager.setTierManager(tierManager);

        claudiaData = new ClaudiaData("org", "vdc", "service");

        tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "false", "payload");
        String hostname = claudiaData.getService() + "-" + tier.getName() + "-" + 1;
        String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + 1;

        vm = new VM();
        vm.setFqn(fqn);
        vm.setHostname(hostname);
        vm.setIp("IP");

    }

    @Test
    public void testCreateInfrasctuctureEnvironmentInstance() throws Exception {

        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);

        Set<Tier> lTier = new HashSet<Tier>();
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

        when(
                claudiaClient.getIP(any(ClaudiaData.class), any(String.class), Matchers.anyInt(), any(VM.class),
                        anyString())).thenReturn(ips);
        Mockito.doNothing().when(claudiaClient)
                .deployVM(any(ClaudiaData.class), any(TierInstance.class), Matchers.anyInt(), any(VM.class));
        when(ovfUtils.changeInitialResources(any(String.class))).thenReturn(null);
        when(propertiesProvider.getProperty("openstack-tcloud.cloudSystem")).thenReturn("4caast");
        when(propertiesProvider.getProperty("vmDeploymentDelay")).thenReturn("1");
        when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);
        when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("vdc");
        when(claudiaClient.browseService(any(ClaudiaData.class))).thenReturn("vapp");
        when(environmentInstanceDao.update(any(EnvironmentInstance.class))).thenReturn(envInst);
        when(
                claudiaClient.browseVMReplica(any(ClaudiaData.class), any(String.class), anyInt(), any(VM.class),
                        anyString())).thenReturn("vapp");

        Mockito.doThrow(new EntityNotFoundException(TierInstance.class, "test", tierInstance))
                .when(tierInstanceManager).load(any(String.class));
        when(tierInstanceManager.create(any(ClaudiaData.class), any(String.class), any(TierInstance.class)))
                .thenReturn(tierInstance);
        when(tierManager.loadTierWithNetworks(any(String.class), any(String.class), any(String.class))).thenReturn(tier);


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

    @Test
    public void testDeleteInfrasctuctureEnvironmentInstance() throws Exception {

        Network net = new Network("NET");
        tier.addNetwork(net);
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        tierInstance.addNetworkInstance(net.toNetworkInstance());

        Set<Tier> lTier = new HashSet<Tier>();
        lTier.add(tier);
        Environment env = new Environment("name", lTier, "description");
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", env);

        String ovf = null;
        tierInstance.setOvf(ovf);
        tierInstance.setVapp("vapp");
        tierInstance.setVM(vm);
        envInst.addTierInstance(tierInstance);

        when(
                claudiaClient.browseVMReplica(any(ClaudiaData.class), any(String.class), anyInt(), any(VM.class),
                        anyString())).thenReturn("vapp");
        Mockito.doNothing().when(monitoringClient).stopMonitoring(any(String.class));
        Mockito.doNothing().when(claudiaClient).undeployVMReplica(any(ClaudiaData.class), any(TierInstance.class));
        Mockito.doNothing().when(networkInstanceManager)
                .delete(any(ClaudiaData.class), any(NetworkInstance.class), anyString());

        /*
         * when(claudiaClient.getIP(any(ClaudiaData.class), any(String.class), Matchers.anyInt(), any(VM.class)))
         * .thenReturn(ips); Mockito.doNothing().when(claudiaClient) .deployVM(any(ClaudiaData.class),
         * any(TierInstance.class), Matchers.anyInt(), any(VM.class));
         * when(ovfUtils.changeInitialResources(any(String.class))).thenReturn(null);
         * when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);
         * when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("vdc");
         * when(claudiaClient.browseService(any(ClaudiaData.class))).thenReturn("vapp");
         * when(environmentInstanceDao.update(any(EnvironmentInstance.class))).thenReturn(envInst);
         * when(claudiaClient.browseVMReplica(any(ClaudiaData.class), any(String.class), anyInt(), any(VM.class)))
         * .thenReturn("vapp"); Mockito.doThrow(new EntityNotFoundException(TierInstance.class, "test", tierInstance))
         * .when(tierInstanceManager).load(any(String.class)); when(tierInstanceManager.create(any(ClaudiaData.class),
         * any(String.class), any(TierInstance.class))) .thenReturn(tierInstance);
         * Mockito.doNothing().when(monitoringClient).startMonitoring(any(String.class), any(String.class));
         */

        manager.deleteEnvironment(claudiaData, envInst);

    }

    @Test
    public void testDeployVMNoFIWARE() throws Exception {
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        when(propertiesProvider.getProperty(any(String.class))).thenReturn("1");
        List<String> ips = new ArrayList();
        ips.add("IP");

        String hostname = claudiaData.getService() + "-" + tier.getName() + "-" + 1;
        String fqn = claudiaData.getOrg().replace("_", ".") + ".customers." + claudiaData.getVdc() + ".services."
                + claudiaData.getService() + ".vees." + tier.getName() + ".replicas." + 1;

        VM vm = new VM();

        vm.setFqn(fqn);
        vm.setHostname(hostname);

        when(
                claudiaClient.getIP(any(ClaudiaData.class), any(String.class), Matchers.anyInt(), any(VM.class),
                        anyString())).thenReturn(ips);
        Mockito.doNothing().when(claudiaClient)
                .deployVM(any(ClaudiaData.class), any(TierInstance.class), Matchers.anyInt(), any(VM.class));
        when(ovfUtils.changeInitialResources(any(String.class))).thenReturn("ovf");
        Mockito.doNothing().when(monitoringClient).startMonitoring(any(String.class), any(String.class));

        manager.deployVM(claudiaData, tierInstance, 1, "ovf", vm);
        assertEquals(vm.getDomain(), "");

        assertEquals(vm.getHostname(), hostname);
        assertEquals(vm.getIp(), "IP");

    }

    @Test
    public void testDeployNetwrok() throws Exception {

        Network net = new Network("NET");
        tier.addNetwork(net);
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);

        when(tierInstanceManager.update(any(TierInstance.class))).thenReturn(tierInstance);
        when(networkInstanceManager.create(any(ClaudiaData.class), any(NetworkInstance.class), anyString()))
                .thenReturn(net.toNetworkInstance());
        when(networkManager.load(any(String.class))).thenReturn(net);
        when(tierManager.loadTierWithNetworks(any(String.class), any(String.class), any(String.class))).thenReturn(tier);

        Mockito.doThrow(EntityNotFoundException.class).when(networkInstanceManager).load(any(String.class));

        manager.deployNetworks(claudiaData, tierInstance);

        assertEquals(tierInstance.getNetworkInstances().size(), 1);

    }

    @Test
    public void testDeployNetwrokInternet() throws Exception {

        Network net = new Network("NET");
        Network net2 = new Network("Internet");
        tier.addNetwork(net);
        tier.addNetwork(net2);
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);

        when(tierManager.loadTierWithNetworks(any(String.class), any(String.class), any(String.class))).thenReturn(tier);
        when(tierManager.update(any(Tier.class))).thenReturn(tier);
        when(networkInstanceManager.create(any(ClaudiaData.class), any(NetworkInstance.class), anyString()))
                .thenReturn(net.toNetworkInstance());
        when(networkManager.load(any(String.class))).thenReturn(net);
        Mockito.doThrow(EntityNotFoundException.class).when(networkInstanceManager).load(any(String.class));

        manager.deployNetworks(claudiaData, tierInstance);

        assertEquals(tierInstance.getNetworkInstances().size(), 1);
        assertEquals(tierInstance.getTier().getFloatingip(), "true");

    }

    @Test
    public void testDeleteNetwroksinEnv() throws Exception {

        Network net = new Network("NET");
        Network net2 = new Network("Internet");
        tier.addNetwork(net);
        tier.addNetwork(net2);
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        Environment env = new Environment("name", "description", null);
        env.addTier(tier);
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", env);
        envInst.addTierInstance(tierInstance);
        String region = "RegionOne";

        when(tierInstanceManager.update(any(TierInstance.class))).thenReturn(tierInstance);
        when(networkInstanceManager.create(any(ClaudiaData.class), any(NetworkInstance.class), anyString()))
                .thenReturn(net.toNetworkInstance());

        Mockito.doThrow(EntityNotFoundException.class).when(networkInstanceManager).load(any(String.class));

        manager.deleteNetworksInEnv(claudiaData, envInst, region);

        assertEquals(envInst.getTierInstances().get(0).getNetworkInstances().size(), 0);

    }

}
