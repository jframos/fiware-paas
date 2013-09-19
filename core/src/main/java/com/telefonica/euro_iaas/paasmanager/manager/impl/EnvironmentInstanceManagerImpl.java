package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;

import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils;

public class EnvironmentInstanceManagerImpl implements
		EnvironmentInstanceManager {

	private EnvironmentInstanceDao environmentInstanceDao;

	private ProductInstanceManager productInstanceManager;
	private EnvironmentManager environmentManager;
	private InfrastructureManager infrastructureManager;
	private TierInstanceManager tierInstanceManager;
	private EnvironmentUtils environmentUtils;

	/** The log. */
	private static Logger log = Logger
			.getLogger(EnvironmentInstanceManagerImpl.class);
	
	/**  Max lenght of an OVF */
	private static final Integer tam_max = 30000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager
	 * #findByCriteria
	 * (com.telefonica.euro_iaas.paasmanager.model.searchcriteria.
	 * EnvironmentInstanceSearchCriteria)
	 */
	public List<EnvironmentInstance> findByCriteria(
			EnvironmentInstanceSearchCriteria criteria) {

		return environmentInstanceDao.findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager
	 * #findAll()
	 */
	public List<EnvironmentInstance> findAll() {
		return environmentInstanceDao.findAll();
	}

	public EnvironmentInstance create(ClaudiaData claudiaData,
			Environment environment) throws InvalidEntityException,
			AlreadyExistsEntityException, NotUniqueResultException,
			InfrastructureException, IPNotRetrievedException,
			ProductInstallatorException, EntityNotFoundException,
			InvalidEnvironmentRequestException,
			InvalidProductInstanceRequestException, InvalidOVFException {

		try {
			environmentInstanceDao.load(claudiaData.getService());
			throw new AlreadyExistsEntityException("The environment instance "
					+ claudiaData.getService()
					+ " already exists in the database, "
					+ "so that, it is not possible "
					+ " to create it. Change the name of the name ");
		} catch (EntityNotFoundException e) {
			log.info("The environment cannot be loaded:" + e + ". Starting"
					+ "to create it.");
		}

		EnvironmentInstance environmentInstance = infrastructureManager
				.createInfrasctuctureEnvironmentInstance(environment,
						environment.getOvf(), claudiaData);

		environmentInstance.setVdc(claudiaData.getVdc());
		environmentInstance.setName(claudiaData.getService());

		environment = environmentUtils.resolveMacros(environmentInstance);

		boolean bScalableEnvironment = installSoftwareInEnvironmentInstance(
				claudiaData, environmentInstance);

		infrastructureManager.StartStopScalability(claudiaData,
				bScalableEnvironment);

		EnvironmentInstance environmentInstanceDB = insertEnvironmentInstanceDB(environmentInstance);

		return environmentInstanceDB;
	}

	private boolean installSoftwareInEnvironmentInstance(
			ClaudiaData claudiaData, EnvironmentInstance environmentInstance)
			throws ProductInstallatorException,
			InvalidProductInstanceRequestException, NotUniqueResultException,
			InfrastructureException, InvalidEntityException {
		// TierInstance by TierInstance let's check if we have to install
		// software
		boolean bScalableEnvironment = false;

		for (TierInstance tierInstance : environmentInstance.getTierInstances()) {
			// check if the tier is scalable
			boolean state = checkScalability(tierInstance.getTier());
			if (!bScalableEnvironment) {
				bScalableEnvironment = (state) ? true : false;
			}
			String newOVF = " ";
			if ((tierInstance.getTier().getProductReleases() != null)
					&& (tierInstance.getTier().getProductReleases().size() != 0)) {

				for (ProductRelease productRelease : tierInstance.getTier()
						.getProductReleases()) {

					ProductInstance productInstance = productInstanceManager
							.install(tierInstance,
									environmentInstance.getVdc(),
									productRelease,
									productRelease.getAttributes());
					tierInstance.addProductInstance(productInstance);
				}

				if (state && tierInstance.getNumberReplica() == 1) {
					String image_Name;
					claudiaData.setFqn(tierInstance.getVM().getFqn());
					image_Name = infrastructureManager
							.ImageScalability(claudiaData);

					newOVF = infrastructureManager.updateVmOvf(
							tierInstance.getVM().getVmOVF(), image_Name);
					tierInstance.setOvf(newOVF);
				}
				
				if (state && tierInstance.getNumberReplica()>1){
					if (!newOVF.equals(" "))
						tierInstance.setOvf(newOVF);
				}
			}
		}
		return bScalableEnvironment;
	}

	private boolean checkScalability(Tier tier) {
		boolean state;
		if (tier.getMaximum_number_instances() > tier
				.getMinimum_number_instances()) {
			state = true;
		} else {
			state = false;
		}
		return state;
	}

	public EnvironmentInstance load(String vdc, String name)
			throws EntityNotFoundException {
		EnvironmentInstance instance = environmentInstanceDao.load(name);
		if (!instance.getVdc().equals(vdc)) {
			throw new EntityNotFoundException(EnvironmentInstance.class, "vdc",
					vdc);
		}
		return instance;
	}

	public EnvironmentInstance loadForDelete(String vdc, String name)
			throws EntityNotFoundException {
		EnvironmentInstance instance = null;
		try {
			instance = environmentInstanceDao.loadForDelete(name);
		} catch (EntityNotFoundException e) {
			instance = environmentInstanceDao.load(name);
			instance.setTierInstances(null);
		}
		return instance;
	}

	public EnvironmentInstance update(EnvironmentInstance envInst)
			throws InvalidEntityException {
		try {
			return environmentInstanceDao.update(envInst);
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			throw new InvalidEntityException(
					"It is not possible to update the environment "
							+ envInst.getName() + " : " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager
	 * #destroy(com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance)
	 */
	public void destroy(ClaudiaData claudiaData, EnvironmentInstance envInstance)
			throws InvalidEntityException {
		try {
			infrastructureManager.deleteEnvironment(claudiaData, envInstance);
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			throw new InvalidEntityException(
					"It is not possible to delete the environment "
							+ envInstance.getName() + " : " + e.getMessage());
		}
		List<TierInstance> tierInstances = envInstance.getTierInstances();

		if (tierInstances != null) {
			envInstance.setTierInstances(null);
			try {
				envInstance = environmentInstanceDao.update(envInstance);
			} catch (InvalidEntityException e) {
				throw new InvalidEntityException(e.getMessage());
			}
			for (TierInstance tierInstance : tierInstances) {
				tierInstanceManager.remove(tierInstance);
			}
		}
		environmentInstanceDao.remove(envInstance);
	}

	// PRVATE METHODS
	private EnvironmentInstance insertEnvironmentInstanceDB(
			EnvironmentInstance environmentInstance)
			throws InvalidEntityException, AlreadyExistsEntityException,
			EntityNotFoundException {

		Environment environment = environmentInstance.getEnvironment();
		try {
			environment = environmentManager.load(environment.getName());
		} catch (EntityNotFoundException e1) {
			try {
				environment = environmentManager.create(environment);
			} catch (InvalidEnvironmentRequestException e) {
				// TODO Auto-generated catch block
				String errorMessage = " Error to create the environment . "
						+ environment.getName() + ". " + "Desc: "
						+ e.getMessage();
				log.error(errorMessage);
				throw new InvalidEntityException(errorMessage);
			}
		}

		environmentInstance.setEnvironment(environment);

		List<TierInstance> tierInstances = insertTierInstancesBD(environmentInstance
				.getTierInstances());

		environmentInstance.setTierInstances(tierInstances);
		environmentInstance.setStatus(Status.INSTALLED);
		environmentInstance.setVdc(environmentInstance.getVdc());
		environmentInstance.setName(environmentInstance.getName());

		try {
			environmentInstance = environmentInstanceDao
					.load(environmentInstance.getName());
		} catch (EntityNotFoundException e) {
			try {
				environmentInstance = environmentInstanceDao
						.create(environmentInstance);
			} catch (InvalidEntityException e1) {
				String errorMessage = " Invalid environmentInstance objetc . Desc: "
						+ e.getMessage();
				log.error(errorMessage);
				throw new InvalidEntityException(errorMessage);
			} catch (AlreadyExistsEntityException e1) {
				String errorMessage = " Already exists environmentInstance objetc . "
						+ environmentInstance.getName()
						+ ". "
						+ "Desc: "
						+ e.getMessage();
				log.error(errorMessage);
				throw new InvalidEntityException(errorMessage);
			} catch (Exception e1) {
				String errorMessage = " Generic Exception inserting . "
						+ environmentInstance.getName() + ". " + "Desc: "
						+ e.getMessage();
				log.error(errorMessage);
				throw new InvalidEntityException(errorMessage);
			}
		}
		return environmentInstance;
	}

	private List<TierInstance> insertTierInstancesBD(
			List<TierInstance> tierInstances) throws EntityNotFoundException,
			InvalidEntityException, AlreadyExistsEntityException {

		TierInstance tierInstanceDB = null;
		List<TierInstance> tierInstancesDB = new ArrayList<TierInstance>();
		for (TierInstance tierInstance : tierInstances) {
			//Max length of an OVF. Delete the product Instances
			
			if (tierInstance.getOvf() != null && tierInstance.getOvf().length()>tam_max){
				String vmOVF = tierInstance.getOvf();
				while (vmOVF.contains("ovfenvelope:ProductSection"))
					vmOVF = infrastructureManager.deleteProductSection(vmOVF);
				tierInstance.setOvf(vmOVF);
			}
			try {
				tierInstanceDB = tierInstanceManager.load(tierInstance
						.getName());
			} catch (EntityNotFoundException e) {
				tierInstanceDB = tierInstanceManager.create(tierInstance);
				//El ovf no puede superar un máximo de caracteres

			}
			tierInstancesDB.add(tierInstanceDB);
		}

		return tierInstancesDB;
	}

	/**
	 * @param environmentInstanceDao
	 *            the environmentInstanceDao to set
	 */
	public void setEnvironmentInstanceDao(
			EnvironmentInstanceDao environmentInstanceDao) {
		this.environmentInstanceDao = environmentInstanceDao;
	}

	/**
	 * @param productInstanceManager
	 *            the productInstanceManager to set
	 */
	public void setProductInstanceManager(
			ProductInstanceManager productInstanceManager) {
		this.productInstanceManager = productInstanceManager;
	}

	/**
	 * @param tierInstanceManager
	 *            the tierInstanceManager to set
	 */
	public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
		this.tierInstanceManager = tierInstanceManager;
	}

	/**
	 * @param environmentManager
	 *            the environmentManager to set
	 */
	public void setEnvironmentManager(EnvironmentManager environmentManager) {
		this.environmentManager = environmentManager;
	}

	/**
	 * @param infrastructureManager
	 *            the infrastructureManager to set
	 */
	public void setInfrastructureManager(
			InfrastructureManager infrastructureManager) {
		this.infrastructureManager = infrastructureManager;
	}

	/**
	 * @param environmentUtils
	 *            the environmentUtils to set
	 */
	public void setEnvironmentUtils(EnvironmentUtils environmentUtils) {
		this.environmentUtils = environmentUtils;
	}
}
