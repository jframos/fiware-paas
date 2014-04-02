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

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * Represents the data about the network to be invoke in the request.
 * 
 * @author Henar Munoz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkDto {

    /* The network name */
    private String networkName;
    /* The subred name */
    private List<SubNetworkDto> subNetworkDto;

    /**
     * Default Constructor.
     */
    public NetworkDto() {
        subNetworkDto = new ArrayList<SubNetworkDto>();
    }

    /**
     * @param networkName
     */
    public NetworkDto(String networkName) {
        this.networkName = networkName;
        subNetworkDto = new ArrayList<SubNetworkDto>();
    }

    public void addSubNetworkDto (SubNetworkDto subNetworkDto) {
        this.subNetworkDto.add(subNetworkDto);
    }

    /**
     * It returns the network object associated to the dto.
     * 
     * @return the network
     */
    public Network fromDto(String vdc) {

        Network net = new Network(this.getNetworkName(), vdc);

        for (SubNetworkDto subNetworkDto: this.getSubNetworkDto()) {
            SubNetwork subnet = new SubNetwork(subNetworkDto.getSubNetName());
            subnet.setCidr(subNetworkDto.getCidr());
            net.addSubNet(subnet);
        }
        return net;
    }

    /**
     * @return the networkName
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * 
     * @return List<SubNetworkDto>
     */
    public List<SubNetworkDto> getSubNetworkDto() {
        return this.subNetworkDto;
    }

    /**
     * @param networkName
     */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }


    /**
     * 
     * @param subNetworkDto
     */
    public void setSubNetworkDto(List<SubNetworkDto> subNetworkDto) {
        this.subNetworkDto = subNetworkDto;
    }

}
