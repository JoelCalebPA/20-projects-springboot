package com.payoyo.working.util;

/**
 * Constantes de mensajes de error y validación
 * Centraliza strings para evitar duplicación y facilitar i18n futuro
 */
public final class MessageConstants {

    // Constructor privado para evitar instanciación
    private MessageConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no debe ser instanciada");
    }

    // ==================== RUTAS API ====================
    public static final String API_BASE_PATH = "/api/contacts";
    public static final String PATH_BY_ID = "/{id}";
    public static final String PATH_BY_EMAIL = "/email/{email}";

    // ==================== MENSAJES DE ERROR ====================
    
    // Contacto no encontrado
    public static final String CONTACT_NOT_FOUND_ID = "Contacto no encontrado con ID: %d";
    public static final String CONTACT_NOT_FOUND_EMAIL = "Contacto no encontrado con email: %s";

    // Email duplicado
    public static final String EMAIL_ALREADY_EXISTS = "El email ya existe: %s";

    // ==================== MENSAJES DE LOG ====================
    
    // Operaciones CRUD
    public static final String CREATING_CONTACT = "Creando nuevo contacto con email: {}";
    public static final String CONTACT_CREATED = "Contacto creado exitosamente con ID: {}";
    public static final String UPDATING_CONTACT = "Actualizando contacto con ID: {}";
    public static final String CONTACT_UPDATED = "Contacto actualizado exitosamente con ID: {}";
    public static final String DELETING_CONTACT = "Eliminando contacto con ID: {}";
    public static final String CONTACT_DELETED = "Contacto eliminado exitosamente con ID: {}";
    
    // Peticiones HTTP
    public static final String REQUEST_RECEIVED_POST = "Petición POST recibida para crear contacto";
    public static final String REQUEST_RECEIVED_GET_ALL = "Petición GET recibida para obtener todos los contactos";
    public static final String REQUEST_RECEIVED_GET_ID = "Petición GET recibida para obtener contacto con ID: {}";
    public static final String REQUEST_RECEIVED_GET_EMAIL = "Petición GET recibida para buscar contacto con email: {}";
    public static final String REQUEST_RECEIVED_PATCH = "Petición PATCH recibida para actualizar contacto con ID: {}";
    public static final String REQUEST_RECEIVED_DELETE = "Petición DELETE recibida para eliminar contacto con ID: {}";
}