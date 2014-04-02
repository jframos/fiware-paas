/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;

public class OVFUtilsDomImplTest {

    String ovfMultipleVMS;
    String ovfOnlyVs;
    // String ovfTomcatVsNoInitial;
    String ovfRECVMName;

    private String getFile(URI file) throws IOException {
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

        URI baseURI = this.getClass().getResource("/").toURI();

        ovfMultipleVMS = getFile(URI.create(baseURI.toString() + "OVFFiwareMultipleVM.xml"));
        ovfOnlyVs = getFile(URI.create(baseURI.toString() + "TomcatOnlyVS.xml"));
        ovfRECVMName = getFile(URI.create(baseURI.toString() + "OVFFiwareRecVMName.xml"));
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

}
