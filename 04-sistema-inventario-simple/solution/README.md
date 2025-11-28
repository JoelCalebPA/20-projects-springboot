# 📮 Guía de Testing con Postman - Sistema de Inventario

## 🎯 Base URL
```
http://localhost:8080/api/products
```

---

## 📋 Tabla de Endpoints Rápida

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/products` | Crear producto |
| GET | `/api/products` | Listar todos |
| GET | `/api/products/{id}` | Obtener por ID |
| GET | `/api/products/sku/{sku}` | Obtener por SKU |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |
| POST | `/api/products/{id}/stock/entrada` | Entrada de stock |
| POST | `/api/products/{id}/stock/salida` | Salida de stock |
| GET | `/api/products/alertas` | Stock bajo |
| GET | `/api/products/search?nombre={texto}` | Buscar por nombre |
| GET | `/api/products/precio?min={min}&max={max}` | Filtrar por precio |

---

## 🔷 1. CREAR PRODUCTO

### Request
```
POST http://localhost:8080/api/products
Content-Type: application/json
```

### Body - Ejemplo 1 (Producto básico)
```json
{
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 25,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001"
}
```

### Body - Ejemplo 2 (Producto sin descripción)
```json
{
  "nombre": "Mouse Logitech MX Master",
  "cantidad": 50,
  "stockMinimo": 15,
  "precio": 89.99,
  "sku": "MOU-0001"
}
```

### Body - Ejemplo 3 (Producto con stock bajo)
```json
{
  "nombre": "Teclado Mecánico Keychron K2",
  "descripcion": "Teclado mecánico inalámbrico RGB, switches blue",
  "cantidad": 5,
  "stockMinimo": 20,
  "precio": 129.99,
  "sku": "TEC-0001"
}
```

### Respuesta Esperada (201 Created)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 25,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T10:30:00",
  "requiereReabastecimiento": false
}
```

---

## 🔷 2. LISTAR TODOS LOS PRODUCTOS

### Request
```
GET http://localhost:8080/api/products
```

### Respuesta Esperada (200 OK)
```json
[
  {
    "id": 1,
    "nombre": "Laptop Dell XPS 15",
    "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
    "cantidad": 25,
    "stockMinimo": 10,
    "precio": 1299.99,
    "sku": "LAP-0001",
    "ultimaActualizacion": "2024-11-24T10:30:00",
    "requiereReabastecimiento": false
  },
  {
    "id": 2,
    "nombre": "Mouse Logitech MX Master",
    "descripcion": null,
    "cantidad": 50,
    "stockMinimo": 15,
    "precio": 89.99,
    "sku": "MOU-0001",
    "ultimaActualizacion": "2024-11-24T10:31:00",
    "requiereReabastecimiento": false
  },
  {
    "id": 3,
    "nombre": "Teclado Mecánico Keychron K2",
    "descripcion": "Teclado mecánico inalámbrico RGB, switches blue",
    "cantidad": 5,
    "stockMinimo": 20,
    "precio": 129.99,
    "sku": "TEC-0001",
    "ultimaActualizacion": "2024-11-24T10:32:00",
    "requiereReabastecimiento": true
  }
]
```

---

## 🔷 3. OBTENER PRODUCTO POR ID

### Request
```
GET http://localhost:8080/api/products/1
```

### Respuesta Esperada (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 25,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T10:30:00",
  "requiereReabastecimiento": false
}
```

### Respuesta Error - Producto no encontrado (404 Not Found)
```json
{
  "timestamp": "2024-11-24T10:35:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con ID 999 no encontrado",
  "path": "/api/products/999"
}
```

---

## 🔷 4. OBTENER PRODUCTO POR SKU

### Request
```
GET http://localhost:8080/api/products/sku/LAP-0001
```

### Respuesta Esperada (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 25,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T10:30:00",
  "requiereReabastecimiento": false
}
```

### Respuesta Error - SKU no encontrado (404 Not Found)
```json
{
  "timestamp": "2024-11-24T10:36:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con SKU 'XXX-9999' no encontrado",
  "path": "/api/products/sku/XXX-9999"
}
```

