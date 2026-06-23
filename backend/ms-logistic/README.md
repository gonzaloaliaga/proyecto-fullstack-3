# Logistic Service

## Propósito
Microservicio de logística. Gestiona las necesidades reportadas en terreno, los transportes disponibles y la planificación de envíos de recursos.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build ms-logistic logistic-db
```

### Local
```bash
cd backend/ms-logistic
./gradlew bootRun
```
> Requiere PostgreSQL corriendo en `localhost:5432` con base de datos `logistic-db`.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC + Spring Data JPA) |
| Librerías | Flyway, PostgreSQL Driver, Jackson Kotlin, SpringDoc OpenAPI 3.0.3, Bean Validation |
| Patrones de diseño | Layered Architecture (Controller → Service → Repository), DTO Pattern, Enum State Machine, Aggregate (Shipment agrega Need y Transport) |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |

## Endpoints — Necesidades

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/needs` | Reportar nueva necesidad |
| GET | `/api/needs` | Listar necesidades (filtros por estado o ubicación) |
| GET | `/api/needs/{id}` | Obtener necesidad por ID |
| PATCH | `/api/needs/{id}/status` | Actualizar estado de necesidad |
| DELETE | `/api/needs/{id}` | Eliminar necesidad |

## Endpoints — Transportes

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/transports` | Registrar transporte |
| GET | `/api/transports` | Listar transportes (filtro por disponibilidad) |
| GET | `/api/transports/{id}` | Obtener transporte por ID |
| PATCH | `/api/transports/{id}/availability` | Cambiar disponibilidad |
| DELETE | `/api/transports/{id}` | Eliminar transporte |

## Endpoints — Envíos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/shipments` | Planificar envío |
| GET | `/api/shipments` | Listar envíos (filtros por estado u origen) |
| GET | `/api/shipments/{id}` | Obtener envío por ID |
| PATCH | `/api/shipments/{id}/transport` | Asignar transporte a envío |
| PATCH | `/api/shipments/{id}/status` | Actualizar estado de envío |
| DELETE | `/api/shipments/{id}` | Eliminar envío |

## Ciclo de vida de una necesidad
REPORTED → IN_PROGRESS → COVERED → CANCELLED

## Ciclo de vida de un envío
PLANNED → IN_TRANSIT → DELIVERED → CANCELLED
