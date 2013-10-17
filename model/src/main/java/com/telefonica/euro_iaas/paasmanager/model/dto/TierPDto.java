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

/**
 * Represents an artifact to be installed on a ProductRelease
 * 
 * @author Henar Mu�oz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TierPDto {

    private String name;
    private String flavour;
    private String image;

    private Integer maximumNumberInstances;
    private Integer minimumNumberInstances;
    private Integer initialNumberInstances;

    private List<ProductReleaseDto> productReleaseDtos;

    private String icono;
    private String security_group;
    private String keypair;
    private String floatingip;

    private List<TierInstancePDto> tierInstancePDto;

    /**
     * Default Constructor
     */
    public TierPDto() {
    }

    public TierPDto(String name) {
        this.name = name;
    }

    public TierPDto(String name, Integer maximumNumberInstances, Integer minimumNumberInstances,
            Integer initialNumberInstances, List<ProductReleaseDto> productReleaseDtos, String flavour, String image,
            String icono, String security_group, String keypair, String floatingip) {
        this.name = name;
        this.maximumNumberInstances = maximumNumberInstances;
        this.minimumNumberInstances = minimumNumberInstances;
        this.initialNumberInstances = initialNumberInstances;
        this.productReleaseDtos = productReleaseDtos;
        this.flavour = flavour;
        this.image = image;
        this.icono = icono;
        this.security_group = security_group;
        this.keypair = keypair;
        this.floatingip = floatingip;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
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
     * @return the initialNumberInstances
     */
    public Integer getInitialNumberInstances() {
        return initialNumberInstances;
    }

    /**
     * @return the productReleases
     */
    public List<ProductReleaseDto> getProductReleaseDtos() {
        return productReleaseDtos;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @param initialNumberInstances
     *            the initialNumberInstances to set
     */
    public void setInitialNumberInstances(Integer initialNumberInstances) {
        this.initialNumberInstances = initialNumberInstances;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    /**
     * @param productReleases
     *            the productReleases to set
     */
    public void setProductReleaseDtos(List<ProductReleaseDto> productReleaseDtos) {
        this.productReleaseDtos = productReleaseDtos;
    }

    public void addProductRelease(ProductReleaseDto productReleaseDto) {
        if (this.productReleaseDtos == null)
            productReleaseDtos = new ArrayList();

        productReleaseDtos.add(productReleaseDto);
    }

    public void removeProductRelease(ProductReleaseDto productReleaseDto) {

        productReleaseDtos.remove(productReleaseDto);
    }

    public void setTierInstances(List<TierInstancePDto> tierInstancePDto) {
        this.tierInstancePDto = tierInstancePDto;
    }

    public List<TierInstancePDto> getTierInstances() {
        return this.tierInstancePDto;
    }

    public void addTierInstance(TierInstancePDto tierInstacesDto) {
        if (this.tierInstancePDto == null)
            tierInstancePDto = new ArrayList();

        tierInstancePDto.add(tierInstacesDto);
    }

    public void removeTierInstances(TierInstancePDto tierInstacesDto) {

        tierInstancePDto.remove(tierInstacesDto);
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getIcono() {
        return this.icono;
    }

    public void setSecurity_group(String security_group) {
        this.security_group = security_group;
    }

    public String getSecurity_group() {
        return this.security_group;
    }

    public void setKeypair(String keypair) {
        this.keypair = keypair;
    }

    public String getKeypair() {
        return this.keypair;
    }

    public void setFloatingip(String floatingip) {
        this.floatingip = floatingip;
    }

    public String getFloatingip() {
        return this.floatingip;
    }

}
