package com.payoyo.solution.exception;

public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(Integer stockActual, Integer cantidadSolicitada) {
        super(String.format("Stock insuficiente. Stock actual: %d, Cantidad solicitada: %d", 
            stockActual, cantidadSolicitada));
    }
}