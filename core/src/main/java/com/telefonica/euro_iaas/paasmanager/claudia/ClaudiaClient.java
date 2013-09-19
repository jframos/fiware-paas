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



import com.telefonica.euro_iaas.paasmanager.exception.InvalidMonitoringRequest;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.OSNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.VMStatusNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;



/**
 * @author jesus.movilla
 *
 */
public interface ClaudiaClient {

	 /**
     * Browse the selected VDC for the given organization (browseVDC).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @return the response
     * @throws ClaudiaResourceNotFoundException if the VDC does not exist
     */
    /*String browseVDC(String org, String vdc, PaasManagerUser user)throws 
		ClaudiaResourceNotFoundException;*/
    String browseVDC(ClaudiaData claudiaData)throws ClaudiaResourceNotFoundException;

    /**
     * Browse the selected Service for the given VDC (browseService).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param service Service identifier
     * @return the response
     * @throws ClaudiaResourceNotFoundException if the srvice does not exist
     */
    /*String browseService(String org, String vdc, String service,
    		PaasManagerUser user)throws ClaudiaResourceNotFoundException;*/
    String browseService(ClaudiaData claudiaData)throws ClaudiaResourceNotFoundException;
    /**
     * Browse the selected VM for the given Service (browseVM).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param service Service identifier
     * @param vm Virtual Machine identifier
     * @return the response
     * @throws ClaudiaResourceNotFoundException if the VM does not exist
     */
    String browseVM(String org, String vdc, String service, String vm,
    		PaasManagerUser user)throws ClaudiaResourceNotFoundException;

    /**
     * Browse the selected VM Replica for the given VM (browseVM).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param service Service identifier
     * @param vm Virtual Machine identifier
     * @return the response
     * @throws ClaudiaResourceNotFoundException if the VM does not exist
     */
    String browseVMReplica(ClaudiaData claudiaData, String repplica) 
    				throws ClaudiaResourceNotFoundException;
    
    /**
     * 
     * @param fqn
     * @param replica
     * @throws InfrastructureException
     */
    void undeployVMReplica (String fqn, String replica) 
    			throws InfrastructureException;
    
    /**
     * Deploy a VDC for the selected organization (deployVDC).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param cpu number of CPUs in the Virtual Data Center
     * @param mem number of MEMs in the Virtual Data Center
     * @param disk number of Disks in the Virtual Data Center
     * @return the response
     * @throws InfrastructureException if there is any problem deploying VDC
     */
    String deployVDC(ClaudiaData claudiaData, String cpu, String mem, String disk)
    		throws InfrastructureException;

    /**
     * Deploy a service known as ServiceId for a given Org, Customer (deployService).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param service Service identifier
     * @param ovf the OVF file to be deployed
     * @return the response
     * @throws InfrastructureException if there is any problem with the connection
     */
    String deployService(ClaudiaData claudiaData, String ovf)
    		throws InfrastructureException;
    
    /**
     * Deploy a VM known as ServiceId for a given Org, Customer (deployService).
     *
     * @param org Organization identifier
     * @param vdc Virtual Data Center identifier
     * @param service Service identifier
     * @param vmName the vmName file to be deployed
     * @param vmPath the vmPath
     *      * @return the response
     * @throws InfrastructureException if there is any problem with the connection
     */
    String deployVM(String org, String vdc, String service, String vmName, 
    		PaasManagerUser user, String vmPath) throws InfrastructureException;
    
    /**
     * Deploy a VM from org, vdc, service and payload
     * @param org
     * @param vdc
     * @param service
     * @param payload
     * @return
     * @throws InfrastructureException
     */
    String deployVM(ClaudiaData claudiaData, String payload) throws InfrastructureException;

    /**
     * 
     * @param fqn
     * @throws InfrastructureException
     */
    void undeployVM (String fqn) 
    		throws InfrastructureException;
    /**
     * 
     * @param org
     * @param vdc
     * @param service
     * @param vmName
     * @return
     * @throws IPNotRetrievedException
     * @throws ClaudiaResourceNotFoundException
     */
    String obtainIPFromFqn(String org, String vdc, String service, 
			String vmName, PaasManagerUser user) throws IPNotRetrievedException,
    	ClaudiaResourceNotFoundException, NetworkNotRetrievedException;
    
    /**
     * 
     * @param org
     * @param vdc
     * @param service
     * @param vmName
     * @return
     * @throws OSNotRetrievedException
     * @throws ClaudiaResourceNotFoundException
     */
    String obtainOS(String org, String vdc, String service, String vmName
    		, PaasManagerUser user) 
    		throws OSNotRetrievedException,	ClaudiaResourceNotFoundException;
    
    /**
     * Get VApp of a Virtual Machine
     * @param org
     * @param vdc
     * @param service
     * @param vmName
     * @return VApp
     * @throws IPNotRetrievedException
     * @throws ClaudiaResourceNotFoundException
     * @throws OSNotRetrievedException 
     */
    String getVApp(String org, String vdc, String service, 
			String vmName, PaasManagerUser user) throws IPNotRetrievedException,
    	ClaudiaResourceNotFoundException, NetworkNotRetrievedException, OSNotRetrievedException;
    
    /**
     * Start/Stop the scalability
     * @param org
     * @param vdc
     * @param service
     * @param fqn 
     * @param vmName 
     * @param b
     * @return vmResponse
     * @throws InfrastructureException
     */

	String OnOffScalability(ClaudiaData claudiaData, String environmentName,
			boolean b) throws InfrastructureException;
	
	
    /**
     * 
     * @param fqn
     * @return InamgeName
     * @throws InvalidMonitoringRequest
     */


	
	//api/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vee-id}/{vm-id}/action/templatize
	///api/org/es_tid/vdc/cc1/vapp/demorm/vees/SuperTryBea2/action/templatize
	//api/org/es_tid/vdc/cc1/vapp/demorm/veesAaUbuntu_Collectd_Bea/vmAna2/action/instantiateOvf
	String createImage(ClaudiaData claudiaData) throws ClaudiaRetrieveInfoException;

	/**
	 * @param org
	 * @param vdc
	 * @param service
	 * @param vmName
	 * @return
	 */
	String switchVMOn(String org, String vdc, String service, String vmName
			, PaasManagerUser user)
			throws InfrastructureException;

	/**
	 * @param vapp
	 * @return
	 */
	String obtainVMStatus(String vapp) 
			throws VMStatusNotRetrievedException;

}
