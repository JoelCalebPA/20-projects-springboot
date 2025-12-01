# Solution - Proyecto 5: Implementación Completa

## Decisiones de Diseño

### 1. Servidor HTTP (`HttpServer`)
- Puerto 8080, endpoint base `/api/activities`
- Context handlers para cada ruta
- Respuesta manual con `HttpExchange`

### 2. Persistencia (CSV)
- Separador: coma (`,`)
- Escape de comillas: `"texto con, comas"`
- Lectura: `BufferedReader` con `Files.newBufferedReader()`
- Escritura: `BufferedWriter` con `Files.newBufferedWriter()`

### 3. Parsing JSON
- Clase `JsonUtil` con métodos:
  - `toJson(Activity)` → String
  - `fromJson(String)` → Activity
  - Implementación manual con `StringBuilder` o regex

### 4. Generación de IDs
- Almacenamiento en memoria: `AtomicLong`
- Recuperación del último ID al iniciar desde CSV

### 5. Cálculos Estadísticos
- Filtrado de fechas con `LocalDate.isAfter()` / `isBefore()`
- Promedio con `DoubleStream.average()`
- Suma con `IntStream.sum()`

## Clases Implementadas

### Activity.java (200 líneas)
- Constructor vacío + sobrecargado
- 8 campos privados con getters/setters
- Método `toString()` para debugging

### ActivityRepository.java (180 líneas)
- `List<Activity> findAll()`
- `Optional<Activity> findById(Long)`
- `Activity save(Activity)` (insert/update)
- `void deleteById(Long)`
- `void loadFromCSV()` / `void saveToCSV()`

### ActivityService.java (250 líneas)
- Lógica de negocio + validaciones
- Métodos de cálculo:
  - `calculateWeeklyStats()`
  - `calculateMonthlyStats()`
  - `calculateTotals()`
- Filtros por tipo y rango de fechas

### ActivityController.java (300 líneas)
- Routing manual (switch por path + método HTTP)
- Parsing de request body (`InputStream` → String)
- Envío de respuestas (headers + body)

### Main.java (30 líneas)
- Inicialización del servidor
- Registro de handlers
- `server.start()`

## Ejemplo de Flujo Completo

1. Usuario envía **POST** `/api/activities`
2. `ActivityController` recibe `HttpExchange`
3. Extrae body JSON y llama `JsonUtil.fromJson()`
4. Valida con `ValidationUtil.validate(activity)`
5. `ActivityService.create()` llama `repository.save()`
6. `ActivityRepository` asigna ID y escribe en CSV
7. Devuelve actividad con ID al controller
8. Controller serializa con `JsonUtil.toJson()` y envía `201 Created`

## Pruebas Manuales Realizadas

- ✅ CRUD completo (200+ requests)
- ✅ Validaciones de campos negativos
- ✅ Cálculo de estadísticas con diferentes rangos
- ✅ Persistencia tras reinicio del servidor
- ✅ Manejo de errores 400/404/500

## Código Destacado

**Parsing CSV:**
```java
String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
// Regex para respetar comillas
```

**Cálculo de promedio semanal:**
```java
LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
List<Activity> weekActivities = activities.stream()
    .filter(a -> LocalDate.parse(a.getDate()).isAfter(oneWeekAgo))
    .collect(Collectors.toList());
```

## Próximas Mejoras (Proyecto 6)

- Introducir relaciones entre entidades
- Volver a Spring Boot con lo aprendido
- Comparar abstracciones vs. control manual