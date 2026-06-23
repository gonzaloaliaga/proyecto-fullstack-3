# Auth Service

## Propósito
Microservicio de autenticación. Valida credenciales de usuarios y emite tokens JWT firmados con RSA.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build ms-auth auth-db
```

### Local
```bash
cd backend/ms-auth
./gradlew bootRun
```
> Requiere PostgreSQL corriendo en `localhost:5432` con base de datos `auth-db` y las variables `JWT_PRIVATE_KEY`, `JWT_PUBLIC_KEY`.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC + Spring Data JPA) |
| Librerías | java-jwt 4.4.0, Flyway, PostgreSQL Driver, Jackson Kotlin |
| Patrones de diseño | Layered Architecture (Controller → Service → Repository), Factory (AuthResponseFactory) |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `JWT_PRIVATE_KEY` | Clave privada RSA en Base64 para firmar tokens |
| `JWT_PUBLIC_KEY` | Clave pública RSA en Base64 para verificar tokens |
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Autenticar usuario, retorna JWT |
| POST | `/api/auth/update-username` | Actualizar username (requiere JWT) |

## Usuarios

| Username | Password | 
|---|---|
| admin | 1111 |
| logistica | 2222 |
| voluntario | 3333 |
| donante | 4444 |
