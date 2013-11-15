/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Template;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

public interface InfrastructureManager {

    /**
     * Obtain a List of VMs
     * 
     * @param number_vms
     * @return lists of VMs.
     */
    // List<VM> getVMs(String vdc, Integer number_vms) throws
    // InfrastructureException,
    // IPNotRetrievedException;

    /**
     * Create a List of VMs from an ovf
     * 
     * @param envInstance
     * @param ovf
     * @param vdc
     * @param org
     * @return list of VMS created
     * @throws InfrastructureException
     */
    // List<VM> createEnvironment(EnvironmentInstance envInstance, Tier
    // tierInstance, String ovf,
    // ClaudiaData claudiaData) throws InfrastructureException,
    // InvalidVappException;

    /**
     * Create an EnvironmentInstance
     * 
     * @param environment
     * @param ovf
     * @param claudiaData
     * @return
     * @throws InfrastructureException
     * @throws InvalidVappException
     * @throws InvalidEntityException 
     * @throws EntityNotFoundException 
     */
    EnvironmentInstance createInfrasctuctureEnvironmentInstance(EnvironmentInstance environmentInstance,
            List<Tier> tiers, ClaudiaData claudiaData) throws InfrastructureException, InvalidVappException,
            InvalidOVFException, InvalidEntityException, EntityNotFoundException;

    /**
     * Delete the environemnt (vms associated to the environmentInstance)
     * 
     * @param envInstance
     * @param vdc
     * @throws InfrastructureException
     */
    void deleteEnvironment(ClaudiaData claudiaData, EnvironmentInstance envInstance) throws InfrastructureException;

    /**
     * Clone the template to a VM with products installed (Element of TierInstance)
     * 
     * @param templateName
     *            name of the template
     * @return the TierInstance
     * @throws InfrastructureException
     */
    TierInstance cloneTemplate(String templateName) throws InfrastructureException;

    /**
     * Scalate a vm
     * 
     * @param org
     * @param vdc
     * @param vapp
     * @param service
     * @param vmName
     * @param fqn
     * @return imagen_name
     * @throws InfrastructureException
     */
    /*
     * String ImageScalability(String org, String vdc, String vapp, String service, String vmName, String fqn,
     * PaasManagerUser user) throws InfrastructureException;
     */
    String ImageScalability(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException;

    /**
     * Create a template from a tierInstance
     * 
     * @param tierInstance
     * @return the template
     * @throws InfrastructureException
     */
    Template createTemplate(TierInstance tierInstance) throws InfrastructureException;

    /**
     * Start or Stop scalability
     * 
     * @param org
     * @param vdc
     * @param service
     * @param vmName
     * @param fqn
     * @param b
     * @return
     * @throws InfrastructureException
     */
    String StartStopScalability(ClaudiaData claudiaData, boolean b) throws InfrastructureException;

    /**
     * Deploy a new VM
     * 
     * @param payload
     * @param org
     * @param vdc
     * @param service
     * @param i
     * @return
     * @throws InfrastructureException
     */
    void deployVM(ClaudiaData claudiaData, TierInstance tier, int replica, String vmOVF, VM vm) throws InfrastructureException;

    /**
     * Delete a Vm replica
     * 
     * @param org
     * @param service
     * @param tierInstance
     * @return ProductInstance
     * @throws InfrastructureException
     */
    public void deleteVMReplica(ClaudiaData claudiaData, TierInstance tierInstance) throws InfrastructureException;

}
