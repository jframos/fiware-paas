package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * Represents an instance of a tier
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierInstance extends InstallableInstance {

    // @Column(nullable=false, length=256)
    // private String name;

    @ManyToOne
    private Tier tier;
    // private int currentNumberInstances;
    /** the vmOVF. ***/
    @Column(length = 100000)
    private String ovf = "";

    /** the VAPP. ***/
    @Column(length = 10000)
    private String vapp = "";
    private String taskId = "";

    private int numberReplica = 0;

    @Embedded
    private VM vm;

    /*
     * @JoinColumn(name = "environmentinstance_id", referencedColumnName = "id",
     * nullable = false)
     * 
     * @ManyToOne(optional = false, fetch = FetchType.LAZY) private
     * EnvironmentInstance environmentInstance= null;
     */

    // @ManyToMany
    // @JoinTable(name = "tierInstance_has_productInstances")

    // @OneToMany(targetEntity = ProductInstance.class, mappedBy =
    // "tierInstance", fetch = FetchType.LAZY)
    // @OneToMany(targetEntity = Artifact.class, mappedBy = "productInstance",
    // fetch = FetchType.LAZY)
    // cascade = CascadeType.ALL

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tierinstance_has_productinstances", joinColumns = { @JoinColumn(name = "tierinstance_ID", nullable = false,
            updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "productinstance_ID", nullable = false, updatable = false) })
            private List<ProductInstance> productInstances;

    /**
     * Default Constructor.
     */
    public TierInstance() {

    }

    /**
     * @param tier
     * @param productInstances
     * @param currentNumberInstances
     * @param ovf
     * @param fqn
     */
    public TierInstance(Tier tier, List<ProductInstance> productInstances,
            String name, VM vm, String ovf) {
        super();
        this.tier = tier;
        this.productInstances = productInstances;
        this.ovf = ovf;
        this.name = name;
        this.vm = vm;
    }

    public TierInstance(Tier tier2, List<ProductInstance> productInstances2,
            VM vm) {
        this.tier = tier2;
        this.productInstances = productInstances2;
        this.vm = vm;
    }

    public TierInstance(Tier tier, String ovf, String name, VM vm) {
        super();
        this.tier = tier;
        this.ovf = ovf;
        this.name = name;
        this.vm = vm;
    }

    public void addProductInstance(ProductInstance productInstance) {
        if (productInstances == null) {
            productInstances = new ArrayList();
        }
        this.productInstances.add(productInstance);
    }

    public void deleteProductInstance(ProductInstance productInstance) {
        if (productInstances.contains(productInstance))
            this.productInstances.remove(productInstance);
    }

    @Override
    public String getName() {
        return name;
    }

    public int getNumberReplica() {
        return numberReplica;
    }

    /**
     * @return the tier
     */
    public String getOvf() {
        return ovf;
    }

    /**
     * @return the productInstances
     */
    public List<ProductInstance> getProductInstances() {
        return productInstances;
    }

    public String getTaskId() {
        return taskId;

    }

    /**
     * @return the tier
     */
    public Tier getTier() {
        return tier;
    }

    public String getVApp() {
        return vapp;
    }

    public VM getVM() {
        return vm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNumberReplica(int numberReplica) {
        this.numberReplica = numberReplica;
    }

    /**
     * @param tier
     *            the tier to set
     */
    public void setOvf(String ovf) {
        this.ovf = ovf;
    }

    /**
     * @param productInstances
     *            the productInstances to set
     */
    public void setProductInstances(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

    public void setTaskId(String id) {
        taskId = id;

    }

    /**
     * @param tier
     *            the tier to set
     */
    public void setTier(Tier tier) {
        this.tier = tier;
    }

    /**
     * @param tier
     *            the tier to set
     */
    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    public void setVM(VM vm) {
        this.vm = vm;
    }

    /*
     * public EnvironmentInstance getEnvironmentInstance() { return
     * environmentInstance; }
     * 
     * public void setEnvironmentInstance(EnvironmentInstance
     * environmentInstance) { this.environmentInstance = environmentInstance; }
     */

    public TierInstanceDto toDto() {
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        tierInstanceDto.setTierInstanceName(getName());
        tierInstanceDto.setTierDto(getTier().toDto());
        tierInstanceDto.setReplicaNumber(getNumberReplica());
        if (this.getVM() != null) {
            tierInstanceDto.setVM(getVM().toDto());
        }

        List<ProductInstanceDto> lProductInstanceDto = new ArrayList<ProductInstanceDto>();
        if (getProductInstances() != null) {
            for (ProductInstance productInstance : getProductInstances()) {
                ProductInstanceDto productInstanceDto = productInstance.toDto();
                lProductInstanceDto.add(productInstanceDto);
            }
        }

        tierInstanceDto.setProductInstanceDtos(lProductInstanceDto);

        if (getPrivateAttributes() != null)
            tierInstanceDto.setAttributes(getPrivateAttributes());

        return tierInstanceDto;
    }

}
