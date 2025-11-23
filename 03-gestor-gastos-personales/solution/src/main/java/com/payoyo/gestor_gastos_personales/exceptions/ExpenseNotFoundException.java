package com.payoyo.gestor_gastos_personales.exceptions;

/**
 * Excepción personalizada lanzada cuando no se encuentra un gasto.
 * 
 * Extiende RuntimeException para que sea una excepción no verificada (unchecked),
 * permitiendo un manejo de errores más limpio sin obligar a capturarla explícitamente.
 * 
 * @author Jose Luis (Payoyo)
 */
public class ExpenseNotFoundException extends RuntimeException{

    /**
     * Constructor que crea la excepción con un mensaje formateado usando el ID.
     * 
     * @param id -> ID del gasto que no fue encontrado
     * 
     * Ejemplo: new ExpenseNotFoundException(5L)
     * Genera: "No se encontró el gasto con ID: 5"
     */
    public ExpenseNotFoundException(Long id) {
        super(String.format("No se encontró el gasto con ID: %d", id));
    }

    /**
     * Constructor que crea la excepción con un mensaje personalizado.
     * Útil para errores más específicos o contextos particulares.
     * 
     * @param message Mensaje de error personalizado
     * 
     * Ejemplo: new ExpenseNotFoundException("No se encontraron gastos para ese periodo")
     */
    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
