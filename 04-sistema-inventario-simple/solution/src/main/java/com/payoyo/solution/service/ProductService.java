package com.payoyo.solution.service;

import java.math.BigDecimal;
import java.util.List;

import com.payoyo.solution.entity.Product;

public interface ProductService {
    
    Product createProduct(Product product);
    
    List<Product> getAllProducts();
    
    Product getProductById(Long id);
    
    Product getProductBySku(String sku);
    
    Product updateProduct(Long id, Product product);
    
    void deleteProduct(Long id);
    
    Product entradaStock(Long id, Integer cantidad);
    
    Product salidaStock(Long id, Integer cantidad);
    
    List<Product> getProductsWithLowStock();
    
    List<Product> searchByName(String name);
    
    List<Product> filterByPriceRange(BigDecimal min, BigDecimal max);
}