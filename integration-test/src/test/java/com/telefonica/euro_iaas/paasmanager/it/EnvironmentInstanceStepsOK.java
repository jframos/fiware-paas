package com.telefonica.euro_iaas.paasmanager.it;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.paasmanager.it.util.EnvironmentInstanceUtils;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

import cuke4duke.Table;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 * Contains the necessary steps to provision an environmentInsance being OK.
 *
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentInstanceStepsOK {

    private List<OS> supportedOOSS;
    private List<Attribute> attributes;
    private ProductType productType;
    private EnvironmentType environmentType;
    private List<ProductRelease> productReleases;
    private List<Tier> tiers;
    private EnvironmentDto environmentDto;
    
    //CREATE ENVIRONMENTINSTANCE
    @Given("^Environment OS:$")
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
	
    @When("^I create an environmentInstance \"([^\"]*)\"$")
    public void iCreateEnvironmentNotFoundException(String envName)
    throws Exception {
        
    	String vdc = getProperty(VDC);
    	System.out.println ("vdc=" + vdc);
    	
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

    }
    
    @Then("^There is an environmentInstance in the bbdd$")
    public void iCreateEnvironmentInstanceII() throws Exception {        
    	System.out.println("Hello");
    }
}

