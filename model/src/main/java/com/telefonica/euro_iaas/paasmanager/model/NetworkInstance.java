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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "NetworkInstance")
public class NetworkInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    /** the network name */
    private String name;

    private String idNetwork;

    private int subNetCount;

    @OneToMany()

  /*  @JoinTable(name = "networkinstancehassubnetworkinstance",
    joinColumns = {
        @JoinColumn(name="networkinstance_ID", unique = true)           
    },
    inverseJoinColumns = {
        @JoinColumn(name="subnetworkinstance_ID")
     }
   )*/

  
    private List<SubNetworkInstance> subNets;

    @OneToMany
    private List<RouterInstance> routers;

    /**
     * Constructor.
     */
    public NetworkInstance() {
        subNetCount = 1;
        subNets = new ArrayList<SubNetworkInstance>();
        routers = new ArrayList<RouterInstance>();

    }

    /**
     * @param networkName
     */
    public NetworkInstance(String name) {
        this.name = name;
        subNets = new ArrayList<SubNetworkInstance>();
        routers = new ArrayList<RouterInstance>();
        subNetCount = 1;
    }

    /**
     * It adds a router to the network.
     *
     * @param router
     * @return
     */
    public void addRouter(RouterInstance router) {
        if (routers == null) {
            routers = new ArrayList<RouterInstance>();
        }
        routers.add(router);
    }

    /**
     * It adds a subnet to the network.
     *
     * @param subNet
     * @return
     */
    public void addSubNet(SubNetworkInstance subNet) {
        if (subNets == null) {
            subNets = new ArrayList<SubNetworkInstance>();
        }
        subNet.setIdNetwork(this.getIdNetwork());
        subNets.add(subNet);
        subNetCount++;
    }

    /**
     * It updates a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void updateSubNet(SubNetworkInstance subNet) {
        if (contains(subNet.getName())) {
            removes(subNet); 
        }
        subNets.add(subNet);
    }
    
    public boolean contains(String subNetName) {
        for (SubNetworkInstance subNet: subNets) {
        	return subNet.getName().equals(subNetName);
        }
        return false;
    }
    
    public void removes(SubNetworkInstance subNetwork) {
    	SubNetworkInstance subNetw =null;
        for (SubNetworkInstance subNet: subNets) {
        	if (subNet.getName().equals(subNetwork.getName())) {
        		subNetw = subNet;
        	}
        }
        subNets.remove(subNetw);
    }
    
    /**
     *
     * @param subNet
     * @return
     */
    public boolean contains(SubNetworkInstance subNet) {
        if (subNets == null) {
            subNets = new ArrayList<SubNetworkInstance>();
        }
        return subNets.contains(subNet);
    }

    /**
     * It obtains the id of the subnet to be used for the router.
     * @return
     */
    public String getIdNetRouter() {
        if (getSubNets().size() != 0) {
            return this.getSubNets().get(0).getIdSubNet();
        }
        else {
            return "";
        }
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
     * It gets the routers.
     * 
     * @return List<Router>
     */
    public List<RouterInstance> getRouters() {
        return this.routers;
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
    public List<SubNetworkInstance> getSubNets() {
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
        } else {
            return "";
        }
    }

    /**
     * It returns the string representations for rest rerquest.
     * 
     * @return the json representation
     */
    public String toJson() {
        return "{" + " \"network\":{" + "    \"name\": \"" + this.name + "\"," + "    \"admin_state_up\": true,"
        + "    \"shared\": false" + "  }" + "}";

    }

}