---

## 🔷 5. ACTUALIZAR PRODUCTO

### Request
```
PUT http://localhost:8080/api/products/1
Content-Type: application/json
```

### Body - Actualización de precio y descripción
```json
{
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7 11va Gen, 16GB RAM, 512GB SSD, pantalla 4K",
  "precio": 1399.99,
  "stockMinimo": 15
}
```

### Body - Actualización solo de stock mínimo
```json
{
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "precio": 1299.99,
  "stockMinimo": 20
}
```

### Respuesta Esperada (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7 11va Gen, 16GB RAM, 512GB SSD, pantalla 4K",
  "cantidad": 25,
  "stockMinimo": 15,
  "precio": 1399.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T11:00:00",
  "requiereReabastecimiento": false
}
```

---

## 🔷 6. ELIMINAR PRODUCTO

### Request
```
DELETE http://localhost:8080/api/products/1
```

### Respuesta Esperada (204 No Content)
```
Sin contenido en el body
```

### Respuesta Error - Producto no encontrado (404 Not Found)
```json
{
  "timestamp": "2024-11-24T11:05:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto con ID 999 no encontrado",
  "path": "/api/products/999"
}
```

---

## 🔷 7. ENTRADA DE STOCK

### Request
```
POST http://localhost:8080/api/products/1/stock/entrada
Content-Type: application/json
```

### Body - Ejemplo 1 (Compra a proveedor)
```json
{
  "cantidad": 50,
  "motivo": "Compra a proveedor TechSupply - Orden #PO-2024-001"
}
```

### Body - Ejemplo 2 (Devolución de cliente)
```json
{
  "cantidad": 3,
  "motivo": "Devolución cliente - Producto sin abrir"
}
```

### Body - Ejemplo 3 (Ajuste de inventario)
```json
{
  "cantidad": 10,
  "motivo": "Ajuste de inventario - Conteo físico"
}
```

### Respuesta Esperada (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 75,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T11:10:00",
  "requiereReabastecimiento": false
}
```

### Respuesta Error - Cantidad inválida (400 Bad Request)
```json
{
  "timestamp": "2024-11-24T11:12:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La cantidad debe ser mayor a 0",
  "path": "/api/products/1/stock/entrada"
}
```

---

## 🔷 8. SALIDA DE STOCK

### Request
```
POST http://localhost:8080/api/products/1/stock/salida
Content-Type: application/json
```

### Body - Ejemplo 1 (Venta)
```json
{
  "cantidad": 5,
  "motivo": "Venta - Pedido #1234"
}
```

### Body - Ejemplo 2 (Producto defectuoso)
```json
{
  "cantidad": 2,
  "motivo": "Producto defectuoso - Baja por garantía"
}
```

### Body - Ejemplo 3 (Transferencia a sucursal)
```json
{
  "cantidad": 10,
  "motivo": "Transferencia a sucursal Norte"
}
```

