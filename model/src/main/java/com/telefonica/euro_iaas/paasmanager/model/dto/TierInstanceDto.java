/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * The Dto for the Tier Instance.
 * 
 * @author henar
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierInstanceDto {

    private String tierInstanceName;
    private TierDto tierDto;
    private Set<Attribute> attributes;
    private int replicaNumber;
    private List<ProductInstanceDto> productInstanceDto;
    private VMDto vm;

    /**
     * the default constructor.
     */
    public TierInstanceDto() {
        this.productInstanceDto = new ArrayList<ProductInstanceDto>();
    }

    /**
     * @param tierInstanceName
     * @param replicaNumber
     * @param productInstanceDtos
     * @param fqn
     */
    public TierInstanceDto(String tierInstanceName, int replicaNumber, List<ProductInstanceDto> productInstanceDto,
            String fqn) {

        this.tierInstanceName = tierInstanceName;
        this.productInstanceDto = productInstanceDto;
        this.replicaNumber = replicaNumber;

    }

    /**
     * @param tierInstanceName
     * @param replicaNumber
     * @param productInstanceDtos
     * @param vm
     */
    public TierInstanceDto(String tierInstanceName, int replicaNumber, List<ProductInstanceDto> productInstanceDto,
            VMDto vm) {

        this.tierInstanceName = tierInstanceName;
        this.productInstanceDto = productInstanceDto;
        this.replicaNumber = replicaNumber;
        this.vm = vm;
    }

    /**
     * @param tierInstanceName
     * @param tierDto
     * @param replicaNumber
     * @param productInstanceDtos
     * @param fqn
     */
    public TierInstanceDto(String tierInstanceName, TierDto tierDto, int replicaNumber,
            List<ProductInstanceDto> productInstanceDto, String fqn) {
        this.tierInstanceName = tierInstanceName;
        this.tierDto = tierDto;
        this.productInstanceDto = productInstanceDto;
        this.replicaNumber = replicaNumber;
    }

    /**
     * Add the product instance dto object.
     * 
     * @param productInstanceDto
     */
    public void addProductInstanceDto(ProductInstanceDto productInstanceDto) {
        if (this.productInstanceDto == null) {
            this.productInstanceDto = new ArrayList<ProductInstanceDto>();
        }
        this.productInstanceDto.add(productInstanceDto);
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public List<ProductInstanceDto> getProductInstanceDtos() {
        return productInstanceDto;
    }

    public int getReplicaNumber() {
        return replicaNumber;

    }

    public TierDto getTierDto() {
        return tierDto;
    }

    public String getTierInstanceName() {
        return tierInstanceName;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setProductInstanceDtos(List<ProductInstanceDto> productInstanceDto) {
        this.productInstanceDto = productInstanceDto;
    }

    public void setReplicaNumber(int replicaNumber) {
        this.replicaNumber = replicaNumber;
    }

    public void setTierDto(TierDto tierDto) {
        this.tierDto = tierDto;
    }

    public void setTierInstanceName(String tierInstanceName) {
        this.tierInstanceName = tierInstanceName;
    }

    public void setVM(VMDto vm) {
        this.vm = vm;
    }
}
