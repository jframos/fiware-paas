/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.exception;

public enum ErrorCode {

    DB_CONNECTION(20, "Could not open connection to database", "(.*)JDBCConnectionException(.*)"),
    HIBERNATE(10, "Problem in database backend", "(.*)org.hibernate(.*)"),
    ENTITY_NOT_FOUND(30, "Entity not found", "(.*)EntityNotFoundException(.*)"),
    DEFAULT(500, "Internal PaasManager Server Error", "(.*)");

    private final int code;
    private final String publicMessage;
    private final String pattern;

    private ErrorCode(int code, String publicMessage, String pattern) {
        this.code = code;
        this.publicMessage = publicMessage;
        this.pattern = pattern;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + publicMessage;
    }

    public static ErrorCode find(String value) {

        ErrorCode[] errors = ErrorCode.values();
        int i = 0;
        while (i < errors.length && !value.matches(errors[i].getPattern())) {
            i++;
        }
        return errors[i];
    }

    public String getPattern() {
        return pattern;
    }
}
