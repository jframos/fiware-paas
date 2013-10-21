/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
import java.util.ArrayList;
import java.util.List;

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
            payload = getFile("src/test/resources/serveraltiersOVF.xml");

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
        List<Tier> ltier = manager.getTiers(payload, "vdc");
        assertEquals(ltier.size(), 3);
        Tier tier0 = ltier.get(0);
        assertEquals(tier0.getName(), "5FlexVM1");
        assertEquals(tier0.getInitialNumberInstances(), new Integer(1));
        assertEquals(tier0.getMaximumNumberInstances(), new Integer(1));
        assertEquals(tier0.getInitialNumberInstances(), new Integer(1));
        assertNotSame(tier0.getPayload(), "2");

        Tier tier1 = ltier.get(1);
        assertEquals(tier1.getName(), "5haproxy");
        assertEquals(tier1.getInitialNumberInstances(), new Integer(1));
        assertEquals(tier1.getMaximumNumberInstances(), new Integer(1));
        assertEquals(tier1.getInitialNumberInstances(), new Integer(1));
        assertNotSame(tier1.getPayload(), "");

        Tier tier2 = ltier.get(2);
        assertEquals(tier2.getName(), "5FlexVM2");
        assertEquals(tier2.getInitialNumberInstances(), new Integer(1));
        assertEquals(tier2.getMaximumNumberInstances(), new Integer(5));
        assertEquals(tier2.getInitialNumberInstances(), new Integer(1));
        assertNotSame(tier2.getPayload(), "");

    }

    @Test
    public void getProductRelease() throws Exception {

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

    public Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }
}
