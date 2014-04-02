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

import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;

/**
 * @author jesus.movilla
 */
public interface OVFUtils {

    public final static String XSQL_ENVELOPE = "/InstantiateOvfParams/Envelope";
    public final static String XSQL_REFERENCES = "/InstantiateOvfParams/Envelope/References";
    public final static String XSQL_FILE = "/InstantiateOvfParams/Envelope/References/File";
    public final static String XSQL_DISKSECTION = "/InstantiateOvfParams/Envelope/DiskSection";
    public final static String XSQL_DISK = "/InstantiateOvfParams/Envelope/DiskSection/Disk";
    public final static String XSQL_VSCOLLECTION = "/InstantiateOvfParams/Envelope/" + "VirtualSystemCollection";
    public final static String XSQL_NEW_VS = "/InstantiateOvfParams/Envelope/VirtualSystem";
    public final static String XSQL_OLD_VS = "/InstantiateOvfParams/Envelope/"
            + "VirtualSystemCollection/VirtualSystem";

    public final static String VIRTUALSYSTEM_TAG = "VirtualSystem";
    public final static String VIRTUALSYSTEMID_TAG = "ovf:id";

    public final static String OPERATINGSYSTEM_SECTION = "ovf:OperatingSystemSection";
    public final static String OSTYPE_ID = "ovf:id";
    public final static String VIRTUAL_SYSTEM_ID = "ovf:id";
    public final static String INSTANTIATEOVFPARMS_SECTION = "InstantiateOvfParams";
    public final static String INSTANTIATEOVFPARMS_NAME_ATTRIBUTE = "name";

    public final static String FILE_TAG = "ovf:File";
    public final static String VIRTUAL_SYSTEM_TAG = "ovf:VirtualSystem";

    /**
     * Splits the MultipleVM OVF in singleVM OVFs
     * 
     * @param ovf
     * @return
     * @throws InvalidOVFException
     */
    public List<String> getOvfsSingleVM(String ovf) throws InvalidOVFException;

    /**
     * Get the servicename from the ovf
     * 
     * @param ovf
     * @return
     * @throws InvalidOVFException
     */
    public String getServiceName(String ovf) throws InvalidOVFException;

    // public String getOSType(String ovfVM) throws InvalidOVFException;

    public String getRECVMNameFromProductSection(String recProductSection) throws InvalidOVFException;

    public String changeInitialResources(String ovf);

    public String deleteRules(String ovf);

    public String deleteProductSection(String ovf);

    public List<String> getProductSectionName(String ovf);

}
