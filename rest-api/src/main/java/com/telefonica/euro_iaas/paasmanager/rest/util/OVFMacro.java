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

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;

/**
 * @author jesus.movilla
 * 
 */
public interface OVFMacro {

	public final static String MACRO_PATTERN = "@{0}({1})";
	public final static String MACRO_NAME_IP = "ip";

	/**
	 * Convert the macros present in the Ovf for their corresponding values
	 * 
	 * @param environment
	 *            with the ovf inside
	 * @return the converted ovf
	 */
	Environment resolveMacros(Environment environment)
			throws InvalidEnvironmentRequestException;
}
