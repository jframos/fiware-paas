/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.Template;

/**
 * @author jesus.movilla
 */
public interface TemplateManager {

    /**
     * Find the Template using the name.
     * 
     * @param name
     *            the template name
     * @throws EntityNotFoundException
     *             if the template does not exists
     */
    Template load(String name) throws EntityNotFoundException;

    /**
     * Retrieve all Template created in the system.
     * 
     * @return the existent templates.
     */
    List<Template> findAll();

    /**
     * Insert the Template in database
     * 
     * @param the
     *            template to be inserted
     * @return the inserted template
     * @throws InvalidEntityException
     *             , AlreadyExistsEntityException
     */
    Template insert(Template template) throws InvalidEntityException, AlreadyExistsEntityException;

    /**
     * Update the template in database
     * 
     * @param template
     * @return the updated template
     * @throws InvalidEntityException
     */
    Template update(Template template) throws InvalidEntityException;

}
