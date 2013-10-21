/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidVappException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.InfrastructureManagerServiceClaudiaImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtilsDomImpl;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.VappUtilsImpl;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductInstallatorRECManagerImplTest extends TestCase {

    private String getFile(String file) throws IOException {
        File f = new File(file);
        System.out.println(f.isFile() + " " + f.getAbsolutePath());
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
    }

    @Override
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInstall() {

        Environment envResult = new Environment();
        envResult.setName("environemntName");

        String ovfname = "src/test/resources/SAP83scal.xml";
        String ovfService = null;
        try {
            ovfService = getFile(ovfname);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        envResult.setOvf(ovfService);
        Tier tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("FlexVM1");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease = new ProductRelease("product", "2.0");
        productReleases.add(productRelease);

        ProductInstance productInstance = new ProductInstance();
        productInstance.setName("productInstance");
        productInstance.setProductRelease(productRelease);
        productReleases.add(productRelease);
        tier.setProductReleases(productReleases);

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        envResult.setTiers(tiers);

        EnvironmentInstance environmentInstance = new EnvironmentInstance();
        environmentInstance.setEnvironment(envResult);
        ClaudiaData claudiaData = new ClaudiaData("org", "vdc", envResult.getName());
        environmentInstance.setName(claudiaData.getVdc() + "-" + envResult.getName());

        String vappname = "src/test/resources/vappsap83.xml";
        String vappService = null;
        try {
            vappService = getFile(vappname);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        InfrastructureManagerServiceClaudiaImpl manager2 = new InfrastructureManagerServiceClaudiaImpl();
        VappUtilsImpl vappUtils = new VappUtilsImpl();
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("ddFIWARE");

        vappUtils.setSystemPropertiesProvider(systemPropertiesProvider);
        manager2.setVappUtils(vappUtils);
        OVFUtilsDomImpl ovfUtils = new OVFUtilsDomImpl();
        manager2.setOvfUtils(ovfUtils);

        List<TierInstance> tierInstances = null;

        try {
            tierInstances = manager2.fromVappToListTierInstance(vappService, envResult, claudiaData);
        } catch (InvalidVappException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidOVFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (TierInstance tierInstance : tierInstances) {
            environmentInstance.addTierInstance(tierInstance);
        }
        environmentInstance.setVapp(vappService);

        ProductInstallatorRECManagerImpl manager = new ProductInstallatorRECManagerImpl();
        manager.setOvfUtils(ovfUtils);
        manager.setSystemPropertiesProvider(systemPropertiesProvider);
        com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils vappUtilRecs = new com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtilsImpl();
        manager.setVappUtils(vappUtilRecs);
        ClaudiaData data = new ClaudiaData("org", "vdc", "");

        try {
            ProductInstance productInstance2 = manager.install(data, "evn",
                    environmentInstance.getTierInstances().get(0), environmentInstance.getTierInstances().get(0)
                            .getTier().getProductReleases().get(0), null);
        } catch (ProductInstallatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
