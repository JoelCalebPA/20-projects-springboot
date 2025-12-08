# Solution - Sistema de Gestión de Contactos

## 📚 Documentación Técnica

Esta carpeta contiene la implementación completa y comentada del Sistema de Contactos.

---

## 🏗️ Arquitectura de la Solución

```
com.contacts
├── entity/
│   └── Contact.java                 → Entidad JPA con validaciones
├── dto/
│   ├── ContactCreateDTO.java        → DTO para creación
│   ├── ContactUpdateDTO.java        → DTO para actualización parcial
│   └── ContactResponseDTO.java      → DTO respuesta
├── repository/
│   └── ContactRepository.java       → Queries personalizadas
├── service/
│   └── ContactService.java          → Lógica de negocio + mapeo
└── controller/
    └── ContactController.java       → REST endpoints
```

---

## 🔧 Componentes Implementados

### 1. Contact.java (Entity)

**Características Clave:**

```java
@Entity
@Table(name = "contacts")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @Column(nullable = false, unique = true)
    private String email;  // Índice único en BD
    
    private LocalDate birthDate;  // Java 8 Date API
    
    @Column(length = 500)
    private String notes;
}
```

**Decisiones de Diseño:**
- `unique = true` en email: Constraint a nivel BD
- `LocalDate` para fechas: Sin hora, formato ISO
- `@Column(length)`: Límites explícitos en BD

---

### 2. ContactCreateDTO.java

**Propósito:** Recibir datos para crear contacto

```java
public class ContactCreateDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre entre 2 y 50 caracteres")
    private String firstName;
    
    @NotBlank
    @Email(message = "Email debe ser válido")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono inválido")
    private String phone;  // Nullable
    
    @Past(message = "Fecha de nacimiento debe ser pasada")
    private LocalDate birthDate;  // Nullable
}
```

**Validaciones:**
- `@NotBlank`: String no vacío ni solo espacios
- `@Email`: Formato email válido
- `@Pattern`: Regex para teléfonos internacionales
- `@Past`: LocalDate anterior a hoy
- `@Size`: Longitud min/max

---

### 3. ContactUpdateDTO.java

**Propósito:** Actualización parcial (todos los campos opcionales)

```java
public class ContactUpdateDTO {
    
    // Todos los campos son opcionales (@Nullable implícito)
    
    @Size(min = 2, max = 50, message = "Nombre entre 2 y 50 caracteres")
    private String firstName;  // Si null, no se actualiza
    
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono inválido")
    private String phone;
    
    // NO incluye email (no se puede cambiar)
    // NO incluye id (viene en URL)
}
```

**Patrón PATCH:**
- Solo campos presentes se actualizan
- `null` = "no modificar este campo"
- Validaciones solo aplican si el campo se envía

---

### 4. ContactResponseDTO.java

**Propósito:** Respuesta segura

```java
public class ContactResponseDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String notes;
    
    // Constructor que recibe Entity Contact
    public ContactResponseDTO(Contact contact) {
        this.id = contact.getId();
        this.firstName = contact.getFirstName();
        // ... mapeo manual
    }
}
```

**Características:**
- Constructor desde Entity facilita mapeo
- Usado en TODAS las respuestas del controller

---

### 5. ContactRepository.java

**Queries Personalizadas:**

```java
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    /**
     * Buscar contacto por email (único)
     * @return Optional vacío si no existe
     */
    Optional<Contact> findByEmail(String email);
    
    /**
     * Verificar si existe un email (para validación)
     * Más eficiente que findByEmail cuando solo necesitas boolean
     */
    boolean existsByEmail(String email);
}
```

**Patrones:**
- `Optional<>`: Manejo explícito de ausencia
- Query methods de Spring Data: Genera SQL automáticamente
- `existsBy`: Optimizado para checks booleanos

---

### 6. ContactService.java

**Lógica de Negocio Completa:**

#### Inyección de Dependencias
```java
@Service
public class ContactService {
    
    @Autowired
    private ContactRepository repository;
}
```

