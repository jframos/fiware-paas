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
