package com.telefonica.euro_iaas.paasmanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OVFUtilsDomImplTest {

    String ovfMultipleVMS;
    String ovfOnlyVs;
    // String ovfTomcatVsNoInitial;
    String ovfRECVMName;

    @Before
    public void setUp() throws Exception {

        ovfMultipleVMS = getFile("src/test/resources/OVFFiwareMultipleVM.xml");
        ovfOnlyVs = getFile("src/test/resources/TomcatOnlyVS.xml");
        // ovfTomcatVsNoInitial = getFile("src/test/resources/tomcatVsNoInitial.xml");
        ovfRECVMName = getFile("src/test/resources/OVFFiwareRecVMName.xml");
    }

    @Test
    public void testChangeInitialResources() {

        OVFUtilsDomImpl manager = new OVFUtilsDomImpl();
        String out = manager.changeInitialResources(ovfOnlyVs);
    }

    @Test
    public void testGetOvfsSingleVM() {
        OVFUtilsDomImpl manager = new OVFUtilsDomImpl();
        List<String> ovfs = new ArrayList<String>();
        try {
            ovfs = manager.getOvfsSingleVM(ovfMultipleVMS);
        } catch (InvalidOVFException e) {
            assertTrue(false);
        }
        assertEquals(ovfs.size(), 3);
        System.out.println(ovfs.get(0));
    }

    @Test
    public void testGetServiceName() {
        OVFUtilsDomImpl manager = new OVFUtilsDomImpl();
        String serviceName = null;
        try {
            serviceName = manager.getServiceName(ovfMultipleVMS);
        } catch (InvalidOVFException e) {
            assertTrue(false);
        }
        assertEquals(serviceName, "contextbrokermongo2");
    }

    @Test
    public void testGetRECVMNameFromProductSection() {
        OVFUtilsDomImpl manager = new OVFUtilsDomImpl();
        String vmName = null;
        try {
            vmName = manager.getRECVMNameFromProductSection(ovfRECVMName);
        } catch (InvalidOVFException e) {
            assertTrue(false);
        }
        assertEquals(vmName, "mongoconfig");
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

}
