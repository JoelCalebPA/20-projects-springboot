# Working - Proyecto 5: Sistema de Actividades Físicas (Java Puro)

## Configuración Inicial

### Requisitos
- JDK 11+ instalado
- Editor de texto o IDE sin Maven/Gradle

### Compilar y Ejecutar
```bash
# Compilar
javac -d bin src/**/*.java src/*.java

# Ejecutar
java -cp bin Main
```

**Servidor levanta en:** `http://localhost:8080`

---

## Endpoints a Implementar

### 1. Crear Actividad
**POST** `/api/activities`

**Request Body:**
```json
{
  "exerciseType": "Running",
  "durationMinutes": 30,
  "distanceKm": 5.5,
  "caloriesBurned": 300,
  "date": "2025-11-30",
  "notes": "Morning jog",
  "heartRate": 145
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "exerciseType": "Running",
  "durationMinutes": 30,
  "distanceKm": 5.5,
  "caloriesBurned": 300,
  "date": "2025-11-30",
  "notes": "Morning jog",
  "heartRate": 145
}
```

### 2. Listar Todas
**GET** `/api/activities`

**Response:** `200 OK`
```json
[
  { "id": 1, "exerciseType": "Running", ... },
  { "id": 2, "exerciseType": "Cycling", ... }
]
```

### 3. Buscar por ID
**GET** `/api/activities/1`

**Response:** `200 OK` o `404 Not Found`

### 4. Actualizar
**PUT** `/api/activities/1`

**Request Body:** (campos a actualizar)
```json
{
  "durationMinutes": 45,
  "caloriesBurned": 400
}
```

### 5. Eliminar
**DELETE** `/api/activities/1`

**Response:** `204 No Content`

### 6. Estadísticas Semanales
**GET** `/api/activities/stats/weekly`

**Response:**
```json
{
  "avgDurationMinutes": 42.5,
  "avgDistanceKm": 8.3,
  "totalCalories": 1500
}
```

### 7. Totales Acumulados
**GET** `/api/activities/totals`

**Response:**
```json
{
  "totalActivities": 15,
  "totalDurationMinutes": 900,
  "totalDistanceKm": 125.5,
  "totalCalories": 8500
}
```

---

## Estructura de Clases a Crear

1. **model/Activity.java** - POJO con getters/setters
2. **repository/ActivityRepository.java** - CRUD + persistencia CSV
3. **service/ActivityService.java** - Lógica de negocio + cálculos
4. **controller/ActivityController.java** - Manejo de HTTP requests
5. **util/JsonUtil.java** - Conversión Object ↔ JSON
6. **util/ValidationUtil.java** - Validaciones de campos
7. **Main.java** - Arranca el servidor

---

## Colección Postman

Importa desde: `../postman/ActivityAPI.postman_collection.json`

**Variables de entorno:**
```
base_url = http://localhost:8080
```

**Tests incluidos:**
- Crear actividad válida
- Validación de duración negativa
- Cálculo de estadísticas semanales
- Filtrado por tipo de ejercicio