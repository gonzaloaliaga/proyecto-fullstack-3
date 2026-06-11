# Donations Service

## Propósito
Microservicio orientado a la gestión de donaciones y su persistencia.

## Estado actual
- Servicio definido como aplicación Spring Boot
- Contiene la configuración base y puntos de entrada
- No se han añadido controladores REST específicos en el código actual

## Tecnologías
- Kotlin 2.2.21
- Spring Boot 4.0.5
- PostgreSQL 16

## Variables de entorno
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Ejecución
### Docker Compose
```bash
docker compose up --build donations-service donations-db
```

> Nota: este servicio usa PostgreSQL y debe levantarse junto a su base de datos mediante Docker Compose.

## Observaciones
Este servicio posee una base de aplicación lista para añadir lógica de donaciones y API REST.
