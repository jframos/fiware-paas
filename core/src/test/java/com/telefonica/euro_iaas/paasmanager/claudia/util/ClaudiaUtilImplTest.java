/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.claudia.util;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_ORG;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_SERVICE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.VM_NAME_PREFIX;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_CPU;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_MEM;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_DISK;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_OVFSERVICE_LOCATION;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_OVFVM_LOCATION;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.claudia.client.OvfInjector;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtilImpl;

/**
 * @author jesus.movilla
 * 
 */
public class ClaudiaUtilImplTest {

	/*
	 * private static final String PRIVATE_NETWORK="gestion"; private static
	 * final String PUBLIC_NETWORK="servicio";
	 * 
	 * private static final String CPU_AMOUNT = "12"; private static final
	 * String MEM_AMOUNT = "6145"; private static final String DISK_AMOUNT =
	 * "6145";
	 * 
	 * private String org; private String vdc; private String service; private
	 * String vmName; private String singleVMPayload; private String vdcPayload,
	 * vmPayload; private String servicePayload; private String url;
	 * 
	 * private SystemPropertiesProvider propertiesProvider;
	 * 
	 * @Before public void setUp() throws Exception { org = "EUROPIAAS-VC1"; vdc
	 * = "vdc"; service = "pmServ"; vmName ="paasmanagerVM8"; url =
	 * "http://10.95.171.89:8080/rest-api-management";
	 * 
	 * 
	 * InputStream is =ClassLoader.getSystemClassLoader().getResourceAsStream(
	 * "InstantiateVDCTemplate.xml"); BufferedReader reader = new
	 * BufferedReader(new InputStreamReader(is)); StringBuffer ruleFile = new
	 * StringBuffer(); String actualString;
	 * 
	 * while ((actualString = reader.readLine()) != null) {
	 * ruleFile.append(actualString).append("\n"); }
	 * 
	 * vdcPayload = ruleFile.toString() .replace("${vdcName}", vdc)
	 * .replace("${vdcDescription}", "vdcDescription")
	 * .replace("${cpuLimit}",CPU_AMOUNT) .replace("${memLimit}",MEM_AMOUNT)
	 * .replace("${diskLimit}",DISK_AMOUNT);
	 * 
	 * 
	 * servicePayload = new OvfInjector().injectNameOnOvf( service,
	 * "D:\\TID\\desarrollo\\paas-manager-server\\core\\src\\" +
	 * "test\\resources\\empty.ovf","VirtualSystemCollection");
	 * 
	 * 
	 * singleVMPayload = new OvfInjector().injectNameOnOvf( vmName,
	 * "D:\\TID\\desarrollo\\paas-manager-server\\core\\src\\" +
	 * "test\\resources\\4caastovfexampleSingleVMSplitted.xml","VirtualSystem");
	 * 
	 * vmPayload = singleVMPayload;
	 * 
	 * }
	 */

	/**
	 * 
	 * @throws Exception
	 */
	/*
	 * @Test public void testGetUrl() throws Exception { List<String> parameters
	 * = new ArrayList<String>(); parameters.add(org); parameters.add(vdc);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl(); String vdcUrl =
	 * claudiaUtil.getUrl(parameters); System.out.println("vdcUrl:" + vdcUrl);
	 * assertEquals(true,
	 * vdcUrl.contains("http://10.95.171.89:8080/rest-api-management/api/org/" +
	 * org + "/vdc/" + vdc ));
	 * 
	 * parameters.add(service); String serviceUrl =
	 * claudiaUtil.getUrl(parameters); System.out.println("serviceUrl:" +
	 * serviceUrl); assertEquals(true,
	 * serviceUrl.contains("http://10.95.171.89:8080/rest-api-management/api/org/"
	 * + org + "/vdc/" + vdc + "/vapp/" + service)); }
	 */

	/*
	 * @Test public void testDeployVDCClaudia() throws Exception {
	 * System.out.println("******DEPLOYVDC**********"); String actionUri =
	 * URICreation.getURIOrg(URICreation.getFQN(org)) +
	 * URICreation.URI_VDC_ADD_MODIFIER; url = url + actionUri;
	 * System.out.println("url: " + url); System.out.println("vdcPayload: " +
	 * vdcPayload);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl(); ClientResponse
	 * response = claudiaUtil.postClaudiaResource(url, vdcPayload);
	 * 
	 * System.out.println(response.getEntity(String.class));
	 * System.out.println("******END DEPLOYVDC**********"); //assertEquals(true,
	 * vdcResponse.contains("AvailableNetworks" )); }
	 */

