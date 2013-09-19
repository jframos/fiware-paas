/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 *
 */
public class OVFGenerationFiwareImpl implements OVFGeneration {

	/** The log. */
	private static Logger log = Logger.getLogger(OVFGenerationFiwareImpl.class);
	
	private SystemPropertiesProvider systemPropertiesProvider;
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration#createOvf(com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto)
	 */
	public String createOvf(EnvironmentInstanceDto environmentInstanceDto) {
				
		String ovf = null;
		try {
			ovf = loadOvfTemplate(systemPropertiesProvider
					.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
							+ "ovfTemplate.ovf");
			//Introducimos el nombre
			ovf = replace(ovf,"\\$\\{envInstanceName\\}"
					,environmentInstanceDto.getEnvironmentInstanceName());
			//Set File and Diss Sections
			ovf = setFilesDisksinOvf(ovf, 
					environmentInstanceDto.getTierInstances().size());
			//SetVirtualSystems
			ovf = setVirtualSystems (ovf , environmentInstanceDto);
			
		} catch (IOException e) {
			String errorMessage =" The VFTemplate was not Found";
			log.error(errorMessage);
		}
		return ovf;
	}
	
	public String createOvf(EnvironmentDto environmentInstanceDto) {
		
		String ovf = null;
		try {
			ovf = loadOvfTemplate(systemPropertiesProvider
					.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
							+ "ovfTemplate.ovf");
			//Introducimos el nombre
			ovf = replace(ovf,"\\$\\{envInstanceName\\}"
					,environmentInstanceDto.getName());
			//Set File and Diss Sections
			ovf = setFilesDisksinOvf(ovf, 
					environmentInstanceDto.getTierDtos().size());
			//SetVirtualSystems
			ovf = setVirtualSystems (ovf , environmentInstanceDto);
			
		} catch (IOException e) {
			String errorMessage =" The VFTemplate was not Found";
			log.error(errorMessage);
		}
		return ovf;
	}

	
	/**
	 * Set the File and DiskFile Section in ovf based on OVFFILE_SECTION and
	 * OVFDISKFILE_SECTION
	 * @param ovf
	 * @param number
	 * @return
	 */
	private String setFilesDisksinOvf (String ovf, int number) throws IOException  {
		
		String fileSection = "";
		String diskSection = "";
		
		String fileSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)+
			"ovfFileSection.ovf");
		
		String diskSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)+
			"ovfDiskSection.ovf");
		
		/*for (int i=0; i < number; i ++){
			fileSection = fileSection + OVFGeneration.OVFFILE_SECTION + "\n";
			diskSection = diskSection + OVFGeneration.OVFDISKFILE_SECTION + "\n";
		}*/
		
		for (int i=0; i < number; i ++){
			fileSection = fileSection + fileSectionTemplate + "\n";
			diskSection = diskSection + diskSectionTemplate + "\n";
		}
		
		ovf = replace(ovf,"\\$\\{ovfFile\\}"
				, fileSection.substring(0, fileSection.length()-1));
		ovf = replace(ovf,"\\$\\{ovfDisk\\}"
				, diskSection.substring(0, diskSection.length()-1));
		
		return ovf;
	}
	
	/**
	 * Set the VirtualSystem sections of the ovf
	 * @param ovf
	 * @param envInstanceDto
	 * @return
	 */
	private String setVirtualSystems (String ovf
			, EnvironmentInstanceDto envInstanceDto) throws IOException {
		
		String virtualSystemSection = "";
		String virtualSystemSectionAux = "";
		String virtualSystemSectionTemplate = null;

		virtualSystemSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)+
			"virtualSystemTemplate.ovf");
				
		for (int i=0; i < envInstanceDto.getTierInstances().size(); i++) {
			TierInstanceDto tierInstanceDto = 
					envInstanceDto.getTierInstances().get(i);
				
			virtualSystemSectionAux = replace(
					virtualSystemSectionTemplate, "\\$\\{tierInstanceName\\}",
					tierInstanceDto.getTierInstanceName());
				
			virtualSystemSectionAux = setProductInstances (
					virtualSystemSectionAux, tierInstanceDto) + "\n";
			
			virtualSystemSection = virtualSystemSection 
					+ virtualSystemSectionAux;
		}
		
		return replace(ovf,"\\$\\{virtualSystemTemplate\\}",
				virtualSystemSection.substring(0,virtualSystemSection.length()-1));
	}
	
	private String setVirtualSystems (String ovf
			, EnvironmentDto envDto) throws IOException {
		
		String virtualSystemSection = "";
		String virtualSystemSectionAux = "";
		String virtualSystemSectionTemplate = null;

		virtualSystemSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)+
			"virtualSystemTemplate.ovf");
				
		for (int i=0; i < envDto.getTierDtos().size(); i++) {
			TierDto tierInstanceDto = 
					envDto.getTierDtos().get(i);
				
			virtualSystemSectionAux = replace(
					virtualSystemSectionTemplate, "\\$\\{tierInstanceName\\}",
					tierInstanceDto.getName());
				
			virtualSystemSectionAux = setProductInstances (
					virtualSystemSectionAux, tierInstanceDto) + "\n";
			
			virtualSystemSection = virtualSystemSection 
					+ virtualSystemSectionAux;
		}
		
		return replace(ovf,"\\$\\{virtualSystemTemplate\\}",
				virtualSystemSection.substring(0,virtualSystemSection.length()-1));
	}
	
	/**
	 * Set the productSection in the OVF
	 * @param ovf
	 * @param tierInstanceDto
	 * @return
	 */
	private String setProductInstances (String virtualSystemOvf, 
			TierInstanceDto tierInstanceDto) throws IOException {
		
		String productSection = "";
		String productSectionAux = "";
		String productSectionTemplate = null;
		
		productSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
					+ "productSectionTemplate.ovf");
		
		for(int i= 0; i < tierInstanceDto.getProductInstanceDtos().size(); i++){
			ProductInstanceDto productInstanceDto = tierInstanceDto
					.getProductInstanceDtos().get(i);
			//ProductName
			productSectionAux = replace(productSectionTemplate,
					"\\$\\{productInstanceName\\}", productInstanceDto.getName());
			//ProductVersion
			productSectionAux = replace(productSectionAux,
					"\\$\\{productInstanceVersion\\}", 
					productInstanceDto.getProductReleaseDto().getVersion());
			//ProductAttributes
			productSectionAux = setProductAttributes (productSectionAux,
					productInstanceDto);
			
			productSection = productSection + productSectionAux + "\n";
			
		}	
		return replace (virtualSystemOvf, "\\$\\{productSectionTemplate\\}", 
				productSection.substring(0, productSection.length()-1));
	}
	
	private String setProductInstances (String virtualSystemOvf, 
			TierDto tierDto) throws IOException {
		
		String productSection = "";
		String productSectionAux = "";
		String productSectionTemplate = null;
		
		productSectionTemplate = loadOvfTemplate(systemPropertiesProvider
				.getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
					+ "productSectionTemplate.ovf");
		
		for(int i= 0; i < tierDto.getProductReleaseDtos().size(); i++){
			ProductReleaseDto productReleaseDto = tierDto.getProductReleaseDtos().get(i);
			//ProductName
			productSectionAux = replace(productSectionTemplate,
					"\\$\\{productInstanceName\\}", productReleaseDto.getProductName());
			//ProductVersion
			productSectionAux = replace(productSectionAux,
					"\\$\\{productInstanceVersion\\}", 
					productReleaseDto.getVersion());
			//ProductAttributes
			productSectionAux = setProductAttributes (productSectionAux,
					productReleaseDto);
			
			productSection = productSection + productSectionAux + "\n";
			
		}	
		return replace (virtualSystemOvf, "\\$\\{productSectionTemplate\\}", 
				productSection.substring(0, productSection.length()-1));
	}
	
	/**
	 * Set the productAttributes section inf the ovf
	 * @param ovf
	 * @param productInstance
	 * @return
	 */
	private String setProductAttributes (String productSectionTemplate, 
			ProductInstanceDto productInstanceDto){
		
		String productAttributeTemplate = PRODUCTATTRIBUTE_SECTION;
		String productAttribute ="";
		String productAttributeAux ="";
		
		if (productInstanceDto.getAttributes() != null) {
			for(int i =0; i < productInstanceDto.getAttributes().size(); i++){
				Attribute attribute = productInstanceDto.getAttributes().get(i);
			
				productAttributeAux = replace(productAttributeTemplate,
					"\\$\\{attributeKey\\}", attribute.getKey());
				productAttributeAux = replace(productAttributeAux,
					"\\$\\{attributeValue\\}", attribute.getValue());
			
				productAttribute = productAttribute + productAttributeAux + "\n"; 
			}
			productSectionTemplate = replace(productSectionTemplate, "\\$\\{productAttributes\\}", 
					productAttribute.substring(0, productAttribute.length() -1));
		} else {
			productSectionTemplate = replace(productSectionTemplate, "\\$\\{productAttributes\\}", 
					"");
		}
		
		return productSectionTemplate;
	}
	
	private String setProductAttributes (String productSectionTemplate, 
			ProductReleaseDto productReleaseDto){
		
		String productAttributeTemplate = PRODUCTATTRIBUTE_SECTION;
		String productAttribute ="";
		String productAttributeAux ="";
		
		if (productReleaseDto.getPrivateAttributes() != null) {
			for(int i =0; i < productReleaseDto.getPrivateAttributes().size(); i++){
				Attribute attribute = productReleaseDto.getPrivateAttributes() .get(i);
			
				productAttributeAux = replace(productAttributeTemplate,
					"\\$\\{attributeKey\\}", attribute.getKey());
				productAttributeAux = replace(productAttributeAux,
					"\\$\\{attributeValue\\}", attribute.getValue());
			
				productAttribute = productAttribute + productAttributeAux + "\n"; 
			}
			productSectionTemplate = replace(productSectionTemplate, "\\$\\{productAttributes\\}", 
					productAttribute.substring(0, productAttribute.length() -1));
		} else {
			productSectionTemplate = replace(productSectionTemplate, "\\$\\{productAttributes\\}", 
					"");
		}
		
		return productSectionTemplate;
	}
	
	/**
	 * Loads the ovfTemplate fromdisk
	 * @return
	 * @throws IOException
	 */
	private String loadOvfTemplate(String fileLocation) throws IOException {
		/*InputStream is = 
				ClassLoader.getSystemClassLoader()
					.getResourceAsStream(fileLocation);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is))*/
		BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
	}
	

	/**
	 * Replace a macro for its value in a ovf
	 * @param ovf
	 * @param macro
	 * @param value
	 * @return
	 */
	private String replace (String ovf, String macro, String value){	
		return ovf.replaceAll(macro, value);
	}
	
	
	/**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
}
