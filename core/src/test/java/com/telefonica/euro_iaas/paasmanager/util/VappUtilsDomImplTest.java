/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


<<<<<<< HEAD
=======
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
/**
 * @author jesus.movilla
 */
<<<<<<< HEAD
public class VappUtilsDomImplTest extends TestCase {
=======
public class VappUtilsDomImplTest {
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de

    private String vappService;
    private String vappReplica;
    private String vappService2VMs;
<<<<<<< HEAD
    private final String vappname = "src/test/resources/4caastservicevapp.xml";
    private final String veename = "src/test/resources/4caastreplicavapp.xml";
    private final String vappname2vms = "src/test/resources/4caastservice2vmvapp.xml";
=======
    private String vappname = "/4caastservicevapp.xml";
    private String veename = "/4caastreplicavapp.xml";
    private String vappname2vms = "/4caastservice2vmvapp.xml";
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de

    private ClaudiaData claudiaData;

    private VappUtilsImpl vappUtilsImpl;
    private SystemPropertiesProvider systemPropertiesProvider;

<<<<<<< HEAD
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

    @Override
=======
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
    @Before
    public void setUp() throws Exception {

        vappService = getFile(vappname);
        vappReplica = getFile(veename);
        vappService2VMs = getFile(vappname2vms);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
<<<<<<< HEAD
        when(systemPropertiesProvider.getProperty(any(String.class)))
        .thenReturn("");
=======
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("");
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
        vappUtilsImpl = new VappUtilsImpl();
        vappUtilsImpl.setSystemPropertiesProvider(systemPropertiesProvider);

    }

    @Test
    public void testGetFqnId() throws Exception {
        String fqn = vappUtilsImpl.getFqnId(vappReplica);
<<<<<<< HEAD
        assertEquals(fqn,
        "4caast.customers.test9.services.jonastest5.vees.jonas5.replicas.1");
=======
        assertEquals(fqn, "4caast.customers.test9.services.jonastest5.vees.jonas5.replicas.1");
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de

    }

    @Test
    public void testGetIP() throws Exception {

        List<String> ips = vappUtilsImpl.getIP(vappReplica);
        assertEquals(ips.get(0), "109.231.80.82");

    }

    @Test
<<<<<<< HEAD
    public void testMacrofuntionality() throws Exception {

        Tier tier = new Tier("tomcat", new Integer(1), new Integer(1),
                new Integer(1), null, "flavour", "image", "icono", "keypair",
                "floatingip", "payload");

        Tier tier2 = new Tier("mysql", new Integer(1), new Integer(1),
                new Integer(1), null, "flavour", "image", "icono", "keypair",
                "floatingip", "payload");
=======
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

    @Ignore("file ovfForTier2.xml not found")
    @Test
    public void testMacrofuntionality() throws Exception {

        Tier tier = new Tier("tomcat", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");

        Tier tier2 = new Tier("mysql", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        TierInstance tierInstance2 = new TierInstance();
        tierInstance2.setTier(tier2);

        List<Tier> lTier = new ArrayList();
        lTier.add(tier);
        lTier.add(tier2);
        Environment env = new Environment("name", lTier, "description");
<<<<<<< HEAD
        EnvironmentInstance envInst = new EnvironmentInstance("blue", "des",
                env);



        String ovf1 = null;
        String ovf2 = null;

        ovf1 = getFile("src/test/resources/ovfForTier1.xml");


=======
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
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
        VM vm = new VM();
        vm.addNetwork("public", "IP1");

        VM vm2 = new VM();
        vm.addNetwork("public", "IP2");

<<<<<<< HEAD

=======
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
        tierInstance.setVM(vm);
        tierInstance.setOvf(ovf1);
        tierInstance.setVapp("vapp");
        tierInstance2.setVM(vm2);
        tierInstance2.setOvf(ovf2);
        tierInstance2.setVapp("vapp");

<<<<<<< HEAD
        List<TierInstance> tierInstances = new ArrayList ();
=======
        List<TierInstance> tierInstances = new ArrayList();
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de
        tierInstances.add(tierInstance);
        tierInstances.add(tierInstance2);
        envInst.setTierInstances(tierInstances);

<<<<<<< HEAD

        String test = vappUtilsImpl.getMacroVapp(ovf1, envInst);
        System.out.println (test);



    }

    @Test
    public void testSplitService2VMsVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData,
                vappService2VMs);
        assertEquals(vapps.size(), 2);
    }

    @Test
    public void testSplitVApp() throws Exception {

        List<String> vapps = vappUtilsImpl.getVappsSingleVM(claudiaData,
                vappService);
        assertEquals(vapps.size(), 1);
=======
        String test = vappUtilsImpl.getMacroVapp(ovf1, envInst);
        System.out.println(test);
>>>>>>> 8869d952aa586c4efac07ce7a4426dc7dbe602de

    }
}
