/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;


/**
 * @author jesus.movilla
 *
 */
public class EnvironmentInstanceSearchCriteria extends AbstractSearchCriteria {
		
    private List<Status> status;
    private String vdc;
	/**
	 * The environment
	 */
	private Environment environment;
	    
    /**
     * The productInstance
    */
    private ApplicationInstance applicationInstance;

    /**
     * Default constructor
     */
    public EnvironmentInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
	 * @param orderBy
     * @param orderType
     * @param environment
     * @param applicationInstance
     */
    public EnvironmentInstanceSearchCriteria(Integer page, Integer pageSize,
    	String orderBy, String orderType,  List<Status> status, String vdc,
    	Environment environment, ApplicationInstance applicationInstance) {
	    	super(page, pageSize, orderBy, orderType);
	    	this.status = status;
	    	this.vdc = vdc;
	        this.applicationInstance = applicationInstance;
	        this.environment = environment;
	}

    /**
     * @return the status
     */
    public List<Status> getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(List<Status> status) {
        this.status = status;
    }
    
    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }
    /**
     * @param environment
     */
    public EnvironmentInstanceSearchCriteria(Environment environment) {
        this.environment = environment;
    }
	    
    /**
     * @param applicationInstance
     */
    public EnvironmentInstanceSearchCriteria(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * @return the productInstance
     */
    public ApplicationInstance getApplicationInstance() {
        return applicationInstance;
    }

    /**
     * @param applicationInstance
     *            the applicationInstance to set
     */
    public void setProductInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }
	    

    @Override
    public String toString() {
        return "EnvironmentInstanceSearchCriteria [Environment=" + environment
        		+ ", applicationInstance=" + applicationInstance + "]";
    }

}


