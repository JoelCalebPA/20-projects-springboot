package com.payoyo.working.services;

import java.util.List;

import com.payoyo.working.dtos.ContactCreateDTO;
import com.payoyo.working.dtos.ContactResponseDTO;
import com.payoyo.working.dtos.ContactUpdateDTO;
import com.payoyo.working.exceptions.DuplicateEmailException;
import com.payoyo.working.exceptions.ResourceNotFoundException;

/**
 * Interface del servicio de Contact
 * Define el contrato de operaciones de negocio
 */
public interface ContactService {
   
    /**
     * Crear nuevo contacto
     * @param dto datos del contacto a crear
     * @return DTO con el contacto creado
     * @throws DuplicateEmailException si el email ya existe
     */
    ContactResponseDTO createContact(ContactCreateDTO dto);

    /**
     * Obtener todos los contactos
     * @return lista de contactos
     */
    List<ContactResponseDTO> getAllContacts();

    /**
     * Obtener contacto por ID
     * @param id identificador del contacto
     * @return DTO del contacto encontrado
     * @throws ResourceNotFoundException si no existe el ID
     */
    ContactResponseDTO getContactById(Long id);

    /**
     * Buscar contacto por email
     * @param email email a buscar
     * @return DTO del contacto encontrado
     * @throws ResourceNotFoundException si no existe el email
     */
    ContactResponseDTO findByEmail(String email);

    /**
     * Actualizar contacto (PATCH - solo campos presentes)
     * @param id identificador del contacto
     * @param dto datos a actualizar
     * @return DTO con el contacto actualizado
     * @throws ResourceNotFoundException si no existe el ID
     */
    ContactResponseDTO updateContact(Long id, ContactUpdateDTO dto);

    /**
     * Eliminar contacto por ID
     * @param id identificador del contacto
     * @throws ResourceNotFoundException si no existe el ID
     */
    void deleteContact(Long id);
}
