/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.SecurityGroupDao;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;

/**
 * @author Henar
 */
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityGroupDaoJpaImpl extends AbstractBaseDao<SecurityGroup, String> implements SecurityGroupDao {

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#findAll()
     */
    public List<SecurityGroup> findAll() {
        // TODO Auto-generated method stub
        return super.findAll(SecurityGroup.class);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#load(java.io.Serializable)
     */
    public SecurityGroup load(String arg0) throws EntityNotFoundException {
        return super.loadByField(SecurityGroup.class, "name", arg0);
    }

    public SecurityGroup updateSecurityGroupId(String idSecurityGroup, SecurityGroup securityGroup)
            throws InvalidEntityException {
        Query query = getEntityManager().createQuery(
                "UPDATE SecurityGroup sg " + "SET sg.idSecurityGroup= :idSecurityGroup" + "  where sg.name = :name");
        query.setParameter("idSecurityGroup", idSecurityGroup);
        query.setParameter("name", securityGroup.getName());
        SecurityGroup secGroup = null;
        try {
            query.executeUpdate();
            secGroup = load(securityGroup.getName());
        } catch (NoResultException e) {
            throw new InvalidEntityException(securityGroup);
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException(securityGroup);
        }
        return secGroup;
    }
}
