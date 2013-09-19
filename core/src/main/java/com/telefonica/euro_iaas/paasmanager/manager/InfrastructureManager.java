/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Template;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

public interface InfrastructureManager {

	/**
	 * Obtain a List of VMs
	 * @param number_vms
	 * @return lists of VMs.
	 */
	//List<VM> getVMs(String vdc, Integer number_vms) throws InfrastructureException,
	//IPNotRetrievedException; 
	
	/**
	 * Create a List of VMs from an ovf
	 * @param envInstance
	 * @param ovf
	 * @param vdc
	 * @param org
	 * @return list of VMS created
	 * @throws InfrastructureException
	 */
	List<VM> createEnvironment (EnvironmentInstance envInstance, Tier tier,
			String ovf, ClaudiaData claudiaData) 
			throws InfrastructureException;
	
	/**
	 * Create an EnvironmentInstance
	 * @param environment
	 * @param ovf
	 * @param claudiaData
	 * @return
	 * @throws InfrastructureException
	 */
	EnvironmentInstance createInfrasctuctureEnvironmentInstance (
			Environment environment,
			String ovf, ClaudiaData claudiaData) 
			throws InfrastructureException;
	

	/**
	 * Delete the environemnt (vms associated to the environmentInstance)
	 * @param envInstance
	 * @param vdc
	 * @throws InfrastructureException
	 */
	void deleteEnvironment (ClaudiaData claudiaData, EnvironmentInstance envInstance) 
			throws InfrastructureException;
	

    /**
     * Clone the template to a VM with products installed (Element of TierInstance)
     * @param templateName name of the template
     * @return the TierInstance 
     * @throws InfrastructureException
     */
    TierInstance cloneTemplate (String templateName) throws InfrastructureException;
    
    /**
     * Scalate a vm 
     * @param org
     * @param vdc
     * @param vapp
     * @param service
     * @param vmName
     * @param fqn
     * @return imagen_name 
     * @throws InfrastructureException
     */
    /*String ImageScalability(String org, String vdc, String vapp, String service, 
    		String vmName, String fqn, PaasManagerUser user) throws InfrastructureException;
    */
    String ImageScalability(ClaudiaData claudiaData) throws InfrastructureException;
    
    /**
     * Create a template from a tierInstance
     * @param tierInstance 
     * @return the template 
     * @throws InfrastructureException
     */
    Template createTemplate (TierInstance tierInstance) throws InfrastructureException;
    
    
    /**
     * Start or Stop scalability
     * @param org
     * @param vdc
     * @param service
     * @param vmName
     * @param fqn
     * @param b
     * @return
     * @throws InfrastructureException
     */
    String StartStopScalability(ClaudiaData claudiaData, boolean b) 
    		throws InfrastructureException;
    
    /**
     * Update the ovf if create a new image
     * @param ovf
     * @param imageName
     * @return
     */
    public String updateVmOvf(String ovf, String imageName);
    
    /**
     * Deploy a new VM
     * @param payload
     * @param org
     * @param vdc
     * @param service
     * @param i
     * @return
     * @throws InfrastructureException
     */
	VM deployVM(ClaudiaData claudiaData, Tier tier, String payload, int i)
			throws InfrastructureException;


	/**
	 * Delete a Vm replica
	 * @param org
	 * @param service
	 * @param tierInstance
	 * @return ProductInstance
	 * @throws InfrastructureException
	 */
	public void deleteVMReplica(ClaudiaData claudiaData, 
			TierInstance tierInstance) throws InfrastructureException;
	
	/**
	 * Delete the product Section from a OVF
	 * @param ovf
	 * @return
	 */
	public String deleteProductSection(String ovf);

}
