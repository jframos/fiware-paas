package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;

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
