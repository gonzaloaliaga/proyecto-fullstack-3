# Profile Service

## Propósito
Microservicio de perfiles de usuario. Almacena y gestiona el rol, email, dirección y RUN de cada usuario.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build ms-profile profile-db
```

### Local
```bash
cd backend/ms-profile
./gradlew bootRun
```
> Requiere PostgreSQL corriendo en `localhost:5432` con base de datos `profile-db`.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC + Spring Data JPA) |
| Librerías | Flyway, PostgreSQL Driver, Jackson Kotlin |
| Patrones de diseño | Layered Architecture (Controller → Service → Repository), DTO Pattern |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/profile/{userId}` | Obtener perfil de usuario |
| PUT | `/api/profile/{userId}` | Actualizar perfil de usuario |

## Perfiles

| ID | Rol | Email |
|---|---|---|
| 1 | ADMIN | admin@donaton.cl |
| 2 | LOGISTIC | logistic@donaton.cl |
| 3 | VOLUNTEER | volunteer@gmail.com |
| 4 | DONOR | donor@gmail.cl |
