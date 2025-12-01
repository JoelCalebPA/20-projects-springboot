# Proyecto 5: Sistema de Registro de Actividades Físicas (Java Puro)

## Objetivo Pedagógico
Desarrollar una API REST funcional usando **únicamente la librería estándar de Java**, sin frameworks ni dependencias externas. Se implementará servidor HTTP, persistencia en archivo de texto, parsing JSON manual y gestión completa de la aplicación sin abstracciones.

## Requisitos Técnicos

### Stack Tecnológico
- **Java SE 11+** (sin Spring Boot, sin Lombok, sin Maven/Gradle)
- **Servidor HTTP**: `com.sun.net.httpserver.HttpServer`
- **Persistencia**: Archivo CSV (`activities.csv`)
- **Formato API**: JSON (parsing manual o con StringBuilder)
- **Colecciones**: ArrayList, HashMap, etc.

### Entidad: Activity

**Campos obligatorios:**
- `id`: Long (generado automáticamente)
- `exerciseType`: String (ej: "Running", "Cycling", "Swimming")
- `durationMinutes`: Integer (min: 1)
- `distanceKm`: Double (min: 0.0, puede ser null)
- `caloriesBurned`: Integer (calculado o manual, min: 0)
- `date`: String (formato ISO: "2025-11-30")
- `notes`: String (opcional)
- `heartRate`: Integer (opcional, rango: 40-220 bpm)

**Validaciones:**
- `durationMinutes` > 0
- `distanceKm` >= 0 (si se proporciona)
- `caloriesBurned` >= 0
- `heartRate` entre 40-220 (si se proporciona)
- `date` no puede ser futura
- `exerciseType` no vacío

### Funcionalidades del Sistema

#### CRUD Básico
1. **POST** `/api/activities` - Crear actividad
2. **GET** `/api/activities` - Listar todas
3. **GET** `/api/activities/{id}` - Buscar por ID
4. **PUT** `/api/activities/{id}` - Actualizar
5. **DELETE** `/api/activities/{id}` - Eliminar

#### Endpoints Estadísticos
6. **GET** `/api/activities/stats/weekly` - Promedio semanal (última semana)
   - Respuesta: `{ "avgDuration": 45.5, "avgDistance": 5.2, "totalCalories": 1200 }`
7. **GET** `/api/activities/stats/monthly` - Promedio mensual (último mes)
8. **GET** `/api/activities/totals` - Totales acumulados históricos

#### Filtros
9. **GET** `/api/activities?type=Running` - Filtrar por tipo
10. **GET** `/api/activities?startDate=2025-11-01&endDate=2025-11-30` - Rango de fechas

### Persistencia (CSV)

**Formato del archivo `activities.csv`:**
```csv
id,exerciseType,durationMinutes,distanceKm,caloriesBurned,date,notes,heartRate
1,Running,30,5.5,300,2025-11-20,Morning jog,145
2,Cycling,60,15.0,450,2025-11-21,,130
```

**Operaciones:**
- Lectura completa al iniciar
- Escritura completa tras cada modificación
- Generación de IDs secuenciales

### Manejo de Errores (Sin Excepciones de Spring)

**Respuestas HTTP manuales:**
- `200 OK` - Éxito
- `201 Created` - Recurso creado
- `400 Bad Request` - Validación fallida (body: `{"error": "Mensaje"}`)
- `404 Not Found` - Recurso no existe
- `500 Internal Server Error` - Error del servidor

### Cálculos Estadísticos

**Promedio Semanal:**
```java
// Actividades de los últimos 7 días
double avgDuration = suma(duraciones) / count;
double avgDistance = suma(distancias) / count;
int totalCalories = suma(calorias);
```

**Conversión de Unidades (Opcional):**
- Millas a km: `km = miles * 1.60934`
- Minutos a horas: `hours = minutes / 60.0`

## Estructura de Directorios
```
proyecto05-actividades-fisicas/
├── README.md (este archivo)
├── working/
│   ├── README.md
│   ├── src/
│   │   ├── Main.java
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── util/
│   └── activities.csv
├── solution/
│   ├── README.md
│   └── src/ (código completo comentado)
└── postman/
    └── ActivityAPI.postman_collection.json
```

## Desafíos Técnicos

1. **Servidor HTTP desde cero** (sin Tomcat embebido)
2. **Routing manual** (mapear rutas a handlers)
3. **Parsing JSON manual** (sin Jackson/Gson)
4. **Gestión de archivos** (BufferedReader/Writer)
5. **Validaciones manuales** (sin Bean Validation)
6. **Manejo de fechas** (LocalDate, DateTimeFormatter)
7. **Cálculos estadísticos** (algoritmos propios)

## Aprendizajes Clave

- Funcionamiento interno de servidores HTTP
- Gestión de streams y archivos en Java
- Manipulación de colecciones (ArrayList, HashMap)
- Parsing y serialización manual
- Arquitectura en capas sin inyección de dependencias
- Control total del flujo de la aplicación