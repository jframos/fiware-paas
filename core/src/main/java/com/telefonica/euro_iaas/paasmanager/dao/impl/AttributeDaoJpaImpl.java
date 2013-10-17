/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.AttributeDao;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

public class AttributeDaoJpaImpl extends AbstractBaseDao<Attribute, String> implements AttributeDao {

    /*
     * Find all the attribute in paas-manager database
     * @return attribute, the list of attribute
     */
    public List<Attribute> findAll() {
        return super.findAll(Attribute.class);
    }

    /*
     * Find an attribute by name-searching
     * @param name, the name of the artifact
     * @return attribute, the attribute
     */
    public Attribute load(String name) throws EntityNotFoundException {
        return super.loadByField(Attribute.class, "name", name);
    }

}
