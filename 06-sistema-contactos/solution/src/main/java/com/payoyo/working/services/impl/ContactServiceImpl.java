package com.payoyo.working.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payoyo.working.dtos.ContactCreateDTO;
import com.payoyo.working.dtos.ContactResponseDTO;
import com.payoyo.working.dtos.ContactUpdateDTO;
import com.payoyo.working.entity.Contact;
import com.payoyo.working.exceptions.DuplicateEmailException;
import com.payoyo.working.exceptions.ResourceNotFoundException;
import com.payoyo.working.mapper.ContactMapper;
import com.payoyo.working.repository.ContactRepository;
import com.payoyo.working.services.ContactService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.payoyo.working.util.MessageConstants.*;

/**
 * Implementación del servicio de Contact
 * - Validaciones (email único)
 * - Usa ContactMapper para transformaciones DTO ↔ Entity
 * - Lanza excepciones personalizadas
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // transacciones de solo lectura por defecto
public class ContactServiceImpl implements ContactService{

    private final ContactRepository repository;
    private final ContactMapper mapper;

    /**
     * Crear nuevo contacto
     * @throws DuplicateEmailException si el email ya existe
     */
    @Override
    @Transactional
    public ContactResponseDTO createContact(ContactCreateDTO dto) {
        log.info(CREATING_CONTACT, dto.getEmail());

        validateEmailNotExists(dto.getEmail());

        Contact contactToSave = mapper.toEntity(dto);
        Contact savedContact = repository.save(contactToSave);

        log.info(CONTACT_CREATED, savedContact.getId());
        return mapper.toResponseDTO(savedContact);
    }

    @Override
    public List<ContactResponseDTO> getAllContacts() {
        log.debug("Obteniendo todos los contactos");
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponseDTO getContactById(Long id) {
        log.debug("Buscando contacto con ID: {}", id);
        Contact contact = findContactByIdOrThrow(id);
        return mapper.toResponseDTO(contact);
    }

    @Override
    public ContactResponseDTO findByEmail(String email) {
        log.debug("Buscando contacto con email: {}", email);
        Contact contact = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CONTACT_NOT_FOUND_EMAIL, email)));
        return mapper.toResponseDTO(contact);
    }

    @Override
    @Transactional // Transacción de escritura
    public ContactResponseDTO updateContact(Long id, ContactUpdateDTO dto) {
        log.info(UPDATING_CONTACT, id);

        Contact existingContact = findContactByIdOrThrow(id);
        mapper.updateEntityFromDTO(existingContact, dto);
        Contact updatedContact = repository.save(existingContact);

        log.info(CONTACT_UPDATED, updatedContact.getId());
        return mapper.toResponseDTO(updatedContact);
    }

    @Override
    @Transactional // Transacción de escritura
    public void deleteContact(Long id) {
        log.info(DELETING_CONTACT, id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND_ID, id));
        }

        repository.deleteById(id);
        log.info(CONTACT_DELETED, id);
    }

    /**
     * Método privado para validar email único
     * Extrae lógica de validación para mejor legibilidad
     */
    private void validateEmailNotExists(String email) {
        if (repository.existsByEmail(email)) {
            log.warn("Intento de crear contacto con email duplicado: {}", email);
            throw new DuplicateEmailException(String.format(EMAIL_ALREADY_EXISTS, email));
        }
    }

    /**
     * Método privado para buscar contacto por ID o lanzar excepción
     * Reutiliza lógica común
     */
    private Contact findContactByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CONTACT_NOT_FOUND_ID, id)));
    }
    
}
