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

/**
 * A sub network.
 * 
 * @author Henar Munoz
 * 
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

    private String idSubNet;

    private String idNetwork;

    private String cidr;

    private String allocationPoolsStart ;

    private String allocationPoolsEnd ;

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
        this.cidr = "10.100." + 1 + ".0/24";
    }

    /**
     * @param networkName
     */
    public SubNetwork(String name, String id) {
        this.name = name;
        this.cidr = "10.100." + id + ".0/24";
    }

    /**
     * @return the id
     */
    public String getCidr() {
        return cidr;
    }

    /**
     * @return the id
     */
    public String getIdNetwork() {
        return idNetwork;
    }



    /**
     * @return the id
     */
    public String getIdSubNet() {
        return idSubNet;
    }

    /**
     * @return the networkName
     */
    public String getName() {
        return name;
    }

    /**
     * @param id
     */
    public void setIdNetwork(String id) {
        this.idNetwork = id;
    }

    /**
     * @param id
     */
    public void setIdSubNet(String id) {
        this.idSubNet = id;
    }

    /**
     * the json for the OPenstack request
     * @return
     */
    public String toJson () {
        return"{\"subnet\":{" +
        "      \"name\":\"" + name + "\"," +
        "      \"network_id\":\"" + this.idNetwork + "\"," +
        "      \"ip_version\":4," +
        "      \"cidr\":\" "+ this.cidr+
        "   }" +
        "}";
    }


}
