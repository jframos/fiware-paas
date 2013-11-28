/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;

@Transactional(propagation = Propagation.REQUIRED)
public class ApplicationTypeDaoJpaImpl extends AbstractBaseDao<ApplicationType, String> implements ApplicationTypeDao {

    public List<ApplicationType> findAll() {
        return super.findAll(ApplicationType.class);
    }

    public ApplicationType load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ApplicationType.class, "name", arg0);
    }

}
