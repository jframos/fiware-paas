/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.keystone;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author jesus.movilla
 */
@Entity
public class User {

    /*
     * @Id
     * @GeneratedValue(strategy = GenerationType.SEQUENCE) private Long userId;
     */

    /** The userId. */
    @Id
    private String id = "";

    /** The name. */
    private String name = "";

    /** The extra information. */
    private String extra = "";

    /**
	 * 
	 */
    public User() {
        super();
    }

    /**
     * @param id
     */
    public User(String id) {
        super();
        this.id = id;
    }

    /**
     * @param id
     * @param name
     * @param extra
     */
    public User(String id, String name, String extra) {
        super();
        this.id = id;
        this.name = name;
        this.extra = extra;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }

    /**
     * @param extra
     *            the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

}
