/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.dto.SubNetworkDto;

/**
 * A sub network.
 * 
 * @author Henar Munoz
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "SubNetwork")
public class SubNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    // the network name //
    private String name;

    private String cidr;


    /**
     * Constructor.
     */
    public SubNetwork() {
    }

    /**
     * @param networkName
     */
    public SubNetwork(String name) {
        this.name = name;
    }

    /**
     * @param networkName
     */
    public SubNetwork(String name, String cidr) {
        this.name = name;
        this.cidr = cidr;
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
    public String getName() {
        return name;
    }

    /**
     * @param cidr
     */
    public void setCidr(String cidr) {
        this.cidr = cidr;
    }


    /**
     * To the subnetwork Dto.
     * @return
     */
    public SubNetworkDto toDto() {
        SubNetworkDto subNetworkDto = new SubNetworkDto(this.getName());
        subNetworkDto.setCidr(this.getCidr());
        return subNetworkDto;

    }
    
    /**
     * To the subnetwork Dto.
     * @return
     */
    public SubNetworkInstance toInstance() {
    	SubNetworkInstance subNetworkInstance = new SubNetworkInstance(this.getName());
    	subNetworkInstance.setCidr(this.getCidr());
        return subNetworkInstance;

    }


}