### Respuesta Esperada (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 70,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001",
  "ultimaActualizacion": "2024-11-24T11:15:00",
  "requiereReabastecimiento": false
}
```

### Respuesta Error - Stock insuficiente (400 Bad Request)
```json
{
  "timestamp": "2024-11-24T11:16:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Stock insuficiente. Stock actual: 5, Cantidad solicitada: 10",
  "path": "/api/products/1/stock/salida"
}
```

### Respuesta Error - Cantidad inválida (400 Bad Request)
```json
{
  "timestamp": "2024-11-24T11:17:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La cantidad debe ser mayor a 0",
  "path": "/api/products/1/stock/salida"
}
```

---

## 🔷 9. OBTENER PRODUCTOS CON STOCK BAJO (ALERTAS)

### Request
```
GET http://localhost:8080/api/products/alertas
```

### Respuesta Esperada (200 OK)
```json
[
  {
    "id": 3,
    "nombre": "Teclado Mecánico Keychron K2",
    "sku": "TEC-0001",
    "cantidad": 5,
    "stockMinimo": 20,
    "deficit": 15
  },
  {
    "id": 5,
    "nombre": "Monitor Samsung 27 pulgadas",
    "sku": "MON-0001",
    "cantidad": 8,
    "stockMinimo": 12,
    "deficit": 4
  },
  {
    "id": 7,
    "nombre": "Webcam Logitech C920",
    "sku": "WEB-0001",
    "cantidad": 2,
    "stockMinimo": 5,
    "deficit": 3
  }
]
```

### Respuesta - Sin alertas (200 OK)
```json
[]
```

---

## 🔷 10. BUSCAR PRODUCTOS POR NOMBRE

### Request - Ejemplo 1
```
GET http://localhost:8080/api/products/search?nombre=laptop
```

### Request - Ejemplo 2
```
GET http://localhost:8080/api/products/search?nombre=logitech
```

### Request - Ejemplo 3
```
GET http://localhost:8080/api/products/search?nombre=mecánico
```

### Respuesta Esperada (200 OK)
```json
[
  {
    "id": 1,
    "nombre": "Laptop Dell XPS 15",
    "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
    "cantidad": 25,
    "stockMinimo": 10,
    "precio": 1299.99,
    "sku": "LAP-0001",
    "ultimaActualizacion": "2024-11-24T10:30:00",
    "requiereReabastecimiento": false
  },
  {
    "id": 4,
    "nombre": "Laptop HP Pavilion",
    "descripcion": "Laptop multimedia con AMD Ryzen 5, 8GB RAM, 256GB SSD",
    "cantidad": 15,
    "stockMinimo": 8,
    "precio": 799.99,
    "sku": "LAP-0002",
    "ultimaActualizacion": "2024-11-24T10:33:00",
    "requiereReabastecimiento": false
  }
]
```

### Respuesta - Sin resultados (200 OK)
```json
[]
```

---

## 🔷 11. FILTRAR PRODUCTOS POR RANGO DE PRECIO

### Request - Ejemplo 1 (Productos económicos)
```
GET http://localhost:8080/api/products/precio?min=0&max=100
```

### Request - Ejemplo 2 (Rango medio)
```
GET http://localhost:8080/api/products/precio?min=100&max=500
```

### Request - Ejemplo 3 (Productos premium)
```
GET http://localhost:8080/api/products/precio?min=1000&max=2000
```

### Respuesta Esperada (200 OK)
```json
[
  {
    "id": 2,
    "nombre": "Mouse Logitech MX Master",
    "descripcion": null,
    "cantidad": 50,
    "stockMinimo": 15,
    "precio": 89.99,
    "sku": "MOU-0001",
    "ultimaActualizacion": "2024-11-24T10:31:00",
    "requiereReabastecimiento": false
  }
]
```

---

## 🔧 Casos de Validación y Errores

### ❌ Crear producto con SKU duplicado
```
POST http://localhost:8080/api/products
```
```json
{
  "nombre": "Otro producto",
  "cantidad": 10,
  "stockMinimo": 5,
  "precio": 99.99,
  "sku": "LAP-0001"
}
```
**Respuesta (409 Conflict):**
```json
{
  "timestamp": "2024-11-24T11:20:00",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un producto con el SKU 'LAP-0001'",
  "path": "/api/products"
}
```

---

### ❌ Crear producto con SKU inválido
```json
{
  "nombre": "Producto Test",
  "cantidad": 10,
  "stockMinimo": 5,
  "precio": 99.99,
  "sku": "invalid-sku"
}
```
**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2024-11-24T11:22:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El SKU debe tener el formato: 3 letras mayúsculas, guión, 4 números (ej: PRD-0001)",
  "path": "/api/products"
}
```

---

