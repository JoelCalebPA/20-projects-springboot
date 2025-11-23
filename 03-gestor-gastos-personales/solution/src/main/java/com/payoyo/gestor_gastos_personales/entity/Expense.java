package com.payoyo.gestor_gastos_personales.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.payoyo.gestor_gastos_personales.entity.enums.CategoryEnum;
import com.payoyo.gestor_gastos_personales.entity.enums.PaymentMethodEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Entidad que representa un gasto personal en el sistema
 * 
 * Esta entidad almacena informacion detallada sobre cada gasto realizado,
 * incluyendo descripcion, cantidad, categoria, fecha y metodo de pago.
 * Tambien mantiene timestamps atomaticos de creacion y actualizacion
 * 
 * @author Jose Luis (Payoyo)
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    
    /*
     * Identificador unico del gasto
     * Se genera automaticamente mediante estrategia IDENTITY (auto-incremento)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Descripción textual del gasto.
     * 
     * Validaciones:
     * - No puede ser nulo ni vacío (@NotBlank)
     * - Debe tener entre 3 y 200 caracteres
     * 
     * Ejemplo: "Almuerzo en restaurante", "Gasolina del coche"
     */
    @NotBlank(message = "La descripcion del gasto es obligatoria")
    @Size(min = 3, max = 200, message = "La descripcion del gasto debe contener entre 3 y 200 caracteres")
    @Column(nullable = false)
    private String description;

    /*
     * Cantidad del gasto.
     * 
     * Se usa BigDecimal en lugar de double o float para evitar problemas
     * de precisión en operaciones con números decimales (crítico en finanzas).
     * 
     * Validaciones:
     * - No puede ser nulo
     * - Debe ser mayor que 0 (@DecimalMin)
     * - Máximo 10 dígitos enteros y 2 decimales (@Digits)
     * 
     * Ejemplo: 25.50, 1250.99
     */
    @NotNull(message = "La cantidad del gasto es obligatoria")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "La cantidad debe tener máximo 10 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 12, scale = 2) // presion=12 permite 10 enteros + 2 decimales
    private BigDecimal amount;

    /*
     * Categoría a la que pertenece el gasto.
     * 
     * Se almacena como STRING en la base de datos para mayor legibilidad
     * y para evitar problemas si se reordenan los valores del enum.
     * 
     * IMPORTANTE: Usar @NotNull (NO @NotBlank) porque es un enum, no un String.
     * 
     * Valores posibles: FOOD, TRANSPORT, ENTERTAINMENT, HEALTH, etc.
     */
    @NotNull(message = "La categoria del gasto es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum category;

    /*
     * Fecha en la que se realizó el gasto.
     * 
     * Se usa LocalDate (Java 8+) en lugar de Date o Calendar por ser:
     * - Inmutable (thread-safe)
     * - Más fácil de usar
     * - Parte de la API moderna de Java
     * 
     * Validaciones:
     * - No puede ser nulo
     * - No puede ser una fecha futura (@PastOrPresent)
     * 
     * Ejemplo: 2024-11-18
     */
    @NotNull(message = "La fecha del gasto es obligatoria")
    @PastOrPresent(message = "La fecha del gasto no puede ser futura")
    @Column(nullable = false)
    private LocalDate date;

    /*
     * Método de pago utilizado para realizar el gasto.
     * 
     * Se almacena como STRING para mantener legibilidad en la base de datos.
     * 
     * IMPORTANTE: Usar @NotNull (NO @NotBlank) porque es un enum, no un String.
     * 
     * Valores posibles: CASH, DEBIT_CARD, CREDIT_CARD, BANK_TRANSFER, DIGITAL_WALLET
     */
    @NotNull(message = "La categoria del gasto es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodEnum paymentMethod;

    /*
     * Timestamp de creación del registro.
     * 
     * Se establece automáticamente por Hibernate cuando se persiste la entidad.
     * Es inmutable (updatable = false) para mantener integridad del histórico.
     * 
     * Útil para auditoría y trazabilidad.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /*
     * Timestamp de última actualización del registro.
     * 
     * Se actualiza automáticamente por Hibernate cada vez que se modifica la entidad.
     * 
     * Útil para auditoría y trazabilidad.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
