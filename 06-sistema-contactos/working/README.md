## 📬 Endpoints Disponibles

### Base URL
```
http://localhost:8080/api/contacts
```

### 1️⃣ Crear Contacto
**POST** `/api/contacts`

**Body (ContactCreateDTO):**
```json
{
  "firstName": "María",
  "lastName": "García",
  "email": "maria.garcia@email.com",
  "phone": "+34600111222",
  "address": "Av. Constitución 45, Sevilla",
  "birthDate": "1988-03-20",
  "notes": "Contacto comercial - Sector tecnología"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "firstName": "María",
  "lastName": "García",
  "email": "maria.garcia@email.com",
  "phone": "+34600111222",
  "address": "Av. Constitución 45, Sevilla",
  "birthDate": "1988-03-20",
  "notes": "Contacto comercial - Sector tecnología"
}
```

---

### 2️⃣ Obtener Todos los Contactos
**GET** `/api/contacts`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "María",
    "lastName": "García",
    "email": "maria.garcia@email.com",
    "phone": "+34600111222",
    "address": "Av. Constitución 45, Sevilla",
    "birthDate": "1988-03-20",
    "notes": "Contacto comercial - Sector tecnología"
  },
  {
    "id": 2,
    "firstName": "Carlos",
    "lastName": "López",
    "email": "carlos.lopez@email.com",
    "phone": "+34611222333",
    "address": "Calle Luna 12, Barcelona",
    "birthDate": "1992-07-10",
    "notes": "Cliente VIP"
  }
]
```

---

### 3️⃣ Obtener Contacto por ID
**GET** `/api/contacts/{id}`

**Ejemplo:** `GET /api/contacts/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "María",
  "lastName": "García",
  "email": "maria.garcia@email.com",
  "phone": "+34600111222",
  "address": "Av. Constitución 45, Sevilla",
  "birthDate": "1988-03-20",
  "notes": "Contacto comercial - Sector tecnología"
}
```

**Response (404 Not Found):**
```json
{
  "timestamp": "2024-12-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Contacto no encontrado con ID: 99"
}
```

---

### 4️⃣ Buscar por Email
**GET** `/api/contacts/email/{email}`

**Ejemplo:** `GET /api/contacts/email/maria.garcia@email.com`

**Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "María",
  "lastName": "García",
  "email": "maria.garcia@email.com",
  "phone": "+34600111222",
  "address": "Av. Constitución 45, Sevilla",
  "birthDate": "1988-03-20",
  "notes": "Contacto comercial - Sector tecnología"
}
```

---

### 5️⃣ Actualizar Contacto (Parcial)
**PATCH** `/api/contacts/{id}`

**Body (ContactUpdateDTO) - Solo campos a actualizar:**
```json
{
  "phone": "+34655999888",
  "notes": "Contacto VIP - Prioritario",
  "address": "Nueva dirección: Calle Sol 99, Sevilla"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "María",
  "lastName": "García",
  "email": "maria.garcia@email.com",
  "phone": "+34655999888",
  "address": "Nueva dirección: Calle Sol 99, Sevilla",
  "birthDate": "1988-03-20",
  "notes": "Contacto VIP - Prioritario"
}
```

> ℹ️ Solo se actualizan los campos enviados en el body

---

### 6️⃣ Eliminar Contacto
**DELETE** `/api/contacts/{id}`

**Response (204 No Content)**  
Sin body en la respuesta

---

## 🧪 Colección Postman

### Archivo: `Contacts-API.postman_collection.json`

Descarga la colección completa con todos los requests configurados:

