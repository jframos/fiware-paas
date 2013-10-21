/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

/*
 import static org.junit.Assert.assertEquals;
 import static org.mockito.Matchers.any;
 import static org.mockito.Matchers.anyList;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.when;
 import static org.junit.Assert;

 import java.util.ArrayList;

 import org.junit.Before;
 import org.junit.Test;*/

import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallatorRECManagerImpl;

/**
 * @author jesus.movilla
 * 
 */
public class ApplicationInstanceManagerImplTest {

    /*
     * private EnvironmentTypeDao environmentTypeDao; private
     * EnvironmentInstanceDao environmentInstanceDao; private EnvironmentDao
     * environmentDao; private TierInstanceDao tierInstanceDao; private TierDao
     * tierDao; private ProductReleaseDao productReleaseDao; private
     * ProductInstanceDao productInstanceDao; private ProductInstanceManager
     * productInstanceManager;
     * 
     * private ProductInstance productInstance; private TierInstance
     * tierInstance; private EnvironmentInstance environmentInstance;
     * 
     * private EnvironmentManager environmentManager; private
     * InfrastructureManager infrastructureManager;
     * 
     * private ProductRelease productRelease; private Tier tier; private
     * Environment environment;
     * 
     * private String extendedOVF; private List<VM> vms;
     */
    private ProductInstallatorRECManagerImpl productInstallator;

    @Test
    public void installApplicationReleaseIntestbed() throws Exception {
        // preparation
        /*
         * ApplicationInstanceManagerImpl manager = new
         * ApplicationInstanceManagerImpl();
         * manager.setProductInstallator(productInstallator);
         * 
         * 
         * String vdc = null; String org = null; EnvironmentInstance
         * environmentInstance = null; ApplicationReleaseDto applicationRelease
         * = null; // execution ApplicationInstance installedApp =
         * manager.install("dd","ss", environmentInstance, applicationRelease);
         * // make assertions ApplicationRelease applicationReleaseAfter = null;
         */
        // Assert.assertEquals(installedApp.getApplicationRelease(),
        // applicationReleaseAfter);

    }

    // @Before
    public void setUp() throws Exception {

        productInstallator = mock(ProductInstallatorRECManagerImpl.class);
        // when(productInstallator.installArtifact(
        // any(ProductInstance.class), any(Artifact.class)));

    }

}
