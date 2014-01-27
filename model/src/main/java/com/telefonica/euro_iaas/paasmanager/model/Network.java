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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.dto.NetworkDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.SubNetworkDto;

/**
 * A network.
 * 
 * @author Henar Munoz
 */ 

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "Network")
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    private String name;
    
    private String vdc;

    @OneToMany
    private Set<SubNetwork> subNets;

    /**
     * Constructor.
     */
    public Network() {
        subNets = new HashSet<SubNetwork>();
    }

    /**
     * @param networkName
     */
    public Network(String name, String vdc) {
        this.name = name;
        this.vdc = vdc;
        subNets = new HashSet<SubNetwork>();
    }


    /**
     * It adds a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void addSubNet(SubNetwork subNet) {
        if (subNets == null) {
            subNets = new HashSet<SubNetwork>();
        }
        subNets.add(subNet);
    }
    
    /**
     * It does a copy of the collection.
     * @return
     */
    public Set<SubNetwork> cloneSubNets(){
        Set<SubNetwork> subNetAux = new HashSet<SubNetwork> ();
        for (SubNetwork subNet2: getSubNets()) {
         	subNetAux.add(subNet2);
        }
        return subNetAux;
   }
    
    /**
     * It deletes a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void deleteSubNet(SubNetwork subNet) {
        if (subNets.contains(subNet)) {
            subNets.remove(subNet); 
        }
    }
    
    /**
     * It updates a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void updateSubNet(SubNetwork subNet) {
        if (subNets.contains(subNet)) {
            subNets.remove(subNet); 
        }
        subNets.add(subNet);
    }

    /**
     * 
     * @param subNet
     * @return
     */
    public boolean contains(SubNetwork subNet) {
        return subNets.contains(subNet);
    }


    /**
     * @return the networkName
     */
    public String getNetworkName() {
        return name;
    }
    

    public void setNetworkName(String name) {
        this.name=name;
    }


    /**
     * It gets the subnets.
     * 
     * @return List<SubNetwork>
     */
    public Set<SubNetwork> getSubNets() {
        return this.subNets;
    }
    
    /**
     * It add the subnet collection
     * @param subNets
     */
    public void setSubNets(Set<SubNetwork> subNets) {
        this.subNets = subNets;
    }


    /**
     * the dto entity.
     * 
     * @return
     */
    public NetworkDto toDto() {
        NetworkDto networkDto = new NetworkDto(this.name);
        for (SubNetwork subnet: this.getSubNets()) {
            SubNetworkDto subNetDto = subnet.toDto();
            networkDto.addSubNetworkDto(subNetDto);
        }
        return networkDto;
    }
    
    /**
    * the network instance.
    * 
    * @return
    */
    public NetworkInstance toNetworkInstance() {
        NetworkInstance networkInstance = new NetworkInstance(this.name, vdc);
        for (SubNetwork subnet: this.getSubNets()) {
            SubNetworkInstance subNetInstance = subnet.toInstance();
            networkInstance.addSubNet(subNetInstance);
        }
        return networkInstance;
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
        Network other = (Network) obj;
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
    @Override
   public int hashCode() {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((id == null) ? 0 : id.hashCode());
       return result;
   }

}
