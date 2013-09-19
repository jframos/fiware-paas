package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//import org.hibernate.annotations.Cascade;





/**
 * Represents an instance of a environment
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@SuppressWarnings( "restriction" )
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "EnvironmentInstance")
public class EnvironmentInstance extends InstallableInstance{

    public final static String VDC_FIELD = "vdc";
	
	
    @ManyToOne ()
	private Environment environment;
	

	
	//@ManyToMany
//	@OneToMany(fetch=FetchType.EAGER)
	
	@ManyToMany(fetch=FetchType.LAZY)
	//@ManyToMany
	@JoinTable(name = "environmentInstance_has_tierInstances",  joinColumns = { 
			@JoinColumn(name = "environmentinstance_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "tierinstance_ID", 
					nullable = false, updatable = false) })
	

        
 //   @OneToMany(fetch=FetchType.LAZY) 
  //  @JoinColumn(name="tierInstance_id")
  //  @OneToMany(fetch = FetchType.LAZY, mappedBy = "environmentInstance")
//	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE })
	private List<TierInstance> tierInstances;	
	
	@Column(length=30000)
	private String vapp;
	
	public EnvironmentInstance() {
	}
	
	/**
	 * @param environment
	 * @param tierInstances
	 */
	public EnvironmentInstance(Environment environment,
			List<TierInstance> tierInstances) {
		super();
		this.environment = environment;
		this.tierInstances = tierInstances;
		setName();
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
	
	public void addTierInstance (TierInstance tierInstance) {
		if (tierInstances == null)
			tierInstances = new ArrayList ();
		tierInstances.add(tierInstance);
		
	}
	
	public void removeTierInstance (TierInstance tierInstance) {
		if (tierInstances.contains(tierInstance))
		tierInstances.remove(tierInstance);
		
	}

	/**
	 * @param tierInstances the tierInstances to set
	 */
	public void setTierInstances(List<TierInstance> tierInstances) {
		this.tierInstances = tierInstances;
	}
	
	/**
	 * @return the vapp
	 */
	public String getVapp() {
		return vapp;
	}

	/**
	 * @param envPayload the vapp to set
	 */
	public void setVapp(String vapp) {
		this.vapp = vapp;
	}

	/*
	 * setting the Name as function of environment and Tiers
	 */
	private void setName(){
		this.name = name;
	}
}
