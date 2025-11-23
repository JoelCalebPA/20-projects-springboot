package com.payoyo.gestor_gastos_personales.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa la estructura estándar de respuesta de error.
 * 
 * Esta clase se utiliza para devolver información consistente y estructurada
 * sobre errores al cliente de la API, facilitando el manejo de errores
 * en el frontend.
 * 
 * @author Jose Luis (Payoyo)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    /**
     * Fecha y hora en que ocurrió el error.
     * Se formatea como "yyyy-MM-dd HH:mm:ss" en la respuesta JSON.
     * 
     * Ejemplo: "2024-11-19 14:30:45"
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP del error.
     * 
     * Ejemplos:
     * - 400: Bad Request
     * - 404: Not Found
     * - 500: Internal Server Error
     */
    private int status;
    
    /**
     * Mensaje descriptivo del error para el usuario.
     * Debe ser claro y útil para entender qué salió mal.
     * 
     * Ejemplo: "No se encontró el gasto con ID: 5"
     */
    private String message;
    
    /**
     * Nombre del tipo de error HTTP.
     * 
     * Ejemplos: "Not Found", "Bad Request", "Internal Server Error"
     */
    private String error;
}