	/**
	 * 
	 * @throws Exception
	 */
	/*
	 * @Test public void testGetVDCClaudia() throws Exception {
	 * System.out.println("******GETVDC**********"); List<String> parameters =
	 * new ArrayList<String>(); parameters.add(org); parameters.add(vdc);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl(); String vdcUrl =
	 * claudiaUtil.getUrl(parameters); String vdcResponse =
	 * claudiaUtil.getClaudiaResource(vdcUrl, "application/xml");
	 * 
	 * System.out.println("vdcResponse:" + vdcResponse); assertEquals(true,
	 * vdcResponse.contains("AvailableNetworks" ));
	 * System.out.println("******GETVDC**********"); }
	 */

	/**
	 * 
	 * @throws Exception
	 */
	/*
	 * @Test public void testDeployEmptyServiceClaudia() throws Exception {
	 * 
	 * System.out.println("******DEPLOYVSERVICE**********"); String actionUri =
	 * URICreation.getURIServiceAdd(URICreation.getFQN(org,vdc)); url = url +
	 * actionUri; System.out.println("url: " + url);
	 * System.out.println("servicePayload: " + servicePayload);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl(); ClientResponse
	 * response = claudiaUtil.postClaudiaResource(url, servicePayload);
	 * 
	 * System.out.println(response.getEntity(String.class));
	 * //assertEquals(true, vdcResponse.contains("AvailableNetworks" ));
	 * System.out.println("******END DEPLOYSERVICE**********"); }
	 * 
	 * @Test public void testDeployVMClaudia() throws Exception {
	 * 
	 * System.out.println("******DEPLOYVM**********");
	 * 
	 * String actionUri =
	 * URICreation.getURIVEEReplicaAdd(URICreation.getFQN(org,vdc,service)); url
	 * = url + actionUri; System.out.println("url: " + url);
	 * System.out.println("vmPayload: " + vmPayload);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl(); ClientResponse
	 * response = claudiaUtil.postClaudiaResource(url, vmPayload);
	 * System.out.println("******END DEPLOYVM**********");
	 * System.out.println(response.getEntity(String.class));
	 * System.out.println("******END DEPLOYVM**********"); //assertEquals(true,
	 * vdcResponse.contains("AvailableNetworks" )); }
	 */

	/*
	 * @Test public void testUnDeployVMClaudia() throws Exception {
	 * System.out.println("******UNDEPLOYVM**********"); String actionUri =
	 * URICreation.getURIVapp(org + "." + URICreation.FQN_SEPARATOR_VDC + "." +
	 * vdc + "."+ URICreation.FQN_SEPARATOR_SERVICE + "." + service+"."
	 * +URICreation.FQN_SEPARATOR_VEE+"." + vmName); url = url + actionUri;
	 * System.out.println("url: " + url);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();
	 * claudiaUtil.deleteClaudiaResource(url);
	 * System.out.println("******END UNDEPLOYVM**********");
	 * 
	 * //assertEquals(true, vdcResponse.contains("AvailableNetworks" )); }
	 * 
	 * @Test public void testUnDeployServiceClaudia() throws Exception {
	 * System.out.println("******UNDEPLOYSERVICE**********");
	 * 
	 * String actionUri = URICreation.getURIService(org + "." +
	 * URICreation.FQN_SEPARATOR_VDC + "." + vdc + "."+
	 * URICreation.FQN_SEPARATOR_SERVICE + "." + service); url = url +
	 * actionUri; System.out.println("url: " + url);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();
	 * claudiaUtil.deleteClaudiaResource(url);
	 * System.out.println("******END UNDEPLOYSERVICE**********");
	 * 
	 * //assertEquals(true, vdcResponse.contains("AvailableNetworks" )); }
	 * 
	 * @Test public void testUnDeployVDCClaudia() throws Exception {
	 * System.out.println("******UNDEPLOYVDC**********"); String actionUri =
	 * URICreation.getURIVDC(org + "." + URICreation.FQN_SEPARATOR_VDC + "." +
	 * vdc); url = url + actionUri; System.out.println("url: " + url);
	 * 
	 * ClaudiaUtilImpl claudiaUtil = new ClaudiaUtilImpl();
	 * claudiaUtil.deleteClaudiaResource(url);
	 * System.out.println("******END UNDEPLOYVDC**********");
	 * 
	 * //assertEquals(true, vdcResponse.contains("AvailableNetworks" )); }
	 * 
	 * @Test public void testpostClaudiaResource() throws Exception {
	 * 
	 * ClaudiaUtilImpl claudiaUtilImpl = new ClaudiaUtilImpl(); String url =
	 * "http://10.95.171.89:8080/rest-api-management/api/org" +
	 * "/EUROPIAAS-VC1/vdc/vdc/vapp/pmServ/action" + "/instantiateOvf";
	 * claudiaUtilImpl.postClaudiaResource(url,singleVMPayload); }
	 */
}
