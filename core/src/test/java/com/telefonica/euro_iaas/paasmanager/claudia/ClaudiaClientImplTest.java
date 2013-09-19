/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.claudia;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_BASEURL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_IP;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_PORT;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.claudia.client.OvfInjector;
import com.telefonica.euro_iaas.paasmanager.claudia.impl.ClaudiaClientImpl;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 *
 */
public class ClaudiaClientImplTest {

	/*private String org;
	private String vdc;
	private String service;
	private String vmName;
	
	private ClaudiaUtil claudiaUtil;
	private SystemPropertiesProvider propertiesProvider;
	
	private String vmPayload;
	
	
	@Before
    public void setUp() throws Exception {
		org = "EUROPIAAS-VC1";
		vdc = "paasmanagerVDC";
		service = "paasmanagerService";
		vmName = "paasmanager4";
		
		String vdcUrl = "10.95.171.89:8080/rest-api-management/api/org/EUROPIAAS-VC1/paasmanagerVDC";
		String vdcResponse = "vdcResponse";
		ClientResponse clientResponse = new ClientResponse(0, null, null, null);
		
		claudiaUtil = mock(ClaudiaUtil.class);
		when(claudiaUtil.getUrl(any(ArrayList.class))).thenReturn(vdcUrl);
		when(claudiaUtil.getClaudiaResource(any(String.class), any(String.class))).thenReturn(vdcResponse);
		when(claudiaUtil.postClaudiaResource(any(String.class)
				, any(String.class))).thenReturn(clientResponse);
		
		propertiesProvider = mock(SystemPropertiesProvider.class);
		when(propertiesProvider.getProperty(NEOCLAUDIA_BASEURL)).thenReturn("http://{0}:{1}/rest-api-management{2}");
		when(propertiesProvider.getProperty(NEOCLAUDIA_IP)).thenReturn("10.95.171.89");
		when(propertiesProvider.getProperty(NEOCLAUDIA_PORT)).thenReturn("8080");
		
        vmPayload = new OvfInjector().injectNameOnOvf(
        		vmName, "D:\\TID\\desarrollo\\paas-manager-server\\core\\src\\"
        		+ "test\\resources\\4caastovfexampleSingleVM.ovf","VirtualSystem");
	}	*/
	
	/**
    *
    * @throws Exception
    */
   /*@Test
   public void testBrowseVDC() throws Exception {
	   
	   ClaudiaClientImpl claudiaClient = new ClaudiaClientImpl();
	   claudiaClient.setClaudiaUtil(claudiaUtil);
	   claudiaClient.setSystemPropertiesProvider(propertiesProvider);
	   
	   String vdcResponse = claudiaClient.browseVDC(org, vdc);
	   assertEquals(vdcResponse,"vdcResponse");
   }*/
   
	/**
    *
    * @throws Exception
    */
   /*
   @Test
   public void testDeployVM() throws Exception {	   
	   ClaudiaClientImpl claudiaClient = new ClaudiaClientImpl();
	   claudiaClient.setClaudiaUtil(claudiaUtil);
	   claudiaClient.setSystemPropertiesProvider(propertiesProvider);
		   
	   //claudiaClient.deployService(org, vdc, service, null);
	   System.out.println("payload:" + vmPayload);
	   claudiaClient.deployVM(org, vdc, service, vmPayload);
	}*/
}
