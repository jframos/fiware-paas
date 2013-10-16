/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * @author jesus.movilla
 */
public class EnvironmentUtilsImpl implements EnvironmentUtils {

    private OVFUtils ovfUtils;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.EnvironmentUtils#resolveMacros
     * (com.telefonica.euro_iaas.paasmanager.model.Environment, java.util.List)
     */
    public Environment resolveMacros(Environment environment, List<VM> vms) {

        List<Tier> tiers = environment.getTiers();
        // Go through all attributes of al ProductReleases of all tiers from
        // environment
        for (int i = 0; i < tiers.size(); i++) {
            List<ProductRelease> productReleases = tiers.get(i).getProductReleases();
            for (int j = 0; j < productReleases.size(); j++) {
                if (productReleases.get(j).getAttributes() != null) {
                    List<Attribute> attributes = productReleases.get(j).getAttributes();
                    for (int k = 0; k < attributes.size(); k++) {
                        // Look at attribute whose value contains @ip
                        if (attributes.get(k).getValue().contains(IP_MACRO_ID)) {
                            // Recover the ipmacro
                            String macroLine = attributes.get(k).getValue();
                            String ipmacro = getIPMacro(macroLine);

                            // Recover vmname from macro @ip(vmname,network)
                            // String vm =
                            // getVMNameFromMacro(attributes.get(k).getValue());
                            String vm = getVMNameFromMacro(ipmacro);
                            // Look for ip of VM whose fqn contains vmname
                            for (int l = 0; l < vms.size(); l++) {
                                if (vms.get(l).getFqn().contains(vm))
                                    attributes.get(k).setValue(macroLine.replace(ipmacro, vms.get(l).getIp()));
                            }
                        }
                    }
                }
            }
        }
        return environment;
    }

    public Environment resolveMacros(EnvironmentInstance envInst, List<Tier> tiers) {

        // List<Tier> tiers = envInst.getEnvironment().getTiers();
        // Go through all attributes of al ProductReleases of all tiers from
        // environment
        for (int i = 0; i < tiers.size(); i++) {
            List<ProductRelease> productReleases = tiers.get(i).getProductReleases();
            for (int j = 0; j < productReleases.size(); j++) {
                if (productReleases.get(j).getAttributes() != null) {
                    List<Attribute> attributes = productReleases.get(j).getAttributes();
                    for (int k = 0; k < attributes.size(); k++) {
                        // Look at attribute whose value contains @ip
                        if (attributes.get(k).getValue().contains(IP_MACRO_ID)) {
                            // Recover the ipmacro
                            String macroLine = attributes.get(k).getValue();
                            String ipmacro = getIPMacro(macroLine);

                            // Recover vmname from macro @ip(vmname,network)
                            // String vm =
                            // getVMNameFromMacro(attributes.get(k).getValue());
                            String nameVm = getVMNameFromMacro(ipmacro);

                            VM vm = getVmWithName(envInst, nameVm);
                            attributes.get(k).setValue(macroLine.replace(ipmacro, vm.getIp()));
                        }
                    }
                }
            }
        }
        return envInst.getEnvironment();
    }

    private VM getVmWithName(EnvironmentInstance environmetnInstance, String nameVM) {
        for (TierInstance tierInstance : environmetnInstance.getTierInstances()) {
            if (tierInstance.getVM().getFqn().contains(nameVM))
                return tierInstance.getVM();
        }
        return null;

    }

    private String getVMNameFromMacro(String attribute) {
        String[] parts = attribute.split("\\,");
        String vmname = parts[0].split("\\(")[1];
        return vmname;
    }

    private String getIPMacro(String line) {
        String macroStart;

        if (line.startsWith(IP_MACRO_ID))
            macroStart = line.substring(3);
        else
            macroStart = line.split(IP_MACRO_ID)[1];

        String ipMacro = IP_MACRO_ID + macroStart.split("\\)")[0] + ")";

        return ipMacro;
    }

    public String updateVmOvf(String ovf, String imageName) {
        if (ovf == null || ovf.equals(""))
            return null;

        if (imageName != null) {

            String[] part_inicio = ovf.split("<References>", 2);
            String[] part_final = part_inicio[1].split("</References>", 2);
            String[] part_middle = part_final[0].split(" ");
            part_middle[2] = "ovf:href=\"" + imageName + "\"";
            String middle = "";
            for (int i = 0; part_middle.length > i; i++) {
                middle = middle + " " + part_middle[i];
            }
            ovf = part_inicio[0] + "<References>" + middle + "</References>" + part_final[1];
            // cambiamos el operating sistem Section
            String[] part_inicio2 = ovf.split("<ovf:OperatingSystemSection", 2);
            // Tenemos la prat_inicio[0] bien
            String[] part_final2 = part_inicio2[1].split("</ovf:OperatingSystemSection>", 2);
            String[] part_middle2 = part_final2[0].split("<Description>", 2);
            String middle2 = part_middle2[0] + "<Description>" + imageName + "</Description>";

            ovf = part_inicio2[0] + "<ovf:OperatingSystemSection" + middle2 + "</ovf:OperatingSystemSection>"
                    + part_final2[1];

            // Ahora hay que cambiar los valores de la VM
        }

        String ovfFinal = ovfUtils.changeInitialResources(ovf);

        while (ovfFinal.contains("ovfenvelope:ProductSection"))
            ovfFinal = ovfUtils.deleteProductSection(ovfFinal);
        while (ovfFinal.contains("rsrvr:GovernanceRuleSection"))
            ovfFinal = ovfUtils.deleteRules(ovfFinal);

        return ovfFinal;
    }

    public String deleteProductSection(String vmOVF) {
        return ovfUtils.deleteProductSection(vmOVF);
    }

    public void setOvfUtils(OVFUtils ovfUtils) {
        this.ovfUtils = ovfUtils;
    }

}
