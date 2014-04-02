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

package com.telefonica.euro_iaas.paasmanager.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 */
public class EnvironmentUtilsImplTest {

    private Environment environment;
    private List<VM> vms;

    private Tier tierMysql, tierTomcat;
    private ProductRelease productReleaseMysql, productReleaseTomcat, productReleaseWar;

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
        Set<Attribute> attributesMysql = new HashSet<Attribute>();
        Set<Attribute> attributesWar = new HashSet<Attribute>();

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

        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tierMysql);
        tiers.add(tierTomcat);

        // Environment
        environment = new Environment();
        environment.setName("environemntName");

        environment.setTiers(tiers);
        environment.setOvf("ovf");

        vms = new ArrayList<VM>();

        VM vmMysql = new VM("ipMysql", "hostnameMysql", "domainMysql", "mysql", "vdcTest");
        VM vmTomcat = new VM("ipTomcat", "hostnameTomcat", "domainTomcat", "tomcat", "vdcTest");
        vms.add(vmMysql);
        vms.add(vmTomcat);
    }

    @Test
    public void testcresolveMacros() throws Exception {
        EnvironmentUtilsImpl environmentUtils = new EnvironmentUtilsImpl();
        environment = environmentUtils.resolveMacros(environment, vms);
    }
}
