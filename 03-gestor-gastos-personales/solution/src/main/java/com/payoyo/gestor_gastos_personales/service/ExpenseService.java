package com.payoyo.gestor_gastos_personales.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.payoyo.gestor_gastos_personales.entity.Expense;
import com.payoyo.gestor_gastos_personales.entity.enums.CategoryEnum;
import com.payoyo.gestor_gastos_personales.entity.enums.PaymentMethodEnum;

/**
 * Servicio que define la lógica de negocio para la gestión de gastos personales.
 * 
 * Esta interfaz establece el contrato de operaciones CRUD, filtros y reportes
 * que la aplicación debe proporcionar para el manejo de gastos.
 * 
 * @author Jose Luis (Payoyo)
 */
public interface ExpenseService {
    
    // ==================== OPERACIONES CRUD (5 métodos) ====================
    
    /**
     * Crea un nuevo gasto en el sistema.
     * Endpoint: POST /api/expenses
     * 
     * @param expense Objeto Expense con los datos del gasto a crear
     * @return Expense creado con su ID generado
     */
    Expense createExpense(Expense expense);
    
    /**
     * Obtiene todos los gastos registrados ordenados por fecha descendente.
     * Endpoint: GET /api/expenses
     * 
     * @return Lista de todos los gastos
     */
    List<Expense> getAllExpenses();
    
    /**
     * Obtiene un gasto por su ID.
     * Endpoint: GET /api/expenses/{id}
     * 
     * @param id ID del gasto a buscar
     * @return Expense encontrado
     * @throws com.payoyo.gestor_gastos_personales.exceptions.ExpenseNotFoundException si el ID no existe
     */
    Expense getExpenseById(Long id);
    
    /**
     * Actualiza un gasto existente.
     * Endpoint: PUT /api/expenses/{id}
     * 
     * @param id ID del gasto a actualizar
     * @param expense Objeto Expense con los nuevos datos
     * @return Expense actualizado
     * @throws com.payoyo.gestor_gastos_personales.exceptions.ExpenseNotFoundException si el ID no existe
     */
    Expense updateExpense(Long id, Expense expense);
    
    /**
     * Elimina un gasto del sistema.
     * Endpoint: DELETE /api/expenses/{id}
     * 
     * @param id ID del gasto a eliminar
     * @throws com.payoyo.gestor_gastos_personales.exceptions.ExpenseNotFoundException si el ID no existe
     */
    void deleteExpense(Long id);
    
    // ==================== FILTROS Y CONSULTAS (3 métodos) ====================
    
    /**
     * Obtiene todos los gastos de una categoría específica.
     * Endpoint: GET /api/expenses/category/{category}
     * 
     * @param category Categoría por la cual filtrar
     * @return Lista de gastos de la categoría especificada, ordenados por fecha descendente
     */
    List<Expense> getExpensesByCategory(CategoryEnum category);
    
    /**
     * Obtiene todos los gastos dentro de un rango de fechas.
     * Endpoint: GET /api/expenses/between?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * 
     * @param startDate Fecha de inicio del período (inclusive)
     * @param endDate Fecha de fin del período (inclusive)
     * @return Lista de gastos en el rango especificado, ordenados por fecha descendente
     */
    List<Expense> getExpensesByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Obtiene todos los gastos realizados con un método de pago específico.
     * Endpoint: GET /api/expenses/payment-method/{paymentMethod}
     * 
     * @param paymentMethod Método de pago por el cual filtrar
     * @return Lista de gastos del método de pago especificado, ordenados por fecha descendente
     */
    List<Expense> getExpensesByPaymentMethod(PaymentMethodEnum paymentMethod);
    
    // ==================== REPORTES (3 métodos) ====================
    
    /**
     * Genera un reporte con el total gastado por cada categoría.
     * Endpoint: GET /api/expenses/reports/by-category
     * 
     * @return Lista de Maps, cada uno con "category", "totalAmount" y "expenseCount"
     * 
     * Ejemplo de respuesta:
     * [
     *   {"category": "FOOD", "totalAmount": 325.50, "expenseCount": 12},
     *   {"category": "TRANSPORT", "totalAmount": 180.00, "expenseCount": 8}
     * ]
     */
    List<Map<String, Object>> getReportByCategory();
    
    /**
     * Genera un reporte del total gastado en un período específico.
     * Endpoint: GET /api/expenses/reports/period?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * 
     * @param startDate Fecha de inicio del período
     * @param endDate Fecha de fin del período
     * @return Map con "startDate", "endDate", "totalAmount", "expenseCount" y "averageExpense"
     * 
     * Ejemplo de respuesta:
     * {
     *   "startDate": "2024-11-01",
     *   "endDate": "2024-11-30",
     *   "totalAmount": 1250.75,
     *   "expenseCount": 45,
     *   "averageExpense": 27.79
     * }
     */
    Map<String, Object> getReportByPeriod(LocalDate startDate, LocalDate endDate);
    
    /**
     * Genera un reporte resumen del mes actual.
     * Endpoint: GET /api/expenses/reports/current-month
     * 
     * @return Map con "month", "year", "totalAmount", "expenseCount", 
     *         "mostExpensiveCategory" y "leastExpensiveCategory"
     * 
     * Ejemplo de respuesta:
     * {
     *   "month": "NOVEMBER",
     *   "year": 2024,
     *   "totalAmount": 856.30,
     *   "expenseCount": 28,
     *   "mostExpensiveCategory": "FOOD",
     *   "leastExpensiveCategory": "EDUCATION"
     * }
     */
    Map<String, Object> getCurrentMonthReport();
}
