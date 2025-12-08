# Proyecto 6: Sistema de Gestión de Contactos

## 📋 Descripción del Proyecto

Sistema REST API para gestión de contactos personales. Este proyecto introduce el uso de DTOs para separar la representación de datos entre entrada y salida, validaciones avanzadas y búsquedas personalizadas.

## 🎯 Objetivos de Aprendizaje

- Implementar DTOs diferenciados (Create, Update, Response)
- Manejo de actualizaciones parciales (PATCH pattern)
- Validaciones con Bean Validation
- Búsquedas personalizadas en Repository
- Fechas con LocalDate
- Validación de unicidad (email)

## 📊 Modelo de Datos

### Entidad Contact

```java
- id: Long (PK, auto-generado)
- firstName: String (requerido, 2-50 caracteres)
- lastName: String (requerido, 2-50 caracteres)
- email: String (requerido, único, formato email)
- phone: String (opcional, formato válido)
- address: String (opcional, máx 200 caracteres)
- birthDate: LocalDate (opcional)
- notes: String (opcional, máx 500 caracteres)
```

## 📦 DTOs Requeridos

### ContactCreateDTO
```json
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@email.com",
  "phone": "+34600123456",
  "address": "Calle Mayor 123, Madrid",
  "birthDate": "1990-05-15",
  "notes": "Cliente preferente"
}
```
**Características:**
- Sin campo `id` (se genera automáticamente)
- Todas las validaciones aplicadas

### ContactUpdateDTO
```json
{
  "firstName": "Juan Carlos",
  "phone": "+34600999888",
  "notes": "VIP - Contactar urgente"
}
```
**Características:**
- Todos los campos opcionales (excepto validaciones si se envían)
- Solo actualiza campos presentes en el request
- No incluye `id` (viene en URL) ni `email` (email no se puede cambiar)

### ContactResponseDTO
```json
{
  "id": 1,
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@email.com",
  "phone": "+34600123456",
  "address": "Calle Mayor 123, Madrid",
  "birthDate": "1990-05-15",
  "notes": "Cliente preferente"
}
```
**Características:**
- Incluye el `id` generado
- Usado en todas las respuestas de la API
- Mismos campos que la Entity

## 🔌 Endpoints Requeridos

### 1. Crear Contacto
```
POST /api/contacts
Body: ContactCreateDTO
Response: ContactResponseDTO (201 Created)
```

### 2. Obtener Todos los Contactos
```
GET /api/contacts
Response: List<ContactResponseDTO> (200 OK)
```

### 3. Obtener Contacto por ID
```
GET /api/contacts/{id}
Response: ContactResponseDTO (200 OK)
Error: 404 si no existe
```

### 4. Buscar por Email
```
GET /api/contacts/email/{email}
Response: ContactResponseDTO (200 OK)
Error: 404 si no existe
```

### 5. Actualizar Contacto (Parcial)
```
PATCH /api/contacts/{id}
Body: ContactUpdateDTO
Response: ContactResponseDTO (200 OK)
Error: 404 si no existe
```

### 6. Eliminar Contacto
```
DELETE /api/contacts/{id}
Response: 204 No Content
Error: 404 si no existe
```

## ✅ Validaciones Requeridas

### ContactCreateDTO
- `firstName`: @NotBlank, @Size(min=2, max=50)
- `lastName`: @NotBlank, @Size(min=2, max=50)
- `email`: @NotBlank, @Email
- `phone`: @Pattern(regex válido) o null
- `address`: @Size(max=200) o null
- `birthDate`: @Past o null
- `notes`: @Size(max=500) o null

### ContactUpdateDTO
- Todos los campos opcionales (@Nullable)
- Si se proporcionan, mismas validaciones que CreateDTO
- `email` no se puede actualizar (no incluir en DTO)

## 🚨 Manejo de Errores

- `404 Not Found`: ID o email no existe
- `400 Bad Request`: Validaciones fallidas
- `409 Conflict`: Email duplicado al crear
- Mensajes de error claros y descriptivos

## 🏗️ Estructura de Capas

```
com.contacts
├── entity
│   └── Contact.java
├── dto
│   ├── ContactCreateDTO.java
│   ├── ContactUpdateDTO.java
│   └── ContactResponseDTO.java
├── repository
│   └── ContactRepository.java
├── service
│   └── ContactService.java
└── controller
    └── ContactController.java
```

## 🎓 Conceptos Clave a Dominar

1. **Separación de DTOs**: Diferentes objetos para diferentes propósitos
2. **Actualizaciones parciales**: PATCH vs PUT
3. **Validaciones contextuales**: Diferentes validaciones según operación
4. **Mapeo manual**: Entity ↔ DTO sin librerías adicionales
5. **Búsquedas personalizadas**: Query methods en Repository

## 📝 Notas de Implementación

- No usar ModelMapper ni MapStruct (mapeo manual)
- Usar `Optional<Contact>` en Repository para búsquedas
- Validar email único en Service antes de guardar
- Actualizaciones parciales: solo modificar campos presentes

## 🚀 Orden de Desarrollo Sugerido

1. Entity Contact
2. DTOs (Create, Update, Response)
3. Repository
4. Service (con lógica de mapeo)
5. Controller
6. Testing con Postman
