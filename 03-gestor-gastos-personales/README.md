# Proyecto 3: Registro de Gastos Personales 💰

## 📋 Descripción del Proyecto

Desarrolla una API REST con Spring Boot para gestionar gastos personales. Este sistema permitirá registrar gastos diarios, categorizarlos y obtener reportes útiles para el control financiero personal.

Este proyecto te ayudará a consolidar conceptos fundamentales de Spring Boot mientras introduces nuevas funcionalidades como:
- Trabajo con enumeraciones (Enums)
- Cálculos y agregaciones de datos
- Filtros dinámicos por diferentes criterios
- Generación de reportes básicos
- Validaciones de negocio más complejas

---

## 🎯 Objetivos de Aprendizaje

Al completar este proyecto, habrás practicado:

1. **Modelado de datos** con tipos específicos (LocalDate, BigDecimal, Enums)
2. **Validaciones avanzadas** con Bean Validation y validaciones personalizadas
3. **Consultas derivadas** en Spring Data JPA con múltiples criterios
4. **Agregaciones y cálculos** sobre colecciones de datos
5. **DTOs especializados** para reportes
6. **Manejo de fechas** con la API de tiempo de Java
7. **Buenas prácticas** en el manejo de datos monetarios

---

## 📐 Requisitos Funcionales

### Entidad Principal: Expense

La entidad `Expense` debe contener los siguientes campos:

| Campo | Tipo | Descripción | Restricciones |
|-------|------|-------------|---------------|
| id | Long | Identificador único | Generado automáticamente |
| description | String | Descripción del gasto | Obligatorio, 3-200 caracteres |
| amount | BigDecimal | Monto del gasto | Obligatorio, mayor que 0 |
| category | CategoryEnum | Categoría del gasto | Obligatorio |
| date | LocalDate | Fecha del gasto | Obligatorio, no puede ser futura |
| paymentMethod | PaymentMethodEnum | Método de pago usado | Obligatorio |

### Categorías de Gastos (CategoryEnum)

- `FOOD` - Alimentación
- `TRANSPORT` - Transporte
- `ENTERTAINMENT` - Entretenimiento
- `HEALTH` - Salud
- `EDUCATION` - Educación
- `UTILITIES` - Servicios (agua, luz, etc.)
- `SHOPPING` - Compras
- `OTHER` - Otros

### Métodos de Pago (PaymentMethodEnum)

- `CASH` - Efectivo
- `DEBIT_CARD` - Tarjeta de débito
- `CREDIT_CARD` - Tarjeta de crédito
- `BANK_TRANSFER` - Transferencia bancaria
- `DIGITAL_WALLET` - Billetera digital (PayPal, etc.)

---

## 🛠️ Requisitos Técnicos

### Estructura del Proyecto

```
src/main/java/com/tuusuario/expensetracker/
├── model/
│   ├── Expense.java
│   ├── CategoryEnum.java
│   └── PaymentMethodEnum.java
├── repository/
│   └── ExpenseRepository.java
├── service/
│   ├── ExpenseService.java
│   └── impl/
│       └── ExpenseServiceImpl.java
├── controller/
│   └── ExpenseController.java
└── exception/
    ├── ExpenseNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🔌 Endpoints a Implementar

### CRUD Básico

#### 1. Crear Gasto
```
POST /api/expenses
Content-Type: application/json

{
  "description": "Almuerzo en restaurante",
  "amount": 25.50,
  "category": "FOOD",
  "date": "2024-11-18",
  "paymentMethod": "CREDIT_CARD"
}

Respuesta: 201 Created
```

#### 2. Obtener Todos los Gastos
```
GET /api/expenses

Respuesta: 200 OK
[
  {
    "id": 1,
    "description": "Almuerzo en restaurante",
    "amount": 25.50,
    "category": "FOOD",
    "date": "2024-11-18",
    "paymentMethod": "CREDIT_CARD"
  },
  ...
]
```

#### 3. Obtener Gasto por ID
```
GET /api/expenses/{id}

Respuesta: 200 OK o 404 Not Found
```

#### 4. Actualizar Gasto
```
PUT /api/expenses/{id}
Content-Type: application/json

Respuesta: 200 OK o 404 Not Found
```

#### 5. Eliminar Gasto
```
DELETE /api/expenses/{id}

