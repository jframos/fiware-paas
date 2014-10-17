/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.HashSet;
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
                lTier.add(tierDto.fromDto(vdc, getName()));
            }
            environment.setTiers(lTier);
        }
        environment.setOrg(org);
        environment.setVdc(vdc);
        return environment;
    }

    /**
     * from Dto.
     *
     * @return
     */
    public Environment fromDto(String org, String vdc) {

        Environment environment = new Environment();
        environment.setName(getName());
        environment.setDescription(getDescription());

        if (getTierDtos() != null) {
            Set<Tier> lTier = new HashSet<Tier>();
            for (TierDto tierDto : getTierDtos()) {
                lTier.add(tierDto.fromDto(vdc, getName()));
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
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param org the org to set
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * @param tierDtos the tierDtos to set
     */
    public void setTierDtos(Set<TierDto> tierDtos) {
        this.tierDtos = tierDtos;
    }

    /**
     * @param vdc the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[EnvironmentDto]");
        sb.append("[tierDtos = ").append(this.tierDtos).append("]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[description = ").append(this.description).append("]");
        sb.append("[org = ").append(this.org).append("]");
        sb.append("[vdc = ").append(this.vdc).append("]");
        sb.append("]");
        return sb.toString();
    }


}
