# Arquitectura del sistema
![Diagrama de Arquitectura](./docs/diagrama_sistema.jpg)

## Descripción
Proyecto de microservicios con un BFF que centraliza autenticación y datos de perfil para el frontend.

## Arquitectura
El proyecto está compuesto por los siguientes servicios:
- `frontend-service`: aplicación React + Vite
- `bff-service`: gateway que valida JWT y reenvía peticiones
- `auth-service`: emite y valida JWT
- `profile-service`: gestiona perfiles de usuario
- `donations-service`: microservicio de donaciones (skeleton)
- `inventory-service`: microservicio de inventario (skeleton)
- `logistic-service`: microservicio de logística (skeleton)
- Bases de datos PostgreSQL para cada microservicio

## Tecnologías
### Frontend
- React 19.2.5
- Vite 8.0.10
- Tailwind CSS 4.3.0
- Node.js

### Backend
- Kotlin 2.2.21
- Spring Boot 4.0.5
- JDK 21
- Spring Data JPA
- JJWT

### Infraestructura
- Docker
- Docker Compose
- PostgreSQL 16

## Arquetipos de Arquitectura
Los arquetipos son patrones estructurales a nivel de arquitectura del sistema:

- **Microservicios**: servicios independientes por responsabilidad (`auth-service`, `profile-service`, `donations-service`, `inventory-service`, `logistic-service`) con despliegue y base de datos aislados en Docker Compose.
  
- **BFF (Backend For Frontend)**: puerta única del frontend (`bff-service/`) que centraliza autorización con JWT y actúa como gateway para reenviar peticiones a los microservicios internos.

## Patrones de Diseño
Los patrones son soluciones específicas implementadas en el código:

- **JWT Authentication**: flujo stateless donde el token portador se valida en el BFF mediante `JwtValidationFilter` y se emite en `auth-service` mediante `JwtService`.

- **Repository Pattern**: acceso a datos abstracto a través de interfaces `JpaRepository`. Implementado en: `auth-service` y `profile-service`.

- **Strategy Pattern**: autenticación configurable mediante estrategias de validación. Definido en `auth-service` con implementaciones `SimplePasswordStrategy` y `EnhancedSecurityStrategy` que permiten cambiar el método de autenticación sin modificar el controlador.

- **Factory Pattern**: creación centralizada de respuestas de autenticación en `auth-service`, usado por `AuthController` para construir `AuthResponse` con los datos del usuario y token.

- **Provider / Context Pattern**: gestión de estado de sesión en React mediante `AuthContext`. Proporciona `AuthProvider` y hook `useAuth()` para acceder al estado de autenticación en toda la aplicación sin prop drilling.

## Flujo de autenticación
1. El frontend envía credenciales a `POST http://localhost:8080/api/auth/login`
2. El `bff-service` reenvía la solicitud a `auth-service`
3. `auth-service` genera un JWT con `id` y `username`
4. El frontend guarda el token en `localStorage`
5. Las peticiones posteriores al BFF incluyen `Authorization: Bearer <token>`
6. El BFF valida el JWT antes de reenviar a los servicios internos

## Servicios principales y endpoints expuestos
### `auth-service`
- `POST /api/auth/login`
- `POST /api/auth/update-username`

### `bff-service`
- `POST /api/auth/login`
- `POST /api/auth/update-username`
- `GET /api/profile/{userId}`
- `PATCH /api/profile/{userId}`

### `profile-service`
- `GET /api/profile/{userId}`
- `PATCH /api/profile/{userId}`

## Ejecución con Docker Compose
```bash
docker compose up --build
```

Una vez que se levanten los servicios:
- Frontend: `http://localhost:5173`
- BFF: `http://localhost:8080`

## Ejecución del proyecto
El proyecto completo debe levantarse con Docker Compose, ya que los servicios con bases de datos dependen de secrets y credenciales que no están preconfiguradas para ejecución local directa.

### Frontend local
El frontend puede ejecutarse localmente para desarrollo, pero para una integración completa se requiere que el BFF y los servicios backend estén levantados con Docker Compose.
```bash
cd frontend-service
npm install
npm run dev
```

### Backend completo con Docker Compose
```bash
docker compose up --build
```

## Variables de entorno clave
- `JWT_SECRET`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVICES_AUTH_URL`
- `SERVICES_PROFILE_URL`

## Notas
- El sistema usa PostgreSQL 16 en Docker Compose.
- El frontend consume el BFF en `http://localhost:8080`.
- Los servicios con base de datos requieren los secrets y variables de entorno definidos en `docker-compose.yml`.
- El despliegue completo debe ejecutarse con `docker compose up --build`.
- Los microservicios `donations-service`, `inventory-service` y `logistic-service` están actualmente como esqueleto y pueden ampliarse con controladores REST adicionales.
