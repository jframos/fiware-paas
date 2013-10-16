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
import javax.persistence.ManyToOne;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

@Entity
public class Environment {

    public final static String ORG_FIELD = "org";
    public final static String VDC_FIELD = "vdc";
    public final static String ENVIRONMENT_NAME_FIELD = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String org;

    @Column(length = 256)
    private String description;

    @ManyToOne
    private EnvironmentType environmentType;

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
     * Default constructor
     */
    public Environment() {
    }

    /**
     * <p>
     * Constructor for Service.
     * </p>
     * 
     * @param id
     *            a {@link java.lang.String} object.
     * @param name
     *            a {@link java.lang.String} object.
     * @param description
     *            a {@link java.lang.String} object.
     * @param productReleases
     *            a {@link List<Attribute>} object.
     */
    public Environment(String name, EnvironmentType environmentType, String description, List<Tier> tiers) {

        this.name = name;
        this.description = description;
        this.environmentType = environmentType;
        this.tiers = tiers;

    }

    public Environment(String name, EnvironmentType environmentType, List<Tier> tiers) {
        this.name = name;
        this.environmentType = environmentType;
        this.tiers = tiers;
    }

    public Environment(String name, List<Tier> tiers, String description) {
        this.name = name;
        this.description = description;
        this.tiers = tiers;

    }

    public Environment(String name, List<Tier> tiers, String description, String org, String vdc) {
        this.name = name;
        this.description = description;
        this.org = org;
        this.tiers = tiers;
        this.vdc = vdc;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public String getVdc() {
        return this.vdc;
    }

    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public String getOvf() {
        return ovf;
    }

    public void setOvf(String ovf) {
        this.ovf = ovf;
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    public void setTiers(List<Tier> tiers) {
        this.tiers = tiers;
    }

    public void addTier(Tier tier) {
        if (this.tiers == null) {
            tiers = new ArrayList();
        }
        tiers.add(tier);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrg() {
        return this.org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void deleteTier(Tier tier) {
        if (tiers.contains(tier)) {
            tiers.remove(tier);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
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

    public EnvironmentDto toDto() {
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName(getName());
        envDto.setDescription(getDescription());

        if (getEnvironmentType() != null) {
            envDto.setEnvironmentType(getEnvironmentType());
        }

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
