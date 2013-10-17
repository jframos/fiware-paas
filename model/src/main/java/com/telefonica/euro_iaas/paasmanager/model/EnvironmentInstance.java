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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstancePDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstancePDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierPDto;

// import org.hibernate.annotations.Cascade;

/**
 * Represents an instance of a environment
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@SuppressWarnings("restriction")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "EnvironmentInstance")
public class EnvironmentInstance extends InstallableInstance {

    public final static String VDC_FIELD = "vdc";
    public final static String ENVIRONMENTNAME_FIELD = "blueprintname";

    @ManyToOne()
    private Environment environment;

    private String description;
    private String blueprintName = "";
    private String taskId = "";

    // @ManyToMany
    // @OneToMany(fetch=FetchType.EAGER)

    @ManyToMany(fetch = FetchType.LAZY)
    // @ManyToMany
    @JoinTable(name = "environmentInstance_has_tierInstances", joinColumns = { @JoinColumn(name = "environmentinstance_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "tierinstance_ID", nullable = false, updatable = false) })
    // @OneToMany(fetch=FetchType.LAZY)
    // @JoinColumn(name="tierInstance_id")
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "environmentInstance")
    // @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE
    // })
    private List<TierInstance> tierInstances;

    @Column(length = 90000)
    private String vapp;

    public EnvironmentInstance() {

    }

    public EnvironmentInstance(String blueprintName, String description) {
        this.blueprintName = blueprintName;
        this.description = description;
    }

    public EnvironmentInstance(String blueprintName, String description, Environment env) {
        this.blueprintName = blueprintName;
        this.description = description;
        this.environment = env;
    }

    /**
     * @param environment
     * @param tierInstances
     */
    public EnvironmentInstance(Environment environment, List<TierInstance> tierInstances) {
        super();
        this.environment = environment;
        this.tierInstances = tierInstances;

    }

    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlueprintName() {
        return this.blueprintName;
    }

    public void setBlueprintName(String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    /**
     * @return the tierInstances
     */
    public List<TierInstance> getTierInstances() {
        return tierInstances;
    }

    public void addTierInstance(TierInstance tierInstance) {
        if (tierInstances == null)
            tierInstances = new ArrayList();
        tierInstances.add(tierInstance);

    }

    public void removeTierInstance(TierInstance tierInstance) {
        if (tierInstances.contains(tierInstance))
            tierInstances.remove(tierInstance);

    }

    /**
     * @param tierInstances
     *            the tierInstances to set
     */
    public void setTierInstances(List<TierInstance> tierInstances) {
        this.tierInstances = tierInstances;
    }

    /**
     * @return the vapp
     */
    public String getVapp() {
        return vapp;
    }

    /**
     * @param envPayload
     *            the vapp to set
     */
    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    /*
     * setting the Name as function of environment and Tiers
     */

    public EnvironmentInstanceDto toDto() {
        EnvironmentInstanceDto envInstanceDto = new EnvironmentInstanceDto();
        envInstanceDto.setEnvironmentInstanceName(getName());
        envInstanceDto.setDescription(this.getDescription());
        envInstanceDto.setBlueprintName(this.getBlueprintName());

        if (this.getStatus() != null) {
            envInstanceDto.setStatus(this.getStatus());
        }
        /*
         * if (envInstance.getEnvironment() != null) { EnvironmentDto envDto = convertToDto(envInstance
         * .getEnvironment()); envInstanceDto.setEnvironmentDto(envDto); }
         */

        List<TierInstanceDto> lTierInstanceDto = new ArrayList<TierInstanceDto>();
        if (getTierInstances() != null) {
            for (TierInstance tierInstance : getTierInstances()) {
                lTierInstanceDto.add(tierInstance.toDto());
            }
            envInstanceDto.setTierInstances(lTierInstanceDto);
        }

        if (getVdc() != null)
            envInstanceDto.setVdc(getVdc());

        if (getPrivateAttributes() != null)
            envInstanceDto.setAttributes(getPrivateAttributes());

        return envInstanceDto;
    }

    public EnvironmentInstancePDto toPDto() {
        EnvironmentInstancePDto envInstanceDto = new EnvironmentInstancePDto();
        envInstanceDto.setEnvironmentInstanceName(getName());
        envInstanceDto.setDescription(this.getDescription());
        envInstanceDto.setBlueprintName(this.getBlueprintName());
        envInstanceDto.setTaskId(this.getTaskId());

        if (this.getStatus() != null) {
            envInstanceDto.setStatus(this.getStatus());
        }

        if (this.getEnvironment() != null) {
            if (this.getEnvironment().getTiers() != null) {
                for (Tier tier : getEnvironment().getTiers()) {
                    TierPDto tierPDto = createTierPDto(tier, this.getTierInstances());
                    envInstanceDto.addTiers(tierPDto);
                }
            }
        }

        List<TierInstanceDto> lTierInstanceDto = new ArrayList<TierInstanceDto>();
        if (getTierInstances() != null) {
            for (TierInstance tierInstance : getTierInstances()) {
                lTierInstanceDto.add(tierInstance.toDto());
            }

        }

        if (getVdc() != null)
            envInstanceDto.setVdc(getVdc());

        return envInstanceDto;
    }

    public EnvironmentInstancePDto toPDtos() {
        EnvironmentInstancePDto envInstanceDto = new EnvironmentInstancePDto();
        envInstanceDto.setEnvironmentInstanceName(getName());
        envInstanceDto.setDescription(this.getDescription());
        envInstanceDto.setBlueprintName(this.getBlueprintName());
        envInstanceDto.setTaskId(this.getTaskId());

        if (this.getStatus() != null) {
            envInstanceDto.setStatus(this.getStatus());
        }

        if (this.getEnvironment() != null) {
            if (this.getEnvironment().getTiers() != null) {
                for (Tier tier : getEnvironment().getTiers()) {
                    TierPDto tierPDto = createTierPDto(tier);
                    envInstanceDto.addTiers(tierPDto);
                }
            }
        }
        if (getVdc() != null)
            envInstanceDto.setVdc(getVdc());

        return envInstanceDto;
    }

    private TierPDto createTierPDto(Tier tier, List<TierInstance> lTierInstance) {
        List<ProductReleaseDto> productReleasedto = new ArrayList();

        for (ProductRelease productRelease : tier.getProductReleases()) {
            if (!productReleasedto.contains(productRelease.toDto())) {
                productReleasedto.add(productRelease.toDto());
            }
        }
        String securitygroup = "";
        if (tier.getSecurityGroup() != null) {
            securitygroup = tier.getSecurityGroup().getName();
        }
        TierPDto tierPDto = new TierPDto(tier.getName(), tier.getMaximumNumberInstances(),
                tier.getMinimumNumberInstances(), tier.getInitialNumberInstances(), productReleasedto,
                tier.getFlavour(), tier.getImage(), tier.getIcono(), securitygroup, tier.getKeypair(),
                tier.getFloatingip());
        if (lTierInstance != null && lTierInstance.size() != 0) {
            for (TierInstance tierInstance : lTierInstance) {

                if (tierInstance.getTier().getName().equals(tier.getName())) {
                    tierPDto.addTierInstance(createInstancePDto(tierInstance));
                }
            }
        }
        return tierPDto;
    }

    private TierPDto createTierPDto(Tier tier) {

        return new TierPDto(tier.getName());
    }

    private TierInstancePDto createInstancePDto(TierInstance tierInstance) {
        List<ProductInstanceDto> productInstanceDtos = new ArrayList();
        if (tierInstance.getProductInstances() != null) {
            for (ProductInstance productInstance : tierInstance.getProductInstances()) {
                productInstanceDtos.add(productInstance.toDto());
            }
        }
        TierInstancePDto tierInstancePDto = null;
        if (tierInstance.getVM() == null) {
            tierInstancePDto = new TierInstancePDto(tierInstance.getName(), productInstanceDtos, null,
                    tierInstance.getStatus(), tierInstance.getTaskId());
        } else {
            tierInstancePDto = new TierInstancePDto(tierInstance.getName(), productInstanceDtos, tierInstance.getVM()
                    .toDto(), tierInstance.getStatus(), tierInstance.getTaskId());
        }
        return tierInstancePDto;
    }
}
