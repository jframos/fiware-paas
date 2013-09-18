package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.exception.PaasManagerServerRuntimeException;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;

public class ProductInstanceDaoJpaImpl extends AbstractBaseDao<ProductInstance, Long>implements ProductInstanceDao {


	@Override
	public List<ProductInstance> findAll() {
		return super.findAll(ProductInstance.class);
	}

	@Override
	public ProductInstance load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(ProductInstance.class, "id", arg0);
	}

	@Override
	public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
		Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(ProductInstance.class);

   
        if (!StringUtils.isEmpty(criteria.getProductName())) {
            baseCriteria.createAlias("productRelease", "rls");
            //.createAlias("rls.productRelease", "prod");
            baseCriteria.add(Restrictions.eq("rls.name",
                    criteria.getProductName()));
        }
        
        if (criteria.getVm() != null) {
            baseCriteria.add(getVMCriteria(baseCriteria, criteria.getVm()));
        }
        
        List<ProductInstance> productInstances = setOptionalPagination(
                criteria, baseCriteria).list();

        return productInstances;
	}
	
	public Criterion getVMCriteria(Criteria baseCriteria, VM vm) {
        if (!StringUtils.isEmpty(vm.getFqn()) && !StringUtils.isEmpty(vm.getIp()) 
        		&& !StringUtils.isEmpty(vm.getDomain())
                && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.eq(ProductInstance.VM_FIELD,
                    vm);
        } else if (!StringUtils.isEmpty(vm.getFqn())) {
            return Restrictions.eq("vm.fqn", vm.getFqn());
        } else if (!StringUtils.isEmpty(vm.getIp())) {
            return Restrictions.eq("vm.ip", vm.getIp());
        } else if (!StringUtils.isEmpty(vm.getDomain())
                && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.and(
                    Restrictions.eq("vm.hostname", vm.getHostname()),
                    Restrictions.eq("vm.domain", vm.getDomain()));
        } else {
            throw new PaasManagerServerRuntimeException(
                    "Invalid VM while finding products by criteria");
        }
    }
	
    @Override
    public ProductInstance findUniqueByCriteria(
            ProductInstanceSearchCriteria criteria)
                    throws NotUniqueResultException {
    	List<ProductInstance> instances = findByCriteria(criteria);
        if (instances.size() > 1) {
            throw new NotUniqueResultException();
        }
        if (instances.size() == 0)
        	throw new NoSuchElementException();
        
        return instances.iterator().next();
    }
    
	private Criterion addStatus(Criterion statusCr, Status status) {
		SimpleExpression expression = Restrictions.eq("status", status);
		if (statusCr == null) {
			statusCr = expression;
		} else {
			statusCr = Restrictions.or(statusCr, expression);
	    }
	        return statusCr;
	}


}
