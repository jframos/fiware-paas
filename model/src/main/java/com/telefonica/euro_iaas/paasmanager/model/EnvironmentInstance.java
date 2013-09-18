package com.telefonica.euro_iaas.paasmanager.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


/**
 * Represents an instance of a environment
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
public class EnvironmentInstance extends InstallableInstance{

	@ManyToOne
	private Environment environment;
	
	@ManyToMany	
	private List<TierInstance> tierInstances;

	/**
	 * @param environment
	 * @param tierInstances
	 */
	public EnvironmentInstance(Environment environment,
			List<TierInstance> tierInstances) {
		super();
		this.environment = environment;
		this.tierInstances = tierInstances;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * @return the tierInstances
	 */
	public List<TierInstance> getTierInstances() {
		return tierInstances;
	}

	/**
	 * @param tierInstances the tierInstances to set
	 */
	public void setTierInstances(List<TierInstance> tierInstances) {
		this.tierInstances = tierInstances;
	}

	
}
