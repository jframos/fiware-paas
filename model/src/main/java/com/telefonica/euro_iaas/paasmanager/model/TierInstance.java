/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * Represents an instance of a tier.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierInstance extends InstallableInstance {

    @ManyToOne
    private Tier tier;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tierinstance_has_productinstances", joinColumns = { @JoinColumn(name = "tierinstance_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "productinstance_ID", nullable = false, updatable = false) })
    private List<ProductInstance> productInstances;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tierinstance_has_networkinstance", joinColumns = { @JoinColumn(name = "tierinstance_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "networkinstance_ID", nullable = false, updatable = false) })
    private Set<NetworkInstance> networkInstances;

    /**
     * Constructor.
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

    public TierInstance(Tier tier, List<ProductInstance> productInstances, String name, VM vm, String ovf) {
        super();
        this.tier = tier;
        this.productInstances = productInstances;
        this.ovf = ovf;
        this.name = name;
        this.vm = vm;
    }

    /**
     * @param tier2
     * @param productInstances2
     * @param vm
     */
    public TierInstance(Tier tier2, List<ProductInstance> productInstances2, VM vm) {
        this.tier = tier2;
        this.productInstances = productInstances2;
        this.vm = vm;
    }

    /**
     * @param tier
     * @param ovf
     * @param name
     * @param vm
     */
    public TierInstance(Tier tier, String ovf, String name, VM vm) {
        super();
        this.tier = tier;
        this.ovf = ovf;
        this.name = name;
        this.vm = vm;
    }

    /**
     * @param productInstance
     */
    public void addProductInstance(ProductInstance productInstance) {
        if (productInstances == null) {
            productInstances = new ArrayList<ProductInstance>();
        }
        this.productInstances.add(productInstance);
    }
    
    /**
     * @param productInstance
     */
    public void addNetworkInstance(NetworkInstance networkInstance) {
        if (networkInstances == null) {
        	networkInstances = new HashSet<NetworkInstance>();
        }
        this.networkInstances.add(networkInstance);
    }

    /**
     * @param productInstance
     */
    public void deleteProductInstance(ProductInstance productInstance) {
        if (productInstances.contains(productInstance)) {
            this.productInstances.remove(productInstance);
        }
    }
    
    /**
     * @param productInstance
     */
    public void deleteNetworkInstance(NetworkInstance networkInstance) {
        if (networkInstances.contains(networkInstance)) {
            this.networkInstances.remove(networkInstance);
        }
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
    
    /**
     * @return the productInstances
     */
    public Set<NetworkInstance> getNetworkInstances() {
    	if (networkInstances == null) {
    		networkInstances = new HashSet<NetworkInstance> ();
    	}
        return networkInstances;
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
     * @param ovf
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
    /**
    * @param productInstances
    *            the productInstances to set
    */
   public void setNetworkInstance(Set<NetworkInstance> networkInstances) {
       this.networkInstances = networkInstances;
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
     * @param vapp
     */
    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    public void setVM(VM vm) {
        this.vm = vm;
    }

    /**
     * The Dto specification.
     * 
     * @return
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

        if (getPrivateAttributes() != null) {
            tierInstanceDto.setAttributes(getPrivateAttributes());
        }

        return tierInstanceDto;
    }
    
    /**
     * to json.
     * @return
     */
    public String toJson() {
        String payload = "{\"server\": " + "{\"key_name\": \""
        + getTier().getKeypair() + "\", ";
        if (getTier().getSecurityGroup() != null) {
            payload = payload + "\"security_groups\": [{ \"name\": \""
            + getTier().getSecurityGroup().getName() + "\"}], ";
        }
        if (this.getNetworkInstances() != null) {
            payload = payload + "\"networks\": [";
            for (NetworkInstance net: this.getNetworkInstances()){

                payload = payload + "{ \"uuid\": \""
                + net.getIdNetwork() + "\"}";
            }
            payload = payload + "], ";

        }

        payload = payload
        + "\"flavorRef\": \"" + getTier().getFlavour() + "\", " + "\"imageRef\": \""
        + getTier().getImage() + "\", " + "\"name\": \"" + name + "\"}}";
        return payload;

    }

}
