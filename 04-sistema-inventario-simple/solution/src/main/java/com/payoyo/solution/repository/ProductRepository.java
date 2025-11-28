package com.payoyo.solution.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.payoyo.solution.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);
    
    List<Product> findAllByOrderByUpdatedAtDesc();
    
    @Query("SELECT p FROM Product p WHERE p.cantidad < p.stockMinimo ORDER BY (p.stockMinimo - p.cantidad) DESC")
    List<Product> findProductsWithLowStock();
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByPrecioBetween(BigDecimal min, BigDecimal max);

}
