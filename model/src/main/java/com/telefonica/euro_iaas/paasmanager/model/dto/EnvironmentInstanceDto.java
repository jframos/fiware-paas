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

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstanceDto {

    private String environmentInstanceName;
    private String vdc;
    private String description;
    private Status status;
    private String blueprintName;

    private EnvironmentDto environmentDto;

    private List<TierInstanceDto> tierInstanceDtos;

    private List<Attribute> attributes;

    /**
     * @param environmentInstanceName
     * @param vdc
     * @param environment
     * @param tierInstances
     */
    public EnvironmentInstanceDto(String environmentInstanceName, EnvironmentDto environmentDto,
            List<TierInstanceDto> tierInstanceDtos, String vdc) {
        this.environmentInstanceName = environmentInstanceName;
        this.environmentDto = environmentDto;
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
     * @return the environment
     */

    public EnvironmentDto getEnvironmentDto() {
        return environmentDto;
    }

    /**
     * @param environment
     *            the environment to set
     */

    public void setEnvironmentDto(EnvironmentDto environmentDto) {
        this.environmentDto = environmentDto;
    }

    /**
     * @return the tierInstances
     */
    public List<TierInstanceDto> getTierInstances() {
        return tierInstanceDtos;
    }

    /**
     * @param tierInstances
     *            the tierInstances to set
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
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public EnvironmentInstance fromDto() {
        EnvironmentInstance environmentInstance = new EnvironmentInstance();
        environmentInstance.setBlueprintName(getBlueprintName());
        environmentInstance.setDescription(getDescription());
        if (getEnvironmentDto() != null) {
            String envInstanceName = vdc + "-" + getEnvironmentDto().getName();
            environmentInstance.setName(envInstanceName);
        }
        if (getEnvironmentDto() != null) {
            Environment environment = getEnvironmentDto().fromDto();
            environmentInstance.setEnvironment(environment);
        }
        return environmentInstance;
    }
}
