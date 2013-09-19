package com.telefonica.euro_iaas.paasmanager.it;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.ORG;
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
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;


import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.table.DataTable;

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
    private List<ProductReleaseDto> productReleaseDtos;
    private List<TierDto> tierDtos;
    private EnvironmentDto environmentDto;
    
    //CREATE ENVIRONMENTINSTANCE
   @Given("^Environment OS:$")
	public void getOOSS(DataTable table) {
    	supportedOOSS = new ArrayList<OS>();
	    for (List<String> row: table.raw()) {
	    	supportedOOSS.add(new OS(row.get(0), row.get(1), row.get(2), "description"));
	    	//System.out.println(row.get(0) + " - " + row.get(1));
	    }
    }

  
    
	@Given("^Environment default attributes:$")
	public void getAttributes(DataTable table) {
		attributes = new ArrayList<Attribute>();
	    for (List<String> row: table.raw()) {
	    	attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
	    }
	}
	
	@Given("^Environment Product Type:$")
	public void getProductTypes(DataTable table) {
		productType = new ProductType();
	    for (List<String> row: table.raw()) {
	    	productType.setDescription(row.get(1));
	    	productType.setName(row.get(0));
	   }
	}
	
	@Given("^Environment the product releases:$")
	public void getProductReleases(DataTable table) {
		productReleaseDtos = new ArrayList<ProductReleaseDto>();
	    
		for (List<String> row: table.raw()) {
	    	ProductReleaseDto productReleaseDto = new ProductReleaseDto();
	    	productReleaseDto.setVersion(row.get(0));
	    	productReleaseDto.setProductName(row.get(0));
	    	productReleaseDto.setPrivateAttributes(attributes);
	    	//productReleaseDto.setProductType(productType);
	    	//productReleaseDto.setSupportedOS(supportedOOSS);
	    	
	    	
	    	productReleaseDtos.add(productReleaseDto);
	   }
	}
	
	@Given("^Environment Tiers:$")
	public void getTiers(DataTable table) {
		tierDtos = new ArrayList<TierDto>();
	    boolean isDescription =true;
		for (List<String> row: table.raw()) {
			if (isDescription)
			{
				isDescription = false;
			    continue;
			}
	    	TierDto tierDto = new TierDto();
	    	tierDto.setInitial_number_instances(Integer.parseInt(row.get(1)));
	    	tierDto.setMaximum_number_instances(Integer.parseInt(row.get(2)));
	    	tierDto.setMinimum_number_instances(Integer.parseInt(row.get(3)));
	    	tierDto.setName(row.get(0));
	    	tierDto.setProductReleaseDtos(productReleaseDtos);
	    	
	    	tierDtos.add(tierDto);
	   }
	}
	
	@Given("^EnvironmentType:$")
	public void getEnvironmentType(DataTable table) {
		environmentType = new EnvironmentType();
	    for (List<String> row: table.raw()) {
	    	environmentType.setDescription(row.get(1));
	    	environmentType.setName(row.get(0));
	   }
	
	}
	
    @When("^I create an environmentInstance \"([^\"]*)\"$")
    public void iCreateEnvironmentNotFoundException(String envName)
    throws Exception {
        
    	String vdc = getProperty(VDC);
    	String org = getProperty(ORG);
    	System.out.println ("vdc=" + vdc);
    	
    	System.out.println("Name: " + envName);
    	if (tierDtos != null) {
    		for (int i=0; i< tierDtos.size(); i++){
    			System.out.println("Tier Name(" + i +"): " + tierDtos.get(i).getName());
    			for (int j=0; j < tierDtos.get(i).getProductReleaseDtos().size(); j++ ){
    				System.out.println("PR Name(" + j +"): " 
    					+ tierDtos.get(i).getProductReleaseDtos().get(j).getProductName());
    			}
    		}
    	}
        environmentDto = new EnvironmentDto();
        environmentDto.setName(envName);
        environmentDto.setTierDtos(tierDtos);
        environmentDto.setEnvironmentType(environmentType);
        
    	EnvironmentInstanceUtils envInstanceManager = new EnvironmentInstanceUtils();
        Task task = envInstanceManager.create(org,
        		vdc, environmentDto, null);
        
        String envInstanceResource = task.getHref();
        System.out.println("envInstanceResource: " + envInstanceResource);

    }
    
 
}

