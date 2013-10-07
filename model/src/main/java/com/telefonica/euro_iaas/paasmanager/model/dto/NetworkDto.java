package com.telefonica.euro_iaas.paasmanager.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * Represents the data about the network to be invoke in the request.
 * 
 * @author Henar Muñoz
 * @version $Id: $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkDto {

    /* The network name */
    private String networkName;
    /* The subred name */
    private String subnetName;



    /**
     * Default Constructor.
     */
    public NetworkDto() {
    }

    /**
     * @param networkName
     */
    public NetworkDto(String networkName) {
        this.networkName = networkName;
    }

    /**
     * @param networkName
     * @param subnetName
     */
    public NetworkDto(String networkName, String subnetName) {
        this.networkName = networkName;
        this.subnetName = subnetName;
    }

    /**
     * It returns the network object associated to the dto.
     * @return the network
     */
    public Network fromDto() {

        Network net = new Network (this.getNetworkName());

        if (this.getSubNetName()!= null) {
            SubNetwork subnet = new SubNetwork (this.getSubNetName(), ""+net.getSubNetCounts());
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
     * @return the networkName
     */
    public String getSubNetName() {
        return subnetName;
    }

    /**
     * @param networkName
     */
    public void setNetworkName(String networkName) {
        this.networkName =networkName;
    }

    /**
     * @param subnetName
     */
    public void setSubNetName(String subnetName) {
        this.subnetName = subnetName;
    }



}
