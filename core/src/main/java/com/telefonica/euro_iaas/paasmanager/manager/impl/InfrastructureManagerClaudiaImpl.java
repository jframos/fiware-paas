package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.FQN1;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.IP1;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.FQN2;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.IP2;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.OSTYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.HOSTNAME1;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.DOMAINNAME1;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.HOSTNAME2;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.DOMAINNAME2;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class InfrastructureManagerClaudiaImpl implements InfrastructureManager {

    private SystemPropertiesProvider systemPropertiesProvider;
    
	@Override
	public List<VM> getVMs(Integer number_vms) {
		
		List<VM> vms = new ArrayList<VM>();
		if (number_vms.intValue() == 1){
			vms.add(new VM(
				systemPropertiesProvider.getProperty(FQN1),
				systemPropertiesProvider.getProperty(IP1),
				null,
				null, 
				systemPropertiesProvider.getProperty(OSTYPE)));
		}
		else if (number_vms.intValue() == 2){
			vms.add(new VM(
				systemPropertiesProvider.getProperty(FQN1),
				systemPropertiesProvider.getProperty(IP1),
				null, 
				null, 
				systemPropertiesProvider.getProperty(OSTYPE)));
			vms.add(new VM(
				systemPropertiesProvider.getProperty(FQN2),
				systemPropertiesProvider.getProperty(IP2),
				null,
				null,
				systemPropertiesProvider.getProperty(OSTYPE)));
		}
		
		return vms;
	}

	
    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