```json
{
  "info": {
    "name": "Contacts API - Proyecto 6",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Crear Contacto",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"firstName\": \"María\",\n  \"lastName\": \"García\",\n  \"email\": \"maria.garcia@email.com\",\n  \"phone\": \"+34600111222\",\n  \"address\": \"Av. Constitución 45, Sevilla\",\n  \"birthDate\": \"1988-03-20\",\n  \"notes\": \"Contacto comercial\"\n}"
        },
        "url": {"raw": "http://localhost:8080/api/contacts", "host": ["localhost"], "port": "8080", "path": ["api", "contacts"]}
      }
    },
    {
      "name": "Obtener Todos",
      "request": {
        "method": "GET",
        "url": {"raw": "http://localhost:8080/api/contacts", "host": ["localhost"], "port": "8080", "path": ["api", "contacts"]}
      }
    },
    {
      "name": "Obtener por ID",
      "request": {
        "method": "GET",
        "url": {"raw": "http://localhost:8080/api/contacts/1", "host": ["localhost"], "port": "8080", "path": ["api", "contacts", "1"]}
      }
    },
    {
      "name": "Buscar por Email",
      "request": {
        "method": "GET",
        "url": {"raw": "http://localhost:8080/api/contacts/email/maria.garcia@email.com", "host": ["localhost"], "port": "8080", "path": ["api", "contacts", "email", "maria.garcia@email.com"]}
      }
    },
    {
      "name": "Actualizar Parcial",
      "request": {
        "method": "PATCH",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"phone\": \"+34655999888\",\n  \"notes\": \"Contacto VIP - Prioritario\"\n}"
        },
        "url": {"raw": "http://localhost:8080/api/contacts/1", "host": ["localhost"], "port": "8080", "path": ["api", "contacts", "1"]}
      }
    },
    {
      "name": "Eliminar Contacto",
      "request": {
        "method": "DELETE",
        "url": {"raw": "http://localhost:8080/api/contacts/1", "host": ["localhost"], "port": "8080", "path": ["api", "contacts", "1"]}
      }
    }
  ]
}
```

**Para importar en Postman:**
1. Copia el JSON anterior
2. Postman → Import → Raw text → Pega el JSON
3. O guarda como `.json` e importa el archivo

---

## ✅ Checklist de Desarrollo

### Fase 1: Configuración Base
- [ ] Crear base de datos `contacts_db`
- [ ] Configurar `application.properties`

### Fase 2: Capa de Datos
- [ ] Crear Entity `Contact`
- [ ] Crear los 3 DTOs (Create, Update, Response)
- [ ] Crear `ContactRepository`

### Fase 3: Lógica de Negocio
- [ ] Crear `ContactService`
- [ ] Implementar mapeo Entity ↔ DTO
- [ ] Validar email único

### Fase 4: API REST
- [ ] Crear `ContactController`
- [ ] Implementar los 6 endpoints
- [ ] Probar con Postman

### Fase 5: Validación
- [ ] Probar todos los endpoints
- [ ] Verificar actualizaciones parciales
- [ ] Verificar manejo de errores

---

## 🎯 Casos de Prueba Importantes

### ✅ Actualización Parcial
1. Crear contacto con todos los campos
2. Actualizar solo `phone` y `notes`
3. Verificar que otros campos permanecen intactos

### ✅ Email Único
1. Crear contacto con email "test@email.com"
2. Intentar crear otro contacto con el mismo email
3. Debe retornar error 409 Conflict

### ✅ Validaciones
1. Intentar crear contacto sin firstName → 400 Bad Request
2. Intentar crear con email inválido → 400 Bad Request
3. Intentar actualizar con phone inválido → 400 Bad Request

---

## 🚨 Errores Comunes a Evitar

❌ No validar email único antes de guardar  
❌ Usar PUT en lugar de PATCH para actualizaciones parciales  
❌ No actualizar solo los campos presentes en UpdateDTO  
❌ Olvidar validaciones en los DTOs  

---

## 📞 ¿Necesitas Ayuda?

Desarrollo clase por clase:
1. Creas la clase
2. Recibes feedback + código corregido con comentarios
3. Siguiente clase

**¡Empieza por `Contact.java` cuando estés listo!**