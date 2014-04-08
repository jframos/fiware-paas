/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.manager;

/*
 * import static org.junit.Assert.assertEquals; import static org.mockito.Matchers.any; import static
 * org.mockito.Matchers.anyList; import static org.mockito.Mockito.mock; import static org.mockito.Mockito.when; import
 * static org.junit.Assert; import java.util.ArrayList; import org.junit.Before; import org.junit.Test;
 */

import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallatorRECManagerImpl;
import org.junit.Test;


import static org.mockito.Mockito.mock;

/**
 * @author jesus.movilla
 */
public class ApplicationInstanceManagerImplTest {

    /*
     * private EnvironmentTypeDao environmentTypeDao; private EnvironmentInstanceDao environmentInstanceDao; private
     * EnvironmentDao environmentDao; private TierInstanceDao tierInstanceDao; private TierDao tierDao; private
     * ProductReleaseDao productReleaseDao; private ProductInstanceDao productInstanceDao; private
     * ProductInstanceManager productInstanceManager; private ProductInstance productInstance; private TierInstance
     * tierInstance; private EnvironmentInstance environmentInstance; private EnvironmentManager environmentManager;
     * private InfrastructureManager infrastructureManager; private ProductRelease productRelease; private Tier tier;
     * private Environment environment; private String extendedOVF; private List<VM> vms;
     */
    private ProductInstallatorRECManagerImpl productInstallator;

    @Test
    public void installApplicationReleaseIntestbed() throws Exception {
        // preparation
        /*
         * ApplicationInstanceManagerImpl manager = new ApplicationInstanceManagerImpl();
         * manager.setProductInstallator(productInstallator); String vdc = null; String org = null; EnvironmentInstance
         * environmentInstance = null; ApplicationReleaseDto applicationRelease = null; // execution ApplicationInstance
         * installedApp = manager.install("dd","ss", environmentInstance, applicationRelease); // make assertions
         * ApplicationRelease applicationReleaseAfter = null;
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
