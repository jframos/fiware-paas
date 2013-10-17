/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * @author jesus.movilla
 */
public class OSNotRetrievedException extends Exception {

    public OSNotRetrievedException(String msg) {
        super(msg);
    }

    public OSNotRetrievedException(Throwable e) {
        super(e);
    }

    public OSNotRetrievedException(String msg, Throwable e) {
        super(msg, e);
    }
}
