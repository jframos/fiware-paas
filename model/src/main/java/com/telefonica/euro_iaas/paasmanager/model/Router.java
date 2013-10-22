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
 * A router.
 * 
 * @author Henar Munoz
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "Router")
public class Router {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    private String name;

    private String idRouter;

    private final String idPublicNetwork;

    /**
     * Constructor.
     * @param idPublicNetwork
     */
    public Router(String idPublicNetwork) {
        this.idPublicNetwork = idPublicNetwork;
    }

    /**
     * Constructor.
     * @param idPublicNetwork
     * @param name
     */
    public Router(String idPublicNetwork, String name) {
        this.name = name;
        this.idPublicNetwork = idPublicNetwork;
    }


    /**
     * @return the id
     */
    public String getIdRouter() {
        return this.idRouter;
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
    public void setIdRouter(String id) {
        this.idRouter = id;
    }

    /**
     * @return string with the json representation for the request
     */

    public String toJson(){
        return  "{" +
        "    \"router\":" +
        "    {" +
        "        \"name\": \"" + name + "\"," +
        "        \"admin_state_up\": true ,"+
        "        \"external_gateway_info\" : {" +
        "             \"network_id\": \"" + idPublicNetwork + "\"" +
        "        }" +
        "    }" +
        "}";
    }


}
