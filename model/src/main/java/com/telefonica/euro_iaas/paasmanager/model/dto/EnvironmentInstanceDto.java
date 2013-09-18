package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstanceDto {
	
	private String environmentInstanceName;
	private String vdc;
	private Environment environment;
	private List<TierInstance> tierInstances;
	private List<Attribute> attributes;
	
    /**
     * @param environmentInstanceName
     * @param vdc
     * @param environment
     * @param tierInstances
     */
    public EnvironmentInstanceDto(String environmentInstanceName, 
    		Environment environment,
    		List<TierInstance> tierInstances,
    		String vdc){
        this.environmentInstanceName = environmentInstanceName;
        this.environment = environment;
        this.tierInstances = tierInstances;
        this.vdc = vdc;
    }

	/**
	 * 
	 */
	public EnvironmentInstanceDto() {
	}

	/**
	 * @return the environmentInstanceName
	 */
	public String getEnvironmentInstanceName() {
		return environmentInstanceName;
	}

	/**
	 * @param environmentInstanceName the environmentInstanceName to set
	 */
	public void setEnvironmentInstanceName(String environmentInstanceName) {
		this.environmentInstanceName = environmentInstanceName;
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

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
    
	
}