Respuesta: 204 No Content o 404 Not Found
```

### Endpoints de Consulta y Filtrado

#### 6. Filtrar por Categoría
```
GET /api/expenses/category/{category}

Ejemplo: GET /api/expenses/category/FOOD

Respuesta: 200 OK - Lista de gastos de esa categoría
```

#### 7. Filtrar por Rango de Fechas
```
GET /api/expenses/between?startDate=2024-11-01&endDate=2024-11-30

Respuesta: 200 OK - Lista de gastos en ese período
```

#### 8. Filtrar por Método de Pago
```
GET /api/expenses/payment-method/{paymentMethod}

Ejemplo: GET /api/expenses/payment-method/CREDIT_CARD

Respuesta: 200 OK
```

### Endpoints de Reportes

#### 9. Reporte: Total por Categoría
```
GET /api/expenses/reports/by-category

Respuesta: 200 OK
[
  {
    "category": "FOOD",
    "totalAmount": 325.50,
    "expenseCount": 12
  },
  {
    "category": "TRANSPORT",
    "totalAmount": 180.00,
    "expenseCount": 8
  },
  ...
]
```

#### 10. Reporte: Total por Período
```
GET /api/expenses/reports/period?startDate=2024-11-01&endDate=2024-11-30

Respuesta: 200 OK
{
  "startDate": "2024-11-01",
  "endDate": "2024-11-30",
  "totalAmount": 1250.75,
  "expenseCount": 45,
  "averageExpense": 27.79
}
```

#### 11. Reporte: Resumen del Mes Actual
```
GET /api/expenses/reports/current-month

Respuesta: 200 OK
{
  "month": "NOVEMBER",
  "year": 2024,
  "totalAmount": 856.30,
  "expenseCount": 28,
  "mostExpensiveCategory": "FOOD",
  "leastExpensiveCategory": "EDUCATION"
}
```

---

## ✅ Validaciones Requeridas

### A Nivel de Entidad (Bean Validation)

1. **Description**:
   - No nulo
   - No vacío
   - Longitud mínima: 3 caracteres
   - Longitud máxima: 200 caracteres

2. **Amount**:
   - No nulo
   - Valor mínimo: 0.01
   - Máximo 2 decimales
   - Usar `@DecimalMin` y `@Digits`

3. **Category**:
   - No nulo
   - Valor válido del enum

4. **Date**:
   - No nulo
   - No puede ser fecha futura

5. **PaymentMethod**:
   - No nulo
   - Valor válido del enum

### Validaciones de Negocio (En el Service)

1. Validar que el ID existe antes de actualizar o eliminar
2. Validar que la fecha no sea futura al crear/actualizar
3. Validar que el monto sea positivo
4. Validar formato de fechas en los filtros


## 🎓 Conceptos Clave a Aplicar

### 1. Uso de BigDecimal para Montos
```java
// ❌ INCORRECTO
private double amount;

// ✅ CORRECTO
private BigDecimal amount;
```

**¿Por qué?** Los tipos `double` y `float` tienen problemas de precisión con números decimales, especialmente en cálculos financieros.

### 2. Uso de LocalDate para Fechas
```java
// ❌ INCORRECTO
private Date date;

