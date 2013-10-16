/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * @author jesus.movilla
 * 
 */
public class ClaudiaResourceNotFoundException extends Exception {

	public ClaudiaResourceNotFoundException(String msg) {
		super(msg);
	}

	public ClaudiaResourceNotFoundException(Throwable e) {
		super(e);
	}

	public ClaudiaResourceNotFoundException(String msg, Throwable e) {
		super(msg, e);

	}
}