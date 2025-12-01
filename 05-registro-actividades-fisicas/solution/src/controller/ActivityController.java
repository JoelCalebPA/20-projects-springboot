package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Activity;
import service.ActivityService;
import util.JsonUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador HTTP para gestionar endpoints de Activities.
 * Implementa HttpHandler para manejar requests manualmente.
 */
public class ActivityController implements HttpHandler {

    private final ActivityService service;

    public ActivityController() {
        this.service = new ActivityService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        try {
            // Routing manual basado en método HTTP y path
            if (path.equals("/api/activities")) {
                handleActivitiesEndpoint(exchange, method);
            } else if (path.startsWith("/api/activities/stats/")) {
                handleStatsEndpoint(exchange, method, path);
            } else if (path.equals("/api/activities/totals")) {
                handleTotalsEndpoint(exchange, method);
            } else if (path.matches("/api/activities/\\d+")) {
                handleActivityByIdEndpoint(exchange, method, path);
            } else {
                sendResponse(exchange, 404, JsonUtil.errorJson("Endpoint no encontrado"));
            }
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, JsonUtil.errorJson(e.getMessage()));
        } catch (Exception e) {
            sendResponse(exchange, 500, JsonUtil.errorJson("Error interno del servidor: " + e.getMessage()));
        }
    }

    // ========== Endpoints principales ==========

    /**
     * Maneja /api/activities - GET (listar) y POST (crear)
     */
    private void handleActivitiesEndpoint(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            handleGetActivities(exchange);
        } else if (method.equals("POST")) {
            handleCreateActivity(exchange);
        } else {
            sendResponse(exchange, 405, JsonUtil.errorJson("Método no permitido"));
        }
    }

    /**
     * Maneja /api/activities/{id} - GET, PUT, DELETE
     */
    private void handleActivityByIdEndpoint(HttpExchange exchange, String method, String path) throws IOException {
        Long id = extractIdFromPath(path);
        
        if (method.equals("GET")) {
            handleGetActivityById(exchange, id);
        } else if (method.equals("PUT")) {
            handleUpdateActivity(exchange, id);
        } else if (method.equals("DELETE")) {
            handleDeleteActivity(exchange, id);
        } else {
            sendResponse(exchange, 405, JsonUtil.errorJson("Método no permitido"));
        }
    }

    /**
     * Maneja /api/activities/stats/{type} - weekly o monthly
     */
    private void handleStatsEndpoint(HttpExchange exchange, String method, String path) throws IOException {
        if (!method.equals("GET")) {
            sendResponse(exchange, 405, JsonUtil.errorJson("Método no permitido"));
            return;
        }

        if (path.endsWith("/weekly")) {
            Map<String, Object> stats = service.calculateWeeklyStats();
            sendResponse(exchange, 200, mapToJson(stats));
        } else if (path.endsWith("/monthly")) {
            Map<String, Object> stats = service.calculateMonthlyStats();
            sendResponse(exchange, 200, mapToJson(stats));
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Estadística no encontrada"));
        }
    }

    /**
     * Maneja /api/activities/totals
     */
    private void handleTotalsEndpoint(HttpExchange exchange, String method) throws IOException {
        if (!method.equals("GET")) {
            sendResponse(exchange, 405, JsonUtil.errorJson("Método no permitido"));
            return;
        }

        Map<String, Object> totals = service.calculateTotals();
        sendResponse(exchange, 200, mapToJson(totals));
    }

    // ========== Implementaciones CRUD ==========

    /**
     * GET /api/activities (con filtros opcionales)
     */
    private void handleGetActivities(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        Map<String, String> params = parseQueryParams(uri.getQuery());
        
        List<Activity> activities;
        
        // Filtrar por tipo si se proporciona
        if (params.containsKey("type")) {
            activities = service.findByType(params.get("type"));
        }
        // Filtrar por rango de fechas
        else if (params.containsKey("startDate") && params.containsKey("endDate")) {
            activities = service.findByDateRange(params.get("startDate"), params.get("endDate"));
        }
        // Sin filtros: retornar todas
        else {
            activities = service.findAll();
        }
        
        String json = JsonUtil.toJsonArray(activities);
        sendResponse(exchange, 200, json);
    }

    /**
     * POST /api/activities
     */
    private void handleCreateActivity(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Activity activity = JsonUtil.fromJson(body);
        
        Activity created = service.create(activity);
        String json = JsonUtil.toJson(created);
        sendResponse(exchange, 201, json);
    }

    /**
     * GET /api/activities/{id}
     */
    private void handleGetActivityById(HttpExchange exchange, Long id) throws IOException {
        Activity activity = service.findById(id);
        String json = JsonUtil.toJson(activity);
        sendResponse(exchange, 200, json);
    }

    /**
     * PUT /api/activities/{id}
     */
    private void handleUpdateActivity(HttpExchange exchange, Long id) throws IOException {
        String body = readRequestBody(exchange);
        Activity updates = JsonUtil.fromJson(body);
        
        Activity updated = service.update(id, updates);
        String json = JsonUtil.toJson(updated);
        sendResponse(exchange, 200, json);
    }

    /**
     * DELETE /api/activities/{id}
     */
    private void handleDeleteActivity(HttpExchange exchange, Long id) throws IOException {
        service.delete(id);
        sendResponse(exchange, 204, ""); // No Content
    }

    // ========== Métodos auxiliares ==========

    /**
     * Lee el body del request como String.
     */
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        byte[] bytes = is.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Envía respuesta HTTP con código y body JSON.
     */
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    /**
     * Extrae ID desde path /api/activities/{id}
     */
    private Long extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    /**
     * Parsea query params desde URI.
     * Ejemplo: ?type=Running&startDate=2025-11-01
     */
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }

    /**
     * Convierte un Map a JSON simple (para estadísticas).
     */
    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        int count = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
            
            if (++count < map.size()) {
                json.append(",");
            }
        }
        json.append("}");
        return json.toString();
    }
}