### ❌ Crear producto con precio negativo
```json
{
  "nombre": "Producto Test",
  "cantidad": 10,
  "stockMinimo": 5,
  "precio": -50.00,
  "sku": "TST-0001"
}
```
**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2024-11-24T11:23:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El precio debe ser mayor a 0",
  "path": "/api/products"
}
```

---

### ❌ Crear producto con cantidad negativa
```json
{
  "nombre": "Producto Test",
  "cantidad": -5,
  "stockMinimo": 5,
  "precio": 99.99,
  "sku": "TST-0001"
}
```
**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2024-11-24T11:24:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La cantidad no puede ser negativa",
  "path": "/api/products"
}
```

---

### ❌ Crear producto sin nombre
```json
{
  "nombre": "",
  "cantidad": 10,
  "stockMinimo": 5,
  "precio": 99.99,
  "sku": "TST-0001"
}
```
**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2024-11-24T11:25:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El nombre es obligatorio",
  "path": "/api/products"
}
```

---

## 📊 Conjunto de Datos de Prueba Completo

Para probar todas las funcionalidades, crea estos productos en orden:

### Producto 1 - Stock Normal
```json
{
  "nombre": "Laptop Dell XPS 15",
  "descripcion": "Laptop profesional con procesador Intel i7, 16GB RAM, 512GB SSD",
  "cantidad": 25,
  "stockMinimo": 10,
  "precio": 1299.99,
  "sku": "LAP-0001"
}
```

### Producto 2 - Stock Alto
```json
{
  "nombre": "Mouse Logitech MX Master",
  "descripcion": "Mouse ergonómico inalámbrico para productividad",
  "cantidad": 50,
  "stockMinimo": 15,
  "precio": 89.99,
  "sku": "MOU-0001"
}
```

### Producto 3 - Stock Bajo (Alerta)
```json
{
  "nombre": "Teclado Mecánico Keychron K2",
  "descripcion": "Teclado mecánico inalámbrico RGB, switches blue",
  "cantidad": 5,
  "stockMinimo": 20,
  "precio": 129.99,
  "sku": "TEC-0001"
}
```

### Producto 4 - Rango de precio medio
```json
{
  "nombre": "Monitor Samsung 27 pulgadas",
  "descripcion": "Monitor 4K IPS, 60Hz, HDR10",
  "cantidad": 8,
  "stockMinimo": 12,
  "precio": 449.99,
  "sku": "MON-0001"
}
```

### Producto 5 - Producto económico
```json
{
  "nombre": "Cable HDMI 2m",
  "descripcion": "Cable HDMI 2.0 de alta velocidad",
  "cantidad": 100,
  "stockMinimo": 30,
  "precio": 12.99,
  "sku": "CAB-0001"
}
```

### Producto 6 - Stock crítico (Alerta máxima)
```json
{
  "nombre": "Webcam Logitech C920",
  "descripcion": "Webcam Full HD 1080p con micrófono dual",
  "cantidad": 2,
  "stockMinimo": 15,
  "precio": 79.99,
  "sku": "WEB-0001"
}
```

---

## 🎯 Flujo de Prueba Recomendado

1. **Crear productos** (POST) - Crear los 6 productos de prueba
2. **Listar todos** (GET) - Verificar que se crearon correctamente
3. **Buscar por ID** (GET) - Probar con ID existente y no existente
4. **Buscar por SKU** (GET) - Probar con SKU válido e inválido
5. **Entrada de stock** (POST) - Añadir 20 unidades al producto con ID 3
6. **Salida de stock** (POST) - Restar 5 unidades del producto con ID 1
7. **Ver alertas** (GET) - Verificar productos con stock bajo
8. **Buscar por nombre** (GET) - Buscar "logitech"
9. **Filtrar por precio** (GET) - Productos entre 50 y 150
10. **Actualizar producto** (PUT) - Cambiar precio del producto con ID 1
11. **Intentar salida mayor al stock** (POST) - Error esperado
12. **Intentar crear SKU duplicado** (POST) - Error esperado
13. **Eliminar producto** (DELETE) - Eliminar el producto con ID 6
14. **Listar todos nuevamente** (GET) - Verificar que se eliminó

---

**¡Listo para probar en Postman! 🚀**