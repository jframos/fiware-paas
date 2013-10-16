package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * Exception thrown when the application deploy fails.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@SuppressWarnings("serial")
public class NotUniqueResultException extends Exception {

    public NotUniqueResultException() {
        super();
    }

    public NotUniqueResultException(Throwable e) {
        super(e);
    }
}
