/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * Represents the data about the network to be invoke in the request.
 * 
 * @author Henar Munoz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SubNetworkDto {

    /* The subred name */
    private String subnetName;
    private String cidr;



    /**
     * Default Constructor.
     */
    public SubNetworkDto() {
    }

    /**
     * @param networkName
     */
    public SubNetworkDto(String subnetName) {
        this.subnetName = subnetName;
    }

    /**
     * @param subnetName
     * @param cidr
     */
    public SubNetworkDto(String subnetName, String cidr) {
        this.cidr = cidr;
        this.subnetName = subnetName;
    }

    /**
     * It returns the network object associated to the dto.
     * @return the network
     */
    public SubNetwork fromDto() {

        SubNetwork subNet = new SubNetwork(this.getSubNetName());
        subNet.setCidr(this.getCidr());
        return subNet;
    }

    /**
     * @return the cidr
     */
    public String getCidr() {
        return cidr;
    }

    /**
     * @return the networkName
     */
    public String getSubNetName() {
        return subnetName;
    }


    /**
     * @param subnetName
     */
    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    /**
     * @param subnetName
     */
    public void setSubNetName(String subnetName) {
        this.subnetName = subnetName;
    }



}
