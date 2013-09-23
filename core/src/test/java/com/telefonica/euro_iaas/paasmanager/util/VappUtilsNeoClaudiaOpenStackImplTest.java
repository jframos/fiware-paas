/**
 * 
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
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import junit.framework.TestCase;

/**
 * @author jesus.movilla
 * 
 */
public class VappUtilsNeoClaudiaOpenStackImplTest extends TestCase {

	private String vappReplica;
	private String VappNeoclaudiaTwoIps = "src/test/resources/VappNeoclaudiaTwoIps.xml";

	private VappUtilsNeoClaudiaOpenStackImpl vappUtilsNeoClaudiaOpenStackImpl;
	private SystemPropertiesProvider systemPropertiesProvider;

	@Before
	public void setUp() throws Exception {
		vappReplica = getFile(VappNeoclaudiaTwoIps);
		systemPropertiesProvider = mock(SystemPropertiesProvider.class);
		// when
		// (systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
		when(
				systemPropertiesProvider
						.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM))
				.thenReturn("FIWARE");
		vappUtilsNeoClaudiaOpenStackImpl = new VappUtilsNeoClaudiaOpenStackImpl();
		vappUtilsNeoClaudiaOpenStackImpl
				.setSystemPropertiesProvider(systemPropertiesProvider);
	}

	@Test
	public void testGetIps() throws Exception {
		List<String> ips = vappUtilsNeoClaudiaOpenStackImpl.getIP(vappReplica);
		// Private ip
		assertEquals(ips.get(0), "172.30.5.29");
		// Public ip
		assertEquals(ips.get(1), "130.206.82.72");
	}

	@Test
	public void testGetVappsSingleVM() throws Exception {

	}

	private String getFile(String file) throws IOException {
		File f = new File(file);
		System.out.println(f.isFile() + " " + f.getAbsolutePath());

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
