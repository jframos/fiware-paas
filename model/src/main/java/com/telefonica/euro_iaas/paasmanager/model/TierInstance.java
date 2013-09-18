package com.telefonica.euro_iaas.paasmanager.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


/**
 * Represents an instance of a tier
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
public class TierInstance  extends InstallableInstance{
	
	@ManyToOne
	private Tier tier;
	
	@ManyToMany	
	private List<ProductInstance> productInstances;

	/**
	 */
	public TierInstance() {
		super();
	}
	
	/**
	 * @param tier
	 * @param productInstances
	 */
	public TierInstance(Tier tier, List<ProductInstance> productInstances) {
		super();
		this.tier = tier;
		this.productInstances = productInstances;
	}

	/**
	 * @return the tier
	 */
	public Tier getTier() {
		return tier;
	}

	/**
	 * @param tier the tier to set
	 */
	public void setTier(Tier tier) {
		this.tier = tier;
	}

	/**
	 * @return the productInstances
	 */
	public List<ProductInstance> getProductInstances() {
		return productInstances;
	}

	/**
	 * @param productInstances the productInstances to set
	 */
	public void setProductInstances(List<ProductInstance> productInstances) {
		this.productInstances = productInstances;
	}

	
	
	
	
}
