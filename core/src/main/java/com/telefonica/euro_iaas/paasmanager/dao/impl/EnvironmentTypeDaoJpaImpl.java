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
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;

public class EnvironmentTypeDaoJpaImpl extends AbstractBaseDao<EnvironmentType, String> implements EnvironmentTypeDao {

    public List<EnvironmentType> findAll() {
        return super.findAll(EnvironmentType.class);
    }

    public EnvironmentType load(String arg0) throws EntityNotFoundException {
        return super.loadByField(EnvironmentType.class, "name", arg0);
    }

}
