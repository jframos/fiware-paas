package com.telefonica.euro_iaas.paasmanager.it;

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.ORG;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.Assert;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
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
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


/**
 * Contains the necessary steps to provision an environmentInsance being OK.
 *
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentOKStepsdef {

	private List<OS> supportedOOSS;
    private List<Attribute> attributes;
    private ProductType productType;
    private EnvironmentType environmentType;
    private List<ProductReleaseDto> productReleaseDtos;
    private List<TierDto> tierDtos;
    private EnvironmentDto environmentDto;
    
    @Autowired
	   private ProductReleaseDao productReleaseDao;
    
    @Autowired
	   private EnvironmentResource environmentResource;
    
  
	
	@Given("^Environment the product releases:$")
	public void getProductReleases(DataTable table) {
		productReleaseDtos = new ArrayList<ProductReleaseDto>();
	    
		for (List<String> row: table.raw()) {
	    	ProductReleaseDto productReleaseDto = new ProductReleaseDto();
	    	productReleaseDto.setVersion(row.get(0));
	    	productReleaseDto.setProductName(row.get(0));
	    	
	    	//productReleaseDto.setProductType(productType);
	    	//productReleaseDto.setSupportedOS(supportedOOSS);
	    	ProductRelease tomcat7 = new ProductRelease(productReleaseDto.getProductName(), productReleaseDto.getVersion(),
					"Tomcat server 7", null);
	    	try {
				tomcat7 = productReleaseDao.create(tomcat7);
			} catch (InvalidEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AlreadyExistsEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	productReleaseDtos.add(productReleaseDto);
	   }

	}
	
	@Given("^Environment Tiers:$")
	public void getTiers(DataTable table) {
		tierDtos = new ArrayList<TierDto>();
	    boolean isDescription =true;
		for (List<String> row: table.raw()) {
			
	    	TierDto tierDto = new TierDto();
	    	tierDto.setInitialNumberInstances(Integer.parseInt(row.get(1)));
	    	tierDto.setMaximumNumberInstances(Integer.parseInt(row.get(2)));
	    	tierDto.setMinimumNumberInstances(Integer.parseInt(row.get(3)));
	    	tierDto.setName(row.get(0));
	    	tierDto.setProductReleaseDtos(productReleaseDtos);
	    	
	    	tierDtos.add(tierDto);
		}

	}
	

	
    @When("^I create an environment \"([^\"]*)\"$")
    public void iCreateEnvironmentNotFoundException(String envName)
    throws Exception {
    	System.out.println ("iCreateEnvironmentNotFoundException");
        
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
       
        environmentResource.insert(org, vdc, environmentDto);

    }
    
 
}

