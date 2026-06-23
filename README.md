# Donatón — Sistema de Gestión de Donaciones

Plataforma fullstack de gestión de donaciones, inventario y logística humanitaria. Construida con arquitectura de microservicios en el backend y una SPA React en el frontend, comunicados a través de un BFF (Backend For Frontend).

## Tecnologías

| Capa | Tecnología |
|---|---|
| Frontend | React 19, TypeScript, Tailwind CSS 4, Vite, React Router 7 |
| BFF y Microservicios | Kotlin 2.2.21, Spring Boot 4.0.5, Spring Data JPA |
| Bases de datos | PostgreSQL 16 |
| Autenticación | JWT con RSA (java-jwt 4.4.0) |
| Migraciones | Flyway |
| Contenedores | Docker, Docker Compose |
| Orquestación | Kubernetes |

## Requisitos previos

- Docker Desktop instalado y corriendo
- Git

## Ejecución con Docker Compose

### 1. Clonar el repositorio

```bash
git clone https://github.com/gonzaloaliaga/proyecto-fullstack-3.git
cd proyecto-fullstack-3
```

### 2. Crear el archivo `.env` en la raíz del proyecto

```env
JWT_PRIVATE_KEY=<clave_privada_rsa_en_base64>
JWT_PUBLIC_KEY=<clave_publica_rsa_en_base64>
```

### 3. Levantar todos los servicios

```bash
docker compose up --build
```

### 4. Acceder a la aplicación

Abrir el navegador en `http://localhost:5173`

### Usuarios

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 1111 | ADMIN |
| logistica | 2222 | LOGISTIC |
| voluntario | 3333 | VOLUNTEER |
| donante | 4444 | DONOR |

## Detener el sistema

```bash
docker compose down
```

Para eliminar también los volúmenes de base de datos:

```bash
docker compose down -v
```

## Documentación de cada microservicio

Cada microservicio tiene su propio `README.md` con instrucciones de ejecución individual y tabla técnica detallada:

- [`backend/bff/README.md`](backend/bff/README.md)
- [`backend/ms-auth/README.md`](backend/ms-auth/README.md)
- [`backend/ms-profile/README.md`](backend/ms-profile/README.md)
- [`backend/ms-donation/README.md`](backend/ms-donation/README.md)
- [`backend/ms-inventory/README.md`](backend/ms-inventory/README.md)
- [`backend/ms-logistic/README.md`](backend/ms-logistic/README.md)

## API disponible

Cuando el sistema está corriendo, la documentación Swagger de cada microservicio está disponible en:

| Servicio | URL |
|---|---|
| ms-donation | http://localhost:8083/swagger-ui.html |
| ms-inventory | http://localhost:8084/swagger-ui.html |
| ms-logistic | http://localhost:8085/swagger-ui.html |

## Roles y accesos

| Rol | Acceso |
|---|---|
| ADMIN | Panel general con resumen de donaciones, centros, necesidades y envíos |
| DONOR | Historial de donaciones |
| LOGISTIC | Inventario, necesidades y envíos |
| VOLUNTEER | Panel de tareas de voluntariado |

Todos los roles tienen acceso a la página de perfil para editar sus datos personales.
