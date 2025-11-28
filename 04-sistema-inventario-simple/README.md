# 📦 PROYECTO 4 - Sistema de Inventario Simple

## 📋 Descripción del Proyecto

Desarrollar una API REST con Spring Boot para gestionar el inventario de productos de una empresa. El sistema debe permitir el control de stock, alertas automáticas de reabastecimiento y gestión completa de productos.

---

## 🎯 Objetivos de Aprendizaje

- Implementar una aplicación Spring Boot con arquitectura en capas
- Trabajar con JPA/Hibernate para persistencia de datos
- Aplicar validaciones complejas con Bean Validation
- Implementar lógica de negocio con reglas específicas
- Crear una API REST siguiendo buenas prácticas
- Manejar excepciones personalizadas
- Documentar código de forma profesional

---

## 🛠️ Requisitos Técnicos

### Tecnologías Obligatorias
- **Java**: 17 o superior
- **Spring Boot**: 3.x
- **Base de datos**: H2 (desarrollo) o PostgreSQL/MySQL (opcional)
- **Gestor de dependencias**: Maven o Gradle

### Dependencias Requeridas
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- base de datos (H2/PostgreSQL/MySQL)
- lombok (recomendado)
```

---

## 📁 Estructura del Proyecto Esperada

```
src/main/java/com/inventario/
├── model/
│   └── Product.java
├── repository/
│   └── ProductRepository.java
├── service/
│   ├── ProductService.java
│   └── impl/
│       └── ProductServiceImpl.java
├── controller/
│   └── ProductController.java
├── exception/
│   ├── ProductNotFoundException.java
│   ├── InsufficientStockException.java
│   ├── DuplicateSkuException.java
│   └── GlobalExceptionHandler.java
└── InventarioApplication.java
```

---

## 📊 Modelo de Datos: Product

### Campos Requeridos

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | Long | PK, Auto-generado | Identificador único |
| `nombre` | String | NOT NULL, 3-100 caracteres | Nombre del producto |
| `descripcion` | String | Opcional, máx. 500 caracteres | Descripción detallada |
| `cantidad` | Integer | NOT NULL, >= 0 | Stock actual |
| `stockMinimo` | Integer | NOT NULL, >= 0 | Umbral de alerta |
| `precio` | BigDecimal | NOT NULL, > 0 | Precio unitario |
| `sku` | String | UNIQUE, NOT NULL, formato específico | Código único del producto |
| `ultimaActualizacion` | LocalDateTime | Auto-actualizado | Última modificación |

### Formato SKU
- Patrón: `[A-Z]{3}-[0-9]{4}` (Ejemplo: PRD-0001, ALM-1234)
- Debe ser único en toda la base de datos

---

## ⚙️ Funcionalidades a Implementar

### 1. CRUD Completo de Productos

#### Crear Producto (POST /api/products)
- Validar todos los campos según las restricciones
- Verificar que el SKU no exista previamente
- Establecer `ultimaActualizacion` automáticamente
- Retornar el producto creado con código 201

#### Listar Productos (GET /api/products)
- Obtener todos los productos
- Ordenar por fecha de última actualización (más reciente primero)
- Incluir indicador de "requiere reabastecimiento" si stock < stockMinimo

#### Obtener Producto por ID (GET /api/products/{id})
- Buscar producto por ID
- Lanzar excepción si no existe

#### Buscar Producto por SKU (GET /api/products/sku/{sku})
- Buscar producto por código SKU
- Lanzar excepción si no existe

#### Actualizar Producto (PUT /api/products/{id})
- Actualizar campos editables (nombre, descripción, precio, stockMinimo)
- NO permitir editar cantidad (usar endpoints específicos de movimiento)
- NO permitir editar SKU
- Actualizar `ultimaActualizacion` automáticamente

#### Eliminar Producto (DELETE /api/products/{id})
- Eliminar lógicamente o físicamente (decisión de diseño)
- Validar que exista antes de eliminar

---

### 2. Gestión de Stock

#### Entrada de Stock (POST /api/products/{id}/stock/entrada)
**Request Body:**
```json
{
  "cantidad": 50,
  "motivo": "Compra proveedor XYZ"
}
```
**Lógica:**
- Incrementar cantidad actual
- Validar que cantidad > 0
- Actualizar `ultimaActualizacion`
- Retornar producto actualizado

#### Salida de Stock (POST /api/products/{id}/stock/salida)
**Request Body:**
```json
{
  "cantidad": 10,
  "motivo": "Venta pedido #1234"
}
```
**Lógica:**
- Decrementar cantidad actual
- Validar que cantidad > 0
- Validar que haya stock suficiente (cantidad actual >= cantidad solicitada)
- Si no hay stock suficiente, lanzar `InsufficientStockException`
- Actualizar `ultimaActualizacion`
- Retornar producto actualizado

---

### 3. Sistema de Alertas de Stock Bajo

#### Obtener Productos con Stock Bajo (GET /api/products/alertas)
**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Producto A",
    "sku": "PRD-0001",
    "cantidad": 5,
    "stockMinimo": 10,
    "deficit": 5
  }
]
```
**Lógica:**
- Filtrar productos donde `cantidad < stockMinimo`
- Calcular déficit: `stockMinimo - cantidad`
- Ordenar por déficit (mayor a menor)

