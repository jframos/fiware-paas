package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.RuleDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.RuleManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import org.apache.log4j.Logger;

public class RuleManagerImpl implements RuleManager {

    private RuleDao ruleDao = null;
    private FirewallingClient firewallingClient = null;
    private static Logger log = Logger.getLogger(RuleManagerImpl.class);

    public Rule create(ClaudiaData claudiaData, Rule rule) throws InvalidEntityException, AlreadyExistsEntityException,
            InfrastructureException {
        log.debug("Create rule " + rule.getFromPort() + " from security group " + rule.getIdParent());
        String idRule = firewallingClient.deployRule(claudiaData, rule);
        log.debug("id rule " + idRule);
        rule.setIdRule(idRule);
        rule = ruleDao.create(rule);
        return rule;
    }

    public void destroy(ClaudiaData claudiaData, Rule rule) throws InvalidEntityException, InfrastructureException {
        log.debug("Destroying rule " + rule.getFromPort() + " from security group " + rule.getIdParent());
        firewallingClient.destroyRule(claudiaData, rule);
        try {
            ruleDao.remove(rule);
        } catch (Exception e) {
            log.error("Error to remove the rule in BD " + e.getMessage());
            throw new InvalidEntityException(rule);
        }

    }

    public List<Rule> findAll() {
        return ruleDao.findAll();
    }

    public Rule load(String name) throws EntityNotFoundException {
        return ruleDao.load(name);
    }

    public void setRuleDao(RuleDao ruleDao) {
        this.ruleDao = ruleDao;
    }

    public void setFirewallingClient(FirewallingClient firewallingClient) {
        this.firewallingClient = firewallingClient;
    }

}
