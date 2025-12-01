package util;

import model.Activity;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidad para convertir objetos Activity a JSON y viceversa.
 * Implementación manual sin librerías externas.
 */
public class JsonUtil {

    /**
     * Convierte un objeto Activity a formato JSON.
     */
    public static String toJson(Activity activity) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        appendField(json, "id", activity.getId(), true);
        appendField(json, "exerciseType", activity.getExerciseType(), true);
        appendField(json, "durationMinutes", activity.getDurationMinutes(), true);
        appendField(json, "distanceKm", activity.getDistanceKm(), true);
        appendField(json, "caloriesBurned", activity.getCaloriesBurned(), true);
        appendField(json, "date", activity.getDate(), true);
        appendField(json, "notes", activity.getNotes(), true);
        appendField(json, "heartRate", activity.getHeartRate(), false); // último campo sin coma
        
        json.append("}");
        return json.toString();
    }

    /**
     * Convierte una lista de Activities a JSON array.
     */
    public static String toJsonArray(List<Activity> activities) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < activities.size(); i++) {
            json.append(toJson(activities.get(i)));
            if (i < activities.size() - 1) {
                json.append(",");
            }
        }
        
        json.append("]");
        return json.toString();
    }

    /**
     * Parsea un JSON string a un objeto Activity.
     * Maneja campos opcionales (null).
     */
    public static Activity fromJson(String json) {
        Activity activity = new Activity();
        
        // Extraer valores usando regex (simple pero funcional)
        activity.setId(extractLong(json, "id"));
        activity.setExerciseType(extractString(json, "exerciseType"));
        activity.setDurationMinutes(extractInteger(json, "durationMinutes"));
        activity.setDistanceKm(extractDouble(json, "distanceKm"));
        activity.setCaloriesBurned(extractInteger(json, "caloriesBurned"));
        activity.setDate(extractString(json, "date"));
        activity.setNotes(extractString(json, "notes"));
        activity.setHeartRate(extractInteger(json, "heartRate"));
        
        return activity;
    }

    /**
     * Crea un JSON de error.
     */
    public static String errorJson(String message) {
        return "{\"error\":\"" + escapeJson(message) + "\"}";
    }

    /**
     * Crea un JSON de errores múltiples.
     */
    public static String errorsJson(List<String> errors) {
        StringBuilder json = new StringBuilder();
        json.append("{\"errors\":[");
        
        for (int i = 0; i < errors.size(); i++) {
            json.append("\"").append(escapeJson(errors.get(i))).append("\"");
            if (i < errors.size() - 1) {
                json.append(",");
            }
        }
        
        json.append("]}");
        return json.toString();
    }

    // ========== Métodos auxiliares ==========

    /**
     * Añade un campo al JSON con su valor.
     * Maneja tipos String, Number y null.
     */
    private static void appendField(StringBuilder json, String fieldName, Object value, boolean addComma) {
        json.append("\"").append(fieldName).append("\":");
        
        if (value == null) {
            json.append("null");
        } else if (value instanceof String) {
            json.append("\"").append(escapeJson((String) value)).append("\"");
        } else {
            json.append(value); // Number
        }
        
        if (addComma) {
            json.append(",");
        }
    }

    /**
     * Escapa caracteres especiales en strings JSON.
     */
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Extrae un valor String de un JSON.
     */
    private static String extractString(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String value = matcher.group(1);
            return value.isEmpty() ? null : value;
        }
        return null;
    }

    /**
     * Extrae un valor Integer de un JSON.
     */
    private static Integer extractInteger(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    /**
     * Extrae un valor Long de un JSON.
     */
    private static Long extractLong(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    /**
     * Extrae un valor Double de un JSON.
     */
    private static Double extractDouble(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*(\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return null;
    }
}