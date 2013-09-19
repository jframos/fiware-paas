/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OSNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.VMStatusNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.FileUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * @author jesus.movilla
 *
 */
public class ClaudiaDummyClientImpl implements ClaudiaClient {

	private SystemPropertiesProvider systemPropertiesProvider;
	private FileUtils fileUtils = null;


	public String OnOffScalability(ClaudiaData claudiaData, 
			String environmentName, boolean b) 
			throws InfrastructureException {		
		return null;
	}

	
	public String browseService(ClaudiaData claudiaData) 
			throws ClaudiaResourceNotFoundException {
		String payload = null;
		try {
			payload = 
					fileUtils.readFile(systemPropertiesProvider
							.getProperty("neoclaudiaVappVMLocation"));
			} catch (FileUtilsException e) {
				throw new ClaudiaResourceNotFoundException(
						"Error in the Claudia Dummy Utils "
								+ e.getMessage());
			}
			return payload;
	}

	
	public String browseVDC(ClaudiaData claudiaData)
			throws ClaudiaResourceNotFoundException {
		// TODO Auto-generated method stub
		return "OK";
	}

	
	public String browseVM(String org, String vdc, String service, String vm,
			PaasManagerUser user)
			throws ClaudiaResourceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String browseVMReplica(ClaudiaData claudiaData, String replica) 
				throws ClaudiaResourceNotFoundException {
		
	    String payload = null;
	    String ip =systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");

		try {
			payload = fileUtils.readFile(systemPropertiesProvider.getProperty("neoclaudiaVappVMLocation"))
			          .replace("{org}",claudiaData.getOrg())
			          .replace("{vdc}",claudiaData.getVdc())
			          .replace("{service}",claudiaData.getService())
			          .replace("{vm}",claudiaData.getVm())
                      .replace("{replica}","1")
                      .replace("{IP}",ip);

		} catch (FileUtilsException e) {
			throw new ClaudiaResourceNotFoundException(
					"Error in the Claudia Dummy Utils "
							+ e.getMessage());
		}
		return payload;
	}

	
	public String createImage(ClaudiaData claudiaData) 
			throws ClaudiaRetrieveInfoException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String deployService(ClaudiaData claudiaData, String ovf) 
			throws InfrastructureException {
		// TODO Auto-generated method stub
		
		return "<task href=\"http://localhost:8081/paasmanager/rest/vdc/hola/task/65\" startTime=\"2013-02-11T11:29:44.713+01:00\" status=\"SUCESS\"> "+
        	"<description>Create environment testtomcatsap8</description> "+
        	"<vdc>hola</vdc> " +
        	"</task>";
	}

	
	public String deployVDC(ClaudiaData claudiaData,
			String cpu, String mem,
			String disk) throws InfrastructureException {
		// TODO Auto-generated method stub
		return "NO used";
	}

	
	public String deployVM(String org, String vdc, String service,
			String vmName, PaasManagerUser user, String vmPath ) throws InfrastructureException {
		// TODO Auto-generated method stub
		return "NO used";
	}

	
	public String deployVM(ClaudiaData claudiaData, String payload) 
					throws InfrastructureException {
		// TODO Auto-generated method stub
		return "<task href=\"http://130.206.80.112:8080/paasmanager/rest/vdc/test1/task/35\" startTime=\"2012-11-22T10:29:20.746+01:00\" status=\"success\">"+
        	" <description>Create environment testtomcatsap5</description>"+
        	" <vdc>test1</vdc>    </task>";
	}

	
	public String getVApp(String org, String vdc, String service, String vmName,
			PaasManagerUser user)
			throws IPNotRetrievedException, ClaudiaResourceNotFoundException,
			NetworkNotRetrievedException, OSNotRetrievedException {
	
		FileUtils fileUtils = null;
	    String payload = null;
	    String ip =systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");

	
		try {
			payload = fileUtils.readFile("VappTemplate.xml","./src/main/resources")
			          .replace("{org}",org)
			          .replace("{vdc}",vdc)
			          .replace("{service}",service)
			          .replace("{vm}",vmName)
                      .replace("{replica}","1")
                      .replace("{IP}",ip);

		} catch (FileUtilsException e) {

			throw new IPNotRetrievedException(
					"Error in the Claudia Dummy Utils "
							+ e.getMessage());
		}
		return payload;
	}
	

	

	
	public String obtainIPFromFqn(String org, String vdc, String service,
			String vmName, PaasManagerUser user) throws IPNotRetrievedException,
			ClaudiaResourceNotFoundException, NetworkNotRetrievedException {
		
		 String ip =systemPropertiesProvider.getProperty("IP_VM_DummyClaudia");
		 return ip;
	}

	
	public String obtainOS(String org, String vdc, String service, String vmName
			, PaasManagerUser user)
			throws OSNotRetrievedException, ClaudiaResourceNotFoundException {

		return "95";
	}

	
	public void undeployVM(String fqn) throws InfrastructureException {
		return;
		
	}

	
	public void undeployVMReplica(String fqn, String replica)
			throws InfrastructureException {
		return;
		
	}
	
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setSystemPropertiesProvider(
            SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
    
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public SystemPropertiesProvider  getSystemPropertiesProvider() {
        return this.systemPropertiesProvider;
    }
    
    public void setFileUtils(
    		FileUtils fileUtils ) {
        this.fileUtils = fileUtils;
    }
    
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public FileUtils getFileUtils() {
        return this.fileUtils;
    }

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#obtainVMStatus(java.lang.String)
	 */
	public String obtainVMStatus(String vapp)
			throws VMStatusNotRetrievedException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient#switchVMOn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String switchVMOn(String org, String vdc, String service,
			String vmName, PaasManagerUser user) throws InfrastructureException {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    
    
    
	




}
