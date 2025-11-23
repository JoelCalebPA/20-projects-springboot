package com.payoyo.gestor_gastos_personales.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payoyo.gestor_gastos_personales.entity.Expense;
import com.payoyo.gestor_gastos_personales.entity.enums.CategoryEnum;
import com.payoyo.gestor_gastos_personales.entity.enums.PaymentMethodEnum;
import com.payoyo.gestor_gastos_personales.exceptions.ExpenseNotFoundException;
import com.payoyo.gestor_gastos_personales.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de gastos personales.
 * 
 * Expone los endpoints de la API para operaciones CRUD, filtros y reportes
 * de gastos personales. Todas las rutas comienzan con /api/expenses.
 * 
 * @author Jose Luis (Payoyo)
 */
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // ==================== OPERACIONES CRUD ====================

    /**
     * Crea un nuevo gasto.
     * Endpoint: POST /api/expenses
     * 
     * @param expense Datos del gasto a crear (validados con @Valid)
     * @return ResponseEntity con el gasto creado y código 201 (Created)
     * 
     * Ejemplo de body:
     * {
     *   "description": "Almuerzo en restaurante",
     *   "amount": 25.50,
     *   "category": "FOOD",
     *   "date": "2024-11-19",
     *   "paymentMethod": "CREDIT_CARD"
     * }
     */
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }

    /**
     * Obtiene todos los gastos ordenados por fecha descendente.
     * Endpoint: GET /api/expenses
     * 
     * @return ResponseEntity con lista de todos los gastos y código 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    /**
     * Obtiene un gasto por su ID.
     * Endpoint: GET /api/expenses/{id}
     * 
     * @param id ID del gasto a buscar
     * @return ResponseEntity con el gasto encontrado y código 200 (OK)
     * @throws ExpenseNotFoundException si el ID no existe (manejado por GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    /**
     * Actualiza un gasto existente.
     * Endpoint: PUT /api/expenses/{id}
     * 
     * @param id ID del gasto a actualizar
     * @param expense Nuevos datos del gasto (validados con @Valid)
     * @return ResponseEntity con el gasto actualizado y código 200 (OK)
     * @throws ExpenseNotFoundException si el ID no existe (manejado por GlobalExceptionHandler)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updatedExpense);
    }

    /**
     * Elimina un gasto.
     * Endpoint: DELETE /api/expenses/{id}
     * 
     * @param id ID del gasto a eliminar
     * @return ResponseEntity vacío con código 204 (No Content)
     * @throws ExpenseNotFoundException si el ID no existe (manejado por GlobalExceptionHandler)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== FILTROS Y CONSULTAS ====================

    /**
     * Filtra gastos por categoría.
     * Endpoint: GET /api/expenses/category/{category}
     * 
     * @param category Categoría para filtrar (FOOD, TRANSPORT, etc.)
     * @return ResponseEntity con lista de gastos de la categoría y código 200 (OK)
     * 
     * Ejemplo: GET /api/expenses/category/FOOD
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable CategoryEnum category) {
        List<Expense> expenses = expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Filtra gastos por rango de fechas.
     * Endpoint: GET /api/expenses/between?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * 
     * @param startDate Fecha de inicio (formato: YYYY-MM-DD)
     * @param endDate Fecha de fin (formato: YYYY-MM-DD)
     * @return ResponseEntity con lista de gastos en el rango y código 200 (OK)
     * 
     * Ejemplo: GET /api/expenses/between?startDate=2024-11-01&endDate=2024-11-30
     * 
     * @DateTimeFormat asegura que Spring parsee correctamente las fechas
     */
    @GetMapping("/between")
    public ResponseEntity<List<Expense>> getExpensesByDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Expense> expenses = expenseService.getExpensesByDateBetween(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Filtra gastos por método de pago.
     * Endpoint: GET /api/expenses/payment-method/{paymentMethod}
     * 
     * @param paymentMethod Método de pago para filtrar (CASH, CREDIT_CARD, etc.)
     * @return ResponseEntity con lista de gastos del método de pago y código 200 (OK)
     * 
     * Ejemplo: GET /api/expenses/payment-method/CREDIT_CARD
     */
    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<Expense>> getExpensesByPaymentMethod(
            @PathVariable PaymentMethodEnum paymentMethod) {
        List<Expense> expenses = expenseService.getExpensesByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(expenses);
    }

    // ==================== REPORTES ====================

    /**
     * Genera reporte de gastos agrupados por categoría.
     * Endpoint: GET /api/expenses/reports/by-category
     * 
     * @return ResponseEntity con lista de Maps (categoría, total, cantidad) y código 200 (OK)
     * 
     * Respuesta ejemplo:
     * [
     *   {"category": "FOOD", "totalAmount": 325.50, "expenseCount": 12},
     *   {"category": "TRANSPORT", "totalAmount": 180.00, "expenseCount": 8}
     * ]
     */
    @GetMapping("/reports/by-category")
    public ResponseEntity<List<Map<String, Object>>> getReportByCategory() {
        List<Map<String, Object>> report = expenseService.getReportByCategory();
        return ResponseEntity.ok(report);
    }

    /**
     * Genera reporte de gastos en un período específico.
     * Endpoint: GET /api/expenses/reports/period?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * 
     * @param startDate Fecha de inicio del período
     * @param endDate Fecha de fin del período
     * @return ResponseEntity con Map de estadísticas del período y código 200 (OK)
     * 
     * Ejemplo: GET /api/expenses/reports/period?startDate=2024-11-01&endDate=2024-11-30
     * 
     * Respuesta ejemplo:
     * {
     *   "startDate": "2024-11-01",
     *   "endDate": "2024-11-30",
     *   "totalAmount": 1250.75,
     *   "expenseCount": 45,
     *   "averageExpense": 27.79
     * }
     */
    @GetMapping("/reports/period")
    public ResponseEntity<Map<String, Object>> getReportByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = expenseService.getReportByPeriod(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * Genera reporte del mes actual.
     * Endpoint: GET /api/expenses/reports/current-month
     * 
     * @return ResponseEntity con Map de estadísticas del mes actual y código 200 (OK)
     * 
     * Respuesta ejemplo:
     * {
     *   "month": "NOVEMBER",
     *   "year": 2024,
     *   "totalAmount": 856.30,
     *   "expenseCount": 28,
     *   "mostExpensiveCategory": "FOOD",
     *   "leastExpensiveCategory": "EDUCATION"
     * }
     */
    @GetMapping("/reports/current-month")
    public ResponseEntity<Map<String, Object>> getCurrentMonthReport() {
        Map<String, Object> report = expenseService.getCurrentMonthReport();
        return ResponseEntity.ok(report);
    }
    
}
