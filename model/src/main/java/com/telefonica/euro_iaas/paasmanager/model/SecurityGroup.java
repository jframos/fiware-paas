/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Security Group entity for iptables.
 * 
 * @author henar
 */
@Entity
public class SecurityGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String idSecurityGroup = "";

    @Column(nullable = false, length = 256)
    private String name;
    private String description = "desc: ";

    @OneToMany
    private List<Rule> rules;

    /**
     * Constructor.
     */
    public SecurityGroup() {

    }

    /**
     * Constructor.
     * 
     * @param name
     * @param description
     */
    public SecurityGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Add rule in the security group.
     * 
     * @param rule
     */
    public void addRule(Rule rule) {
        if (rules == null) {
            rules = new ArrayList<Rule>();
        }
        rules.add(rule);
    }

    /**
     * Delete a rule in the security group.
     * 
     * @param rule
     */
    public void deleteRule(Rule rule) {

        if (rules.contains(rule)) {
            rules.remove(rule);
        }
    }

    /**
     * From json.
     * 
     * @param jsonNode
     */
    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject jsonNode) {
        name = jsonNode.getString("name");
        description = jsonNode.getString("description");
        // tenant_id = jsonNode.getJSONObject("tenant_id");
        idSecurityGroup = jsonNode.getString("id");

        // Rules
        JSONArray rulesjsonArray = jsonNode.getJSONArray("rules");
        List<Rule> rules = new ArrayList<Rule>();
        for (int i = 0; i < rulesjsonArray.size(); i++) {
            JSONObject object = rulesjsonArray.getJSONObject(i);
            Rule rule = new Rule();
            rule.fromJson(object);
            rules.add(rule);
        }
        setRules(rules);
    }

    public String getDescription() {
        return description;
    }

    public String getIdSecurityGroup() {
        return idSecurityGroup;
    }

    public String getName() {
        return name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdSecurityGroup(String idSecurityGroup) {
        this.idSecurityGroup = idSecurityGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Json.
     * 
     * @return
     */
    public String toJSON() {
        return "{\"security_group\": \n" + "{" + "\"name\": \"" + name + "\", " + "\"description\":  \"" + description
                + "\" " + "}" + "}";
    }
}
