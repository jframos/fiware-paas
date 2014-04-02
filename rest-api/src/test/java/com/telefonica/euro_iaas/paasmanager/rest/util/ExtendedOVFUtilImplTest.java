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

package com.telefonica.euro_iaas.paasmanager.rest.util;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;

/**
 * @author jesus.movilla
 */
public class ExtendedOVFUtilImplTest extends TestCase {

    private ClaudiaUtil claudiaUtil;
    private ExtendedOVFUtilImpl manager;
    private OVFUtils ovfUtils;

    @Before
    public void setUp() throws Exception {
        manager = new ExtendedOVFUtilImpl();
        claudiaUtil = mock(ClaudiaUtil.class);
        ovfUtils = mock(OVFUtils.class);
        manager.setOvfUtils(ovfUtils);
    }

    @Test
    public void getEnvironmentNameOK() throws Exception {

        // ExtendedOVFUtilImpl extendedOVF
    }

    @Test
    public void testGetTiersOK() throws Exception {

        manager.setClaudiaUtil(claudiaUtil);

        String payload = null;
        try {
            URI url = this.getClass().getResource("/serveraltiersOVF.xml").toURI();
            payload = getFile(url);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document doc = stringToDom(payload);
        when(claudiaUtil.stringToDom(any(String.class))).thenReturn(doc);
        List<String> ovfs = new ArrayList();
        ovfs.add("ovf");
        ovfs.add("ovf");
        ovfs.add("ovf");
        when(ovfUtils.getOvfsSingleVM(any(String.class))).thenReturn(ovfs);
        Set<Tier> ltier = manager.getTiers(payload, "vdc");
        assertEquals(ltier.size(), 3);

    }

    @Test
    public void getProductRelease() throws Exception {

    }

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

    public Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }
}
