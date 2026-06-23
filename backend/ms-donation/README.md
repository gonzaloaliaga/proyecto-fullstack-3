# Donation Service

## Propósito
Microservicio de gestión de donaciones. Permite registrar, listar y actualizar el estado del ciclo de vida de cada donación.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build ms-donation donation-db
```

### Local
```bash
cd backend/ms-donation
./gradlew bootRun
```
> Requiere PostgreSQL corriendo en `localhost:5432` con base de datos `donation-db`.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC + Spring Data JPA) |
| Librerías | Flyway, PostgreSQL Driver, Jackson Kotlin, SpringDoc OpenAPI 2.5.0, Bean Validation |
| Patrones de diseño | Layered Architecture (Controller → Service → Repository), DTO Pattern, Enum State Machine |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/donations` | Registrar nueva donación |
| GET | `/api/donations` | Listar donaciones (filtros opcionales por centro o estado) |
| GET | `/api/donations/{id}` | Obtener donación por ID |
| PATCH | `/api/donations/{id}/status` | Actualizar estado de donación |
| DELETE | `/api/donations/{id}` | Eliminar donación |

## Ciclo de vida de una donación
PENDING → RECEIVED → ASSIGNED → DELIVERED
