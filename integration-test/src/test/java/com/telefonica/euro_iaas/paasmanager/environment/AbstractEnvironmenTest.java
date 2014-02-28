/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.resources.AbstractEnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.AbstractTierResource;


@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class AbstractEnvironmenTest {

    @Autowired
    private AbstractEnvironmentResource abstractEnvironmentResource;
    
    @Autowired
    private AbstractTierResource abstractTierResource;

    String org = "FIWARE";


   @Test
    public void testAbstractEnvironment() throws Exception {
        EnvironmentDto environmentBk = new EnvironmentDto();
        environmentBk.setName("absenvtest");
        environmentBk.setDescription("Description First environment");
        abstractEnvironmentResource.insert(org, environmentBk);
        
        EnvironmentDto result= abstractEnvironmentResource.load(org, environmentBk.getName());
        assertNotNull (result);      
    }
    

    public void testAbstractEnvironmentWithTier() throws APIException  {
        Environment environment= new Environment();
        environment.setName("name");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("tierdto2", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
       
        environment.addTier(tier);
        abstractEnvironmentResource.insert(org, environment.toDto());  
        EnvironmentDto result= abstractEnvironmentResource.load(org, environment.getName());
        assertNotNull (result);      
    }
    
    @Test(expected = APIException.class)
    public void testErrorAbstractEnvironment() throws APIException  {
        EnvironmentDto environmentBk = new EnvironmentDto();
        environmentBk.setName("");
        environmentBk.setDescription("Description First environment");
        abstractEnvironmentResource.insert(org, environmentBk);      
    }
    
    @Test(expected = APIException.class)
    public void testErrorAbstractEnvironmentWithTier() throws APIException  {
        Environment environment= new Environment();
        environment.setName("name");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
       
        environment.addTier(tier);
        abstractEnvironmentResource.insert(org, environment.toDto());  
  
    }
    
    @Test
    public void testFindAll() throws APIException  {
        int number= abstractEnvironmentResource.findAll(org, null, null, null, null).size();
        Environment environment= new Environment();
        environment.setName("nassme");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("sss", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
       
        environment.addTier(tier);
        abstractEnvironmentResource.insert(org, environment.toDto());  
        
        assertEquals (abstractEnvironmentResource.findAll(org, null, null, null, null).size(), number+1);
  
    }

    
    @Test
    public void testAddAbstractTier() throws APIException  {
        Environment environment= new Environment();
        environment.setName("ndame");
        environment.setDescription("Description First environment");
        abstractEnvironmentResource.insert(org, environment.toDto());  
        
        Tier tier = new Tier("ddd", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
        Network net = new Network("testabstrainsert", "dd");
        tier.addNetwork(net);
        
        abstractTierResource.insert(org, environment.getName(), tier.toDto());
  
    }
    
    @Test
    public void testAddAndLoadAbstractTier() throws APIException  {
        Environment environment= new Environment();
        environment.setName("ndame5");
        environment.setDescription("Description First environment");
        abstractEnvironmentResource.insert(org, environment.toDto());  
        
        Tier tier = new Tier("ddd", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
        Network net = new Network("testabsstrainsert", "dd");
        tier.addNetwork(net);
        
        abstractTierResource.insert(org, environment.getName(), tier.toDto());
        abstractTierResource.load(org, environment.getName(), tier.getName());
  
    }
   
   @Test
    public void testDeleteAbstractTier() throws APIException  {
        Environment environment= new Environment();
        environment.setName("ndameDATI");
        environment.setDescription("Description First environment");
        abstractEnvironmentResource.insert(org, environment.toDto());  
        abstractEnvironmentResource.load(org, environment.getName());  
        
        Tier tier = new Tier("henar", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
        Network net = new Network("testdeat", "dd");
        tier.addNetwork(net);
        
        abstractTierResource.insert(org, environment.getName(), tier.toDto());
        abstractTierResource.delete(org, environment.getName(), tier.getName());
  
    }
    
    @Test
    public void testDeleteAbstractTierII() throws APIException  {
        Environment environment= new Environment();
        environment.setName("ndameDATII");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("aa", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
        Network net = new Network("ss", "dd");
        tier.addNetwork(net);
        
        environment.addTier(tier);
        
        abstractEnvironmentResource.insert(org, environment.toDto());  
        abstractTierResource.delete(org, environment.getName(), tier.getName());
  
    }
    
    @Test
    public void testDeleteAbstractEnv() throws APIException  {
        Environment environment= new Environment();
        environment.setName("nameAB");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("tierdto2", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
       
        environment.addTier(tier);
        abstractEnvironmentResource.insert(org, environment.toDto());  
        abstractEnvironmentResource.delete(org, environment.getName());
  
    }
    
    @Test
    public void testDeleteAbstractEnvWithNetworks() throws APIException  {
        Environment environment= new Environment();
        environment.setName("nameABN");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("tierdto2", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
        Network net = new Network("list", "dd");
        tier.addNetwork(net);
       
        environment.addTier(tier);
        abstractEnvironmentResource.insert(org, environment.toDto());  
        abstractEnvironmentResource.delete(org, environment.getName()); 
  
    }
}
