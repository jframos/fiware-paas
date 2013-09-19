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
import com.telefonica.euro_iaas.paasmanager.dao.TemplateDao;
import com.telefonica.euro_iaas.paasmanager.model.Template;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TemplateSearchCriteria;

/**
 * @author jesus.movilla
 *
 */
public class TemplateDaoJpaImpl extends AbstractBaseDao<Template, String> 
	implements TemplateDao {

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#findAll()
	 */
	public List<Template> findAll() {
		// TODO Auto-generated method stub
		return super.findAll(Template.class);
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#load(java.io.Serializable)
	 */
	public Template load(String arg0) throws EntityNotFoundException {
		return super.loadByField(Template.class, "name", arg0);
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.TemplateDao#findByCriteria(com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TemplateSearchCriteria)
	 */
	public List<Template> findByCriteria(TemplateSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}


}
