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

    private String idNetwork;

    private int subNetCount;

    @OneToMany
    private List<SubNetwork> subNets;

    /**
     * Constructor.
     */
    public Network() {
        subNetCount = 1;
        subNets = new ArrayList<SubNetwork>();
    }

    /**
     * @param networkName
     */
    public Network(String name) {
        this.name = name;
        subNets = new ArrayList<SubNetwork>();
        subNetCount = 1;
    }

    /**
     * It gets the id for the subnet to specify the cidr.
     * 
     * @param subNet
     * @return
     */
    public void addSubNet(SubNetwork subNet) {
        if (subNets == null) {
            subNets = new ArrayList<SubNetwork>();
        }
        subNet.setIdNetwork(this.getIdNetwork());
        subNets.add(subNet);
        subNetCount++;
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
    public String getIdNetwork() {
        return idNetwork;
    }

    /**
     * @return the networkName
     */
    public String getNetworkName() {
        return name;
    }

    /**
     * It gets the id for the subnet to specify the cidr.
     * 
     * @return
     */
    public int getSubNetCounts() {
        return subNetCount;
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
     * @param networkName
     */
    public void setIdNetwork(String id) {
        this.idNetwork = id;
    }

    /**
     * It obtains the json for adding this subnet into a router.
     * @return
     */
    public String toAddInterfaceJson() {
        if (getSubNets().size() != 0) {
            return this.getSubNets().get(0).toJsonAddInterface();
        }
        else {
            return "";
        }
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
     * It returns the string representations for rest rerquest.
     * 
     * @return the json representation
     */
    public String toJson() {
        return "{" + " \"network\":{" + "    \"name\": \"" + this.name + "\"," + "    \"admin_state_up\": false,"
                + "    \"shared\": false" + "  }" + "}";

    }

}
