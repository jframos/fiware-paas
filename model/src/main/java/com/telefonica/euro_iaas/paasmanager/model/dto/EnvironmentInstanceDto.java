package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstanceDto {
	
	private String environmentInstanceName;
	private String vdc;
	//private EnvironmentDto environmentDto;
	
	private List<TierInstanceDto> tierInstanceDtos;
	
	private List<Attribute> attributes;
	
    /**
     * @param environmentInstanceName
     * @param vdc
     * @param environment
     * @param tierInstances
     */
    public EnvironmentInstanceDto(String environmentInstanceName, 
    		EnvironmentDto environmentDto,
    		List<TierInstanceDto> tierInstanceDtos,
    		String vdc){
        this.environmentInstanceName = environmentInstanceName;
       // this.environmentDto = environmentDto;
        this.tierInstanceDtos = tierInstanceDtos;
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
	/*public EnvironmentDto getEnvironmentDto() {
		return environmentDto;
	}*/

	/**
	 * @param environment the environment to set
	 */
	/*public void setEnvironmentDto(EnvironmentDto environmentDto) {
		this.environmentDto = environmentDto;
	}*/

	/**
	 * @return the tierInstances
	 */
	public List<TierInstanceDto> getTierInstances() {
		return tierInstanceDtos;
	}

	/**
	 * @param tierInstances the tierInstances to set
	 */
	public void setTierInstances(List<TierInstanceDto> tierInstanceDtos) {
		this.tierInstanceDtos = tierInstanceDtos;
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
