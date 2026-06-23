# BFF Service

## Propósito
Backend For Frontend que actúa como única puerta de entrada para el frontend. Valida JWT y distribuye las peticiones a los microservicios internos.

## Instrucciones de ejecución

### Docker Compose
```bash
docker compose up --build bff
```

### Local
```bash
cd backend/bff
./gradlew bootRun
```
> Requiere las variables de entorno `JWT_PUBLIC_KEY`, y los microservicios internos corriendo.

## Tabla técnica

| Categoría | Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.5 (Spring MVC) |
| Librerías | java-jwt 4.4.0, Apache HttpClient5 5.2.1, Jackson Kotlin |
| Patrones de diseño | BFF (Backend For Frontend), Proxy, Chain of Responsibility (filtro JWT) |

## Variables de entorno

| Variable | Descripción |
|---|---|
| `JWT_PUBLIC_KEY` | Clave pública RSA en Base64 para validar tokens |
| `SERVICES_AUTH_URL` | URL interna de ms-auth |
| `SERVICES_PROFILE_URL` | URL interna de ms-profile |
| `SERVICES_DONATIONS_URL` | URL interna de ms-donation |
| `SERVICES_INVENTORY_URL` | URL interna de ms-inventory |
| `SERVICES_LOGISTIC_URL` | URL interna de ms-logistic |

## Endpoints expuestos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Login (público) |
| GET | `/api/profile/{userId}` | Obtener perfil |
| PUT | `/api/profile/{userId}` | Actualizar perfil |
| GET | `/api/donations` | Listar donaciones |
| POST | `/api/donations` | Crear donación |
| PATCH | `/api/donations/{id}/status` | Actualizar estado donación |
| GET | `/api/collection-centers` | Listar centros de acopio |
| GET | `/api/inventory-items` | Listar items de inventario |
| GET | `/api/needs` | Listar necesidades |
| GET | `/api/shipments` | Listar envíos |
