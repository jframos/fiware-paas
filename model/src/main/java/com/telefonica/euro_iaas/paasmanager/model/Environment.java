package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Environment {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(nullable=false, length=256)
	private String name;
	
	@Column(length=256)
	private String description;

    @ManyToOne
	private EnvironmentType environmentType;
	
    @Column(length=30000)
	private String ovf;
	
//	@JoinTable(name = "environment_has_tiers")
	@ManyToMany(fetch = FetchType.LAZY)
	//@ManyToMany
	@JoinTable(name = "environment_has_tiers",  joinColumns = { 
			@JoinColumn(name = "environment_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "tier_ID", 
					nullable = false, updatable = false) })
					
	private List<Tier> tiers;
	
	
	
	/**
     * Default constructor
     */
    public Environment() {
    }

    /**
     * <p>Constructor for Service.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param name a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param productReleases a {@link List<Attribute>} object.
     */
    public Environment(String name, EnvironmentType environmentType, String description
    		, List<Tier> tiers) {
    	this.name = name;
    	this.environmentType = environmentType;
        this.tiers = tiers;
        this.description = description;
    }
    
    public Environment(String name, EnvironmentType environmentType, List<Tier> tiers) {
    	this.name = name;
    	this.environmentType = environmentType;
        this.tiers = tiers;
    }
    
    public Environment(String name, List<Tier> tiers, String description) {
    	this.name = name;
        this.tiers = tiers;
        this.description = description;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EnvironmentType getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(EnvironmentType environmentType) {
		this.environmentType = environmentType;
	}

	public String getOvf() {
		return ovf;
	}

	public void setOvf(String ovf) {
		this.ovf = ovf;
	}
	
	public List<Tier> getTiers() {
		return tiers;
	}

	public void setTiers(List<Tier> tiers) {
		this.tiers = tiers;
	}
	
	public void addTier(Tier tier) {
		if (this.tiers == null)
		{
			tiers  = new ArrayList ();
		}
		tiers.add(tier);
	}
	
	public void deleteTier(Tier tier) {
		if (tiers.contains(tier))
		tiers.remove(tier);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Environment other = (Environment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


}
