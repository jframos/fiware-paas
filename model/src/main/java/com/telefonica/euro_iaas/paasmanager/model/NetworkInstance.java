/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.model;


import java.util.HashSet;
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

import org.json.JSONException;
import org.json.JSONObject;




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
    private String vdc;

    private String idNetwork;
    
    private String region;
    
    private boolean shared;
    
    private boolean adminStateUp = false;
    private boolean netDefault = false;
    private String tenantId;
    private boolean external;


    private int subNetCount;

    @OneToMany()  
    private Set<SubNetworkInstance> subNets;

    @OneToMany
    private Set<RouterInstance> routers;

    /**
     * Constructor.
     */
    public NetworkInstance() {
        subNetCount = 1;
        subNets = new HashSet<SubNetworkInstance>();
        routers = new HashSet<RouterInstance>();

    }

    /**
     * @param networkName
     */
    public NetworkInstance(String name, String vdc, String region) {
        this.name = name;
        this.vdc = vdc;
        this.region = region;
        subNets = new HashSet<SubNetworkInstance>();
        routers = new HashSet<RouterInstance>();
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
            routers = new HashSet<RouterInstance>();
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
            subNets = new HashSet<SubNetworkInstance>();
        }
        subNet.setIdNetwork(this.getIdNetwork());
        subNets.add(subNet);
        subNetCount++;
    }
    
    /**
     * It does a clone of the collection.
     * @return
     */
    public Set<SubNetworkInstance> cloneSubNets () {
   	 Set<SubNetworkInstance> subNetAux = new HashSet<SubNetworkInstance> ();
        for (SubNetworkInstance subNet2: getSubNets()) {
        	subNetAux.add(subNet2);
        }
        return subNetAux;
   }

    /**
     * It updates a subnet to the network.
     * 
     * @param subNet
     * @return
     */
    public void updateSubNet(SubNetworkInstance subNet) {
        removes(subNet);
        subNets.add(subNet);
    }

    public void removes(SubNetworkInstance subNetwork) {
        if (subNets.contains(subNetwork)){
            subNets.remove(subNetwork);
        }
    }
    

    
    public boolean contains (SubNetworkInstance subNet) {
        System.out.println (subNet.hashCode());
        for (SubNetworkInstance subNetInst: subNets) {
            System.out.println (subNetInst.hashCode());
            if (subNetInst.equals(subNet)) {
                return true;
            }
        }
        return false;
        
    }

    /**
     * It obtains the id of the subnet to be used for the router.
     * @return
     */
    public String getIdNetRouter() {
    	for (SubNetworkInstance subNet: this.getSubNets()) {
    		return subNet.getIdSubNet();
    	}
        return "";
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
     * @return the shared
     */
    public boolean getShared() {
        return shared;
    }
    

    /**
     * 
     * @param shared
     */
    public void setShared(boolean shared) {
        this.shared = shared;
    }
    
    /**
     * @return the shared
     */
    public boolean getExternal() {
        return external;
    }
    

    /**
     * 
     * @param shared
     */
    public void setExternal(boolean external) {
        this.external = external;
    }
    
    /**
     * @return the netDefault
     */
    public boolean isDefaultNet() {
        return this.netDefault;
    }
    

    /**
     * 
     * @param shared
     */
    public void setDefaultNet(boolean netDefault) {
        this.netDefault = netDefault;
    }
    
    /**
     * @return the shared
     */
    public boolean getAdminStateUp() {
        return adminStateUp;
    }

    /**
     * It gets the routers.
     * 
     * @return List<Router>
     */
    public Set<RouterInstance> getRouters() {
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
    public Set<SubNetworkInstance> getSubNets() {
        return this.subNets;
    }

    /**
     * @param networkName
     */
    public void setIdNetwork(String id) {
        this.idNetwork = id;
    }
    
    public String getRegionName () {
    	return this.region;
    }

    /**
     * It obtains the json for adding this subnet into a router.
     * @return
     */
    public String toAddInterfaceJson() {
    	for (SubNetworkInstance subNet: this.getSubNets()) {
    		return subNet.toJsonAddInterface();
    	}
        return "";
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
    
    public static NetworkInstance fromJson (JSONObject jsonNet, String region) throws JSONException {
        String name = (String) jsonNet.get("name");
        boolean shared = (Boolean) jsonNet.get("shared");
        String id = (String) jsonNet.get("id");
        boolean adminStateUp = (Boolean) jsonNet.get("admin_state_up");
        String tenantId = (String) jsonNet.get("tenant_id");
        boolean external = (Boolean) jsonNet.get("router:external");


        NetworkInstance netInst = new NetworkInstance(name, tenantId, region);
        netInst.setIdNetwork(id);
        netInst.setShared(shared);
        netInst.setTenantId(tenantId);
        netInst.setAdminStateUp(adminStateUp);
        netInst.setExternal(external);
        return netInst;
    }

	public void setSubNets(Set<SubNetworkInstance> subNets2) {
		this.subNets = subNets2;
		
	}

    public void setAdminStateUp(boolean adminStateUp) {
        this.adminStateUp = adminStateUp;
        
    }

    public void setTenantId(String tenantId) {
        this.tenantId=tenantId; 
    }
    
    public String getTenantId() {
        return tenantId;
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
        NetworkInstance other = (NetworkInstance) obj;
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
