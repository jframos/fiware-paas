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

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.URLNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class ClaudiaClientImplTest {

    private ClaudiaClientImpl claudiaClient;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;
    private ClaudiaUtil claudiaUtil;
    private SystemPropertiesProvider systemPropertiesProvider;
    private VappUtils vappUtils;

    private Environment envResult;

    @Before
    public void setUp() throws Exception {
        claudiaClient = new ClaudiaClientImpl();
        claudiaResponseAnalyser = mock(ClaudiaResponseAnalyser.class);
        claudiaClient.setClaudiaResponseAnalyser(claudiaResponseAnalyser);
        claudiaUtil = mock(ClaudiaUtil.class);
        when(claudiaUtil.postClaudiaResource(any(PaasManagerUser.class), any(String.class))).thenReturn(
                new ClientResponse(0, null, null, null));
        claudiaClient.setClaudiaUtil(claudiaUtil);

        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("xxxxx");

        claudiaClient.setSystemPropertiesProvider(systemPropertiesProvider);

        vappUtils = mock(VappUtils.class);
        claudiaClient.setVappUtils(vappUtils);

        envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");

        Tier tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease = new ProductRelease("product", "2.0");
        productReleases.add(productRelease);
        tier.setProductReleases(productReleases);

        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tier);
        envResult.setTiers(tiers);

    }

    /*
     * @Test public void testDeployVDC() { ClaudiaData claudiaData = new ClaudiaData("dd", "2", "service"); try {
     * claudiaClient.deployVDC(claudiaData, "1", "1", "1"); } catch (InfrastructureException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); } }
     */

    @Test
    public void testBrowseService() {
        try {
            ClaudiaData claudiaData = new ClaudiaData("dd", "2", "dd");
            try {
                when(claudiaUtil.getUrl(any(List.class))).thenReturn("url");
                when(claudiaUtil.getClaudiaResource(any(PaasManagerUser.class), any(String.class), any(String.class)))
                        .thenReturn("url");
            } catch (URLNotRetrievedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClaudiaRetrieveInfoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            claudiaClient.browseService(claudiaData);
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * @Test public void testBrowseVM() { fail("Not yet implemented"); }
     * @Test public void testBrowseVMReplica() { fail("Not yet implemented"); }
     * @Test public void testGetIP() { fail("Not yet implemented"); }
     * @Test public void testUndeployVMReplica() { fail("Not yet implemented"); }
     * @Test public void testDeployService() { fail("Not yet implemented"); }
     * @Test public void testDeployVMStringStringStringStringPaasManagerUserString() { fail("Not yet implemented"); }
     * @Test public void testDeployVMClaudiaDataTier() { fail("Not yet implemented"); }
     * @Test public void testUndeployVM() { fail("Not yet implemented"); }
     * @Test public void testObtainIPFromFqn() { fail("Not yet implemented"); }
     * @Test public void testObtainOS() { fail("Not yet implemented"); }
     * @Test public void testGetVApp() { fail("Not yet implemented"); }
     * @Test public void testObtainVMStatus() { fail("Not yet implemented"); }
     * @Test public void testSwitchVMOn() { fail("Not yet implemented"); }
     * @Test public void testOnOffScalability() { fail("Not yet implemented"); }
     * @Test public void testCreateImage() { fail("Not yet implemented"); }
     */

}
