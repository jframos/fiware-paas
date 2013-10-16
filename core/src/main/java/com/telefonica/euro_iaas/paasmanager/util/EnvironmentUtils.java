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

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

import java.util.List;

/**
 * @author jesus.movilla
 * 
 */
public interface EnvironmentUtils {

	public final String IP_MACRO_ID = "@ip";

	/**
	 * Resolve the macros present in teh attrbutes of the products belonging to
	 * environment
	 * 
	 * @param environment
	 * @param vms
	 * @return The environment with the values of the attributes resolved
	 */
	Environment resolveMacros(Environment environment, List<VM> vms);

	Environment resolveMacros(EnvironmentInstance envInst, List<Tier> tiers);

	public String updateVmOvf(String ovf, String imageName);

	String deleteProductSection(String vmOVF);
}
