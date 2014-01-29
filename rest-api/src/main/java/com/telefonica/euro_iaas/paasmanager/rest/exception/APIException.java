/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.exception;

public class APIException extends RuntimeException {

    private String message;
    private String publicMessage;
    private Integer code;
    private Integer httpCode;

    public APIException(Throwable cause) {

        parseMessage(cause.toString());

    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private void parseMessage(String message) {

        ErrorCode errorCode = ErrorCode.find(message);
        this.code = errorCode.getCode();
        this.publicMessage = errorCode.getPublicMessage();
        this.message = errorCode.toString() + "#" + message;
        this.httpCode = errorCode.getHttpCode();

    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public Integer getHttpCode() {
        return httpCode;
    }
}
