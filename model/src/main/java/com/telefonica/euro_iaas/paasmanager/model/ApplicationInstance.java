package com.telefonica.euro_iaas.paasmanager.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * Represents an instance of a application
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
public class ApplicationInstance extends InstallableInstance {
	
	@ManyToOne
	private ApplicationRelease applicationRelease;
	
	@ManyToOne
	private EnvironmentInstance environmentInstance;

	/**
	 * @param applicationRelease
	 * @param environmentInstance
	 */
	public ApplicationInstance(ApplicationRelease applicationRelease,
			EnvironmentInstance environmentInstance) {
		this.applicationRelease = applicationRelease;
		this.environmentInstance = environmentInstance;
	}

	/**
	 * @return the applicationRelease
	 */
	public ApplicationRelease getApplicationRelease() {
		return applicationRelease;
	}

	/**
	 * @param applicationRelease the applicationRelease to set
	 */
	public void setApplicationRelease(ApplicationRelease applicationRelease) {
		this.applicationRelease = applicationRelease;
	}

	/**
	 * @return the environmentInstance
	 */
	public EnvironmentInstance getEnvironmentInstance() {
		return environmentInstance;
	}

	/**
	 * @param environmentInstance the environmentInstance to set
	 */
	public void setEnvironmentInstance(EnvironmentInstance environmentInstance) {
		this.environmentInstance = environmentInstance;
	}

}
