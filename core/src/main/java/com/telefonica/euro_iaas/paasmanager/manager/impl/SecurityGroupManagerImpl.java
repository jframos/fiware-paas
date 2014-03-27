/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.SecurityGroupDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.manager.RuleManager;
import com.telefonica.euro_iaas.paasmanager.manager.SecurityGroupManager;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

public class SecurityGroupManagerImpl implements SecurityGroupManager {

    private SecurityGroupDao securityGroupDao = null;
    private RuleManager ruleManager = null;
    private FirewallingClient firewallingClient = null;

    private static Logger log = Logger.getLogger(SecurityGroupManagerImpl.class);

    public SecurityGroup create(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InvalidEntityException, AlreadyExistsEntityException, InfrastructureException {

        SecurityGroup securityGroupDB = new SecurityGroup();
        String idSecurityGroup = firewallingClient.deploySecurityGroup(region, token, vdc, securityGroup);
        log.debug("Create security group " + securityGroup.getName() + " with idSecurityGroup " + idSecurityGroup);

        if (securityGroup.getRules() != null) {
            for (Rule rule : securityGroup.getRules()) {
                rule.setIdParent(idSecurityGroup);
                rule = ruleManager.create(region, token, vdc, rule);
                securityGroupDB.addRule(rule);
            }
        }
        securityGroup.setIdSecurityGroup(idSecurityGroup);
        securityGroupDB = insert(securityGroup);
        return securityGroupDB;
    }

    public void addRule(String region, String token, String vdc, SecurityGroup securityGroup, Rule rule)
            throws InvalidEntityException, AlreadyExistsEntityException, InfrastructureException {

        rule.setIdParent(securityGroup.getIdSecurityGroup());
        rule = ruleManager.create(region, token, vdc, rule);
        securityGroup.addRule(rule);
        securityGroupDao.update(securityGroup);

    }

    public SecurityGroup insert(SecurityGroup securityGroup) throws AlreadyExistsEntityException,
            InvalidEntityException {
        SecurityGroup securityGroupDB = new SecurityGroup();
        try {
            securityGroupDB = securityGroupDao.load(securityGroup.getName());
            String errorMessage = "The securityGroup Object " + securityGroup.getName() + " already exist in Database";
            throw new InvalidEntityException(errorMessage);

        } catch (EntityNotFoundException e) {
        }

        securityGroupDB.setName(securityGroup.getName());
        securityGroupDB.setDescription(securityGroup.getDescription());
        securityGroupDB.setIdSecurityGroup(securityGroup.getIdSecurityGroup());

        if (securityGroup.getRules() != null) {
            securityGroupDB.setRules(securityGroup.getRules());
        }

        try {
            securityGroupDB = securityGroupDao.create(securityGroupDB);
        } catch (AlreadyExistsEntityException aee) {
            throw new AlreadyExistsEntityException(securityGroup);
        } catch (Exception aee) {
            String errorMessage = "The securityGroup Object " + securityGroupDB.getName() + " is " + "NOT valid";
            throw new InvalidEntityException(errorMessage);
        }

        return securityGroupDB;
    }
    
    /**
     * It destroy a security group.
     */

    public void destroy(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InvalidEntityException, InfrastructureException {
        
        if (securityGroup.getRules().isEmpty()) {
            log.warn ("There is not any rule associated to the security group");
        } else {
            List<Rule> rules = securityGroup.cloneRules ();
            securityGroup.setRules(null);
            securityGroupDao.update(securityGroup);
            
            for (Rule rule : rules) {
                try {
                    ruleManager.destroy(region, token, vdc, rule); 
                } catch (Exception e) {
                    log.warn("Error to destroy the rule " + e.getMessage());
                }
            }
        }

        try {
            firewallingClient.destroySecurityGroup(region, token, vdc, securityGroup);
        } catch (Exception e) {
            log.warn("There is not any rule associated to the security group");
        }
        
        securityGroupDao.remove(securityGroup);

    }

    public List<SecurityGroup> findAll() {
        return securityGroupDao.findAll();
    }

    public SecurityGroup load(String name) throws EntityNotFoundException {
        return securityGroupDao.load(name);
    }

    public void setSecurityGroupDao(SecurityGroupDao securityGroupDao) {
        this.securityGroupDao = securityGroupDao;
    }

    public void setRuleManager(RuleManager ruleManager) {
        this.ruleManager = ruleManager;
    }

    public void setFirewallingClient(FirewallingClient firewallingClient) {
        this.firewallingClient = firewallingClient;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.paasmanager.manager.SecurityGroupManager#update(com.telefonica.euro_iaas.paasmanager
     * .model.SecurityGroup)
     */
    public SecurityGroup update(SecurityGroup securityGroup) throws InvalidEntityException {
        return securityGroupDao.update(securityGroup);
    }

}
