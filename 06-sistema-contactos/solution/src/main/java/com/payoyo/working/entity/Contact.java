package com.payoyo.working.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad Contact para gestión de contactos personales
 */
@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString // util para debugging / logging
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column es opcional pero aqui es buana practica especificar constraints
    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "Apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    // unique = true crea constraint en DB para prevenir duplicados
    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe tener formato válido")
    private String email;

    // campos opcionales - pueden ser null
    @Column(length = 20)
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono debe contener entre 9 y 15 dígitos")
    private String phone;

    @Column(length = 200)
    @Size(max = 200, message = "Dirección no puede exceder 200 caracteres")
    private String address;

    // LocalDate: solo fecha, sin hora 
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate birthDate;

    @Column(length = 500)
    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notes;

}