#### Crear Contacto
```java
public ContactResponseDTO createContact(ContactCreateDTO dto) {
    // 1. Validar email único
    if (repository.existsByEmail(dto.getEmail())) {
        throw new RuntimeException("Email ya existe: " + dto.getEmail());
    }
    
    // 2. Mapear DTO → Entity
    Contact contact = new Contact();
    contact.setFirstName(dto.getFirstName());
    contact.setLastName(dto.getLastName());
    contact.setEmail(dto.getEmail());
    contact.setPhone(dto.getPhone());
    contact.setAddress(dto.getAddress());
    contact.setBirthDate(dto.getBirthDate());
    contact.setNotes(dto.getNotes());
    
    // 3. Guardar
    Contact saved = repository.save(contact);
    
    // 4. Retornar DTO respuesta
    return new ContactResponseDTO(saved);
}
```

#### Actualizar Contacto (Parcial)
```java
public ContactResponseDTO updateContact(Long id, ContactUpdateDTO dto) {
    // 1. Buscar contacto existente
    Contact contact = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Contacto no encontrado: " + id));
    
    // 2. Actualizar SOLO campos presentes (patrón PATCH)
    if (dto.getFirstName() != null) {
        contact.setFirstName(dto.getFirstName());
    }
    if (dto.getLastName() != null) {
        contact.setLastName(dto.getLastName());
    }
    if (dto.getPhone() != null) {
        contact.setPhone(dto.getPhone());
    }
    if (dto.getAddress() != null) {
        contact.setAddress(dto.getAddress());
    }
    if (dto.getBirthDate() != null) {
        contact.setBirthDate(dto.getBirthDate());
    }
    if (dto.getNotes() != null) {
        contact.setNotes(dto.getNotes());
    }
    
    // 3. Guardar cambios
    Contact updated = repository.save(contact);
    
    // 4. Retornar DTO respuesta
    return new ContactResponseDTO(updated);
}
```

**Flujo de Actualización:**
1. Buscar entidad existente
2. Actualizar solo campos != null
3. Guardar en BD
4. Mapear a DTO respuesta

#### Buscar por Email
```java
public ContactResponseDTO findByEmail(String email) {
    Contact contact = repository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Contacto no encontrado con email: " + email));
    return new ContactResponseDTO(contact);
}
```

**Uso de Optional:**
- `orElseThrow()`: Convierte Optional vacío en excepción
- Alternativa: `orElse(defaultValue)`, `ifPresent(lambda)`

---

### 7. ContactController.java

**REST API Completa:**

```java
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    @Autowired
    private ContactService service;
    
    // POST /api/contacts
    @PostMapping
    public ResponseEntity<ContactResponseDTO> create(@Valid @RequestBody ContactCreateDTO dto) {
        ContactResponseDTO created = service.createContact(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // 201
    }
    
    // GET /api/contacts
    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllContacts());  // 200
    }
    
    // GET /api/contacts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getContactById(id));
    }
    
    // GET /api/contacts/email/{email}
    @GetMapping("/email/{email}")
    public ResponseEntity<ContactResponseDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }
    
    // PATCH /api/contacts/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ContactResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ContactUpdateDTO dto) {
        return ResponseEntity.ok(service.updateContact(id, dto));
    }
    
    // DELETE /api/contacts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteContact(id);
        return ResponseEntity.noContent().build();  // 204
    }
}
```

**Detalles Técnicos:**
- `@Valid`: Activa validaciones Bean Validation
- `@RequestBody`: Deserializa JSON → DTO
- `@PathVariable`: Extrae variable de URL
- `ResponseEntity<>`: Control completo de respuesta HTTP
- `@PatchMapping`: Actualización parcial (vs PUT = reemplazo completo)

---

## 🧪 Casos de Prueba Implementados

### Test 1: Actualización Parcial
```json
// Request: Solo actualizar teléfono
PATCH /api/contacts/1
{
  "phone": "+34655111222"
}

// Verificar que otros campos no cambiaron
```

### Test 2: Email Único
```java
// Intentar crear dos contactos con mismo email → Error 409
```

