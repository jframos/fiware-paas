/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 */
public class VappUtilsNeoClaudiaOpenStackImplTest extends TestCase {

    private String vappReplica;
    private final String vappNeoclaudiaTwoIps = "/VappNeoclaudiaTwoIps.xml";

    private VappUtilsNeoClaudiaOpenStackImpl vappUtilsNeoClaudiaOpenStackImpl;
    private SystemPropertiesProvider systemPropertiesProvider;

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

    @Override
    @Before
    public void setUp() throws Exception {
        vappReplica = getFile(this.getClass().getResource(vappNeoclaudiaTwoIps).toURI());
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        // when
        // (systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM)).thenReturn("FIWARE");
        vappUtilsNeoClaudiaOpenStackImpl = new VappUtilsNeoClaudiaOpenStackImpl();
        vappUtilsNeoClaudiaOpenStackImpl.setSystemPropertiesProvider(systemPropertiesProvider);
    }

    @Test
    public void testGetIps() throws Exception {
        List<String> ips = vappUtilsNeoClaudiaOpenStackImpl.getIP(vappReplica);
        // Private ip
        assertEquals(ips.get(0), "172.30.5.29");
        // Public ip
        assertEquals(ips.get(1), "130.206.82.72");
    }

}
