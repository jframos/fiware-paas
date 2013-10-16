/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * @author jesus.movilla
 */
public class VappUtilsDomImplTest {

    private String vappService;
    private String vappReplica;
    private String vappService2VMs;
    private String vappname = "/4caastservicevapp.xml";
    private String veename = "/4caastreplicavapp.xml";
    private String vappname2vms = "/4caastservice2vmvapp.xml";

    private ClaudiaData claudiaData;

    private VappUtilsImpl vappUtilsImpl;
    private SystemPropertiesProvider systemPropertiesProvider;

    @Before
    public void setUp() throws Exception {

        vappService = getFile(vappname);
        vappReplica = getFile(veename);
        vappService2VMs = getFile(vappname2vms);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("");
        vappUtilsImpl = new VappUtilsImpl();
        vappUtilsImpl.setSystemPropertiesProvider(systemPropertiesProvider);

    }

    @Test
    public void testGetFqnId() throws Exception {
        String fqn = vappUtilsImpl.getFqnId(vappReplica);
        assertEquals(fqn, "4caast.customers.test9.services.jonastest5.vees.jonas5.replicas.1");

    }

    @Test
    public void testGetIP() throws Exception {

        List<String> ips = vappUtilsImpl.getIP(vappReplica);
        assertEquals(ips.get(0), "109.231.80.82");

    }

    @Test
    public void testSplitVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData, vappService);
        assertEquals(vapps.size(), 1);

    }

    @Test
    public void testSplitService2VMsVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData, vappService2VMs);
        assertEquals(vapps.size(), 2);
    }

    private String getFile(String file) throws IOException {
        // File f = new File(file);
        // System.out.println(f.isFile() + " " + f.getAbsolutePath());
        InputStream is = this.getClass().getResourceAsStream(file);
        // InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
    }

    @Ignore("file ovfForTier1.xml not found")
    @Test
    public void testMacrofuntionality() throws Exception {

        Tier tier = new Tier("tomcat", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");

        Tier tier2 = new Tier("mysql", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        TierInstance tierInstance2 = new TierInstance();
        tierInstance2.setTier(tier2);

        List<Tier> lTier = new ArrayList();
        lTier.add(tier);
        lTier.add(tier2);
        Environment env = new Environment("name", lTier, "description");
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", env);

        String ovf1 = null;
        String ovf2 = null;
        try {
            ovf1 = getFile("/ovfForTier1.xml");
            ovf2 = getFile("/ovfForTier2.xml");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        VM vm = new VM();
        vm.addNetwork("public", "IP1");

        VM vm2 = new VM();
        vm.addNetwork("public", "IP2");

        tierInstance.setVM(vm);
        tierInstance.setOvf(ovf1);
        tierInstance.setVapp("vapp");
        tierInstance2.setVM(vm2);
        tierInstance2.setOvf(ovf2);
        tierInstance2.setVapp("vapp");

        List<TierInstance> tierInstances = new ArrayList();
        tierInstances.add(tierInstance);
        tierInstances.add(tierInstance2);
        envInst.setTierInstances(tierInstances);

        String test = vappUtilsImpl.getMacroVapp(ovf1, envInst);
        System.out.println(test);

    }
}
