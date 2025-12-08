package com.payoyo.working.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payoyo.working.dtos.ContactCreateDTO;
import com.payoyo.working.dtos.ContactResponseDTO;
import com.payoyo.working.dtos.ContactUpdateDTO;
import com.payoyo.working.exceptions.ResourceNotFoundException;
import com.payoyo.working.services.ContactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.payoyo.working.util.MessageConstants.*;

import java.util.List;

/**
 * REST Controller para la gestion de contactos
 * Endpoins: CRUD completo + busqueda por email
 * 
 * Vase path: /api/contacts
 */
@Slf4j
@RestController
@RequestMapping(API_BASE_PATH)
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactService service;

    /**
     * Crear nuevo contacto
     * POST /api/contacts
     * 
     * @param dto datos del contacto a crear
     * @return 201 Created + ContactResponseDTO
     */
    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(
        @Valid @RequestBody ContactCreateDTO dto
    ){
        log.info(REQUEST_RECEIVED_POST);
        ContactResponseDTO created = service.createContact(dto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
    }

    /**
     * Obtener todos los contactos
     * GET /api/contacts
     * 
     * @return 200 OK + List<ContactResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getAllContacts() {
        log.info(REQUEST_RECEIVED_GET_ALL);
        List<ContactResponseDTO> contacts = service.getAllContacts();
        
        return ResponseEntity.ok(contacts);
    }

    /**
     * Obtener contacto por ID
     * GET /api/contacts/{id}
     * 
     * @param id identificador del contacto
     * @return 200 OK + ContactResponseDTO
     * @throws ResourceNotFoundException si no existe el ID (retorna 404)
     */
    @GetMapping(PATH_BY_ID)
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long id) {
        log.info(REQUEST_RECEIVED_GET_ID, id);
        ContactResponseDTO contact = service.getContactById(id);
        
        return ResponseEntity.ok(contact);
    }

    /**
     * Buscar contacto por email
     * GET /api/contacts/email/{email}
     * 
     * @param email email a buscar
     * @return 200 OK + ContactResponseDTO
     * @throws ResourceNotFoundException si no existe el email (retorna 404)
     */
    @GetMapping(PATH_BY_EMAIL)
    public ResponseEntity<ContactResponseDTO> getContactByEmail(@PathVariable String email) {
        log.info(REQUEST_RECEIVED_GET_EMAIL, email);
        ContactResponseDTO contact = service.findByEmail(email);
        
        return ResponseEntity.ok(contact);
    }

    /**
     * Actualizar contacto (PATCH - actualización parcial)
     * PATCH /api/contacts/{id}
     * Solo actualiza campos presentes en el DTO
     * 
     * @param id identificador del contacto
     * @param dto datos a actualizar
     * @return 200 OK + ContactResponseDTO actualizado
     * @throws ResourceNotFoundException si no existe el ID (retorna 404)
     */
    @PatchMapping(PATH_BY_ID)
    public ResponseEntity<ContactResponseDTO> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactUpdateDTO dto) {
        
        log.info(REQUEST_RECEIVED_PATCH, id);
        ContactResponseDTO updated = service.updateContact(id, dto);
        
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar contacto
     * DELETE /api/contacts/{id}
     * 
     * @param id identificador del contacto
     * @return 204 No Content
     * @throws ResourceNotFoundException si no existe el ID (retorna 404)
     */
    @DeleteMapping(PATH_BY_ID)
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info(REQUEST_RECEIVED_DELETE, id);
        service.deleteContact(id);
        
        return ResponseEntity.noContent().build();
    }
}
