/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.TemplateDao;
import com.telefonica.euro_iaas.paasmanager.manager.TemplateManager;
import com.telefonica.euro_iaas.paasmanager.model.Template;

/**
 * @author jesus.movilla
 */
public class TemplateManagerImpl implements TemplateManager {

    private TemplateDao templateDao;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.TemplateManager#load(java .lang.String)
     */
    public Template load(String name) throws EntityNotFoundException {
        return templateDao.load(name);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.TemplateManager#findAll()
     */
    public List<Template> findAll() {
        return templateDao.findAll();
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.TemplateManager#insert(com
     * .telefonica.euro_iaas.paasmanager.model.Template)
     */
    public Template insert(Template template) throws InvalidEntityException, AlreadyExistsEntityException {
        return templateDao.create(template);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.TemplateManager#update(com
     * .telefonica.euro_iaas.paasmanager.model.Template)
     */
    public Template update(Template template) throws InvalidEntityException {
        return templateDao.update(template);
    }

    /**
     * @param templateDao
     *            the templateDao to set
     */
    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

}
