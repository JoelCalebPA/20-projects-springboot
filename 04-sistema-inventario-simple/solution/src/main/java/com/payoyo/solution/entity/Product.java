package com.payoyo.solution.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe contener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 500, message = "La descripcion debe contener 500 caracteres maximo")
    @Column(length = 500)
    private String description;

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El stock minimo es obligatorio")
    @PositiveOrZero(message = "El stock minimo no puede ser negativo")
    @Column(nullable = false, name = "stock_minimo")
    private Integer stockMinimo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /*
    * SKU (Stock Keeping Unit) - Código único de identificación del producto.
    */
   @NotBlank(message = "El SKU es obligatorio")
   @Pattern(
    regexp = "^[A-Z]{3}-[0-9]{4}",
    message = "El SKU debe tener el formato: 3 letras mayúsculas, guión, 4 números (ejemplo: PRD-0001)"
   )
   @Column(
    name = "sku",
    nullable = false,
    unique = true,
    length = 8
   )
   private String sku;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =========== MÉTODOS DE NEGOCIO ==========
    /**
     * Verificamos si el producto necesita reabastecimiento
     * @return true si cantidad < stockMinimo
     */
    public boolean requiereReabastecimiento(){
        return this.cantidad < this.stockMinimo;
    }

    /**
     * Incrementa el stock del producto (entrada de mercancia)
     * 
     * @param cantidadEntrada cantidad a añadir (debe ser > 0)
     * @throws IllegalArgumentException si la cntidad es invalida
     */
    public void incrementarStock(Integer cantidadEntrada) {
        if (cantidadEntrada == null || cantidadEntrada <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.cantidad += cantidadEntrada;
    }

    /**
     * Decrementa el stock del producto (salida de mercancía).
     * 
     * @param cantidadSalida cantidad a restar (debe ser > 0)
     * @throws IllegalArgumentException si la cantidad es inválida o excede el stock
     */
    public void decrementarStock(Integer cantidadSalida) {
        if (cantidadSalida == null || cantidadSalida <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (this.cantidad < cantidadSalida) {
            throw new IllegalArgumentException(
                String.format("Stock insuficiente. Stock actual: %d, Cantidad solicitada: %d",
                    this.cantidad, cantidadSalida)
            );
        }
        this.cantidad -= cantidadSalida;
    }

}
