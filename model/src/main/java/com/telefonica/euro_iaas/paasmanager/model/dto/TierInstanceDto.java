/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.List;
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
    private List<Attribute> attributes;
    private int replicaNumber;
    private List<ProductInstanceDto> productInstanceDtos;
    private VMDto vm;

    /**
     * the default constructor.
     */
    public TierInstanceDto() {
        this.productInstanceDtos = new ArrayList<ProductInstanceDto>();
    }

    /**
     * @param tierInstanceName
     * @param replicaNumber
     * @param productInstanceDtos
     * @param fqn
     */
    public TierInstanceDto(String tierInstanceName, int replicaNumber, List<ProductInstanceDto> productInstanceDtos,
            String fqn) {

        this.tierInstanceName = tierInstanceName;
        this.productInstanceDtos = productInstanceDtos;
        this.replicaNumber = replicaNumber;
        this.productInstanceDtos = new ArrayList<ProductInstanceDto>();
    }

    /**
     * @param tierInstanceName
     * @param replicaNumber
     * @param productInstanceDtos
     * @param vm
     */
    public TierInstanceDto(String tierInstanceName, int replicaNumber, List<ProductInstanceDto> productInstanceDtos,
            VMDto vm) {

        this.tierInstanceName = tierInstanceName;
        this.productInstanceDtos = productInstanceDtos;
        this.replicaNumber = replicaNumber;
        this.vm = vm;
        this.productInstanceDtos = new ArrayList<ProductInstanceDto>();
    }

    /**
     * @param tierInstanceName
     * @param tierDto
     * @param replicaNumber
     * @param productInstanceDtos
     * @param fqn
     */
    public TierInstanceDto(String tierInstanceName, TierDto tierDto, int replicaNumber,
            List<ProductInstanceDto> productInstanceDtos, String fqn) {
        this.tierInstanceName = tierInstanceName;
        this.tierDto = tierDto;
        this.productInstanceDtos = productInstanceDtos;
        this.replicaNumber = replicaNumber;
        this.productInstanceDtos = new ArrayList<ProductInstanceDto>();
    }

    /**
     * Add the product instance dto object.
     * 
     * @param productInstanceDto
     */
    public void addProductInstanceDto(ProductInstanceDto productInstanceDto) {
        if (this.productInstanceDtos == null) {
            this.productInstanceDtos = new ArrayList<ProductInstanceDto>();
        }
        this.productInstanceDtos.add(productInstanceDto);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<ProductInstanceDto> getProductInstanceDtos() {
        return productInstanceDtos;
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

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setProductInstanceDtos(List<ProductInstanceDto> productInstanceDtos) {
        this.productInstanceDtos = productInstanceDtos;
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
