/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */

public class ApplicationReleaseDaoJpaImplTest extends AbstractJpaDaoTest {

    /*
     * private ApplicationReleaseDao applicationReleaseDao; private ApplicationTypeDao applicationTypeDao; private
     * EnvironmentTypeDao environmentTypeDao; public final static String APPR_NAME = "APPReleaseName"; public final
     * static String APPR_DESCRIPTION = "APPReleaseDescription"; public final static String APPR_VERSION =
     * "APPReleaseVersion"; public final static String APPR2_NAME = "APPReleaseNameNewVersion"; public final static
     * String APPR2_DESCRIPTION = "APPReleaseDescription"; public final static String APPR2_VERSION =
     * "APPReleaseVersionNewVersion"; public final static String ARTIFACT_NAME = "ArtifactName"; public final static
     * String ARTIFACT_PATH = "ArtifactPath";
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate() throws Exception { ApplicationRelease applicationRelease = new
     * ApplicationRelease(APPR_NAME, APPR_VERSION); EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest = new
     * EnvironmentTypeDaoJpaImplTest (); environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
     * EnvironmentType environmentType = environmentTypeDaoJpaImplTest.create(new EnvironmentType("EnvName",
     * "EnvDesc")); List<EnvironmentType> environmentTypes = new ArrayList<EnvironmentType>();
     * environmentTypes.add(environmentType); AppicationTypeDaoJpaImplTest appicationTypeDaoJpaImplTest = new
     * AppicationTypeDaoJpaImplTest (); appicationTypeDaoJpaImplTest.setApplicationTypeDao(applicationTypeDao);
     * ApplicationType applicationType = appicationTypeDaoJpaImplTest.create(new ApplicationType( "ApplicationTypeName",
     * "ApplicationTypeDesc", environmentTypes)); List<ApplicationRelease> transitableReleases = new
     * ArrayList<ApplicationRelease>(); transitableReleases.add(new ApplicationRelease(APPR2_NAME,APPR2_VERSION));
     * applicationRelease.setApplicationType(applicationType); applicationRelease.setDescription(APPR_DESCRIPTION);
     * assertNotNull(applicationRelease.getId()); ApplicationRelease createdAppRelease =
     * applicationReleaseDao.create(applicationRelease); assertNotNull(createdAppRelease.getId());
     * assertEquals(createdAppRelease.getId(), applicationRelease.getId()); ApplicationRelease findAppRelease =
     * applicationReleaseDao.load(createdAppRelease.getId()); assertEquals(createdAppRelease, findAppRelease); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate2() throws Exception { ApplicationRelease applicationRelease = new
     * ApplicationRelease(APPR2_NAME, APPR2_VERSION); EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest = new
     * EnvironmentTypeDaoJpaImplTest (); environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
     * EnvironmentType environmentType = environmentTypeDaoJpaImplTest.create(new EnvironmentType("Env2Name",
     * "Env2Desc")); List<EnvironmentType> environmentTypes = new ArrayList<EnvironmentType>();
     * environmentTypes.add(environmentType); AppicationTypeDaoJpaImplTest appicationTypeDaoJpaImplTest = new
     * AppicationTypeDaoJpaImplTest (); appicationTypeDaoJpaImplTest.setApplicationTypeDao(applicationTypeDao);
     * ApplicationType applicationType = appicationTypeDaoJpaImplTest.create(new ApplicationType(
     * "Application2TypeName", "Application2TypeDesc", environmentTypes)); List<ApplicationRelease> transitableReleases
     * = new ArrayList<ApplicationRelease>(); transitableReleases.add(new ApplicationRelease(APPR_NAME,APPR_VERSION));
     * applicationRelease.setApplicationType(applicationType); applicationRelease.setDescription(APPR2_DESCRIPTION);
     * assertNotNull(applicationRelease.getId()); ApplicationRelease createdAppRelease =
     * applicationReleaseDao.create(applicationRelease); assertNotNull(createdAppRelease.getId());
     * assertEquals(createdAppRelease.getId(), applicationRelease.getId()); ApplicationRelease findAppRelease =
     * applicationReleaseDao.load(createdAppRelease.getId()); assertEquals(createdAppRelease, findAppRelease); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0, applicationReleaseDao.findAll().size());
     * testCreate(); List<ApplicationRelease> applicationReleases = applicationReleaseDao.findAll(); assertEquals(1,
     * applicationReleases.size()); ApplicationRelease applicationRelease = applicationReleases.get(0);
     * applicationRelease.setDescription("newDescription"); applicationReleaseDao.update(applicationRelease);
     * assertEquals("newDescription", applicationReleaseDao.load(applicationRelease.getId()).getDescription());
     * applicationReleaseDao.remove(applicationRelease); assertEquals(0, applicationReleaseDao.findAll().size()); }
     */

    /**
     * @param applicationTypeDao
     *            the applicationTypeDao to set
     */
    /*
     * public void setApplicationTypeDao(ApplicationTypeDao applicationTypeDao) { this.applicationTypeDao =
     * applicationTypeDao; }
     */

    /**
     * @param environmentTypeDao
     *            the environmentTypeDao to set
     */
    /*
     * public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) { this.environmentTypeDao =
     * environmentTypeDao; }
     */

    /**
     * @param applicationReleaseDao
     *            the applicationReleaseDao to set
     */
    /*
     * public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) { this.applicationReleaseDao =
     * applicationReleaseDao; }
     */
}
