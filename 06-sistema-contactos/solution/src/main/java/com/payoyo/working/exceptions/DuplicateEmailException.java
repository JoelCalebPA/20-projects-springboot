package com.payoyo.working.exceptions;

/**
 * Excepción lanzada cuando se intenta crear un contacto con email duplicado
 * Será capturada por @ControllerAdvice y retornará 409 Conflict
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }

    // Constructor alternativo con causa
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
