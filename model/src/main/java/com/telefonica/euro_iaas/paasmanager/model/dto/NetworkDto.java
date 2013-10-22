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
     * @return the network
     */
    public Network fromDto() {

        Network net = new Network(this.getNetworkName());

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
