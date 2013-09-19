/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.it;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.paasmanager.it.util.ApplicationInstanceUtils;
import com.telefonica.euro_iaas.paasmanager.it.util.EnvironmentInstanceUtils;
import com.telefonica.euro_iaas.paasmanager.it.util.TaskUtils;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;


import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;


/**
 * Contains the necessary steps to provision an applicationInstance being OK.
 * 
 * @author jesus.movilla
 *
 */
public class ApplicationInstanceStepsOK {

	//ENVIRONMENTINSTANCE
	private List<OS> supportedOOSS;
    private List<Attribute> attributes;
    private ProductType productType;
    private EnvironmentType environmentType;
    private List<ProductRelease> productReleases;
    private List<Tier> tiers;
    private EnvironmentDto environmentDto;
    
    //APPLICATIONINSTANCEINSTANCE
    private List<Attribute> appAttributes;
    private List<Artifact> artifacts;
    private List<ArtifactType> artifactTypes;
    private List<EnvironmentType> environmentTypes;
    private ApplicationType applicationType;
    private EnvironmentType appEnvironmentType;
    private ProductType appProductType;
    private ApplicationInstanceDto applicationInstanceDto;
    private String applicationName;
    private String applicationVersion;
    private String environmentInstanceName;
    
 
    //CREATE ENVIRONMENTINSTANCE
    /*@Given("^Environment OS:$")
	public void getOOSS(Table table) {
   	supportedOOSS = new ArrayList<OS>();
	    for (List<String> row: table.rows()) {
	    	supportedOOSS.add(new OS(row.get(0), row.get(1), row.get(2), "description"));
	    	//System.out.println(row.get(0) + " - " + row.get(1));
	    }
    }  
	@Given("^Environment default attributes:$")
	public void getAttributes(Table table) {
		attributes = new ArrayList<Attribute>();
	    for (List<String> row: table.rows()) {
	    	attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
	    }
	}
	@Given("^Environment Product Type:$")
	public void getProductTypes(Table table) {
		productType = new ProductType();
	    for (List<String> row: table.rows()) {
	    	productType.setDescription(row.get(1));
	    	productType.setName(row.get(0));
	   }
	}	
	@Given("^Environment the product releases:$")
	public void getProductReleases(Table table) {
		productReleases = new ArrayList<ProductRelease>();
	    
		for (List<String> row: table.rows()) {
	    	ProductRelease productRelease = new ProductRelease(row.get(0), row.get(1));
	    	productRelease.setAttributes(attributes);
	    	productRelease.setProductType(productType);
	    	productRelease.setSupportedOOSS(supportedOOSS);
	    	
	    	productReleases.add(productRelease);
	   }
	}	
	@Given("^Environment Tiers:$")
	public void getTiers(Table table) {
		tiers = new ArrayList<Tier>();
	    
		for (List<String> row: table.rows()) {
	    	Tier tier = new Tier();
	    	tier.setInitial_number_instances(Integer.parseInt(row.get(1)));
	    	tier.setMaximum_number_instances(Integer.parseInt(row.get(2)));
	    	tier.setMinimum_number_instances(Integer.parseInt(row.get(3)));
	    	tier.setName(row.get(0));
	    	tier.setProductReleases(productReleases);
	    	
	    	tiers.add(tier);
	   }
	}	
	@Given("^EnvironmentType:$")
	public void getEnvironmentType(Table table) {
		environmentType = new EnvironmentType();
	    for (List<String> row: table.rows()) {
	    	environmentType.setDescription(row.get(1));
	    	environmentType.setName(row.get(0));
	   }
	
	}
	

   //APPLICATION INSTANCE
    @Given("^Environment Type:$")
	public void getAppEnvironmentType(Table table) {
    	environmentTypes = new ArrayList<EnvironmentType>();
    	for (List<String> row: table.rows()) {
    		environmentType = new EnvironmentType(row.get(0), row.get(1));
	    }
    	environmentTypes.add(environmentType);
	}
	
    @Given("^an Application Release \"([^\"]*)\" and \"([^\"]*)\"$")
    public void getApplicationrelease(String name, String version)  
    	throws Exception {
    	System.out.println ("ApplicationReleaseName: " + name);
    	System.out.println ("ApplicationReleaseVersion: " + version);
    	applicationName = name;
    	applicationVersion = version;
    }   
    @Given("^Application Type:$")
	public void getApplicationType(Table table) {
	    for (List<String> row: table.rows()) {
	    	applicationType = new ApplicationType(row.get(0), row.get(1),
	    			environmentTypes);
	    }
	}
    
    @Given("^Application Release attributes:$")
	public void getAppAttributes(Table table) {
		attributes = new ArrayList<Attribute>();
	    for (List<String> row: table.rows()) {
	    	attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
	    }
	}	
    @Given("^Product Type:$")
	public void getProductType(Table table) {
		for (List<String> row: table.rows()) {
			productType = new ProductType(row.get(0), row.get(1));
	    }	    
	}	
    @Given("^Artifact Types:$")
	public void getArtifactTypes(Table table) {
		artifactTypes = new ArrayList<ArtifactType>();
	    for (List<String> row: table.rows()) {
	    	artifactTypes.add(new ArtifactType(row.get(0), row.get(1), productType));
	    }
	}   	
    @Given("^Application Release artifacts:$")
	public void getArtifacts(Table table) {
		artifacts = new ArrayList<Artifact>();		
	    for (List<String> row: table.rows()) {
	    	String productName = row.get(3);
	    	for (int i=0; i < artifactTypes.size(); i++){
	    		artifacts.add(new Artifact(row.get(0),row.get(1), artifactTypes.get(i),new ProductRelease (row.get(3), row.get(4))));
	    	}
	    }
	}
	
    @When("^I install an applicationInstance$")
    public void iInstallApplicationInstance() throws Exception {        
    	String vdc = getProperty(VDC);
    	System.out.println ("vdc=" + vdc);
    	
    	String envName ="production2vms";
    	//Create EnvironmentInstance
    	System.out.println("Name: " + envName);
    	if (tiers != null) {
    		for (int i=0; i< tiers.size(); i++){
    			System.out.println("Tier Name(" + i +"): " + tiers.get(i).getName());
    			for (int j=0; j < tiers.get(i).getProductReleases().size(); j++ ){
    				System.out.println("PR Name(" + j +"): " 
    					+ tiers.get(i).getProductReleases().get(j).getName());
    			}
    		}
    	}
        environmentDto = new EnvironmentDto();
        environmentDto.setName(envName);
        environmentDto.setTiers(tiers);
        environmentDto.setEnvironmentType(environmentType);
        
    	EnvironmentInstanceUtils envInstanceManager = new EnvironmentInstanceUtils();
        Task task = envInstanceManager.create(
        		vdc, environmentDto, null);
        
        String taskHref = task.getHref();
        String taskResource = task.getHref();
        long id = Long.parseLong(taskResource.split("/")[8]);
        System.out.println("taskResource: " + taskResource + "  id:" + id);
        TaskUtils taskManager = new TaskUtils();
        Task tasTask = taskManager.load(vdc, id);
        
        String envInstanceResource = tasTask.getResult().getHref();
        String name = envInstanceResource.split("/")[8];
        System.out.println("envInstanceResource: " + envInstanceResource + 
        		" name: " + name);
        
        EnvironmentInstanceDto envInstanceDto = envInstanceManager.load(vdc, name);
        
        //Create ApplicationInstance
    	applicationInstanceDto = new ApplicationInstanceDto();
    	System.out.println ("new ApplicationInstanceDto()");
    	applicationInstanceDto.setApplicationName(applicationName);
    	System.out.println ("new setApplicationName");
    	applicationInstanceDto.setApplicationType(applicationType.getName());
    	System.out.println ("new setApplicationType");   	
    	applicationInstanceDto.setArtifacts(artifacts);
    	System.out.println ("new setArtifacts");
    	applicationInstanceDto.setAttributes(attributes);
    	System.out.println ("new setAttributes");
    	applicationInstanceDto.setVersion(applicationVersion);
    	System.out.println ("new setVersion");
    	applicationInstanceDto.setEnvironmentInstanceName(
    			envInstanceDto.getEnvironmentInstanceName());
    	System.out.println ("new setEnvironmentInstanceName" + 
    			envInstanceDto.getEnvironmentInstanceName());
    	
    	ApplicationInstanceUtils appInstanceManager = new ApplicationInstanceUtils();
        Task appTask = appInstanceManager.install(
        		vdc, applicationInstanceDto, null);

    }
    
    @Then("^There is an applicationInstance in the bbdd$")
    public void iLoadApplicationInstance() throws Exception {        
    	System.out.println("Hello");
    }*/
}
