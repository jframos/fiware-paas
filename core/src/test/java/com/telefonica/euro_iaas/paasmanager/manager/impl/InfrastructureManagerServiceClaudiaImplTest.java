/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.monitoring.MonitoringClient;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.VappUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InfrastructureManagerServiceClaudiaImplTest extends TestCase {

    private InfrastructureManagerServiceClaudiaImpl manager;
    private ClaudiaClient claudiaClient;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;
    private ClaudiaUtil claudiaUtil;
    private MonitoringClient monitoringClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private VappUtils vappUtils;
    private OVFUtils ovfUtils;
    private EnvironmentUtils environmentUtils;
    private TierInstanceManager tierInstanceManager;
    private TierInstanceDao tierInstanceDao;
    private final List<String> ips = new ArrayList<String>();
    private Environment envResult;
    private TierInstance tierInstanceDB;
    private ClaudiaData claudiaData;

    @Test
    public void getVMFromVapp() {

        String fqn = "org.customers.vdc.services.vapp.vees.tierName.replicas.1";
        try {
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getFqnId(any(String.class))).thenReturn(fqn);
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String vapp = "";

        VM vm = null;
        try {
            vm = manager.getVMFromVapp(vapp);
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(vm.getFqn(), fqn);
        assertEquals(vm.getHostname(), "tierName");
        assertEquals(vm.getIp(), "10.99.88.33");
    }

    @Override
    @Before
    public void setUp() throws Exception {

        ips.add("10.99.88.33");
        claudiaData = new ClaudiaData("dd", "2", "service");

        manager = new InfrastructureManagerServiceClaudiaImpl();
        claudiaClient = mock(ClaudiaClient.class);
        manager.setClaudiaClient(claudiaClient);
        claudiaResponseAnalyser = mock(ClaudiaResponseAnalyser.class);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);
        claudiaUtil = mock(ClaudiaUtil.class);
        manager.setClaudiaUtil(claudiaUtil);
        monitoringClient = mock(MonitoringClient.class);
        manager.setMonitoringClient(monitoringClient);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        manager.setSystemPropertiesProvider(systemPropertiesProvider);
        ovfUtils = mock(OVFUtils.class);
        manager.setOvfUtils(ovfUtils);
        vappUtils = mock(VappUtils.class);
        manager.setVappUtils(vappUtils);

        environmentUtils = mock(EnvironmentUtils.class);
        manager.setEnvironmentUtils(environmentUtils);
        tierInstanceManager = mock(TierInstanceManager.class);
        manager.setTierInstanceManager(tierInstanceManager);

        envResult = new Environment();
        envResult.setName("environemntName");

        envResult.setOvf("ovf");
        Tier tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease = new ProductRelease("product", "2.0");
        productReleases.add(productRelease);
        tier.setProductReleases(productReleases);

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        envResult.setTiers(tiers);

        VM vm = new VM(
                "FIWARE.customers.ebe6d9ec7b024361b7a3882c65a57dda.services.contextbrokermongo2.vees.contextbroker",
                "ip", "hostname", "domain");
        tierInstanceDB = new TierInstance();
        tierInstanceDB.setTier(tier);
        tierInstanceDB.setName("name");
        tierInstanceDB.setVM(vm);

    }

    @Test
    public void testCreateAndDeleteInfrasctuctureEnvironmentInstance() {

        String fqn = "org.customers.vdc.services.vapp.vees.tierName.replicas.1";
        List<String> vapps = new ArrayList();
        vapps.add("ddd");

        List<String> ovfs = new ArrayList();

        ovfs.add("ovf:VirtualSystem ovf:id=\"tierName\"");

        try {
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getFqnId(any(String.class))).thenReturn(fqn);
            when(vappUtils.getReplica(any(String.class))).thenReturn("1");
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");
            when(vappUtils.getVappsSingleVM(any(ClaudiaData.class), any(String.class))).thenReturn(vapps);
            when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);
            try {
                when(tierInstanceManager.load(any(String.class))).thenReturn(tierInstanceDB);
            } catch (com.telefonica.euro_iaas.commons.dao.EntityNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("");
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String ovf = "ss";
        String vapp = "ss";
        try {
            when(claudiaClient.deployServiceFull(any(ClaudiaData.class), any(String.class))).thenReturn("String");
        } catch (InfrastructureException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        when(claudiaResponseAnalyser.getTaskUrl(any(String.class))).thenReturn("String");
        when(claudiaResponseAnalyser.getTaskStatus(any(String.class))).thenReturn("sucess");
        try {
            when(claudiaClient.browseService(any(ClaudiaData.class))).thenReturn(vapp);
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EnvironmentInstance instance = new EnvironmentInstance();
        instance.setVdc("vdc");
        instance.setEnvironment(envResult);
        try {
            instance = manager.createInfrasctuctureEnvironmentInstance(instance, instance.getEnvironment().getTiers(),
                    claudiaData);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Mockito.doNothing().doThrow(new RuntimeException()).when(claudiaClient)
                    .undeployVMReplica(any(ClaudiaData.class), any(TierInstance.class));
        } catch (InfrastructureException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Mockito.doNothing().doThrow(new RuntimeException()).when(monitoringClient).stopMonitoring(any(String.class));
        try {
            manager.deleteEnvironment(claudiaData, instance);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testCreateInfrasctuctureEnvironmentInstance() {

        String fqn = "org.customers.vdc.services.vapp.vees.tierName.replicas.1";
        List<String> vapps = new ArrayList();
        vapps.add("ddd");

        List<String> ovfs = new ArrayList();

        ovfs.add("ovf:VirtualSystem ovf:id=\"tierName\"");

        try {
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getFqnId(any(String.class))).thenReturn(fqn);
            when(vappUtils.getReplica(any(String.class))).thenReturn("1");
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");
            when(vappUtils.getVappsSingleVM(any(ClaudiaData.class), any(String.class))).thenReturn(vapps);
            when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("");
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String ovf = "";
        String vapp = "";
        try {
            when(claudiaClient.deployServiceFull(any(ClaudiaData.class), any(String.class))).thenReturn("String");
        } catch (InfrastructureException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        when(claudiaResponseAnalyser.getTaskUrl(any(String.class))).thenReturn("String");
        when(claudiaResponseAnalyser.getTaskStatus(any(String.class))).thenReturn("sucess");
        try {
            when(claudiaClient.browseService(any(ClaudiaData.class))).thenReturn(vapp);
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", envResult);
        EnvironmentInstance instance = null;
        try {
            instance = manager.createInfrasctuctureEnvironmentInstance(envInst, envInst.getEnvironment().getTiers(),
                    claudiaData);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(instance.getName(), claudiaData.getVdc() + "-" + envResult.getName());
        assertEquals(instance.getEnvironment(), envResult);
        assertEquals(instance.getTierInstances().size(), 1);

        assertEquals(instance.getBlueprintName(), "blue");
        // assertEquals(envInst2.getName(),
        // claudiaData.getVdc()+"-"+tier.getName());
        assertEquals(instance.getTierInstances().size(), 1);
        // assertEquals(instance.getTierInstances().get(0).getTier().getName(),
        // envResult.getTiers().get(0).getName());
        // assertEquals(envInst2.getTierInstances().get(0).getVM().getHostname(),
        // hostname);
        // verify(tierInstanceManager, times(1)).create(tierInstance);

    }

    @Test
    public void testCreateTierInstanceFromVapp() {

        String fqn = "org.customers.vdc.services.vapp.vees.tierName.replicas.1";
        HashMap<String, String> nets = new HashMap();
        nets.put("public", "10.99.88.33");

        try {
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getFqnId(any(String.class))).thenReturn(fqn);
            when(vappUtils.getReplica(any(String.class))).thenReturn("1");
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");
            when(vappUtils.getNetworkAndIP(any(String.class))).thenReturn(nets);

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String vapp = "";
        TierInstance tierInstance = null;

        try {
            tierInstance = manager.createTierInstanceFromVapp(envResult.getTiers().get(0), envResult.getName(),
                    claudiaData, vapp, "ovf");
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(tierInstance.getName(), envResult.getName() + "-" + envResult.getTiers().get(0).getName() + "-1");
        assertEquals(tierInstance.getNumberReplica(), 1);
        assertEquals(tierInstance.getVM().getHostname(), "tierName");
        assertEquals(tierInstance.getVM().getNetworks().get("public"), "10.99.88.33");

    }

    @Test
    public void testDeployVDC() {

        try {
            when(claudiaClient.browseVDC(any(ClaudiaData.class))).thenReturn("OK");
        } catch (ClaudiaResourceNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            manager.deployVDC(claudiaData);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testFromVappToListTierInstance() {

        String fqn = "org.customers.vdc.services.vapp.vees.tierName.replicas.1";
        List<String> vapps = new ArrayList();
        vapps.add("ddd");
        List<String> ovfs = new ArrayList();
        ovfs.add("ovf:VirtualSystem ovf:id=\"tierName\"");
        try {
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getFqnId(any(String.class))).thenReturn(fqn);
            when(vappUtils.getReplica(any(String.class))).thenReturn("1");
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");
            when(vappUtils.getVappsSingleVM(any(ClaudiaData.class), any(String.class))).thenReturn(vapps);
            when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String vapp = "";
        List<TierInstance> tierInstances = null;

        try {
            tierInstances = manager.fromVappToListTierInstance(vapp, envResult, claudiaData);
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(tierInstances.size(), 1);
        assertEquals(tierInstances.get(0).getTier().getName(), "tierName");

    }

    @Test
    public void testGetProcessService(String vappService) {

        List<String> vapps = new ArrayList();
        vapps.add("ddd");

        try {
            when(vappUtils.getVappsSingleVM(any(ClaudiaData.class), any(String.class))).thenReturn(vapps);
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
            when(vappUtils.getReplica(any(String.class))).thenReturn("1");
            when(vappUtils.getVMName(any(String.class))).thenReturn("tierName");
            when(claudiaUtil.getUrl(any(List.class))).thenReturn("url");
            when(claudiaUtil.getClaudiaResource(any(PaasManagerUser.class), any(String.class), any(String.class)))
                    .thenReturn("url");

        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URLNotRetrievedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClaudiaRetrieveInfoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            manager.getProcessService(claudiaData);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testGetProcessVapp(String vappService) {

        List<String> vapps = new ArrayList();
        vapps.add("ddd");

        try {
            when(vappUtils.getVappsSingleVM(any(ClaudiaData.class), any(String.class))).thenReturn(vapps);
            when(vappUtils.getIP(any(String.class))).thenReturn(ips);
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            manager.getProcessVapp(any(ClaudiaData.class), vappService);
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * @Test public void testDeployService() { ClaudiaData claudiaData = new ClaudiaData ("dd", "2"); try {
     * when(claudiaClient.browseService(any(ClaudiaData.class))).thenThrow(new ClaudiaResourceNotFoundException ("d"));
     * } catch (ClaudiaResourceNotFoundException e1) { // TODO Auto-generated catch block e1.printStackTrace(); } String
     * ovf =""; String vapp = ""; when(claudiaClient.deployServiceFull(any(ClaudiaData.class),
     * any(String.class))).thenReturn("String"); when(claudiaResponseAnalyser.getTaskUrl
     * (any(String.class))).thenReturn("String"); when(claudiaResponseAnalyser.getTaskStatus
     * (any(String.class))).thenReturn("sucess"); try { when (claudiaUtil.getClaudiaResource(any(PaasManagerUser.class),
     * any(String.class), any(String.class))).thenReturn(""); } catch (ClaudiaRetrieveInfoException e1) { // TODO
     * Auto-generated catch block e1.printStackTrace(); } catch (ClaudiaResourceNotFoundException e1) { // TODO
     * Auto-generated catch block e1.printStackTrace(); } try { manager.deployService(claudiaData,ovf ); } catch
     * (InfrastructureException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
     */

}
