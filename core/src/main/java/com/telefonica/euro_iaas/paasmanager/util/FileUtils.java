/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;

public interface FileUtils {
    public String readFile(String path, String parentPath) throws FileUtilsException;

    public void generationFile(String filename, String text) throws FileUtilsException;

    public String readFile(String property) throws FileUtilsException;

}
