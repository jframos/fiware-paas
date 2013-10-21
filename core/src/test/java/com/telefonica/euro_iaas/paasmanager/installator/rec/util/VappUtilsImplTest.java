/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.rec.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class VappUtilsImplTest {

    private String picId;
    private String ovf, vapp4Caast;
    private SystemPropertiesProvider systemPropertiesProvider;

    private String getFile(String filename) throws IOException {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
    }

    @Before
    public void setUp() throws Exception {
        picId = "servicemixPIC";

        ovf = getFile("DeployScenario8.1ACs.xml");
        vapp4Caast = getFile("vappsap83.xml");

        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("4Caast");

    }

    @Test
    public void testGetAcId() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> acs = vappUtils.getACProductSections(ovf);

        String acId = vappUtils.getAcId(acs.get(0));
        assertEquals("tenantRegistry", acId);
    }

    @Test
    public void testGetACProductSections() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> acs = vappUtils.getACProductSections(ovf);
        assertEquals(38, acs.size());
    }

    @Test
    public void testGetACProductSectionsByPicId() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> acs = vappUtils.getACProductSectionsByPicId(ovf, picId);
        assertEquals(4, acs.size());
    }

    @Test
    public void testGetAppId() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        String appId = vappUtils.getAppId(ovf);
        assertEquals("jonastest5", appId);
    }

    @Test
    public void testGetIP4Caast() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        vappUtils.setSystemPropertiesProvider(systemPropertiesProvider);
        String ip = vappUtils.getIP(vapp4Caast);
        assertEquals("109.231.80.84", ip);
    }

    @Test
    public void testGetLogin() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        ovf = ovf.replace("ovfenvelope:Property", "Property");
        ovf = ovf.replace("ovfenvelope:value", "value");
        ovf = ovf.replace("ovfenvelope:key", "key");
        String login = vappUtils.getLogin(ovf);
        assertEquals("ubuntu", login);
    }

    @Test
    public void testGetNetworks() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        String network = vappUtils.getNetworks(ovf);
        assertEquals("public", network);
    }

    @Test
    public void testGetPassword() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        ovf = ovf.replace("ovfenvelope:Property", "Property");
        ovf = ovf.replace("ovfenvelope:value", "value");
        ovf = ovf.replace("ovfenvelope:key", "key");
        String password = vappUtils.getPassword(ovf);
        assertEquals("YEDwE4KGc7zMiCRd", password);
    }

    @Test
    public void testGetPicId() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> pics = vappUtils.getPICProductSections(ovf);

        String picId = vappUtils.getPicId(pics.get(0));
        assertEquals("postgresql_PIC", picId);
    }

    @Test
    public void testGetPicIdFromAC() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> acs = vappUtils.getACProductSections(ovf);

        String picId = vappUtils.getPicIdFromAC(acs.get(0));
        assertEquals("postgresql_PIC", picId);
    }

    @Test
    public void testGetPICProductSection() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        String picPS = vappUtils.getPICProductSection("postgresql_PIC", ovf);
        assertEquals(true, picPS.contains("0.0.4"));
    }

    @Test
    public void testGetPICProductSections() throws Exception {
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        List<String> pics = vappUtils.getPICProductSections(ovf);
        assertEquals(3, pics.size());
    }

}
