/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.sf.json.JSONObject;

/**
 * Rule entity for security groups.
 * 
 * @author henar
 */
@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ipProtocol;
    private String fromPort;
    private String toPort;
    private String sourceGroup;
    private String cidr;

    private String idparent;
    private String idrule;

    /**
     * The default constructor.
     */
    public Rule() {
    }

    /**
     * Constructor.
     * 
     * @param ipProtocol
     * @param fromPort
     * @param toPort
     * @param sourceGroup
     * @param cidr
     */
    public Rule(String ipProtocol, String fromPort, String toPort, String sourceGroup, String cidr) {
        this.ipProtocol = ipProtocol;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.sourceGroup = sourceGroup;
        this.cidr = cidr;

    }

    /**
     * From json to entity.
     * 
     * @param jsonNode
     */
    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject jsonNode) {
        ipProtocol = jsonNode.getString("ip_protocol");
        fromPort = jsonNode.getString("from_port");
        toPort = jsonNode.getString("to_port");
        sourceGroup = jsonNode.getString("group");
        idparent = jsonNode.getString("parent_group_id");
        cidr = (jsonNode.getJSONObject("ip_range")).getString("cidr");
    }

    public String getCidr() {
        return cidr;
    }

    public String getFromPort() {
        return fromPort;
    }

    public String getIdParent() {
        return idparent;
    }

    public String getIdRule() {
        return idrule;
    }

    public String getIpProtocol() {
        return ipProtocol;
    }

    public String getSourceGroup() {
        return sourceGroup;
    }

    public String getToPort() {
        return toPort;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }

    public void setIdParent(String idparent) {
        this.idparent = idparent;
    }

    public void setIdRule(String idrule) {
        this.idrule = idrule;
    }

    public void setIpProtocol(String ipProtocol) {
        this.ipProtocol = ipProtocol;
    }

    public void setSourceGroup(String sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    /**
     * to json for request Openstack.
     * 
     * @return
     */
    public String toJSON() {
        String payload = "{\"security_group_rule\": \n" + "{" + "\"ip_protocol\": \"" + getIpProtocol() + "\", \n"
                + "\"from_port\": \"" + getFromPort() + "\", \n" + "\"to_port\": \"" + getToPort() + "\", \n"
                + "\"cidr\": \"" + getCidr() + "\", \n";

        if (!getSourceGroup().equals("")) {
            payload = payload + "\"group_id\": \"" + getSourceGroup() + "\", \n";
        }
        payload = payload + "\"parent_group_id\": \"" + idparent + "\" }}";
        return payload;

    }

}
