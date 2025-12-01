package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import model.Activity;

/**
 * Utilidad para validar campos de Activity.
 * Retorna lista de errores para dar feedback completo al usuario.
 */
public class ValidationUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Valida una actividad completa.
     * @return Lista de mensajes de error (vacía si es válida)
     */
    public static List<String> validate(Activity activity) {
        List<String> errors = new ArrayList<>();

        // validar tipo de ejercicio
        if (activity.getExerciseType() == null || activity.getExerciseType().trim().isEmpty()) {
            errors.add("El tipo de ejercicio no puede estar vacio");
        }

        // validar duracion
        if (activity.getDurationMinutes() == null) {
            errors.add("La duración es obligatoria");
        } else if (activity.getDurationMinutes() <= 0) {
            errors.add("La duración debe ser mayor a 0 minutos");
        }

        // Validar distancia (opcional, pero si existe debe ser >= 0)
        if (activity.getDistanceKm() != null && activity.getDistanceKm() < 0) {
            errors.add("La distancia no puede ser negativa");
        }

        // Validar calorías
        if (activity.getCaloriesBurned() == null) {
            errors.add("Las calorías quemadas son obligatorias");
        } else if (activity.getCaloriesBurned() < 0) {
            errors.add("Las calorías no pueden ser negativas");
        }

        // Validar fecha
        if (activity.getDate() == null || activity.getDate().trim().isEmpty()) {
            errors.add("La fecha es obligatoria");
        } else {
            try {
                LocalDate activityDate = LocalDate.parse(activity.getDate(), DATE_FORMATTER);
                if (activityDate.isAfter(LocalDate.now())) {
                    errors.add("La fecha no puede ser futura");
                }
            } catch (DateTimeParseException e) {
                errors.add("Formato de fecha inválido. Use formato ISO: YYYY-MM-DD");
            }
        }

        // Validar frecuencia cardíaca (opcional, pero si existe debe estar en rango)
        if (activity.getHeartRate() != null) {
            if (activity.getHeartRate() < 40 || activity.getHeartRate() > 220) {
                errors.add("La frecuencia cardíaca debe estar entre 40 y 220 bpm");
            }
        }

        return errors;
    }

    /**
     * Valida que un ID sea válido (no null y mayor a 0).
     */
    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    /**
     * Valida formato de fecha sin crear el objeto Activity.
     */
    public static boolean isValidDateFormat(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Verifica si una actividad es válida (sin errores).
     */
    public static boolean isValid(Activity activity) {
        return validate(activity).isEmpty();
    }
}