// ✅ CORRECTO
private LocalDate date;
```

**¿Por qué?** `LocalDate` es parte de la API moderna de Java (Java 8+), es inmutable y más fácil de usar.

### 3. Enums para Valores Fijos
```java
@Enumerated(EnumType.STRING)
private CategoryEnum category;
```

**¿Por qué?** Los enums garantizan que solo se usen valores válidos y hacen el código más legible.

### 4. Query Methods en Repository
```java
List<Expense> findByCategoryOrderByDateDesc(CategoryEnum category);
List<Expense> findByDateBetween(LocalDate start, LocalDate end);
```

### 5. Cálculos en el Service
Los cálculos de totales, promedios y agregaciones deben hacerse en la capa de servicio, no en el controlador.

---

## 🚀 Criterios de Aceptación

Tu proyecto estará completo cuando:

- [ ] Todos los endpoints CRUD funcionen correctamente
- [ ] Las validaciones rechacen datos inválidos con mensajes claros
- [ ] Los filtros por categoría, fecha y método de pago funcionen
- [ ] Los reportes calculen correctamente los totales
- [ ] El manejo de excepciones sea consistente
- [ ] El código siga las convenciones de nomenclatura de Java
- [ ] Los métodos del repository usen Query Methods (derived queries)
- [ ] Se use BigDecimal para los montos
- [ ] Se use LocalDate para las fechas
- [ ] Los enums estén correctamente implementados

---

## 🌟 Funcionalidades Extra (Opcional)

Si quieres ir más allá, puedes implementar:

1. **Paginación y Ordenamiento**
   - Agregar `Pageable` a los endpoints de listado
   - Permitir ordenar por fecha, monto, categoría

2. **Búsqueda Combinada**
   - Filtrar por múltiples criterios a la vez
   - Usar `@Query` personalizadas con JPQL

3. **Estadísticas Avanzadas**
   - Gasto promedio por categoría
   - Día de la semana con más gastos
   - Comparación mes actual vs mes anterior

4. **Exportación de Datos**
   - Exportar gastos a CSV
   - Generar reporte en formato JSON

5. **Presupuestos**
   - Definir presupuesto mensual por categoría
   - Alertas cuando se exceda el presupuesto

---

## 📝 Notas Importantes

### Sobre BigDecimal
```java
// Para crear BigDecimal desde String (RECOMENDADO)
BigDecimal amount = new BigDecimal("25.50");

// Para comparar BigDecimal
if (amount.compareTo(BigDecimal.ZERO) > 0) {
    // amount es mayor que 0
}

// Para sumar BigDecimal
BigDecimal total = amount1.add(amount2);
```

### Sobre LocalDate
```java
// Obtener fecha actual
LocalDate today = LocalDate.now();

// Validar que no sea futura
if (date.isAfter(LocalDate.now())) {
    throw new ValidationException("La fecha no puede ser futura");
}

// Trabajar con períodos
LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
LocalDate endOfMonth = LocalDate.now().withDayOfMonth(
    LocalDate.now().lengthOfMonth()
);
```

### Sobre Enums en JPA
```java
@Enumerated(EnumType.STRING)  // ✅ Guarda el nombre (recomendado)
private CategoryEnum category;

@Enumerated(EnumType.ORDINAL) // ❌ Guarda el índice (no recomendado)
private CategoryEnum category;
```

---

## 🧪 Cómo Probar tu API

### Usando Postman o Thunder Client

1. **Crear algunos gastos** con diferentes categorías y fechas
2. **Listar todos** para verificar que se guardaron
3. **Filtrar por categoría** para ver si el filtro funciona
4. **Probar el reporte por categoría** y verificar los cálculos
5. **Probar el reporte de período** con fechas específicas
6. **Intentar crear un gasto inválido** (monto negativo, fecha futura)
7. **Verificar los mensajes de error** cuando algo falla

---

## 📚 Recursos Útiles

- [Spring Data JPA - Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- [Bean Validation con Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation-beanvalidation)
- [Trabajar con BigDecimal](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html)
- [LocalDate API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)

---

## 🎯 Plan de Desarrollo Sugerido

### 1: Configuración y Modelos
1. Crear el proyecto Spring Boot
2. Implementar los Enums
3. Crear la entidad Expense con todas sus validaciones
4. Crear el Repository básico

### 2: CRUD Básico
1. Implementar el Service con operaciones CRUD
2. Implementar el Controller con los endpoints básicos
3. Probar cada endpoint

### 3: Filtros y Consultas
1. Agregar query methods al Repository
2. Implementar métodos de filtrado en el Service
3. Agregar endpoints de filtrado en el Controller

### 4: Reportes y Cálculos
1. Crear los DTOs para reportes
2. Implementar lógica de cálculos en el Service
3. Agregar endpoints de reportes
4. Probar todos los reportes

### 5: Refinamiento
1. Mejorar el manejo de excepciones
2. Agregar más validaciones
3. Documentar el código
4. Testing final

---

## 💡 Consejo Final

No te apresures. Este proyecto introduce varios conceptos nuevos (BigDecimal, LocalDate, Enums, agregaciones). Tómate tu tiempo para entender cada parte antes de pasar a la siguiente.

**¡Recuerda!**: El objetivo no es solo que funcione, sino que entiendas el porqué de cada decisión de diseño.

---

**¡Manos a la obra! 💪 Empieza por crear los Enums y la entidad Expense, y envíamelos cuando estén listos.**
