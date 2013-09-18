package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

/**
 * Provides some criteria to search Application Instance entities.
 *
 * @author Jesus M. Movilla
 *
 */
public class ApplicationInstanceSearchCriteria extends AbstractSearchCriteria {

	private ApplicationRelease applicationRelease;
    /**
     * The vdc where the product is installed.
     */
    private String vdc;

    private List<Status> status;
    
    /**
     * Default constructor
     */
    public ApplicationInstanceSearchCriteria() {
    }
   
	/**
	 * @param page
	 * @param pageSize
	 * @param orderBy
	 * @param orderType
	 * @param applicationRelease
	 * @param vdc
	 */
	public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status,ApplicationRelease applicationRelease,
			String vdc) {
		super(page, pageSize, orderBy, orderType);
		this.applicationRelease = applicationRelease;
		this.vdc = vdc;
		this.status = status;
	}

	/**
	 * @param page
	 * @param pageSize
	 * @param applicationRelease
	 * @param vdc
	 */
	public ApplicationInstanceSearchCriteria(String orderBy, String orderType, 
			List<Status> status, ApplicationRelease applicationRelease,
			String vdc) {
		super(orderBy, orderType);
		this.applicationRelease = applicationRelease;
		this.vdc = vdc;
		this.status = status;
	}
	
	/**
	 * @param page
	 * @param pageSize
	 * @param applicationRelease
	 * @param vdc
	 */
	public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize,
			List<Status> status, ApplicationRelease applicationRelease,
			String vdc) {
		super(page, pageSize);
		this.applicationRelease = applicationRelease;
		this.vdc = vdc;
		this.status = status;
	}
	
	/**
	 * @param applicationRelease
	 * @param vdc
	 */
	public ApplicationInstanceSearchCriteria(List<Status> status,
			ApplicationRelease applicationRelease,
			String vdc) {
		this.status=status;
		this.applicationRelease = applicationRelease;
		this.vdc = vdc;
	}
	
	/**
	 * @return the applicationRelease
	 */
	public ApplicationRelease getApplicatonRelease() {
		return applicationRelease;
	}
	/**
	 * @param applicationRelease the applicationRelease to set
	 */
	public void setApplicatonRelease(ApplicationRelease applicationRelease) {
		this.applicationRelease = applicationRelease;
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
}
