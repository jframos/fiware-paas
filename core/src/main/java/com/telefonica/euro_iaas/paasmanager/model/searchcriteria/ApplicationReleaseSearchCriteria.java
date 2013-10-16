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

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;

/**
 * @author jesus.movilla
 * 
 */
public class ApplicationReleaseSearchCriteria extends AbstractSearchCriteria {

	private Artifact artifact;

	/**
	 * Default constructor
	 */
	public ApplicationReleaseSearchCriteria() {
	}

	/**
	 * @param page
	 * @param pagesize
	 * @param orderBy
	 * @param orderType
	 * @param vm
	 * @param application
	 * @param productRelease
	 */
	public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize,
			String orderBy, String orderType, Artifact artifact) {
		super(page, pageSize, orderBy, orderType);
		this.artifact = artifact;
	}

	/**
	 * @param orderBy
	 * @param orderType
	 * @param vm
	 * @param application
	 * @param productRelease
	 */
	public ApplicationReleaseSearchCriteria(String orderBy, String orderType,
			Artifact artifact) {
		super(orderBy, orderType);
		this.artifact = artifact;
	}

	/**
	 * @param page
	 * @param pagesize
	 * @param vm
	 * @param application
	 * @param productRelease
	 */
	public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize,
			Artifact artifact) {
		super(page, pageSize);
		this.artifact = artifact;
	}

	/**
	 * @param application
	 * @param productRelease
	 */
	public ApplicationReleaseSearchCriteria(Artifact artifact) {
		this.artifact = artifact;
	}

	/**
	 * @return the artifact
	 */
	public Artifact getArtifact() {
		return artifact;
	}

	/**
	 * @param Application
	 *            the application to set
	 */
	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

}
