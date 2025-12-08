package com.payoyo.working.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualización parcial de contacto (PATCH)
 * - Todos los campos son opcionales (nullable)
 * - Solo se actualizan campos presentes (no null) en el request
 * - NO incluye 'id' (viene en URL) ni 'email' (no se puede cambiar)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateDTO {
    
    // Todos los campos opcionales - validaciones solo aplican si se envían
    
    @Size(min = 2, max = 50, message = "Nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @Size(min = 2, max = 50, message = "Apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono debe contener entre 9 y 15 dígitos")
    private String phone;

    @Size(max = 200, message = "Dirección no puede exceder 200 caracteres")
    private String address;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate birthDate;

    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notes;
    
}
