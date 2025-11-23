package com.payoyo.gestor_gastos_personales.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para toda la aplicación.
 * 
 * Esta clase intercepta excepciones lanzadas en cualquier controlador
 * y las transforma en respuestas HTTP estructuradas y consistentes.
 * 
 * @RestControllerAdvice combina @ControllerAdvice (intercepta excepciones)
 * con @ResponseBody (devuelve respuestas JSON automáticamente).
 * 
 * @author Jose Luis (Payoyo)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Maneja ExpenseNotFoundException cuando un gasto no se encuentra.
     * Devuelve una respuesta HTTP 404 (Not Found) con detalles del error.
     * 
     * @param ex -> Excepción capturada
     * @return ResponseEntity con ErrorResponse y status 404
     * 
     * Ejemplo de respuesta:
     * {
     *   "timestamp": "2024-11-19 14:30:45",
     *   "status": 404,
     *   "message": "No se encontró el gasto con ID: 5",
     *   "error": "Not Found"
     * }
     */
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExpenseNotFound(ExpenseNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .error("Not Found")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja errores de validación de Bean Validation (@NotNull, @Size, etc.).
     * Devuelve una respuesta HTTP 400 (Bad Request) con todos los errores de validación.
     * 
     * @param ex Excepción capturada con los errores de validación
     * @return ResponseEntity con Map de errores y status 400
     * 
     * Ejemplo de respuesta:
     * {
     *   "timestamp": "2024-11-19 14:30:45",
     *   "status": 400,
     *   "error": "Validation failed",
     *   "errors": {
     *     "amount": "El monto debe ser mayor que 0",
     *     "description": "La descripción del gasto es obligatoria"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Mapa que almacena todos los errores de validación (campo -> mensaje)
        Map<String, String> errors = new HashMap<>();

        // Itera sobre todos los errores de validación y los agrega al mapa
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Construye la respuesta completa con metadata y los errores
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja cualquier excepción no capturada por otros handlers.
     * Actúa como red de seguridad para errores inesperados.
     * Devuelve una respuesta HTTP 500 (Internal Server Error).
     * 
     * @param ex Excepción capturada
     * @return ResponseEntity con ErrorResponse y status 500
     * 
     * Nota: En producción, evita exponer detalles internos del error.
     * 
     * Ejemplo de respuesta:
     * {
     *   "timestamp": "2024-11-19 14:30:45",
     *   "status": 500,
     *   "error": "Internal Server Error",
     *   "message": "Ocurrió un error inesperado. Por favor, inténtelo de nuevo más tarde"
     * }
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ocurrió un error inesperado. Por favor, inténtelo de nuevo más tarde")
                .build();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
