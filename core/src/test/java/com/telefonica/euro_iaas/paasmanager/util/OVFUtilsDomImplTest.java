/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.util;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;


/**
 * @author jesus.movilla
 *
 */
public class OVFUtilsDomImplTest {

	private String ovf;
	private String ovfname = "4caastovfexample.xml";
	
	@Before
    public void setUp() throws Exception {
		//Taking ovf from a file
		InputStream is = 
				ClassLoader.getSystemClassLoader().getResourceAsStream(ovfname);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer ruleFile = new StringBuffer();
		String actualString;

		while ((actualString = reader.readLine()) != null) {
			ruleFile.append(actualString).append("\n");
		}
		ovf = ruleFile.toString();
		
	}

	@Test
	public void testSplitOvf() throws Exception {
		OVFUtilsDomImpl ovfUtilsDomImpl = new OVFUtilsDomImpl ();
		
		ovfUtilsDomImpl.getOvfsSingleVM(ovf);
	}
}
