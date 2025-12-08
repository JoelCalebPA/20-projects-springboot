package com.payoyo.working.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payoyo.working.entity.Contact;

/**
 * Repository para Contact
 * JpaRepository proporciona métodos CRUD básicos:
 * - save(), findById(), findAll(), deleteById(), etc.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
    
    /**
     * Buscar contacto por email (campo unico)
     * Spring Data genera automaticamente la query: SELECT * FROM contacts WHERE email = ?
     * @param email el email a buscar
     * @return Optional<Contact> - vacio si no existe
     */
    Optional<Contact> findByEmail(String email);

    /**
     * Verificar si existe un email en la DB
     * Más eficiente que findByEmail cuando solo necesitas un boolean
     * Spring Data genera: SELECT COUNT(*) > 0 FROM contacts WHERE email = ?
     * @param email el email a verificar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);
}
