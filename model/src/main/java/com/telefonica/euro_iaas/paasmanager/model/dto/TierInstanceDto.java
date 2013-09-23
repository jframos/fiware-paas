package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierInstanceDto {

	private String tierInstanceName;
	private TierDto tierDto;
	private List<Attribute> attributes;
	private int replicaNumber;
	private List<ProductInstanceDto> productInstanceDtos;
	private VMDto vm;

	public TierInstanceDto(String tierInstanceName, int replicaNumber,
			List<ProductInstanceDto> productInstanceDtos, String fqn) {

		this.tierInstanceName = tierInstanceName;
		this.productInstanceDtos = productInstanceDtos;
		this.replicaNumber = replicaNumber;
	}

	public TierInstanceDto(String tierInstanceName, int replicaNumber,
			List<ProductInstanceDto> productInstanceDtos, VMDto vm) {

		this.tierInstanceName = tierInstanceName;
		this.productInstanceDtos = productInstanceDtos;
		this.replicaNumber = replicaNumber;
		this.vm = vm;
	}

	public TierInstanceDto(String tierInstanceName, TierDto tierDto,
			int replicaNumber, List<ProductInstanceDto> productInstanceDtos,
			String fqn) {

		this.tierInstanceName = tierInstanceName;
		this.tierDto = tierDto;
		this.productInstanceDtos = productInstanceDtos;
		this.replicaNumber = replicaNumber;
	}

	/**
	 * 
	 */
	public TierInstanceDto() {
	}

	public String getTierInstanceName() {
		return tierInstanceName;
	}

	public void setTierInstanceName(String tierInstanceName) {
		this.tierInstanceName = tierInstanceName;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public TierDto getTierDto() {
		return tierDto;
	}

	public void setTierDto(TierDto tierDto) {
		this.tierDto = tierDto;
	}

	public void setVM(VMDto vm) {
		this.vm = vm;
	}

	public int getReplicaNumber() {
		return replicaNumber;
	}

	public void setReplicaNumber(int replicaNumber) {
		this.replicaNumber = replicaNumber;
	}

	public List<ProductInstanceDto> getProductInstanceDtos() {
		return productInstanceDtos;
	}

	public void setProductInstanceDtos(
			List<ProductInstanceDto> productInstanceDtos) {
		this.productInstanceDtos = productInstanceDtos;
	}
}
