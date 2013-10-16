package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.Rule;

/**
 * Defines the methods needed to persist Environment objects.
 * 
 * @author Henar Muñoz
 */
public interface RuleDao extends BaseDAO<Rule, String> {

}