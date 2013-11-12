/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jesus.movilla
 */
public class EnvironmentInstanceManagerImplTest {

    private EnvironmentInstanceDao environmentInstanceDao;
    private EnvironmentDao environmentDao;
    private TierDao tierDao;
    private ProductReleaseDao productReleaseDao;
    private ProductInstanceManager productInstanceManager;

    private ProductInstance productInstance;
    private TierInstance tierInstance;
    private EnvironmentInstance environmentInstance;

    private EnvironmentManager environmentManager;
    private InfrastructureManager infrastructureManager;

    private ProductRelease productRelease;
    private Tier tier;
    private Environment environment;

    private List<VM> vms;

    private PaasManagerUser user;
    private ClaudiaData claudiaData;

    Collection<? extends GrantedAuthority> authorities;

    @Before
    public void setUp() throws Exception {
        // OVF
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("4caastovfexample_attributes.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }

        claudiaData = new ClaudiaData("org", "vdc", "service");
        // Collection<GrantedAuthority> authorities = null;
        authorities = Mockito.anyCollection();

        user = new PaasManagerUser("user", "paasword", authorities);
        claudiaData.setUser(user);

        // Environment
        productRelease = new ProductRelease("product", "2.0");
        ProductType productType = new ProductType("Generic", "Generic");
        productRelease.setProductType(productType);
        OS os = new OS("94", "ip", "hostname", "domain");
        List<OS> oss = new ArrayList<OS>();
        oss.add(os);
        productRelease.setSupportedOOSS(oss);

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        tier.setProductReleases(productReleases);

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        tiers.add(tier);

        productReleaseDao = mock(ProductReleaseDao.class);
        when(productReleaseDao.load(any(String.class))).thenReturn(productRelease);

        tierDao = mock(TierDao.class);
        when(tierDao.load(any(String.class))).thenReturn(tier);

        environment = new Environment();
        environment.setName("environemntName");

        environment.setTiers(tiers);

        environmentDao = mock(EnvironmentDao.class);
        when(environmentDao.create(any(Environment.class))).thenReturn(environment);

        environmentManager = mock(EnvironmentManager.class);
        when(environmentManager.load(any(String.class), any(String.class))).thenReturn(environment);

        // Instance
        vms = new ArrayList<VM>();
        vms.add(new VM("fqn1", "ip1", "hostname1", "domain"));
        vms.add(new VM("fqn2", "ip2", "hostname2", "domain2"));

        productInstance = new ProductInstance();
        productInstance.setProductRelease(productRelease);
        // productInstance.setVm(vms.get(0));
        productInstance.setStatus(Status.INSTALLED);
        productInstance.setName("name");
        productInstance.setVdc("vdc");

        productInstanceManager = mock(ProductInstanceManager.class);
        when(
                productInstanceManager.install(any(TierInstance.class), any(ClaudiaData.class), any(String.class),
                        any(ProductRelease.class), anySet())).thenReturn(productInstance);

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(productInstance);

        tierInstance = new TierInstance();
        tierInstance.setName("nametierInstance");
        tierInstance.setTier(tier);
        tierInstance.setVdc("vdc");
        tierInstance.setStatus(Status.INSTALLED);
        tierInstance.setProductInstances(productInstances);

        List<TierInstance> tierInstances = new ArrayList<TierInstance>();
        tierInstances.add(tierInstance);

        environmentInstance = new EnvironmentInstance();
        environmentInstance.setName("name");
        environmentInstance.setTierInstances(tierInstances);
        environmentInstance.setVdc("vdc");
        environmentInstance.setStatus(Status.INSTALLED);
        environmentInstance.setEnvironment(environment);

        environmentInstanceDao = mock(EnvironmentInstanceDao.class);
        when(environmentInstanceDao.load(any(String.class))).thenReturn(environmentInstance);
    }

    /*
     * @Test public void testCreateEnvironment() throws Exception { EnvironmentInstanceManagerImpl manager = new
     * EnvironmentInstanceManagerImpl(); manager.setEnvironmentInstanceDao(environmentInstanceDao);
     * manager.setEnvironmentManager(environmentManager); manager.setInfrastructureManager(infrastructureManager);
     * manager.setProductInstanceManager(productInstanceManager); EnvironmentInstance environmentInstanceCreated =
     * manager.create( claudiaData, environmentInstance);
     * assertEquals(environmentInstanceCreated.getEnvironment().getName(), environment.getName());
     * assertEquals(environmentInstanceCreated.getTierInstances().size(), 1);
     * assertEquals(environmentInstanceCreated.getTierInstances().get(0) .getVM().getHostname(), "hostname1");
     * assertEquals(environmentInstanceCreated.getTierInstances().get(0) .getVM().getFqn(), "fqn1");
     * assertEquals(environmentInstanceCreated.getTierInstances().get(0) .getVM().getIp(), "ip1"); }
     */
}
