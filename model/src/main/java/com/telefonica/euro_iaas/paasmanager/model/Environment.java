/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

/**
 * Environment entity.
 * 
 * @author henar
 */
@Entity
public class Environment {

    public static final String ORG_FIELD = "org";
    public static final String VDC_FIELD = "vdc";
    public static final String ENVIRONMENT_NAME_FIELD = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String org;

    @Column(length = 256)
    private String description;

    @Column(length = 256)
    private String vdc;

    @Column(length = 90000)
    private String ovf;

    // @JoinTable(name = "environment_has_tiers")
    @ManyToMany(fetch = FetchType.LAZY)
    // @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "environment_has_tiers", joinColumns = { @JoinColumn(name = "environment_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "tier_ID", nullable = false, updatable = false) })
    private List<Tier> tiers;

    /**
     * Default constructor.
     */
    public Environment() {
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param tiers
     */
    public Environment(String name, List<Tier> tiers) {
        this.name = name;
        this.tiers = tiers;
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param tiers
     * @param description
     */
    public Environment(String name, List<Tier> tiers, String description) {
        this.name = name;
        this.description = description;
        this.tiers = tiers;

    }

    /**
     * Constructor.
     * 
     * @param name
     * @param tiers
     * @param description
     * @param org
     * @param vdc
     */
    public Environment(String name, List<Tier> tiers, String description, String org, String vdc) {
        this.name = name;
        this.description = description;
        this.org = org;
        this.tiers = tiers;
        this.vdc = vdc;
    }

    /**
     * <p>
     * Constructor for Service.
     * </p>
     * 
     * @param name
     *            a {@link java.lang.String} object.
     * @param description
     *            a {@link java.lang.String} object.
     * @param tiers
     */
    public Environment(String name, String description, List<Tier> tiers) {

        this.name = name;
        this.description = description;
        this.tiers = tiers;

    }

    /**
     * Add a tier to the environment.
     * 
     * @param tier
     */
    public void addTier(Tier tier) {
        if (this.tiers == null) {
            tiers = new ArrayList<Tier>();
        }
        tiers.add(tier);
    }

    /**
     * Delete a tier in the environment.
     * 
     * @param tier
     */
    public void deleteTier(Tier tier) {
        if (tiers.contains(tier)) {
            tiers.remove(tier);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Environment other = (Environment) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrg() {
        return this.org;
    }

    public String getOvf() {
        return ovf;
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    public String getVdc() {
        return this.vdc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setOvf(String ovf) {
        this.ovf = ovf;
    }

    public void setTiers(List<Tier> tiers) {
        this.tiers = tiers;
    }

    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * It returns the dto specification.
     * 
     * @return EnvironmentDto.class
     */
    public EnvironmentDto toDto() {
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName(getName());
        envDto.setDescription(getDescription());

        List<TierDto> lTierDto = new ArrayList<TierDto>();
        if (getTiers() != null) {
            for (Tier tier : getTiers()) {
                lTierDto.add(tier.toDto());
            }
            envDto.setTierDtos(lTierDto);
        }

        return envDto;
    }

}
