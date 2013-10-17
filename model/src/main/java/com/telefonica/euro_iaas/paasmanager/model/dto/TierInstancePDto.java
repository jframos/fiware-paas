/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierInstancePDto {

    private String tierInstanceName;
    private Status status;
    private String taskId;

    private List<ProductInstanceDto> productInstanceDtos;
    private VMDto vm;

    public TierInstancePDto(String tierInstanceName, List<ProductInstanceDto> productInstanceDtos, VMDto vm,
            Status status, String taskId) {

        this.tierInstanceName = tierInstanceName;
        this.productInstanceDtos = productInstanceDtos;
        this.vm = vm;
        this.status = status;
        this.taskId = taskId;
    }

    /**
	 * 
	 */
    public TierInstancePDto() {
    }

    public String getTierInstanceName() {
        return tierInstanceName;
    }

    public void setTierInstanceName(String tierInstanceName) {
        this.tierInstanceName = tierInstanceName;
    }

    public void setVM(VMDto vm) {
        this.vm = vm;
    }

    public VMDto getVM() {
        return this.vm;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public List<ProductInstanceDto> getProductInstanceDtos() {
        return productInstanceDtos;
    }

    public void setProductInstanceDtos(List<ProductInstanceDto> productInstanceDtos) {
        this.productInstanceDtos = productInstanceDtos;
    }
}
