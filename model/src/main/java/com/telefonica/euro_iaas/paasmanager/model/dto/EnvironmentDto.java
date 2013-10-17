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

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentDto {
    private EnvironmentType environmentType;
    private List<TierDto> tierDtos;
    private String name;
    private String description;
    private String org;
    private String vdc;

    /**
	 */
    public EnvironmentDto() {
    }

    /**
     * @param environmentType
     * @param tiers
     * @param name
     */
    public EnvironmentDto(EnvironmentType environmentType, List<TierDto> tierDtos, String name, String description) {
        this.environmentType = environmentType;
        this.tierDtos = tierDtos;
        this.name = name;
        this.description = description;
    }

    public EnvironmentDto(List<TierDto> tierDtos, String name, String description) {
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
     * @param environmentType
     *            the environmentType to set
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
     * @param tiers
     *            the tiers to set
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
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @return the name
     */
    public String getOrg() {
        return org;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * @return the name
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Environment fromDto() {

        Environment environment = new Environment();
        environment.setName(getName());
        environment.setDescription(getDescription());
        if (getEnvironmentType() != null) {
            environment.setEnvironmentType(getEnvironmentType());
        }
        if (getTierDtos() != null) {
            List<Tier> lTier = new ArrayList<Tier>();
            for (TierDto tierDto : getTierDtos()) {
                lTier.add(tierDto.fromDto());
            }
            environment.setTiers(lTier);
        }
        environment.setOrg(org);
        environment.setVdc(vdc);
        return environment;
    }

}
