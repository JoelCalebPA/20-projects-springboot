package com.payoyo.working.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo contacto
 * - Sin campo 'id' (se genera automáticamente)
 * - Validaciones estrictas en campos obligatorios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreateDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "Apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe tener formato válido")
    private String email;

    // Campos opcionales - validación solo aplica si se proporciona valor
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono debe contener entre 9 y 15 dígitos")
    private String phone;

    @Size(max = 200, message = "Dirección no puede exceder 200 caracteres")
    private String address;

    // @Past valida solo si birthDate no es null
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate birthDate;

    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notes;

    // Constructor con campos obligatorios (útil para testing)
    public ContactCreateDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
