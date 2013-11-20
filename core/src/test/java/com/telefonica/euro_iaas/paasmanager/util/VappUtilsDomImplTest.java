/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.junit.Assert.assertEquals;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
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
    private final String vappname = "src/test/resources/4caastservicevapp.xml";
    private final String veename = "src/test/resources/4caastreplicavapp.xml";
    private final String vappname2vms = "src/test/resources/4caastservice2vmvapp.xml";

    private ClaudiaData claudiaData;

    private VappUtilsImpl vappUtilsImpl;
    private SystemPropertiesProvider systemPropertiesProvider;

    private String getFile(String file) throws IOException {
        File f = new File(file);
        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
    }

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
        // assertEquals(ips.get(0).indexOf("109.231.80.82"), -1);

    }

    @Test
    public void testMacrofuntionality() throws Exception {

        Tier tier = new Tier("tomcat", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");

        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);

        Set<Tier> lTier = new HashSet<Tier>();
        lTier.add(tier);

        Environment env = new Environment("name", lTier, "description");
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des", env);

        String ovf1 = null;

        try {
            ovf1 = getFile("src/test/resources/ovfForTier1.xml");

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

        List<TierInstance> tierInstances = new ArrayList();
        tierInstances.add(tierInstance);

        envInst.setTierInstances(tierInstances);

        String test = vappUtilsImpl.getMacroVapp(ovf1, envInst, tierInstance);

    }

    @Test
    public void testSplitService2VMsVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData, vappService2VMs);
        assertEquals(vapps.size(), 2);
    }

    @Test
    public void testSplitVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData, vappService);
        assertEquals(vapps.size(), 1);

    }
}
