package com.telefonica.euro_iaas.paasmanager.dao;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

/**
 * Defines the methods needed to persist Environment objects.
 * 
 * @author Henar Mu�oz
 */
public interface SecurityGroupDao extends BaseDAO<SecurityGroup, String> {

    SecurityGroup updateSecurityGroupId(String securityGroupId, SecurityGroup securityGroup)
            throws InvalidEntityException;
}
