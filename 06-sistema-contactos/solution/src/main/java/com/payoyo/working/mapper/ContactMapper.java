package com.payoyo.working.mapper;

import org.springframework.stereotype.Component;

import com.payoyo.working.dtos.ContactCreateDTO;
import com.payoyo.working.dtos.ContactResponseDTO;
import com.payoyo.working.dtos.ContactUpdateDTO;
import com.payoyo.working.entity.Contact;

/**
 * Mapper para transformaciones entre DTOs y Entity Contact
 * Responsabilidad única: mapeo de objetos
 * Sin lógica de negocio
 */
@Component
public class ContactMapper {

    /**
     * Convierte ContactCreateDTO a Contact Entity
     * Usado en creación de nuevos contactos
     */
    public Contact toEntity(ContactCreateDTO dto) {
        Contact contact = new Contact();
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setAddress(dto.getAddress());
        contact.setBirthDate(dto.getBirthDate());
        contact.setNotes(dto.getNotes());
        return contact;
    }

    /**
     * Convierte Contact Entity a ContactResponseDTO
     * Usado en todas las respuestas de la API
     */
    public ContactResponseDTO toResponseDTO(Contact contact) {
        return new ContactResponseDTO(contact);
    }

    /**
     * Actualiza Contact Entity con datos de ContactUpdateDTO
     * Implementa patrón PATCH: solo actualiza campos != null
     * 
     * @param target Entity existente a actualizar (modificado in-place)
     * @param source DTO con campos a actualizar
     */
    public void updateEntityFromDTO(Contact target, ContactUpdateDTO source) {
        if (source.getFirstName() != null) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getLastName() != null) {
            target.setLastName(source.getLastName());
        }
        if (source.getPhone() != null) {
            target.setPhone(source.getPhone());
        }
        if (source.getAddress() != null) {
            target.setAddress(source.getAddress());
        }
        if (source.getBirthDate() != null) {
            target.setBirthDate(source.getBirthDate());
        }
        if (source.getNotes() != null) {
            target.setNotes(source.getNotes());
        }
    }
}
