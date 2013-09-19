package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;


import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Service;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;


/**
 * Provides some criteria to search TierInstance entities.
 *
 * @author Jesus M. Movilla
 *
 */
public class TierSearchCriteria extends AbstractSearchCriteria {

  
    private Environment environment;

    /**
     * Default constructor
     */
    public TierSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param productInstance
     * @param environment
     */
    public TierSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, ProductInstance productInstance,
            Service service, Environment environment) {
        super(page, pageSize, orderBy, orderType);

        this.environment = environment;

    }




	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}