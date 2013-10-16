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

@Entity
public class SecurityGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String idSecurityGroup = "";

    @Column(nullable = false, length = 256)
    private String name;
    private String description;
    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "securitygroup_has_rules", joinColumns = {
    // @JoinColumn(name = "securitygroup_ID", nullable = false, updatable =
    // false) }, inverseJoinColumns = { @JoinColumn(name = "rule_ID", nullable =
    // false, updatable = false) })

    @OneToMany
    private List<Rule> rules;

    public SecurityGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SecurityGroup() {
        // TODO Auto-generated constructor stub
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdSecurityGroup(String idSecurityGroup) {
        this.idSecurityGroup = idSecurityGroup;
    }

    public String getIdSecurityGroup() {
        return idSecurityGroup;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        if (rules == null) {
            rules = new ArrayList<Rule>();
        }
        rules.add(rule);
    }

    public void deleteRule(Rule rule) {

        if (rules.contains(rule))
            rules.remove(rule);
    }

    public String toJSON() {
        return "{\"security_group\": \n" + "{" + "\"name\": \"" + name + "\", " + "\"description\":  \"" + description
                + "\" " + "}" + "}";
    }

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
}
