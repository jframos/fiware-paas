/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * EnvironmentDto for the Environment entity.
 * 
 * @author henar
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentDto {

    private Set<TierDto> tierDtos;
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
    public EnvironmentDto(Set<TierDto> tierDtos, String name, String description) {
        this.tierDtos = tierDtos;
        this.name = name;
        this.description = description;
    }

    /**
     * from Dto.
     * 
     * @return
     */
    public Environment fromDto() {

        Environment environment = new Environment();
        environment.setName(getName());
        environment.setDescription(getDescription());

        if (getTierDtos() != null) {
            Set<Tier> lTier = new HashSet<Tier>();
            for (TierDto tierDto : getTierDtos()) {
                lTier.add(tierDto.fromDto(vdc));
            }
            environment.setTiers(lTier);
        }
        environment.setOrg(org);
        environment.setVdc(vdc);
        return environment;
    }

    /**
     * @return the name
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the name
     */
    public String getOrg() {
        return org;
    }

    /**
     * @return the tiers
     */
    public Set<TierDto> getTierDtos() {
        return tierDtos;
    }

    /**
     * @return the name
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param org
     *            the org to set
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * @param tierDtos
     *            the tierDtos to set
     */
    public void setTierDtos(Set<TierDto> tierDtos) {
        this.tierDtos = tierDtos;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

}
