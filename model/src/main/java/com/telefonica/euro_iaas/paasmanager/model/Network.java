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

    // the network name //
    private String name;

 //   private String idNetwork;

//    private int subNetCount;

    @OneToMany
    private List<SubNetwork> subNets;

  //  @OneToMany
   // private List<Router> routers;

    /**
     * Constructor.
     */
    public Network() {
    //    subNetCount = 1;
        subNets = new ArrayList<SubNetwork>();
     //   routers = new ArrayList<Router>();

    }

    /**
     * @param networkName
     */
    public Network(String name) {
        this.name = name;
        subNets = new ArrayList<SubNetwork>();
    //    routers = new ArrayList<Router>();
     //   subNetCount = 1;
       
    }


    /**
     * It adds a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void addSubNet(SubNetwork subNet) {
        if (subNets == null) {
            subNets = new ArrayList<SubNetwork>();
        }
  //      subNet.setIdNetwork(this.getIdNetwork());
        subNets.add(subNet);
    //    subNetCount++;
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
     * 
     * @param subNet
     * @return
     */
    public boolean contains(SubNetwork subNet) {
        if (subNets == null) {
            subNets = new ArrayList<SubNetwork>();
        }
        return subNets.contains(subNet);
    }


    /**
     * @return the networkName
     */
    public String getNetworkName() {
        return name;
    }


    /**
     * It gets the subnets.
     * 
     * @return List<SubNetwork>
     */
    public List<SubNetwork> getSubNets() {
        return this.subNets;
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
       NetworkInstance networkInstance = new NetworkInstance(this.name);
       for (SubNetwork subnet: this.getSubNets()) {
           SubNetworkInstance subNetInstance = subnet.toInstance();
           networkInstance.addSubNet(subNetInstance);
       }
       return networkInstance;
   }


}
