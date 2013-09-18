package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

public interface InfrastructureManager {

	/**
	 * Obtain a List of VMs
	 * @param number_vms
	 * @return lists of VMs.
	 */
	List<VM> getVMs(Integer number_vms);
}
