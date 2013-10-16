package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import org.apache.log4j.Logger;

public class EnvironmentManagerImpl implements EnvironmentManager {

    private EnvironmentDao environmentDao;
    private TierManager tierManager;

    /** The log. */
    private static Logger log = Logger.getLogger(EnvironmentManagerImpl.class);

    public Environment create(ClaudiaData claudiaData, Environment environment)
            throws InvalidEnvironmentRequestException {

        log.debug("Creating environment " + environment.getName() + " with escription " + environment.getDescription()
                + " org " + environment.getOrg() + " vdc " + environment.getVdc() + " tiers  " + environment.getTiers());

        Environment environmentDB = new Environment();

        environmentDB.setName(environment.getName());
        environmentDB.setDescription(environment.getDescription());
        environmentDB.setOrg(environment.getOrg());
        environmentDB.setVdc(environment.getVdc());

        if (environment.getOvf() != null) {
            environmentDB.setOvf(environment.getOvf());
        }
        if (environment.getTiers() != null) {
            log.debug("Number of Tiers " + environment.getTiers().size());
            for (Tier tier : environment.getTiers()) {
                Tier tierDB = null;
                try {

                    try {
                        tierDB = tierManager.load(tier.getName(), claudiaData.getVdc(), environment.getName());
                    } catch (EntityNotFoundException e) {
                        try {
                            tierDB = tierManager.create(claudiaData, environment.getName(), tier);
                        } catch (InfrastructureException e1) {
                            throw new InvalidEnvironmentRequestException(e1.getMessage(), e1);
                        }
                    }

                    environmentDB.addTier(tierDB);

                } catch (InvalidEntityException e) {
                    throw new InvalidEnvironmentRequestException(e.getMessage(), e);
                } catch (InvalidSecurityGroupRequestException e) {
                    throw new InvalidEnvironmentRequestException(e.getMessage(), e);
                }
            }
        }

        /*
         * try { environmentType = environmentTypeDao.load(environment .getEnvironmentType().getName()); } catch
         * (EntityNotFoundException e) { try { environmentType = environmentTypeDao.create(environment
         * .getEnvironmentType()); } catch (AlreadyExistsEntityException e1) { String errorMessage =
         * "The EnvironmentType Object " + environmentType.getName() + "is already in Database"; throw new
         * InvalidEnvironmentRequestException (errorMessage); } catch (InvalidEntityException e2) { String errorMessage
         * = " The EnvironmentType Object " + environmentType.getName() + "is invaled" + e2.getMessage(); throw new
         * InvalidEnvironmentRequestException (errorMessage); } } environmentDB.setEnvironmentType(environmentType);
         */

        try {
            environmentDB = environmentDao.create(environmentDB);
        } catch (InvalidEntityException iee) {
            String errorMessage = "The Environment Object " + environment.getName() + " is " + "NOT valid: "
                    + iee.getMessage();
            throw new InvalidEnvironmentRequestException(errorMessage);
        } catch (AlreadyExistsEntityException aee) {
            String errorMessage = "The Environment Object " + environment.getName() + " already exist in Database";
            throw new InvalidEnvironmentRequestException(errorMessage);
        } catch (Exception aee) {
            String errorMessage = "The Environment Object " + environment.getName() + " is " + "NOT valid";
            throw new InvalidEnvironmentRequestException(errorMessage);
        }

        return environmentDB;
    }

    public void destroy(ClaudiaData data, Environment environment) throws InvalidEntityException,
            InfrastructureException {

        List<Tier> tiers = environment.getTiers();

        if (tiers != null && tiers.size() > 0) {
            environment.setTiers(null);

            try {
                environmentDao.update(environment);
            } catch (InvalidEntityException e) {
                throw new InvalidEntityException(EnvironmentDao.class, e);
            }
            for (Tier tier : tiers) {
                try {
                    tierManager.delete(data, tier);
                } catch (EntityNotFoundException e) {
                    throw new InvalidEntityException(TierManager.class, e);
                }

            }
        }

        environmentDao.remove(environment);
    }

    public Environment load(String name, String vdc) throws EntityNotFoundException {
        return environmentDao.load(name, vdc);
    }

    public Environment load(String name) throws EntityNotFoundException {
        return environmentDao.load(name);
    }

    public List<Environment> findAll() {
        return environmentDao.findAll();
    }

    public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
        return environmentDao.findByCriteria(criteria);
    }

    /**
     * @param environmentDao
     *            the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }

    public Environment update(Environment environment) throws EntityNotFoundException, InvalidEntityException {
        return environmentDao.update(environment);
    }

    public Environment updateTier(Environment environment, Tier tierold, Tier tiernew) throws EntityNotFoundException,
            InvalidEntityException {
        int idTierOld = environment.getTiers().indexOf(tierold);
        environment.deleteTier(tierold);
        environmentDao.update(environment);
        environment.getTiers().add(idTierOld, tiernew);
        return environmentDao.update(environment);
    }

}
