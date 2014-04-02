/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.util.List;
import java.util.Set;

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

        Set<Tier> tiers = environment.getTiers();
        // Go through all attributes of al ProductReleases of all tiers from
        // environment
        for (Tier tier: tiers) {
            List<ProductRelease> productReleases = tier.getProductReleases();
            for (int j = 0; j < productReleases.size(); j++) {
                if (productReleases.get(j).getAttributes() != null) {
                    Set<Attribute> attributes = productReleases.get(j).getAttributes();
                    for (Attribute att: attributes) {
                        // Look at attribute whose value contains @ip
                        if (att.getValue().contains(IP_MACRO_ID)) {
                            // Recover the ipmacro
                            String macroLine = att.getValue();
                            String ipmacro = getIPMacro(macroLine);

                            // Recover vmname from macro @ip(vmname,network)
                            // String vm =
                            // getVMNameFromMacro(attributes.get(k).getValue());
                            String vm = getVMNameFromMacro(ipmacro);
                            // Look for ip of VM whose fqn contains vmname
                            for (int l = 0; l < vms.size(); l++) {
                                if (vms.get(l).getFqn().contains(vm)) {
                                    att.setValue(macroLine.replace(ipmacro, vms.get(l).getIp()));
                                }
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
                    Set<Attribute> attributes = productReleases.get(j).getAttributes();
                    for (Attribute att: attributes) {
                        // Look at attribute whose value contains @ip
                        if (att.getValue().contains(IP_MACRO_ID)) {
                            // Recover the ipmacro
                            String macroLine = att.getValue();
                            String ipmacro = getIPMacro(macroLine);

                            // Recover vmname from macro @ip(vmname,network)
                            // String vm =
                            // getVMNameFromMacro(attributes.get(k).getValue());
                            String nameVm = getVMNameFromMacro(ipmacro);

                            VM vm = getVmWithName(envInst, nameVm);
                            att.setValue(macroLine.replace(ipmacro, vm.getIp()));
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
