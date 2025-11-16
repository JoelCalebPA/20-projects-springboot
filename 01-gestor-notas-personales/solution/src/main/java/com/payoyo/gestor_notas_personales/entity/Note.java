package com.payoyo.gestor_notas_personales.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Entidad que representa una nota personal.
 * 
 * Buenas prácticas aplicadas:
 * - Validaciones a nivel de entidad y base de datos
 * - Timestamps automáticos con Hibernate
 * - Comparación de entidades solo por ID
 */
@Entity // Marca esta clase como entidad JPA que se mapea a una tabla
@Table(name = "notes") // Nombre de la tabla en la base de datos
@Getter // Lombok: genera getters automáticamente
@Setter // Lombok: genera setters automáticamente
@NoArgsConstructor // Lombok: constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Lombok: constructor con todos los argumentos
@ToString // Lombok: genera método toString() para debugging
@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
// Solo compara entidades por ID (best practice en JPA)
// Evita problemas con lazy loading y es más eficiente
public class Note {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento en BD
    @EqualsAndHashCode.Include // Este campo SÍ se usa para equals/hashCode
    private Long id;

    /*
     * Título de la nota.
     * 
     * @NotBlank: No puede ser null, vacío o solo espacios
     * @Size(max=255): Máximo 255 caracteres
     * nullable=false: Constraint a nivel de base de datos (doble validación)
     */
    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres")
    @Column(nullable = false)
    private String title;

    /*
     * Contenido de la nota.
     * 
     * length=1000: Define VARCHAR(1000) en BD (coherente con @Size)
     */
    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 1000, message = "El contenido no puede exceder los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String content;

    /*
     * Fecha de creación automática.
     * 
     * @CreationTimestamp: Hibernate asigna la fecha al crear (solo INSERT)
     * updatable=false: Este campo NUNCA se actualiza (inmutable)
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /*
     * Fecha de última modificación automática.
     * 
     * @UpdateTimestamp: Hibernate actualiza la fecha en cada INSERT y UPDATE
     * NO tiene updatable=false para que se actualice en cada modificación
     */
    @UpdateTimestamp
    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

}
