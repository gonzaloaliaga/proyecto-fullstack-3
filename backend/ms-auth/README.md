# Auth Service

## Propósito
Servicio de autenticación principal responsable de la emisión y validación de tokens JWT para los usuarios.

## Funcionalidades
- Autenticación de usuarios mediante `POST /api/auth/login`
- Actualización segura del nombre de usuario mediante `POST /api/auth/update-username`
- Generación de tokens JWT con `id` y `username`

## Endpoints
- `POST /api/auth/login`
  - Request body: `{ "username": string, "password": string }`
  - Response: `{ "id": number, "username": string, "token": string }`

- `POST /api/auth/update-username`
  - Headers: `Authorization: Bearer <token>`
  - Request body: `{ "newUsername": string }`
  - Response: `{ "message": string, "username": string }`

## Tecnologías
- Kotlin 2.2.21
- Spring Boot 4.0.5
- Spring Data JPA
- PostgreSQL 16
- Flyway
- JJWT (JSON Web Token)

## Variables de entorno
- `JWT_SECRET`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Ejecución
### Docker Compose
```bash
docker compose up --build auth-service auth-db
```

> Nota: este servicio requiere la base de datos y los secrets de PostgreSQL definidos en `docker-compose.yml`. La ejecución local directa con `./gradlew bootRun` no está preconfigurada para este entorno sin proporcionar manualmente las variables de entorno necesarias.

## Observaciones
Este servicio usa la base de datos PostgreSQL configurada en `docker-compose.yml` y emite tokens que el `bff-service` valida antes de permitir el acceso a otros microservicios.
