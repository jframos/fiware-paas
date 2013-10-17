/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

public interface FirewallingClient {

    String deploySecurityGroup(ClaudiaData claudiaData, SecurityGroup securityGroup) throws InfrastructureException;

    String deployRule(ClaudiaData claudiaData, Rule rule) throws InfrastructureException;

    void destroyRule(ClaudiaData claudiaData, Rule rule) throws InfrastructureException;

    void destroySecurityGroup(ClaudiaData claudiaData, SecurityGroup securityGroup) throws InfrastructureException;

    /**
     * Load a SecurityGroup from OpenStack
     * 
     * @param claudiaData
     * @param securityGroupId
     * @return
     * @throws EntityNotFoundException
     */
    SecurityGroup loadSecurityGroup(ClaudiaData claudiaData, String securityGroupId) throws EntityNotFoundException;

    /**
     * Loads all securityGroups associated to a certain vdc
     * 
     * @param claudiaData
     * @return List<SecurityGroup>
     */
    List<SecurityGroup> loadAllSecurityGroups(ClaudiaData claudiaData) throws OpenStackException;
}
