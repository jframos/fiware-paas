package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentDto {
	private EnvironmentType environmentType;
	private List<Tier> tiers;
	private String name;
	
	/**
	 */
	public EnvironmentDto() {
	}
	
	/**
	 * @param environmentType
	 * @param tiers
	 * @param name
	 */
	public EnvironmentDto(EnvironmentType environmentType, List<Tier> tiers,
			String name) {
		this.environmentType = environmentType;
		this.tiers = tiers;
		this.name = name;
	}
	/**
	 * @return the environmentType
	 */
	public EnvironmentType getEnvironmentType() {
		return environmentType;
	}
	/**
	 * @param environmentType the environmentType to set
	 */
	public void setEnvironmentType(EnvironmentType environmentType) {
		this.environmentType = environmentType;
	}
	/**
	 * @return the tiers
	 */
	public List<Tier> getTiers() {
		return tiers;
	}
	/**
	 * @param tiers the tiers to set
	 */
	public void setTiers(List<Tier> tiers) {
		this.tiers = tiers;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
