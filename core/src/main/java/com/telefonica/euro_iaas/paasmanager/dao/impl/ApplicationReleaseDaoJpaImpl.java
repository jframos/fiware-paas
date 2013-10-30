/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;

public class ApplicationReleaseDaoJpaImpl extends AbstractBaseDao<ApplicationRelease, String> implements
        ApplicationReleaseDao {

    public List<ApplicationRelease> findAll() {
        return super.findAll(ApplicationRelease.class);
    }

    public ApplicationRelease load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ApplicationRelease.class, "name", arg0);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao#findByCriteria
     * (com.telefonica.euro_iaas.paasmanager.model.searchcriteria. ApplicationReleaseSearchCriteria)
     */
    public List<ApplicationRelease> findByCriteria(ApplicationReleaseSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ApplicationRelease.class);

        List<ApplicationRelease> applicationReleases = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getArtifact() != null) {
            applicationReleases = filterByArtifact(applicationReleases, criteria.getArtifact());
        }

        return applicationReleases;
    }

    /**
     * Filter the result by application release
     * 
     * @param applicationReleasess
     * @param artifact
     * @return
     */
    private List<ApplicationRelease> filterByArtifact(List<ApplicationRelease> applicationReleases, Artifact artifact) {
        List<ApplicationRelease> result = new ArrayList<ApplicationRelease>();
        for (ApplicationRelease applicationRelease : applicationReleases) {
            if (applicationRelease.getArtifacts().contains(artifact)) {
                result.add(applicationRelease);
            }
        }
        return result;
    }

}
