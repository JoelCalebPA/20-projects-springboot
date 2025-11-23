package com.payoyo.gestor_gastos_personales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payoyo.gestor_gastos_personales.entity.Expense;
import java.util.List;
import com.payoyo.gestor_gastos_personales.entity.enums.CategoryEnum;
import com.payoyo.gestor_gastos_personales.entity.enums.PaymentMethodEnum;
import java.time.LocalDate;

/**
 * Repositorio para la gestion de datos personales
 * 
 * Proporciona métodos de consulta derivados (Query Methods) para filtrar
 * y recuperar gastos según diferentes criterios como categoría, método de pago
 * y rangos de fechas.
 * 
 * Spring Data JPA genera automáticamente la implementación de estos métodos
 * basándose en sus nombres siguiendo las convenciones de nomenclatura.
 * 
 * @author Jose Luis (Payoyo)
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    
    /**
     * Encuentra todos los gastos de una categoría específica.
     * Ordena los resultados por fecha de forma descendente (más recientes primero).
     * 
     * @param category -> Categoría por la cual filtrar los gastos
     * @return Lista de gastos de la categoría especificada, ordenados por fecha descendente
     * 
     * Ejemplo: findByCategory(CategoryEnum.FOOD)
     * SQL generado: SELECT * FROM expenses WHERE category = ? ORDER BY date DESC
     */
    List<Expense> findByCategoryOrderByDateDesc(CategoryEnum category);

    /**
     * Encuentra todos los gastos realizados con un método de pago específico.
     * Ordena los resultados por fecha de forma descendente (más recientes primero).
     * 
     * @param paymentMethod Método de pago por el cual filtrar los gastos
     * @return Lista de gastos del método de pago especificado, ordenados por fecha descendente
     * 
     * Ejemplo: findByPaymentMethod(PaymentMethodEnum.CREDIT_CARD)
     * SQL generado: SELECT * FROM expenses WHERE payment_method = ? ORDER BY date DESC
     */
    List<Expense> findByPaymentMethodOrderByDateDesc(PaymentMethodEnum paymentMethod);

    /**
     * Encuentra todos los gastos dentro de un rango de fechas (inclusive).
     * Ordena los resultados por fecha de forma descendente (más recientes primero).
     * 
     * @param startDate Fecha de inicio del rango (inclusive)
     * @param endDate Fecha de fin del rango (inclusive)
     * @return Lista de gastos en el rango de fechas especificado, ordenados por fecha descendente
     * 
     * Ejemplo: findByDateBetween(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30))
     * SQL generado: SELECT * FROM expenses WHERE date BETWEEN ? AND ? ORDER BY date DESC
     */
    List<Expense> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

    /**
     * Encuentra los gastos más recientes limitando el número de resultados.
     * Útil para mostrar un resumen de gastos recientes en la interfaz.
     * 
     * @return Lista de todos los gastos ordenados por fecha descendente (más recientes primero)
     * 
     * Nota: Puedes limitar los resultados usando Pageable en el Service
     * SQL generado: SELECT * FROM expenses ORDER BY date DESC
     */
    List<Expense> findAllByOrderByDateDesc();

    /**
     * Encuentra todos los gastos de una categoría en un rango de fechas.
     * Útil para reportes específicos de categoría en un período determinado.
     * 
     * @param category -> Categoría del gasto
     * @param startDate -> Fecha de inicio del rango (inclusive)
     * @param endDate -> Fecha de fin del rango (inclusive)
     * @return Lista de gastos que cumplen ambos criterios, ordenados por fecha descendente
     * 
     * Ejemplo: findByCategoryAndDateBetween(CategoryEnum.FOOD, inicio, fin)
     * SQL generado: SELECT * FROM expenses WHERE category = ? AND date BETWEEN ? AND ? ORDER BY date DESC
     */
    List<Expense> findByCategoryAndDateBetweenOrderByDateDesc(
            CategoryEnum category,
            LocalDate startDate,
            LocalDate endDate
    );


}
