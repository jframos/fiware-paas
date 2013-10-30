/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
