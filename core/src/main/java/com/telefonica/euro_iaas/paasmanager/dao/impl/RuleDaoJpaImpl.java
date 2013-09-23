/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.RuleDao;
import com.telefonica.euro_iaas.paasmanager.model.Rule;

/**
 * @author Henar Muñoz
 * 
 */
public class RuleDaoJpaImpl extends AbstractBaseDao<Rule, String> implements
		RuleDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#findAll()
	 */
	public List<Rule> findAll() {
		// TODO Auto-generated method stub
		return super.findAll(Rule.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.commons.dao.BaseDAO#load(java.io.Serializable)
	 */
	public Rule load(String arg0) throws EntityNotFoundException {
		return super.loadByField(Rule.class, "name", arg0);
	}

}
