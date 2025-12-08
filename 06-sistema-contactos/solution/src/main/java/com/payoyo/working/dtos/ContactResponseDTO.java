package com.payoyo.working.dtos;

import java.time.LocalDate;

import com.payoyo.working.entity.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de la API
 * - Incluye 'id' generado
 * - Constructor desde Entity facilita mapeo
 * - Usado en TODAS las respuestas (GET, POST, PATCH)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String notes;

    // Constructor desde Entity - facilita mapeo en Service
    public ContactResponseDTO(Contact contact) {
        this.id = contact.getId();
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.email = contact.getEmail();
        this.phone = contact.getPhone();
        this.address = contact.getAddress();
        this.birthDate = contact.getBirthDate();
        this.notes = contact.getNotes();
    }
}
