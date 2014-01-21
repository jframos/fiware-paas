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

import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Represents an artifact to be installed on a ProductRelease.
 * 
 * @author Henar Mu�oz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierDto {

    private String name;
    private String flavour;
    private String image;

    private Integer maximumNumberInstances;
    private Integer minimumNumberInstances;
    private Integer initialNumberInstances;

    private List<ProductReleaseDto> productReleaseDtos;

    private List<NetworkDto> networkDto;

    private String icono = "";
    private String securityGroup = "";
    private String keypair = "";
    private String floatingip = "";

    private String region = "";

    /**
     * Default Constructor.
     */
    public TierDto() {
        this.networkDto = new ArrayList<NetworkDto>();
        this.productReleaseDtos = new ArrayList<ProductReleaseDto>();
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param maximumNumberInstances
     * @param minimumNumberInstances
     * @param initialNumberInstances
     * @param productReleaseDtos
     */

    public TierDto(String name, Integer maximumNumberInstances, Integer minimumNumberInstances,
            Integer initialNumberInstances, List<ProductReleaseDto> productReleaseDtos) {

        this.name = name;
        this.maximumNumberInstances = maximumNumberInstances;
        this.minimumNumberInstances = minimumNumberInstances;
        this.initialNumberInstances = initialNumberInstances;
        this.productReleaseDtos = productReleaseDtos;

        this.networkDto = new ArrayList<NetworkDto>();
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param maximumNumberInstances
     * @param minimumNumberInstances
     * @param initialNumberInstances
     * @param productReleaseDtos
     * @param flavour
     * @param image
     * @param icono
     * @param keypair
     * @param floatingip
     */

    public TierDto(String name, Integer maximumNumberInstances, Integer minimumNumberInstances,
            Integer initialNumberInstances, List<ProductReleaseDto> productReleaseDtos, String flavour, String image,
            String icono, String keypair, String floatingip) {

        this.name = name;
        this.maximumNumberInstances = maximumNumberInstances;
        this.minimumNumberInstances = minimumNumberInstances;
        this.initialNumberInstances = initialNumberInstances;
        this.productReleaseDtos = productReleaseDtos;
        this.flavour = flavour;
        this.image = image;
        this.icono = icono;
        this.keypair = keypair;
        this.floatingip = floatingip;
        this.networkDto = new ArrayList<NetworkDto>();
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param maximumNumberInstances
     * @param minimumNumberInstances
     * @param initialNumberInstances
     * @param productReleaseDtos
     * @param flavour
     * @param image
     * @param icono
     * @param securityGroup
     * @param keypair
     * @param floatingip
     */

    public TierDto(String name, Integer maximumNumberInstances, Integer minimumNumberInstances,
            Integer initialNumberInstances, List<ProductReleaseDto> productReleaseDtos, String flavour, String image,
            String icono, String securityGroup, String keypair, String floatingip) {

        this.name = name;
        this.maximumNumberInstances = maximumNumberInstances;
        this.minimumNumberInstances = minimumNumberInstances;
        this.initialNumberInstances = initialNumberInstances;
        this.productReleaseDtos = productReleaseDtos;
        this.flavour = flavour;
        this.image = image;
        this.icono = icono;
        this.keypair = keypair;
        this.floatingip = floatingip;
        this.securityGroup = securityGroup;
        this.networkDto = new ArrayList<NetworkDto>();
    }

    /**
     * Add network dto.
     * 
     * @param networkDto
     *            the network do to be add to the array.
     */
    public void addNetworkDto(NetworkDto networkDto) {
        if (networkDto == null) {
            this.networkDto = new ArrayList<NetworkDto>();
        }
        this.networkDto.add(networkDto);
    }

    /**
     * Add product release.
     * 
     * @param productReleaseDto
     *            the product release to be added.
     */
    public void addProductRelease(ProductReleaseDto productReleaseDto) {
        if (this.productReleaseDtos == null) {
            productReleaseDtos = new ArrayList<ProductReleaseDto>();
        }
        productReleaseDtos.add(productReleaseDto);
    }

    /**
     * It obtains the Tier object associated.
     * 
     * @tier
     */
    public Tier fromDto(String vdc) {

        Tier tier = new Tier();
        tier.setName(getName());
        tier.setInitialNumberInstances(getInitialNumberInstances());
        tier.setMaximumNumberInstances(getMaximumNumberInstances());
        tier.setMinimumNumberInstances(getMinimumNumberInstances());
        tier.setIcono(getIcono());
        tier.setFlavour(getFlavour());
        tier.setImage(getImage());
        tier.setKeypair(getKeypair());
        tier.setFloatingip(getFloatingip());
        tier.setRegion(getRegion());

        for (ProductReleaseDto pReleaseDto : getProductReleaseDtos()) {
            ProductRelease pRelease = new ProductRelease();
            pRelease.setProduct(pReleaseDto.getProductName());
            pRelease.setVersion(pReleaseDto.getVersion());

            if (pReleaseDto.getProductDescription() != null) {
                pRelease.setDescription(pReleaseDto.getProductDescription());
            }
            tier.addProductRelease(pRelease);
        }

        for (NetworkDto networkDto : this.getNetworksDto()) {
            Network network = networkDto.fromDto(vdc);
            tier.addNetwork(network);
        }
        return tier;
    }

    public String getFlavour() {
        return flavour;
    }

    public String getFloatingip() {
        return this.floatingip;
    }

    public String getIcono() {
        return this.icono;
    }

    public String getImage() {
        return this.image;
    }

    /**
     * @return the initialNumberInstances
     */
    public Integer getInitialNumberInstances() {
        return initialNumberInstances;
    }

    public String getKeypair() {
        return this.keypair;
    }

    /**
     * @return the maximum_number_instances
     */
    public Integer getMaximumNumberInstances() {
        return maximumNumberInstances;
    }

    /**
     * @return the minimumNumberInstances
     */
    public Integer getMinimumNumberInstances() {
        return minimumNumberInstances;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the list of networks associated
     */
    public List<NetworkDto> getNetworksDto() {
        return networkDto;
    }

    /**
     * @return the productReleases
     */
    public List<ProductReleaseDto> getProductReleaseDtos() {
        if (productReleaseDtos == null) {
            this.productReleaseDtos = new ArrayList<ProductReleaseDto>();
        }
        return productReleaseDtos;
    }

    /**
     * Get the security group.
     * 
     * @return
     */
    public String getSecurityGroup() {
        return this.securityGroup;
    }

    /**
     * It removes the product release.
     * 
     * @param productReleaseDto
     */
    public void removeProductRelease(ProductReleaseDto productReleaseDto) {

        productReleaseDtos.remove(productReleaseDto);
    }

    /**
     * @param flavour
     *            the flavour to set
     */
    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public void setFloatingip(String floatingip) {
        this.floatingip = floatingip;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @param initialNumberInstances
     *            the initialNumberInstances to set
     */
    public void setInitialNumberInstances(Integer initialNumberInstances) {
        this.initialNumberInstances = initialNumberInstances;
    }

    public void setKeypair(String keypair) {
        this.keypair = keypair;
    }

    /**
     * @param maximumNumberInstances
     *            the maximumNumberInstances to set
     */
    public void setMaximumNumberInstances(Integer maximumNumberInstances) {
        this.maximumNumberInstances = maximumNumberInstances;
    }

    /**
     * @param minimumNumberInstances
     *            the minimumNumberInstances to set
     */
    public void setMinimumNumberInstances(Integer minimumNumberInstances) {
        this.minimumNumberInstances = minimumNumberInstances;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param networkDto
     *            the network do to be add to the array
     */
    public void setNetworksDto(List<NetworkDto> networkDto) {
        this.networkDto = networkDto;
    }

    /**
     * @param productReleaseDtos
     *            the productReleases to set
     */
    public void setProductReleaseDtos(List<ProductReleaseDto> productReleaseDtos) {
        this.productReleaseDtos = productReleaseDtos;
    }

    /**
     * @param securityGroup
     *            the securityGroup to set
     */
    public void setSecurityGroup(String securityGroup) {
        this.securityGroup = securityGroup;

    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
