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

package com.telefonica.euro_iaas.paasmanager.rest.util;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;

/**
 * @author jesus.movilla
 */
public interface OVFGeneration {

    String OVFFILE_SECTION = "<ovf:File ovf:id=\"8195dcf5-32a6-4a20-ae93-ccde5e3b459d\""
            + " ovf:href=\"file:///admin.template.snapshotpaas\""
            + " rsrvr:digest=\"8f1643c4fdf83ab3827190ab771f76e1\"/>";

    String OVFDISKFILE_SECTION = "<ovf:Disk ovf:diskId=\"disks-2000\""
            + " ovf:fileRef=\"8195dcf5-32a6-4a20-ae93-ccde5e3b459d\"" + " ovf:capacity=\"0\""
            + " ovf:format=\"http://www.gnome.org/~markmc/qcow-image-format.html\"/>";

    String PRODUCTATTRIBUTE_SECTION = "<ovfenvelope:Property ovfenvelope:key=\"${attributeKey}\""
            + " ovfenvelope:value=\"${attributeValue}\"/>";

    String createOvf(EnvironmentInstanceDto environmentInstanceDto);

    String createOvf(EnvironmentDto environmentInstanceDto);
}
