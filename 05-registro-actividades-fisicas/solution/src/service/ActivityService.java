package service;

import model.Activity;
import repository.ActivityRepository;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para Activities.
 * Maneja validaciones, cálculos estadísticos y operaciones complejas.
 */
public class ActivityService {

    private final ActivityRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public ActivityService() {
        this.repository = new ActivityRepository();
    }

    // ========== CRUD Operations ==========

    /**
     * Crea una nueva actividad después de validarla.
     * @return Activity creada con ID generado
     * @throws IllegalArgumentException si la validación falla
     */
    public Activity create(Activity activity) {
        List<String> errors = ValidationUtil.validate(activity);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
        
        activity.setId(null); // Asegurar que se genere nuevo ID
        return repository.save(activity);
    }

    /**
     * Obtiene todas las actividades.
     */
    public List<Activity> findAll() {
        return repository.findAll();
    }

    /**
     * Busca una actividad por ID.
     * @throws IllegalArgumentException si no existe
     */
    public Activity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad con ID " + id + " no encontrada"));
    }

    /**
     * Actualiza una actividad existente.
     * Permite actualización parcial (solo campos no-null).
     */
    public Activity update(Long id, Activity updates) {
        Activity existing = findById(id);
        
        // Aplicar actualizaciones solo a campos no-null
        if (updates.getExerciseType() != null) {
            existing.setExerciseType(updates.getExerciseType());
        }
        if (updates.getDurationMinutes() != null) {
            existing.setDurationMinutes(updates.getDurationMinutes());
        }
        if (updates.getDistanceKm() != null) {
            existing.setDistanceKm(updates.getDistanceKm());
        }
        if (updates.getCaloriesBurned() != null) {
            existing.setCaloriesBurned(updates.getCaloriesBurned());
        }
        if (updates.getDate() != null) {
            existing.setDate(updates.getDate());
        }
        if (updates.getNotes() != null) {
            existing.setNotes(updates.getNotes());
        }
        if (updates.getHeartRate() != null) {
            existing.setHeartRate(updates.getHeartRate());
        }
        
        // Validar actividad completa después de actualizaciones
        List<String> errors = ValidationUtil.validate(existing);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
        
        return repository.save(existing);
    }

    /**
     * Elimina una actividad por ID.
     * @throws IllegalArgumentException si no existe
     */
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new IllegalArgumentException("Actividad con ID " + id + " no encontrada");
        }
    }

    // ========== Filtros ==========

    /**
     * Filtra actividades por tipo de ejercicio.
     */
    public List<Activity> findByType(String exerciseType) {
        return repository.findByType(exerciseType);
    }

    /**
     * Filtra actividades por rango de fechas.
     */
    public List<Activity> findByDateRange(String startDate, String endDate) {
        if (!ValidationUtil.isValidDateFormat(startDate) || !ValidationUtil.isValidDateFormat(endDate)) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD");
        }
        return repository.findByDateRange(startDate, endDate);
    }

    // ========== Estadísticas ==========

    /**
     * Calcula estadísticas de la última semana (7 días).
     * @return Map con avgDurationMinutes, avgDistanceKm, totalCalories
     */
    public Map<String, Object> calculateWeeklyStats() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        
        List<Activity> weekActivities = repository.findAll().stream()
                .filter(a -> {
                    LocalDate activityDate = LocalDate.parse(a.getDate(), DATE_FORMATTER);
                    return activityDate.isAfter(oneWeekAgo) || activityDate.isEqual(oneWeekAgo);
                })
                .collect(Collectors.toList());
        
        return calculateStats(weekActivities);
    }

    /**
     * Calcula estadísticas del último mes (30 días).
     */
    public Map<String, Object> calculateMonthlyStats() {
        LocalDate oneMonthAgo = LocalDate.now().minusDays(30);
        
        List<Activity> monthActivities = repository.findAll().stream()
                .filter(a -> {
                    LocalDate activityDate = LocalDate.parse(a.getDate(), DATE_FORMATTER);
                    return activityDate.isAfter(oneMonthAgo) || activityDate.isEqual(oneMonthAgo);
                })
                .collect(Collectors.toList());
        
        return calculateStats(monthActivities);
    }

    /**
     * Calcula totales acumulados de todas las actividades.
     */
    public Map<String, Object> calculateTotals() {
        List<Activity> allActivities = repository.findAll();
        
        Map<String, Object> totals = new HashMap<>();
        totals.put("totalActivities", allActivities.size());
        
        int totalDuration = allActivities.stream()
                .mapToInt(Activity::getDurationMinutes)
                .sum();
        totals.put("totalDurationMinutes", totalDuration);
        
        double totalDistance = allActivities.stream()
                .filter(a -> a.getDistanceKm() != null)
                .mapToDouble(Activity::getDistanceKm)
                .sum();
        totals.put("totalDistanceKm", Math.round(totalDistance * 100.0) / 100.0);
        
        int totalCalories = allActivities.stream()
                .mapToInt(Activity::getCaloriesBurned)
                .sum();
        totals.put("totalCalories", totalCalories);
        
        return totals;
    }

    /**
     * Calcula estadísticas promedio de una lista de actividades.
     */
    private Map<String, Object> calculateStats(List<Activity> activities) {
        Map<String, Object> stats = new HashMap<>();
        
        if (activities.isEmpty()) {
            stats.put("avgDurationMinutes", 0.0);
            stats.put("avgDistanceKm", 0.0);
            stats.put("totalCalories", 0);
            return stats;
        }
        
        // Promedio de duración
        double avgDuration = activities.stream()
                .mapToInt(Activity::getDurationMinutes)
                .average()
                .orElse(0.0);
        stats.put("avgDurationMinutes", Math.round(avgDuration * 100.0) / 100.0);
        
        // Promedio de distancia (solo actividades con distancia)
        double avgDistance = activities.stream()
                .filter(a -> a.getDistanceKm() != null)
                .mapToDouble(Activity::getDistanceKm)
                .average()
                .orElse(0.0);
        stats.put("avgDistanceKm", Math.round(avgDistance * 100.0) / 100.0);
        
        // Total de calorías
        int totalCalories = activities.stream()
                .mapToInt(Activity::getCaloriesBurned)
                .sum();
        stats.put("totalCalories", totalCalories);
        
        return stats;
    }
}