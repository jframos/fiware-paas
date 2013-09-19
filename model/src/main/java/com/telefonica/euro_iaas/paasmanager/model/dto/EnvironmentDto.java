package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentDto {
	private EnvironmentType environmentType;
	private List<TierDto> tierDtos;
	private String name;
	private String description;
	
	/**
	 */
	public EnvironmentDto() {
	}
	
	/**
	 * @param environmentType
	 * @param tiers
	 * @param name
	 */
	public EnvironmentDto(EnvironmentType environmentType, List<TierDto> tierDtos,
			String name, String description) {
		this.environmentType = environmentType;
		this.tierDtos = tierDtos;
		this.name = name;
		this.description = description;
	}
	
	public EnvironmentDto(List<TierDto> tierDtos,
			String name, String description) {
		this.tierDtos = tierDtos;
		this.name = name;
		this.description = description;
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
	public List<TierDto> getTierDtos() {
		return tierDtos;
	}
	/**
	 * @param tiers the tiers to set
	 */
	public void setTierDtos(List<TierDto> tierDtos) {
		this.tierDtos = tierDtos;
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
