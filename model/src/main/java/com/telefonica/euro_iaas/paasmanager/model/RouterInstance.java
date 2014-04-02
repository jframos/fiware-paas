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
public class RouterInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    private String name;

    private String idRouter;

    private String idPublicNetwork;

    public RouterInstance() {

    }

    /**
     * Constructor.
     * @param idPublicNetwork
     */
    public RouterInstance(String idPublicNetwork) {
        this.idPublicNetwork = idPublicNetwork;

    }

    /**
     * Constructor.
     * @param idPublicNetwork
     * @param name
     */
    public RouterInstance(String idPublicNetwork, String name) {
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
