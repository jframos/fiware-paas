/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * @author jesus.movilla
 */
public class OVFMacroImpl implements OVFMacro {

    private ExtendedOVFUtil extendedOVFUtil;
    /** The log. */
    private static Logger log = Logger.getLogger(OVFMacroImpl.class);

    // ip(vm,network)
    // port(product)
    // login(vm)
    // password(login)
    // el patron ser� "name(source)" Name es el nombre de la propiedad y
    // source el origen donde haya que buscarlo si source=product hay
    // que buscarlo en el ovf si es vm hay que buscarlo en el objeto VM

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.OVFMacro#convertMacros(com.
     * telefonica.euro_iaas.paasmanager.util.EnvironmentInstance)
     */
    public Environment resolveMacros(Environment environment) throws InvalidEnvironmentRequestException {

        String ovf = environment.getOvf();

        environment.setName(extendedOVFUtil.getEnvironmentName(ovf));
        environment.setTiers(extendedOVFUtil.getTiers(ovf, environment.getVdc()));

        if (ovf == null) {
            String error = "The VApp to be macro-treated is not present " + "in the environmentInstance";
            log.error(error);
            throw new InvalidEnvironmentRequestException(error);
        }

        List<String> macros = getAllMacros(ovf);
        List<String> values = new ArrayList<String>();

        for (int i = 0; i < macros.size(); i++) {
            String[] macroParts = macros.get(i).split("\\(");
            String macroProperty = (String) macroParts[0].substring(1);
            String macroReference = (String) macroParts[1].substring(0, macroParts[1].length() - 1);

            if (macroProperty.equals(MACRO_NAME_IP)) {
                values.add(macros.get(i));
            } else {
                values.add(getProductAttributeFromEnvironment(environment, macroProperty, macroReference));
            }

            ovf = setMacroValue(ovf, macros.get(i), values.get(i));
        }
        environment.setOvf(ovf);

        return environment;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.OVFMacro#getAllMacros(java. lang.String)
     */
    /*
     * private HashMap<String, String> getMacroKeyValue(String xml) { log.info("OVFMacroImpl.getAllMacros()");
     * HashMap<String, String> macros = new HashMap<String, String>(); String[] parts = xml.split("\""); List<String>
     * macroList = new ArrayList<String>(); for (int i= 0; i < parts.length; i++){ if (parts[i].startsWith("@"))
     * macroList.add(parts[i]); } for (int j=0; j < macroList.size(); j++) { String macroProperty =
     * macroList.get(j).split("(")[0].substring(1); String macroReference = macroList.get(j).split("(")[1].substring(0,
     * macroList.get(j).split("(")[1].length() -1); macros.put(macroProperty, macroReference); } return macros; }
     */
    private List<String> getAllMacros(String xml) {
        log.info("OVFMacroImpl.getAllMacros()");
        String[] parts = xml.split("@");
        List<String> macroList = new ArrayList<String>();
        // We skip the first element which is not a macro
        for (int i = 1; i < parts.length; i++) {
            String[] macros = parts[i].split("\\)");
            String macro = "@" + macros[0] + ")";
            macroList.add(macro);
            // if (parts[i].startsWith("@"))
            // macroList.add(parts[i]);
        }
        return macroList;
    }

    /*
     * private List<String> getAllMacros(String xml) { log.info("OVFMacroImpl.getAllMacros()"); String[] parts =
     * xml.split("\""); List<String> macroList = new ArrayList<String>(); for (int i= 0; i < parts.length; i++){ if
     * (parts[i].startsWith("@")) macroList.add(parts[i]); } return macroList; }
     */

    /**
     * Getting the ip of a VM present in the EnvironmentInstance named vmnae
     * 
     * @param envInstance
     * @param vmname
     *            the name of the VM we want the ip from
     * @return
     */
    private String getIPFromEnvironmentInstance(EnvironmentInstance envInstance, String vmname, String network)
            throws InvalidEnvironmentRequestException {
        // So far network is not taken into account
        String ip = null;
        // Go through all VMs present in EnvironmentInstance looking for ip
        for (int i = 0; i < envInstance.getTierInstances().size(); i++) {
            TierInstance tierInstance = envInstance.getTierInstances().get(i);
            // for (int j=0; j < tierInstance.getProductInstances().size();
            // j++){
            // ProductInstance productInstance
            // = tierInstance.getProductInstances().get(j);

            if ((tierInstance.getVM().getFqn().contains(vmname))
                    && (tierInstance.getVM().getNetworks().get(network)) != null) {

                if (tierInstance.getVM().getNetworks().get(network) != null) {
                    ip = tierInstance.getVM().getNetworks().get(network);
                } else {
                    String errorMessage = "The VM does not have an ip associated";
                    log.error(errorMessage);
                    throw new InvalidEnvironmentRequestException(errorMessage);
                }

            }
            // }
        }

        if (ip == null) {
            String errorMessage = "The VM " + vmname + " is not present in " + " the environmentInstace: "
                    + envInstance.getName();
            log.error(errorMessage);
            throw new InvalidEnvironmentRequestException(errorMessage);
        }

        return ip;
    }

    private String getProductAttributeFromEnvironment(Environment environment, String macroAttribute,
            String macroProductName) throws InvalidEnvironmentRequestException {

        String macroValue = null;
        // Go through all VMs present in EnvironmentInstance looking for an
        // attribute
        for (int i = 0; i < environment.getTiers().size(); i++) {
            Tier tier = environment.getTiers().get(i);
            for (int j = 0; j < tier.getProductReleases().size(); j++) {
                ProductRelease productRelease = tier.getProductReleases().get(j);

                if ((productRelease.getName().contains(macroProductName)) && (productRelease.getAttributes() != null)) {
                    List<Attribute> attributes = productRelease.getAttributes();

                    for (int k = 0; k < attributes.size(); k++) {
                        if (attributes.get(k).getKey().equals(macroAttribute)) {
                            macroValue = attributes.get(k).getValue();
                        }
                    }
                }
            }
        }

        if (macroValue == null) {
            String errorMessage = " Attribute " + macroAttribute + " is not present in ProductReleases of Environment "
                    + environment.getName() + ". Macro could not be resolved ";
            log.info(errorMessage);
            macroValue = "@" + macroAttribute + "(" + macroProductName + ")";
            // throw new InvalidEnvironmentRequestException(errorMessage);
        }

        return macroValue;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.util.OVFMacro#setMacroValue(java .lang.String, java.lang.String,
     * java.lang.String)
     */
    private String setMacroValue(String xml, String macro, String macroValue) {
        return xml.replace(macro, macroValue);
    }

    /**
     * @param extendedOVFUtil
     *            the extendedOVFUtil to set
     */
    public void setExtendedOVFUtil(ExtendedOVFUtil extendedOVFUtil) {
        this.extendedOVFUtil = extendedOVFUtil;
    }

}
