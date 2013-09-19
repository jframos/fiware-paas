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

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
/**
 * @author jesus.movilla
 *
 */
public class EnvironmentUtilsImpl implements EnvironmentUtils {

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils#resolveMacros(com.telefonica.euro_iaas.paasmanager.model.Environment, java.util.List)
	 */
	public Environment resolveMacros(Environment environment, List<VM> vms) {
		
		List<Tier> tiers = environment.getTiers();
		//Go through all attributes of al ProductReleases of all tiers from environment
		for (int i=0; i < tiers.size(); i++) {
			List<ProductRelease> productReleases = tiers.get(i).getProductReleases();			
			for (int j=0; j < productReleases.size(); j++){			
				if (productReleases.get(j).getAttributes() != null) {
					List<Attribute> attributes = productReleases.get(j).getAttributes();				
						for (int k=0; k < attributes.size(); k++){
							//Look at attribute whose value contains @ip
							if (attributes.get(k).getValue().contains(IP_MACRO_ID)){
								//Recover the ipmacro
								String macroLine = attributes.get(k).getValue();
								String ipmacro = getIPMacro(macroLine);
								
								//Recover vmname from macro @ip(vmname,network)
								//String vm = getVMNameFromMacro(attributes.get(k).getValue());
								String vm = getVMNameFromMacro(ipmacro);
								//Look for ip of VM whose fqn contains vmname
								for (int l=0; l < vms.size(); l++) {
									if (vms.get(l).getFqn().contains(vm))
										attributes.get(k).setValue(macroLine.replace(ipmacro, vms.get(l).getIp()));
								}					
							}
						}
				}			
			}
		}
		return environment;
	}
	
   public Environment resolveMacros(EnvironmentInstance envInst) {
		
		List<Tier> tiers = envInst.getEnvironment().getTiers();
		//Go through all attributes of al ProductReleases of all tiers from environment
		for (int i=0; i < tiers.size(); i++) {
			List<ProductRelease> productReleases = tiers.get(i).getProductReleases();			
			for (int j=0; j < productReleases.size(); j++){			
				if (productReleases.get(j).getAttributes() != null) {
					List<Attribute> attributes = productReleases.get(j).getAttributes();				
						for (int k=0; k < attributes.size(); k++){
							//Look at attribute whose value contains @ip
							if (attributes.get(k).getValue().contains(IP_MACRO_ID)){
								//Recover the ipmacro
								String macroLine = attributes.get(k).getValue();
								String ipmacro = getIPMacro(macroLine);
								
								//Recover vmname from macro @ip(vmname,network)
								//String vm = getVMNameFromMacro(attributes.get(k).getValue());
								String nameVm = getVMNameFromMacro(ipmacro);
								
								VM vm = getVmWithName (envInst, nameVm);
								attributes.get(k).setValue(macroLine.replace(ipmacro, vm.getIp()));				
							}
						}
				}			
			}
		}
		return envInst.getEnvironment();
	}
   
   private VM getVmWithName (EnvironmentInstance environmetnInstance, String nameVM)
   {
	   for (TierInstance tierInstance: environmetnInstance.getTierInstances()) {
		   if (tierInstance.getVM().getFqn().contains(nameVM))
			return tierInstance.getVM();
	   }  
	   return null;

   }

	private String getVMNameFromMacro(String attribute) {	
		String[] parts = attribute.split("\\,");
		String vmname = parts[0].split("\\(")[1];
		return vmname;
	}
	
	private String getIPMacro(String line){
		String macroStart;
		
		if (line.startsWith(IP_MACRO_ID))
			macroStart = line.substring(3);
		else
			macroStart = line.split(IP_MACRO_ID)[1];
		
		String ipMacro = IP_MACRO_ID + macroStart.split("\\)")[0] + ")";
		
		return ipMacro;
	}
}
