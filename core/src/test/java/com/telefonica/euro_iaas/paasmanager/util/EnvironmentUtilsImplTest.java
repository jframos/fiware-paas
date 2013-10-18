/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * @author jesus.movilla
 * 
 */
public class EnvironmentUtilsImplTest {

    private Environment environment;
    private List<VM> vms;

    private Tier tierMysql, tierTomcat;
    private ProductRelease productReleaseMysql, productReleaseTomcat,
    productReleaseWar;

    @Before
    public void setUp() throws Exception {

        environment = new Environment();

        // *******************************
        // Setting an Environment
        // ProductReleaseMysql
        productReleaseMysql = new ProductRelease("mysql", "2.0");
        ProductType productType = new ProductType("Generic", "Generic");
        productReleaseMysql.setProductType(productType);
        OS os = new OS("94", "ip", "hostname", "domain");
        List<OS> oss = new ArrayList<OS>();
        oss.add(os);
        productReleaseMysql.setSupportedOOSS(oss);

        // ProductRelease
        productReleaseTomcat = new ProductRelease("tomcat", "7.0");
        productReleaseMysql.setProductType(productType);
        productReleaseMysql.setSupportedOOSS(oss);

        // ProductRelease
        productReleaseWar = new ProductRelease("war", "1.0");
        productReleaseMysql.setProductType(productType);
        productReleaseMysql.setSupportedOOSS(oss);

        // Attributes;
        List<Attribute> attributesMysql = new ArrayList<Attribute>();
        List<Attribute> attributesWar = new ArrayList<Attribute>();

        Attribute attr1 = new Attribute("login", "login1");
        Attribute attr2 = new Attribute("password", "password1");

        Attribute attr3 = new Attribute("IP", "@ip(mysql,gestion)");
        Attribute attr4 = new Attribute("login", "@login(myql)");
        Attribute attr5 = new Attribute("password", "@paasword(myql)");
        Attribute attr6 = new Attribute("xxxxxxx", "@xxxx(mysql)");

        attributesMysql.add(attr1);
        attributesMysql.add(attr2);

        attributesWar.add(attr3);
        attributesWar.add(attr4);
        attributesWar.add(attr5);
        attributesWar.add(attr6);

        productReleaseMysql.setAttributes(attributesMysql);
        productReleaseWar.setAttributes(attributesWar);

        List<ProductRelease> productReleasesMysql = new ArrayList<ProductRelease>();
        productReleasesMysql.add(productReleaseMysql);

        List<ProductRelease> productReleasesTomcat = new ArrayList<ProductRelease>();
        productReleasesTomcat.add(productReleaseTomcat);
        productReleasesTomcat.add(productReleaseWar);
        // Tier
        tierMysql = new Tier();
        tierMysql.setInitialNumberInstances(new Integer(1));
        tierMysql.setMaximumNumberInstances(new Integer(5));
        tierMysql.setMinimumNumberInstances(new Integer(1));
        tierMysql.setName("mysql");
        tierMysql.setProductReleases(productReleasesMysql);

        tierTomcat = new Tier();
        tierMysql.setInitialNumberInstances(new Integer(1));
        tierMysql.setMaximumNumberInstances(new Integer(5));
        tierMysql.setMinimumNumberInstances(new Integer(1));
        tierTomcat.setName("tomcat");
        tierTomcat.setProductReleases(productReleasesTomcat);

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tierMysql);
        tiers.add(tierTomcat);

        // Environment
        environment = new Environment();
        environment.setName("environemntName");

        environment.setTiers(tiers);
        environment.setOvf("ovf");

        vms = new ArrayList<VM>();

        VM vmMysql = new VM("ipMysql", "hostnameMysql", "domainMysql", "mysql",
        "vdcTest");
        VM vmTomcat = new VM("ipTomcat", "hostnameTomcat", "domainTomcat",
                "tomcat", "vdcTest");
        vms.add(vmMysql);
        vms.add(vmTomcat);
    }

    @Test
    public void testcresolveMacros() throws Exception {
        EnvironmentUtilsImpl environmentUtils = new EnvironmentUtilsImpl();
        environment = environmentUtils.resolveMacros(environment, vms);
    }
}