### Test 3: Validaciones
```json
// Intentar crear sin firstName → Error 400
// Intentar crear con email inválido → Error 400
```

---

## 📊 Flujo de Datos

### Crear Contacto
```
Cliente → ContactCreateDTO
    ↓
Controller: @Valid valida DTO
    ↓
Service: 
    - Verifica email único
    - Mapea DTO → Entity
    - Guarda en BD
    ↓
Repository: INSERT en MySQL
    ↓
Service: Mapea Entity → ContactResponseDTO
    ↓
Controller: ResponseEntity 201 Created
    ↓
Cliente ← ContactResponseDTO
```

### Actualizar Contacto
```
Cliente → ContactUpdateDTO (campos parciales)
    ↓
Controller: @Valid valida campos presentes
    ↓
Service:
    - Busca contacto por ID
    - Actualiza SOLO campos != null
    - Guarda cambios
    ↓
Repository: UPDATE en MySQL
    ↓
Service: Mapea Entity → ContactResponseDTO
    ↓
Cliente ← ContactResponseDTO actualizado
```

---

## 🎯 Conceptos Dominados

### 1. DTOs Diferenciados
- **CreateDTO**: Campos obligatorios para creación
- **UpdateDTO**: Todos opcionales + actualización parcial
- **ResponseDTO**: Incluye ID generado

### 2. Patrón PATCH
- Solo actualizar campos presentes
- `null` = no modificar
- Diferente de PUT (reemplazo completo)

### 3. Validaciones Bean Validation
- `@NotBlank`, `@Email`, `@Size`, `@Pattern`, `@Past`
- `@Valid` en Controller activa validaciones
- Mensajes personalizados

### 4. Manejo de Optional
- `findByEmail()` → `Optional<Contact>`
- `.orElseThrow()` para lanzar excepción
- `.existsByEmail()` para checks booleanos

### 5. Mapeo Manual
- Constructor en ResponseDTO facilita mapeo
- Mapeo explícito DTO → Entity en Service

---

## 🚀 Mejoras Futuras (Fuera del Alcance)

- [ ] Paginación en GET all
- [ ] Búsqueda por nombre/teléfono con filtros
- [ ] Auditoría (createdAt, updatedAt)
- [ ] Soft delete
- [ ] MapStruct para mapeo automático
- [ ] Tests unitarios con JUnit + Mockito
- [ ] Manejo de excepciones con @ControllerAdvice

---

## 📝 Lecciones Aprendidas

1. **Separación de DTOs es fundamental**: Diferentes necesidades = diferentes objetos
2. **Actualizaciones parciales requieren lógica explícita**: Check manual de cada campo
3. **Bean Validation ahorra código**: Validaciones declarativas en DTOs
4. **Optional mejora claridad**: Manejo explícito de valores ausentes
5. **Email único requiere validación en Service**: No solo constraint BD

---

## 🎓 Comparación con Proyectos Anteriores

| Aspecto | Proyecto 5 | Proyecto 6 (Actual) |
|---------|-----------|---------------------|
| DTOs | Un solo DTO genérico | 3 DTOs especializados |
| Actualizaciones | PUT completo | PATCH parcial |
| Validaciones | Básicas | Avanzadas + contextuales |
| Búsquedas | Solo por ID | Por ID y por email |
| Email único | No validado | Validado en Service |

---

## ✅ Checklist de Verificación

- [x] Entity Contact sin campos sensibles
- [x] DTOs separados (Create, Update, Response)
- [x] Repository con queries `findByEmail` y `existsByEmail`
- [x] Service mapea Entity ↔ DTO manualmente
- [x] Controller usa `@Valid` para validaciones
- [x] PATCH actualiza solo campos presentes
- [x] Validación email único antes de guardar
- [x] Manejo de errores con RuntimeException

---

**Proyecto Completado con Éxito ✅**

Este proyecto sienta las bases para el uso correcto de DTOs, mostrando cómo manejar diferentes representaciones de un mismo objeto según el contexto y cómo implementar actualizaciones parciales de manera efectiva.