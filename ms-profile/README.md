# Profile Service

## Propósito
Microservicio responsable de la gestión y persistencia del perfil de usuario.

## Funcionalidades
- Lectura del perfil de usuario mediante `GET /api/profile/{userId}`
- Actualización parcial del perfil mediante `PATCH /api/profile/{userId}`

## Endpoints
- `GET /api/profile/{userId}`
  - Response: objeto `Profile`
- `PATCH /api/profile/{userId}`
  - Request body: campos opcionales `role`, `email`, `address`, `run`

## Tecnologías
- Kotlin 2.2.21
- Spring Boot 4.0.5
- Spring Data JPA
- PostgreSQL 16
- Flyway

## Variables de entorno
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Ejecución
### Docker Compose
```bash
docker compose up --build profile-service profile-db
```

> Nota: este servicio requiere la base de datos de PostgreSQL y las variables de entorno definidas en `docker-compose.yml`. La ejecución local directa no está preconfigurada sin los secrets de base de datos.

## Observaciones
El servicio persiste los datos del perfil y recibe las peticiones frontend a través del `bff-service`.
