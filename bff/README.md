# BFF Service

## Propósito
Servicio Backend For Frontend (BFF) que actúa como puerta de acceso al ecosistema de microservicios.

## Funcionalidades
- Expone endpoints de autenticación y perfil al frontend
- Valida el token JWT en las peticiones entrantes
- Reenvía las solicitudes autorizadas a `auth-service` y `profile-service`

## Endpoints
- `POST /api/auth/login`
  - Reenvía a `auth-service`
- `POST /api/auth/update-username`
  - Reenvía a `auth-service` con el header `Authorization`
- `GET /api/profile/{userId}`
  - Reenvía a `profile-service` con el header `Authorization`
- `PATCH /api/profile/{userId}`
  - Reenvía a `profile-service` con el header `Authorization`

## Tecnologías
- Kotlin 2.2.21
- Spring Boot 4.0.5
- Spring MVC
- JJWT
- Apache HttpClient

## Variables de entorno
- `JWT_SECRET`
- `SERVICES_AUTH_URL`
- `SERVICES_PROFILE_URL`
- `SERVICES_DONATIONS_URL`
- `SERVICES_INVENTORY_URL`
- `SERVICES_LOGISTIC_URL`

## Ejecución
### Local
```bash
cd bff-service
./gradlew bootRun
```

### Docker Compose
```bash
docker compose up --build bff-service
```

## Observaciones
Este gateway protege los endpoints del frontend, valida el JWT y garantiza que solo las solicitudes con token válido lleguen a los servicios internos.
