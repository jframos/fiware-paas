/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
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

    /**
     * It creates a OVF from an envioronment.
     */
    public String createOvf(EnvironmentDto environmentDto) {

        String ovf = null;
        try {
            ovf = loadOvfTemplate(systemPropertiesProvider
                    .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                    + "ovfTemplate.ovf");
            // Introducimos el nombre
            ovf = replace(ovf, "\\$\\{envInstanceName\\}",
                    environmentDto.getName());
            // Set File and Diss Sections
            ovf = setFilesDisksinOvf(ovf, environmentDto);
            // SetVirtualSystems
            ovf = setVirtualSystems(ovf, environmentDto);

        } catch (IOException e) {
            String errorMessage = " The VFTemplate was not Found";
            log.error(errorMessage);
        }
        return ovf;
    }

    /**
     * It creates a OVF from an envioronment.
     */
    public String createOvf(EnvironmentInstanceDto environmentInstanceDto) {

        String ovf = null;
        try {
            ovf = loadOvfTemplate(systemPropertiesProvider
                    .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                    + "ovfTemplate.ovf");
            // Introducimos el nombre
            ovf = replace(ovf, "\\$\\{envInstanceName\\}",
                    environmentInstanceDto.getBlueprintName());
            // Set File and Diss Sections
            ovf = setFilesDisksinOvf(ovf, environmentInstanceDto.getEnvironmentDto());
            // SetVirtualSystems
            ovf = setVirtualSystems(ovf, environmentInstanceDto.getEnvironmentDto());

        } catch (IOException e) {
            String errorMessage = " The VFTemplate was not Found";
            log.error(errorMessage);
        }
        return ovf;
    }

    /**
     * Loads the ovfTemplate fromdisk.
     * 
     * @return
     * @throws IOException
     */
    private String loadOvfTemplate(String fileLocation) throws IOException {
        /*
         * InputStream is = ClassLoader.getSystemClassLoader()
         * .getResourceAsStream(fileLocation); BufferedReader reader = new
         * BufferedReader(new InputStreamReader(is))
         */
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
            ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
    }

    /**
     * Replace a macro for its value in a ovf.
     * 
     * @param ovf
     * @param macro
     * @param value
     * @return
     */
    private String replace(String ovf, String macro, String value) {
        return ovf.replaceAll(macro, value);
    }

    /**
     * Set the File and DiskFile Section in ovf based on OVFFILE_SECTION and
     * OVFDISKFILE_SECTION.
     * 
     * @param ovf
     * @param number
     * @return
     */
    private String setFilesDisksinOvf(String ovf, EnvironmentDto environment)
    throws IOException {

        String fileSection = "";
        String diskSection = "";

        String fileSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "ovfFileSection.ovf");

        String diskSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "ovfDiskSection.ovf");

        for (TierDto tier : environment.getTierDtos()) {
            String fileTier = replace(fileSectionTemplate, "\\$\\{file_id\\}",
                    tier.getName());
            fileTier = replace(fileTier, "\\$\\{href_file\\}", tier.getImage());
            fileSection = fileSection + fileTier;
        }

        for (TierDto tier : environment.getTierDtos()) {
            String diskTier = replace(diskSectionTemplate, "\\$\\{disk_id\\}",
                    tier.getName());
            diskTier = replace(diskTier, "\\$\\{disk_ref\\}", tier.getName());
            diskSection = diskSection + diskTier;
        }
        ovf = replace(ovf, "\\$\\{ovfFile\\}", fileSection.substring(0,
                fileSection.length() - 1));
        ovf = replace(ovf, "\\$\\{ovfDisk\\}", diskSection.substring(0,
                diskSection.length() - 1));

        return ovf;
    }

    private String setInfoTier(TierDto tierDto) throws IOException {

        String infoTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "info.ovf");
        if (tierDto.getKeypair() == null && tierDto.getFloatingip() == null
                && tierDto.getSecurityGroup() == null) {
            return "";
        }
        infoTemplate = replace(infoTemplate, "\\$\\{key_name\\}", tierDto
                .getKeypair());
        infoTemplate = replace(infoTemplate, "\\$\\{floating_ip\\}", tierDto
                .getFloatingip());
        infoTemplate = replace(infoTemplate, "\\$\\{security_group\\}", tierDto
                .getSecurityGroup());
        return infoTemplate;

    }

    /**
     * Set the productAttributes section inf the ovf.
     * 
     * @param ovf
     * @param productInstance
     * @return
     */
    private String setProductAttributes(String productSectionTemplate,
            ProductInstanceDto productInstanceDto) {

        String productAttributeTemplate = PRODUCTATTRIBUTE_SECTION;
        String productAttribute = "";
        String productAttributeAux = "";

        if (productInstanceDto.getAttributes() != null) {
            for (int i = 0; i < productInstanceDto.getAttributes().size(); i++) {
                Attribute attribute = productInstanceDto.getAttributes().get(i);

                productAttributeAux = replace(productAttributeTemplate,
                        "\\$\\{attributeKey\\}", attribute.getKey());
                productAttributeAux = replace(productAttributeAux,
                        "\\$\\{attributeValue\\}", attribute.getValue());

                productAttribute = productAttribute + productAttributeAux
                + "\n";
            }
            productSectionTemplate = replace(productSectionTemplate,
                    "\\$\\{productAttributes\\}", productAttribute.substring(0,
                            productAttribute.length() - 1));
        } else {
            productSectionTemplate = replace(productSectionTemplate,
                    "\\$\\{productAttributes\\}", "");
        }

        return productSectionTemplate;
    }

    private String setProductAttributes(String productSectionTemplate,
            ProductReleaseDto productReleaseDto) {

        String productAttributeTemplate = PRODUCTATTRIBUTE_SECTION;
        String productAttribute = "";
        String productAttributeAux = "";

        if (productReleaseDto.getPrivateAttributes() != null) {
            for (int i = 0; i < productReleaseDto.getPrivateAttributes().size(); i++) {
                Attribute attribute = productReleaseDto.getPrivateAttributes()
                .get(i);

                productAttributeAux = replace(productAttributeTemplate,
                        "\\$\\{attributeKey\\}", attribute.getKey());
                productAttributeAux = replace(productAttributeAux,
                        "\\$\\{attributeValue\\}", attribute.getValue());

                productAttribute = productAttribute + productAttributeAux
                + "\n";
            }
            productSectionTemplate = replace(productSectionTemplate,
                    "\\$\\{productAttributes\\}", productAttribute.substring(0,
                            productAttribute.length() - 1));
        } else {
            productSectionTemplate = replace(productSectionTemplate,
                    "\\$\\{productAttributes\\}", "");
        }

        return productSectionTemplate;
    }

    private String setProductInstances(String virtualSystemOvf, TierDto tierDto)
    throws IOException {

        String productSection = "";
        String productSectionAux = "";
        String productSectionTemplate = null;

        productSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "productSectionTemplate.ovf");

        for (ProductReleaseDto productReleaseDto: tierDto.getProductReleaseDtos()) {

            // ProductName
            productSectionAux = replace(productSectionTemplate,
                    "\\$\\{productInstanceName\\}", productReleaseDto
                    .getProductName());
            // ProductVersion
            productSectionAux = replace(productSectionAux,
                    "\\$\\{productInstanceVersion\\}", productReleaseDto
                    .getVersion());
            // ProductAttributes
            productSectionAux = setProductAttributes(productSectionAux,
                    productReleaseDto);

            productSection = productSection + productSectionAux + "\n";

        }
        if (tierDto.getProductReleaseDtos().size() == 0){
            return replace(virtualSystemOvf, "\\$\\{productSectionTemplate\\}",
            "");
        }
        return replace(virtualSystemOvf, "\\$\\{productSectionTemplate\\}",
                productSection.substring(0, productSection.length() - 1));
    }

    /**
     * Set the productSection in the OVF.
     * 
     * @param ovf
     * @param tierInstanceDto
     * @return
     */
    private String setProductInstances(String virtualSystemOvf,
            TierInstanceDto tierInstanceDto) throws IOException {

        String productSection = "";
        String productSectionAux = "";
        String productSectionTemplate = null;

        productSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "productSectionTemplate.ovf");

        for (int i = 0; i < tierInstanceDto.getProductInstanceDtos().size(); i++) {
            ProductInstanceDto productInstanceDto = tierInstanceDto
            .getProductInstanceDtos().get(i);
            // ProductName
            productSectionAux = replace(productSectionTemplate,
                    "\\$\\{productInstanceName\\}", productInstanceDto
                    .getName());
            // ProductVersion
            productSectionAux = replace(productSectionAux,
                    "\\$\\{productInstanceVersion\\}", productInstanceDto
                    .getProductReleaseDto().getVersion());
            // ProductAttributes
            productSectionAux = setProductAttributes(productSectionAux,
                    productInstanceDto);

            productSection = productSection + productSectionAux + "\n";

        }
        return replace(virtualSystemOvf, "\\$\\{productSectionTemplate\\}",
                productSection.substring(0, productSection.length() - 1));
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(
            SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    private String setVirtualSystems(String ovf, EnvironmentDto envDto)
    throws IOException {

        String virtualSystemSection = "";
        String virtualSystemSectionAux = "";
        String virtualSystemSectionTemplate = null;

        virtualSystemSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "virtualSystemTemplate.ovf");

        for (int i = 0; i < envDto.getTierDtos().size(); i++) {
            TierDto tierDto = envDto.getTierDtos().get(i);

            virtualSystemSectionAux = replace(virtualSystemSectionTemplate,
                    "\\$\\{tierName\\}", tierDto.getName());
            virtualSystemSectionAux = replace(virtualSystemSectionAux,
                    "\\$\\{num_min\\}", tierDto.getMinimumNumberInstances()
                    + "");
            virtualSystemSectionAux = replace(virtualSystemSectionAux,
                    "\\$\\{num_max\\}", tierDto.getMaximumNumberInstances()
                    + "");
            virtualSystemSectionAux = replace(virtualSystemSectionAux,
                    "\\$\\{num_initial\\}", tierDto.getInitialNumberInstances()
                    + "");

            /*
             * .replace("\\$\\{num_min\\}",
             * tierDto.getMinimum_number_instances()
             * +"").replace("\\$\\{num_max\\}",
             * tierDto.getMaximum_number_instances
             * ()+"").replace("\\$\\{num_initial\\}",
             * tierDto.getInitial_number_instances()+"");
             */

            try {
                virtualSystemSectionAux = setProductInstances(
                        virtualSystemSectionAux, tierDto)
                        + "\n";
            } catch (NullPointerException e) {
                log.info(tierDto.getName() + " does not have products");
                virtualSystemSectionAux = virtualSystemSectionAux.replaceAll("\\$\\{productSectionTemplate\\}","");
            }
            String info = setInfoTier(tierDto) + "\n";
            virtualSystemSectionAux = replace(virtualSystemSectionAux,
                    "\\$\\{info\\}", info);

            virtualSystemSection = virtualSystemSection
            + virtualSystemSectionAux;
        }

        return replace(ovf, "\\$\\{virtualSystemTemplate\\}",
                virtualSystemSection);

    }

    /**
     * Set the VirtualSystem sections of the ovf
     * 
     * @param ovf
     * @param envInstanceDto
     * @return
     */
    private String setVirtualSystems(String ovf,
            EnvironmentInstanceDto envInstanceDto) throws IOException {

        String virtualSystemSection = "";
        String virtualSystemSectionAux = "";
        String virtualSystemSectionTemplate = null;

        virtualSystemSectionTemplate = loadOvfTemplate(systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.OVF_TEMPLATE_LOCATION)
                + "virtualSystemTemplate.ovf");

        for (int i = 0; i < envInstanceDto.getTierInstances().size(); i++) {
            TierInstanceDto tierInstanceDto = envInstanceDto.getTierInstances()
            .get(i);

            virtualSystemSectionAux = replace(virtualSystemSectionTemplate,
                    "\\$\\{tierName\\}", tierInstanceDto.getTierInstanceName());

            virtualSystemSectionAux = setProductInstances(
                    virtualSystemSectionAux, tierInstanceDto)
                    + "\n";

            virtualSystemSection = virtualSystemSection
            + virtualSystemSectionAux;
        }

        return replace(ovf, "\\$\\{virtualSystemTemplate\\}",
                virtualSystemSection.substring(0,
                        virtualSystemSection.length() - 1));
    }
}