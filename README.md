# DONATON - Gestión Humanitaria Tecnológica

## Índice
1. [Contexto](#contexto)
2. [Problemática y necesidades](problemática-y-necesidades)
3. [Levantamiento de requerimientos](levantamiento-de-requerimientos)
4. [Casos de Uso](casos-de-uso)
5. [Propuesta de Solución y Arquitectura](#propuesta-de-solución-y-arquitectura)
6. [Consideraciones éticas](#consideraciones-éticas)
7. [Conclusión](#conclusión)

## Contexto
La plataforma Donaton surge como una respuesta tecnológica avanzada frente a una problemática crítica en la gestión de emergencias: la falta de una infraestructura digital capaz de coordinar la ayuda humanitaria de manera ágil y centralizada. En situaciones de catástrofe natural o crisis social, la capacidad de respuesta es el factor determinante para mitigar el sufrimiento de las comunidades afectadas.

Actualmente, el ecosistema de ayuda en el país descansa sobre la buena voluntad de voluntarios y diversas instituciones, pero carece de un eje integrador. Históricamente, la recepción de alimentos y artículos se ha gestionado mediante procesos manuales o herramientas aisladas que no se actualizan en tiempo real. Este proyecto se enfoca en transformar esa operativa en un flujo automatizado y transparente, donde cada actor esté conectado bajo un estándar de alta disponibilidad tecnológica.

## Problemática y necesidades
El crecimiento operativo de Donaton ha superado la capacidad de sus sistemas tradicionales, haciendo necesario implementar una arquitectura de microservicios para resolver desafíos críticos:  

- Falta de trazabilidad y transparencia: Imposibilidad actual de rastrear el ciclo de vida completo de una donación (qué se donó, quién lo hizo y dónde se encuentra).  

- Descoordinación logística: Ausencia de un inventario global en tiempo real, lo que provoca mala distribución de recursos (duplicidad de esfuerzos o desabastecimiento).  

- Inseguridad de datos: Necesidad de proteger desde el diseño la información confidencial y vulnerable de los damnificados y centros de acopio.

## Levantamiento de requerimientos

Para dar solución a la problemática, el sistema se divide en tres módulos principales, cada uno con requisitos funcionales específicos, además de una serie de requisitos no funcionales que garantizan la calidad del sistema.

### Requisitos Funcionales por Módulo

**1. Módulo de Gestión de Donaciones**
- **Registro de aportes:** El sistema debe permitir a donantes (personas o empresas) registrar el tipo de recurso, cantidad y origen.
- **Generación de comprobantes:** El sistema debe generar un identificador único (ID de seguimiento) para cada donación registrada.
- **Trazabilidad:** El sistema debe permitir actualizar el estado de la donación (ej. *Ingresada, En Tránsito, Recibida en Acopio*) y reflejar en qué centro se encuentra.

**2. Módulo de Gestión de Logística y Distribución**
- **Gestión de Acopios:** Administrar la información de los diferentes centros de acopio a nivel nacional.
- **Control de Inventario:** Mantener un conteo en tiempo real de los recursos disponibles en cada centro de acopio.
- **Orquestación de Transporte:** Permitir al equipo logístico asignar vehículos y conductores a lotes de donaciones para su traslado.

**3. Módulo de Gestión de Necesidades en Terreno**
- **Reporte de Emergencia:** Proveer un formulario para que personal autorizado (ej. municipalidades) reporte necesidades críticas de suministros.
- **Geolocalización:** El sistema debe capturar y registrar las coordenadas exactas de la necesidad reportada.
- **Mapeo Interactivo:** Visualizar los reportes de necesidades en un mapa global para facilitar la toma de decisiones.

### Requisitos No Funcionales
- **Seguridad (Autenticación y Autorización):** Proteger endpoints sensibles utilizando un API Gateway y tokens JWT.
- **Disponibilidad y Escalabilidad:** Soportar alta concurrencia mediante una arquitectura de microservicios contenerizados (Docker).
- **Integridad de Datos:** Garantizar transacciones seguras (ACID) utilizando bases de datos relacionales independientes (PostgreSQL) por microservicio.
- **Accesibilidad (a11y):** La interfaz de usuario debe cumplir con normativas de contraste y navegación por teclado para ser operable bajo estrés o por personas con discapacidades.

## Casos de Uso

A continuación, se detallan los flujos principales que interactúan con el sistema:

### CU-01: Trazabilidad Completa de Donaciones
- **Actores:** Donante (Persona/Empresa), Recepcionista de Acopio.
- **Descripción:** Garantiza que una donación sea registrada y monitoreada hasta su llegada al centro de acopio.
- **Flujo Principal:** 1. El Donante ingresa a la plataforma y registra los insumos a donar.
  2. El sistema emite un código de seguimiento único.
  3. Al llegar físicamente al centro, el Recepcionista busca el código en el sistema y marca la donación como "Recibida".
  4. El sistema actualiza automáticamente el inventario del centro de acopio.

### CU-02: Mapeo y Reporte de Crisis en Tiempo Real
- **Actores:** Personal Municipal / Autoridad en Terreno.
- **Descripción:** Permite visibilizar las zonas afectadas y sus necesidades urgentes.
- **Flujo Principal:**
  1. El Personal Municipal accede al módulo de necesidades desde su ubicación.
  2. Ingresa los recursos faltantes, el nivel de urgencia y permite al sistema capturar sus coordenadas exactas.
  3. El sistema registra la alerta y actualiza el mapa interactivo (basado en Leaflet) en tiempo real, haciéndolo visible para el equipo logístico.

### CU-03: Asignación y Orquestación de Despachos
- **Actores:** Equipo de Logística.
- **Descripción:** Conecta los recursos disponibles con las necesidades del terreno mediante el despacho de insumos.
- **Flujo Principal:**
  1. El operario de Logística visualiza el mapa de crisis (generado en CU-02).
  2. Consulta el inventario disponible en el centro de acopio más cercano (actualizado en CU-01).
  3. Crea una orden de despacho vinculando los insumos requeridos con un vehículo y conductor disponible.
  4. El sistema cambia el estado de los insumos a "Despachados" y alerta a la zona de destino sobre la llegada en curso.

## Propuesta de Solución y Arquitectura
El proyecto es una aplicación de microservicios con un BFF que centraliza la autenticación y los datos de perfil para el frontend, planteado sobre un entorno de contenedores (Docker/Kubernetes) para asegurar alta disponibilidad y escalabilidad.

![Diagrama de Arquitectura](./docs/diagrama_sistema.jpg)

### Componentes y Tecnologías
- [Frontend](./frontend-service/): Interfaz construida con React, Vite y Tailwind CSS, servida por Nginx actuando como proxy inverso.
- [BFF (Backend For Frontend)](./bff-service/): Gateway orquestador en Spring Boot que agrupa peticiones, centraliza la autorización con JWT y reenvía solicitudes a los servicios internos. 
- Microservicios: Lógica de negocio dividida por responsabilidad utilizando Kotlin y Spring Boot ([auth-service](./auth-service/), [profile-service](./profile-service/), [donations-service](./donations-service/), [inventory-service](./inventory-service/) y [logistic-service](./logistic-service/)).
- Capa de Datos y Red: Despliegues individuales de [bases de datos PostgreSQL](./docker-compose.yml#101) para garantizar la integridad transaccional (ACID), apoyados por un API Gateway (KrakenD) en despliegues completos para validación perimetral.

### Arquetipos de Arquitectura
- Microservicios: Servicios independientes por responsabilidad con despliegue y base de datos aislados en Docker Compose.
- BFF (Backend For Frontend): Puerta única del frontend (bff-service/) que centraliza la autorización.

### Patrones de Diseño
- JWT Authentication: Flujo stateless donde el token portador se valida en el BFF mediante JwtValidationFilter y se emite en auth-service mediante JwtService.
- Repository Pattern: Acceso a datos abstracto a través de interfaces JpaRepository en auth-service y profile-service.
- Strategy Pattern: Autenticación configurable mediante estrategias en auth-service (SimplePasswordStrategy y EnhancedSecurityStrategy).
- Factory Pattern: Creación centralizada de respuestas de autenticación en auth-service.Provider / Context Pattern: Gestión de estado de sesión en React mediante AuthContext y useAuth().

## Consideraciones éticas
El desarrollo de Donaton conlleva una gran responsabilidad social, rigiéndose por pilares éticos:  

- Integridad de datos como supervivencia: Un dato perdido puede desviar ayuda vital, por ello se utiliza PostgreSQL y su cumplimiento ACID.  

- Seguridad y privacidad (JWT): Implementación de firmas asimétricas para proteger datos sensibles de los damnificados.  

- Inclusión digital (a11y): La interfaz seguirá las normas WCAG para garantizar la operatividad sin barreras por personas con discapacidades visuales o motoras.

## Conclusión
Esta arquitectura trasciende lo técnico para convertirse en una solución de alta responsabilidad social. Mediante el uso de tecnologías robustas y un enfoque orientado a microservicios, se garantiza una plataforma resiliente capaz de proteger la privacidad de los sectores vulnerables y liderar la gestión de emergencias con estándares éticos de vanguardia.

Para conocer a fondo la arquitectura, patrones de diseño, endpoints y guías de despliegue local, revisa nuestro [Documento Técnico del Sistema](SISTEMA.md).