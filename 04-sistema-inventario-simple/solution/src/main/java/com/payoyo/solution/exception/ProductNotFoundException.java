package com.payoyo.solution.exception;

public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(Long id) {
        super("Producto con ID " + id + " no encontrado");
    }
    
    public ProductNotFoundException(String sku) {
        super("Producto con SKU '" + sku + "' no encontrado");
    }
}