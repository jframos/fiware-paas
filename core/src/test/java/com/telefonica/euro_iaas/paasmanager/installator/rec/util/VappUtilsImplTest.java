/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.installator.rec.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 *
 */
public class VappUtilsImplTest {

	String vapp, picId;
	@Before
    public void setUp() throws Exception {
		picId = "servicemixPIC";
				
		InputStream is = 
				ClassLoader.getSystemClassLoader()
				.getResourceAsStream("DeployScenario8.1ACs.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }

        vapp = ruleFile.toString();
		
	}
	
	@Test
	public void testGetACProductSectionsByPicId() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> acs = vappUtils.getACProductSectionsByPicId(vapp, picId);
		assertEquals(4, acs.size());
	}
	
	@Test
	public void testGetPICProductSections() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> pics = vappUtils.getPICProductSections(vapp);
		assertEquals(3, pics.size());
	}
	
	@Test
	public void testGetACProductSections() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> acs = vappUtils.getACProductSections(vapp);
		assertEquals(38, acs.size());
	}
	
	@Test
	public void testGetAppId() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		String appId = vappUtils.getAppId(vapp);
		assertEquals("jonastest5", appId);
	}
	
	@Test
	public void testGetPicId() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> pics = vappUtils.getPICProductSections(vapp);
		
		String picId = vappUtils.getPicId(pics.get(0));
		assertEquals("postgresql_PIC", picId);
	}
	
	@Test
	public void testGetAcId() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> acs = vappUtils.getACProductSections(vapp);
		
		String acId = vappUtils.getAcId(acs.get(0));
		assertEquals("tenantRegistry", acId);
	}
	
	@Test
	public void testGetPicIdFromAC() throws Exception {
		VappUtilsImpl vappUtils = new VappUtilsImpl();
		List<String> acs = vappUtils.getACProductSections(vapp);
		
		String picId = vappUtils.getPicIdFromAC(acs.get(0));
		assertEquals("postgresql_PIC", picId);
	}
}