---

### 4. Búsqueda y Filtros

#### Buscar por Nombre (GET /api/products/search?nombre={texto})
- Búsqueda case-insensitive
- Buscar coincidencias parciales (LIKE %texto%)

#### Filtrar por Rango de Precio (GET /api/products/precio?min={min}&max={max})
- Retornar productos con precio entre min y max (inclusive)

---

## 🎯 Reglas de Negocio

### Validaciones Obligatorias

1. **SKU único**: No pueden existir dos productos con el mismo SKU
2. **Formato SKU**: Debe cumplir el patrón `[A-Z]{3}-[0-9]{4}`
3. **Precio positivo**: El precio debe ser siempre mayor a 0
4. **Stock no negativo**: La cantidad nunca puede ser negativa
5. **Stock mínimo razonable**: stockMinimo debe ser >= 0
6. **Nombre obligatorio**: El nombre no puede estar vacío ni ser solo espacios
7. **Última actualización automática**: Debe actualizarse en cada modificación

### Lógica de Negocio

1. **Actualización automática**: Cada vez que se modifique un producto, actualizar `ultimaActualizacion`
2. **Control de stock**: Las salidas no pueden dejar el stock en negativo
3. **Alertas automáticas**: Identificar productos con stock < stockMinimo
4. **Inmutabilidad del SKU**: Una vez creado, el SKU no se puede modificar

---

## 📝 Endpoints Esperados (Resumen)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/products` | Crear producto |
| GET | `/api/products` | Listar todos los productos |
| GET | `/api/products/{id}` | Obtener producto por ID |
| GET | `/api/products/sku/{sku}` | Obtener producto por SKU |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |
| POST | `/api/products/{id}/stock/entrada` | Registrar entrada de stock |
| POST | `/api/products/{id}/stock/salida` | Registrar salida de stock |
| GET | `/api/products/alertas` | Productos con stock bajo |
| GET | `/api/products/search` | Buscar por nombre |
| GET | `/api/products/precio` | Filtrar por rango de precio |

---


## 🏆 Bonus Points (Opcional)

- [ ] Implementar paginación en listado de productos
- [ ] Añadir ordenamiento dinámico (por nombre, precio, stock)
- [ ] Crear un historial de movimientos de stock
- [ ] Implementar soft delete en lugar de eliminación física
- [ ] Añadir campo `categoría` con enum
- [ ] Tests unitarios con JUnit y Mockito
- [ ] Documentación con Swagger/OpenAPI
- [ ] Implementar cache para búsquedas frecuentes

---

## 📚 Recursos de Apoyo

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Bean Validation](https://beanvalidation.org/)
- [REST API Best Practices](https://restfulapi.net/)

---

## 🚀 Pasos Sugeridos para el Desarrollo

1. **Configuración inicial**: Crear proyecto Spring Boot con dependencias
2. **Modelo de datos**: Implementar entidad `Product` con validaciones
3. **Capa de persistencia**: Crear `ProductRepository`
4. **Capa de servicio**: Implementar `ProductService` con lógica de negocio
5. **DTOs**: Crear objetos de transferencia de datos
6. **Capa de controlador**: Implementar endpoints REST
7. **Manejo de excepciones**: Crear excepciones personalizadas y GlobalExceptionHandler
8. **Pruebas**: Probar todos los endpoints con Postman/Thunder Client
9. **Refinamiento**: Aplicar buenas prácticas y refactorizar

---

**¡Mucho éxito con el proyecto! 🎯**