/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.exception;

import javax.servlet.ServletException;

public class APIException extends ServletException {

    private String message;
    private String publicMessage;
    private Integer code;
    private Integer httpCode;
    private Throwable cause;

    public APIException(Throwable cause) {

        super(cause);
        this.cause = cause;

    }

    public APIException(Throwable cause, int error) {

        this.cause = cause;
        this.httpCode = error;

    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        if (message == null) {
            parseCause();
        }

        return this.message;
    }

    public void parseCause() {

        ErrorCode errorCode = ErrorCode.find(cause.toString());
        this.code = errorCode.getCode();
        this.publicMessage = errorCode.getPublicMessage();
        this.message = errorCode.toString() + "#" + cause.getMessage();
        this.httpCode = errorCode.getHttpCode();

    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public Integer getHttpCode() {
        return httpCode;
    }
}
