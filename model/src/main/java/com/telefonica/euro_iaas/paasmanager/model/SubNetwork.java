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
    public SubNetwork(String name, String id) {
        this.name = name;
        this.cidr = "10.100." + id + ".0/24";
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
        SubNetwork other = (SubNetwork) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
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
