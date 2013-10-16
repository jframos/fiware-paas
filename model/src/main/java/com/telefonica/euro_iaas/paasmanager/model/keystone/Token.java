/**
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.model.keystone;

import javax.persistence.Entity;
import javax.persistence.Id;

import net.sf.json.JSONObject;

/**
 * @author jesus.movilla
 */
@Entity
public class Token {

    /*
     * @Id
     * @GeneratedValue(strategy = GenerationType.SEQUENCE) private Long tokenId;
     */

    /** The tokenId. */
    @Id
    private String id = "";

    /** The date when the token expires. */
    private String expires = "";

    /** extra Info. */
    private String extra = "";

    /** tenantId **/
    private String tenantId = "";

    /**
	 * 
	 */
    public Token() {
        super();
    }

    /**
     * @param id
     */
    public Token(String id) {
        super();
        this.id = id;
    }

    /**
     * @param id
     * @param expires
     * @param extra
     */
    public Token(String id, String expires, String extra) {
        super();
        this.id = id;
        this.expires = expires;
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
     * @return the expires
     */
    public String getExpires() {
        return expires;
    }

    /**
     * @param expires
     *            the expires to set
     */
    public void setExpires(String expires) {
        this.expires = expires;
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

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId
     *            the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantId(JSONObject object) {
        this.tenantId = object.getString("tenantId");
    }
}
