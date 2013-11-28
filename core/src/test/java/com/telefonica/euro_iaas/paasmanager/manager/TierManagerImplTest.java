/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.TierManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class TierManagerImplTest extends TestCase {

    private TierManagerImpl tierManager;
    private TierDao tierDao;
    private ProductReleaseManager productReleaseManager;
    private SecurityGroupManager securityGroupManager;
    private ProductRelease productRelease;
    private SystemPropertiesProvider systemPropertiesProvider;
    private Tier tier;

    private List<ProductRelease> productReleases;

    private ClaudiaData data;

    @Override
    @Before
    public void setUp() throws Exception {

        tierManager = new TierManagerImpl();
        tierDao = mock(TierDao.class);
        productReleaseManager = mock(ProductReleaseManager.class);
        securityGroupManager = mock(SecurityGroupManager.class);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        tierManager.setProductReleaseManager(productReleaseManager);
        tierManager.setSecurityGroupManager(securityGroupManager);
        tierManager.setSystemPropertiesProvider(systemPropertiesProvider);
        tierManager.setTierDao(tierDao);

        productRelease = new ProductRelease("product", "2.0");

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        tier.setProductReleases(productReleases);

        when(productReleaseManager.load(any(String.class))).thenReturn(productRelease);

        data = new ClaudiaData("dd", "dd", "dd");
        List<? extends GrantedAuthority> authorities = new ArrayList();
        PaasManagerUser user = new PaasManagerUser("user", "pass", authorities);
        data.setUser(user);
        user.setToken("token");

    }

    @Test
    public void testcreateSecurityGroup() {
        productRelease = new ProductRelease("product", "2.0");
        productRelease.addAttribute(new Attribute("openports", "8080"));

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = tierManager.generateSecurityGroup(data, tier);
        assertEquals(securityGroup.getName(), "sg_dd_dd_" + tier.getName());
        assertEquals(securityGroup.getRules().size(), 3);
    }

    @Test
    public void testcreateSecurityGroupNoAttributes() {
        productRelease = new ProductRelease("product", "2.0");
        // productRelease.addAttributeport(new Attribute("puerto", "8080"));

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = tierManager.generateSecurityGroup(data, tier);
        assertEquals(securityGroup.getName(), "sg_dd_dd_" + tier.getName());
        assertEquals(securityGroup.getRules().size(), 2);
    }

    @Test
    public void testcreateSecurityGroupNoAttributes2ProductReleases() {
        productRelease = new ProductRelease("product", "2.0");
        productRelease.addAttribute(new Attribute("openports", "8080"));

        ProductRelease productRelease2 = new ProductRelease("product2", "2.0");
        productRelease2.addAttribute(new Attribute("openports", "8083"));

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);
        productReleases.add(productRelease2);
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = tierManager.generateSecurityGroup(data, tier);
        assertEquals(securityGroup.getName(), "sg_dd_dd_" + tier.getName());
        assertEquals(securityGroup.getRules().size(), 4);
    }

    @Test
    public void testTierAddProduct() throws Exception {

        productRelease = new ProductRelease("product", "2.0");
        productRelease.addAttribute(new Attribute("openports", "8080 2323"));

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(securityGroupManager.create(anyString(), anyString(), anyString(), any(SecurityGroup.class))).thenReturn(
                securityGroup);
        when(tierDao.create(any(Tier.class))).thenReturn(tier);
        Mockito.doThrow(new EntityNotFoundException(Tier.class, "test", tier)).when(tierDao)
                .load(any(String.class), any(String.class), any(String.class));

        tier.addProductRelease(productRelease);
        tierManager.addSecurityGroupToProductRelease(data, tier, productRelease);
        tier.addProductRelease(productRelease);
        tierManager.addSecurityGroupToProductRelease(data, tier, productRelease);
        assertEquals(tier.getSecurityGroup().getRules().size(), 1);
        assertEquals(tier.getProductReleases().size(), 2);

    }

    @Test
    public void testTierAddSecurityGroupToProductRelease() throws Exception {

        productRelease = new ProductRelease("product", "2.0");
        productRelease.addAttribute(new Attribute("openports", "8080 2323"));

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(securityGroupManager.create(anyString(), anyString(), anyString(), any(SecurityGroup.class))).thenReturn(
                securityGroup);
        when(tierDao.create(any(Tier.class))).thenReturn(tier);
        Mockito.doThrow(new EntityNotFoundException(Tier.class, "test", tier)).when(tierDao)
                .load(any(String.class), any(String.class), any(String.class));

        tierManager.addSecurityGroupToProductRelease(data, tier, productRelease);
        assertEquals(tier.getSecurityGroup().getRules().size(), 1);

    }

    @Test
    public void testTierAllData() throws Exception {

        productRelease = new ProductRelease("product", "2.0");
        productRelease.addAttribute(new Attribute("openports", "8080"));

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(securityGroupManager.create(anyString(), anyString(), anyString(), any(SecurityGroup.class))).thenReturn(
                securityGroup);
        when(tierDao.create(any(Tier.class))).thenReturn(tier);
        Mockito.doThrow(new EntityNotFoundException(Tier.class, "test", tier)).when(tierDao)
                .load(any(String.class), any(String.class), any(String.class));
        when(productReleaseManager.load(any(String.class), any(String.class))).thenReturn(productRelease);

        Tier tier3 = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");
        Tier tier2 = tierManager.create(data, "env", tier3);
        assertEquals(tier2.getName(), tier.getName());
        assertEquals(tier2.getKeypair(), tier.getKeypair());

    }

    @Test
    public void testTierAllDataSecurityPortNoAttributesInProductRelease() throws Exception {

        productRelease = new ProductRelease("product", "2.0");
        // productRelease.addAttributeport(new Attribute("puerto", "8080"));

        productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);
        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases);

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(securityGroupManager.create(anyString(), anyString(), anyString(), any(SecurityGroup.class))).thenReturn(
                securityGroup);
        when(tierDao.create(any(Tier.class))).thenReturn(tier);
        Mockito.doThrow(new EntityNotFoundException(Tier.class, "test", tier)).when(tierDao)
                .load(any(String.class), any(String.class), any(String.class));
        when(productReleaseManager.load(any(String.class))).thenReturn(productRelease);
        Tier tier3 = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");
        Tier tier2 = tierManager.create(data, "env", tier3);
        assertEquals(tier2.getName(), tier.getName());
        assertEquals(tier2.getKeypair(), tier.getKeypair());

    }

    @Test
    public void testTierNoProductRelease() throws Exception {

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image", "icono",
                "keypair", "floatingip", "payload");

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");
        when(securityGroupManager.create(anyString(), anyString(), anyString(), any(SecurityGroup.class))).thenReturn(
                securityGroup);
        when(tierDao.create(any(Tier.class))).thenReturn(tier);
        Mockito.doThrow(new EntityNotFoundException(Tier.class, "test", tier)).when(tierDao)
                .load(any(String.class), any(String.class), any(String.class));
        Tier tier3 = new Tier("name", new Integer(1), new Integer(1), new Integer(1), null, "flavour", "image",
                "icono", "keypair", "floatingip", "payload");

        Tier tier2 = tierManager.create(data, "env", tier3);
        assertEquals(tier2.getName(), tier.getName());
        assertEquals(tier2.getKeypair(), tier.getKeypair());

    }

}
