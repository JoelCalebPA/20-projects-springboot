package com.payoyo.working.exceptions;

/**
 * Excepción lanzada cuando un contacto no se encuentra
 * Será capturada por @ControllerAdvice y retornará 404
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor alternativo con causa
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
