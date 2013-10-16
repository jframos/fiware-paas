package com.telefonica.euro_iaas.paasmanager.exception;

/**
 * Exception thrown when trying to Insert a ProductRelease that already exists.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }

    public EntityNotFoundException(Throwable e) {
        super(e);
    }

    public EntityNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

}
