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
 * 
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

    private String idNetwork;

    /**
     * Constructor.
     */
    public Router() {
    }

    /**
     * @param networkName
     */
    public Router(String name) {
        this.name = name;
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
    public void setIdNetwork(String id) {
        this.idNetwork = id;
    }

    /**
     * @param id
     */
    public void setIdRouter(String id) {
        this.idRouter = id;
    }


}
