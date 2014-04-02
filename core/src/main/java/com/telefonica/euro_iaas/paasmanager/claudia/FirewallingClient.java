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
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

public interface FirewallingClient {

    String deploySecurityGroup(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InfrastructureException;

    String deployRule(String region, String token, String vdc, Rule rule) throws InfrastructureException;

    void destroyRule(String region, String token, String vdc, Rule rule) throws InfrastructureException;

    void destroySecurityGroup(String region, String token, String vdc, SecurityGroup securityGroup)
            throws InfrastructureException;

    /**
     * Load a SecurityGroup from OpenStack.
     */
    SecurityGroup loadSecurityGroup(String region, String token, String vdc, String securityGroupId)
            throws EntityNotFoundException, InfrastructureException;

    /**
     * Loads all securityGroups associated to a certain vdc.
     */
    List<SecurityGroup> loadAllSecurityGroups(String region, String token, String vdc) throws InfrastructureException;
}
