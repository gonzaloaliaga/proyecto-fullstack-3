# Inventory Service

## Propósito
Microservicio de gestión de inventario. Administra los centros de acopio y el stock de recursos almacenados en cada uno.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build ms-inventory inventory-db
```

### Local
```bash
cd backend/ms-inventory
./gradlew bootRun
```
> Requiere PostgreSQL corriendo en `localhost:5432` con base de datos `inventory-db`.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC + Spring Data JPA) |
| Librerías | Flyway, PostgreSQL Driver, Jackson Kotlin, SpringDoc OpenAPI 3.0.3, Bean Validation |
| Patrones de diseño | Layered Architecture (Controller → Service → Repository), DTO Pattern, Aggregate Root (CollectionCenter contiene InventoryItems) |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/collection-centers` | Crear centro de acopio |
| GET | `/api/collection-centers` | Listar centros (filtros por región o activos) |
| GET | `/api/collection-centers/{id}` | Obtener centro por ID |
| PATCH | `/api/collection-centers/{id}` | Actualizar centro |
| DELETE | `/api/collection-centers/{id}` | Eliminar centro |
| POST | `/api/inventory-items` | Registrar item de inventario |
| GET | `/api/inventory-items` | Listar items (filtro por centro) |
| GET | `/api/inventory-items/{id}` | Obtener item por ID |
| PATCH | `/api/inventory-items/{id}/stock` | Ajustar stock (suma o resta) |
| DELETE | `/api/inventory-items/{id}` | Eliminar item |

## Documentación API
Disponible en `http://localhost:8084/swagger-ui.html` cuando el servicio está corriendo.
