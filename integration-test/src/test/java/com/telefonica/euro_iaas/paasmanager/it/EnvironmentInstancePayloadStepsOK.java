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

import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.PAYLOAD_LOATION;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.ORG;
import static com.telefonica.euro_iaas.paasmanager.it.util.QAProperties.getProperty;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.it.util.EnvironmentInstanceUtils;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;




import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

/**
 * @author jesus.movilla
 *
 */
public class EnvironmentInstancePayloadStepsOK {

	private String payload;
	
    //CREATE ENVIRONMENTINSTANCE
    @Given("^Payload Location$")
	public void getPayloadLocation() {
    	
    	InputStream is;
		try {
			is = new FileInputStream(getProperty(PAYLOAD_LOATION));
		
				//ClassLoader.getSystemClassLoader().getResourceAsStream("InstantiateVDCTemplate.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer ruleFile = new StringBuffer();
			String actualString;

			while ((actualString = reader.readLine()) != null) {
				ruleFile.append(actualString).append("\n");
			}
			
	        payload = ruleFile.toString();
	        System.out.println("payload: " + payload);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println ("IOException File not found");
		}
    }
    
    @When("^I create an environmentInstance$")
    public void iCreateEnvironmentPayloadOK()
    	throws Exception {
    	
    	String vdc = getProperty(VDC);
    	String org = getProperty(ORG);
    	
    	EnvironmentInstanceUtils envInstanceManager = new EnvironmentInstanceUtils();
        Task task = envInstanceManager.create(org, vdc, payload, null);
    }
    
    @Then("^There is an environmentInstance in the bbdd$")
    public void There_is_an_environmentInstance_in_the_bbdd() {
        // Express the Regexp above with the code you wish you had
    //	envInstanceManager.
    }
    
    
}
