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

import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstancePDto {

    private String environmentInstanceName;
    private String vdc;
    private String description;
    private Status status;
    private String blueprintName;
    private String taskId;

    private List<TierPDto> tierDto;

    /**
     * @param environmentInstanceName
     * @param vdc
     * @param environment
     * @param tierInstances
     */
    public EnvironmentInstancePDto(String environmentInstanceName, List<TierPDto> tierPDtos, String vdc) {
        this.environmentInstanceName = environmentInstanceName;

        this.tierDto = tierPDtos;
        this.vdc = vdc;
    }

    /**
	 * 
	 */
    public EnvironmentInstancePDto() {
    }

    /**
     * @return the environmentInstanceName
     */
    public String getEnvironmentInstanceName() {
        return environmentInstanceName;
    }

    /**
     * @param environmentInstanceName
     *            the environmentInstanceName to set
     */
    public void setEnvironmentInstanceName(String environmentInstanceName) {
        this.environmentInstanceName = environmentInstanceName;
    }

    public void setBlueprintName(String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public String getBlueprintName() {
        return blueprintName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @param tierInstances
     *            the tierInstances to set
     */
    public void setTiers(List<TierPDto> tierPDtos) {
        this.tierDto = tierPDtos;
    }

    public void addTiers(TierPDto tierPDto) {
        if (tierDto == null) {
            tierDto = new ArrayList();
        }
        tierDto.add(tierPDto);
    }

    public List<TierPDto> getTiers() {
        return this.tierDto;
    }

}
