/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

/**
 * Unit test for ArtifactDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */
public class ArtifactDaoJpaImplTest extends AbstractJpaDaoTest {

    /*
     * private ProductReleaseDao productReleaseDao; private ArtifactDao artifactDao; private ArtifactTypeDao
     * artifactTypeDao; private OSDao osDao; private ProductTypeDao productTypeDao; public final static String
     * ARTIFACT_NAME = "ArtfactName"; public final static String ARTIFACT_PATH = "/Artifact/path"; public final static
     * String ARTIFACT_TYPE = "ArtfactType"; public final static String ARTIFACT_TYPE_NAME = "ArtfactTypeName"; /** Test
     * the create and load method
     */
    /*
     * public void testCreate() throws Exception { ProductTypeDaoJpaImplTest productTypeDaoJpaImplTest = new
     * ProductTypeDaoJpaImplTest(); productTypeDaoJpaImplTest.setProductTypeDao(productTypeDao); ProductType productType
     * = productTypeDaoJpaImplTest.create(new ProductType("ProductType", "ProductTypeDesc")); ArtifactTypeDaoJpaImplTest
     * artifactTypeDaoJpaImplTest = new ArtifactTypeDaoJpaImplTest();
     * artifactTypeDaoJpaImplTest.setArtifactTypeDao(artifactTypeDao); ArtifactType artifactType =
     * artifactTypeDao.create(new ArtifactType(ARTIFACT_NAME, ARTIFACT_PATH , productType));
     * ProductReleaseDaoJpaImplTest productReleaseDaoJpaImplTest = new ProductReleaseDaoJpaImplTest();
     * productReleaseDaoJpaImplTest.setOsDao(osDao);
     * productReleaseDaoJpaImplTest.setProductReleaseDao(productReleaseDao);
     * productReleaseDaoJpaImplTest.setProductTypeDao(productTypeDao); productReleaseDaoJpaImplTest.testCreate1();
     * ProductRelease productRelease = productReleaseDao.findAll().get(0); Artifact artifact = new
     * Artifact(ARTIFACT_NAME, ARTIFACT_PATH , artifactType, productRelease); Artifact createdArtifact =
     * artifactDao.create(artifact); assertNotNull(createdArtifact.getId()); assertEquals(artifact.getId(),
     * createdArtifact.getId()); Artifact findArtifact = artifactDao.load(createdArtifact.getName());
     * assertEquals(createdArtifact, findArtifact); }
     */

    /**
     * Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0, artifactDao.findAll().size());
     * testCreate(); List<Artifact> artifacts = artifactDao.findAll(); assertEquals(1, artifacts.size()); Artifact
     * artifact = artifacts.get(0); artifact.setPath("/Artifact2/path2"); artifactDao.update(artifact);
     * assertEquals("/Artifact2/path2", artifactDao.load(artifact.getName()).getPath()); artifactDao.remove(artifact);
     * assertEquals(0, artifactDao.findAll().size()); }
     */

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    /*
     * public void setProductReleaseDao(ProductReleaseDao productReleaseDao) { this.productReleaseDao =
     * productReleaseDao; }
     */

    /**
     * @param osDao
     *            the osDao to set
     */
    /*
     * public void setOSDao(OSDao osDao) { this.osDao = osDao; }
     */

    /**
     * @param artifactDao
     *            the artifactDao to set
     */
    /*
     * public void setArtifactDao(ArtifactDao artifactDao) { this.artifactDao = artifactDao; }
     */

    /**
     * @param productTypeDao
     *            the productTypeDao to set
     */
    /*
     * public void setProductTypeDao(ProductTypeDao productTypeDao) { this.productTypeDao = productTypeDao; }
     */

    /**
     * @param artifactTypeDao
     *            the artifactTypeDao to set
     */
    /*
     * public void setArtifactTypeDao(ArtifactTypeDao artifactTypeDao) { this.artifactTypeDao = artifactTypeDao; }
     */
}
