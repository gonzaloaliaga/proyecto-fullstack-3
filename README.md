# Arquitectura del sistema
![Diagrama de Arquitectura](./docs/diagrama_sistema.jpg)

## 🛠️ Stack Tecnológico
* **Frontend:** React 19.2.5, Vite 8.0.10 y Tailwind CSS 4.3.0 como librerías core para la interfaz de usuario de tipo SPA.
* **Backend Core:** Kotlin 2.2.21 y Spring Boot 4.0.5 ejecutándose sobre entornos JDK 21 virtuales para todos los servicios backend del ecosistema.
* **Persistencia:** PostgreSQL 16 configurado en modalidad políglota para mantener un aislamiento estricto de esquemas por cada microservicio autónomo.
* **Infraestructura:** Docker Engine y Docker Compose orquestando las redes bridge y los volúmenes persistentes locales del clúster.

## 🏗️ Arquitectura y Funcionamiento Interno
* **Frontend Service:** Aplicación cliente SPA que gestiona el estado global de la sesión del usuario mediante el patrón de diseño *Provider / Context* (`AuthContext`), exponiendo vistas dinámicas y componentes de control de acuerdo con el rol asignado a la identidad.
* **BFF Service:** Gateway de acceso único que centraliza la seguridad perimetral mediante un `JwtValidationFilter` personalizado. Valida las firmas de los tokens entrantes, rechaza accesos no autorizados y actúa como un proxy inverso redirigiendo las llamadas válidas mediante `RestTemplate`.
* **Auth Service:** Núcleo criptográfico del sistema. En lugar de firmas simétricas básicas, implementa criptografía asimétrica mediante algoritmos **RSA256** utilizando llaves públicas y privadas para expedir tokens JSON Web Tokens (JWT) robustos. Aplica el patrón de diseño *Strategy* (`AuthenticationStrategy`) para desacoplar las reglas de verificación de contraseñas y el patrón *Factory* para la estructuración unificada de payloads de respuesta.
* **Profile Service:** Servicio a cargo del ciclo de vida y datos maestros de la información operacional de los perfiles de usuario. Aprovecha las bondades nativas de inmutabilidad de los Data Classes de Kotlin implementando el método `.copy()` dentro del endpoint REST de tipo `PATCH`, lo que permite realizar actualizaciones parciales precisas sobre los registros sin comprometer el estado original.
* **Módulos Esqueleto (Evolutivos):** El diseño contempla los servicios `donations-service`, `inventory-service` y `logistic-service` estructurados conceptualmente con su base tecnológica lista para escalar mediante la adición de controladores y modelos JPA.

## 📋 Infraestructura y Dependencias
* **Estructura Interna del Mono-repositorio:** Cada componente opera dentro de un directorio propio y autónomo equipado con su documentación técnica de bajo nivel. Esto asegura un desacoplamiento a nivel de equipo y facilita el despliegue selectivo.
* **Topología de Redes y Aislamiento de Capas:** El clúster se levanta bajo una red virtual aislada de tipo bridge llamada `donaton-network`. La persistencia utiliza **5 instancias PostgreSQL independientes** con volúmenes mapeados, asegurando que un microservicio jamás comparta bases de datos de forma directa con otro servicio, cumpliendo con los estándares de arquitectura desacoplada.

## 🚀 Levantamiento del Entorno Local
* **Compilación y Orquestación:** El clúster se compila utilizando una estrategia de construcción en múltiples etapas (*multi-stage builds*) e inicia de forma coordinada la totalidad de los microservicios y sus bases de datos asociadas mediante la ejecución del comando unificado `docker compose up --build` en la terminal raíz.
* **Aplicación Cliente (Frontend):** Interfaz gráfica SPA construida en React y Vite que es expuesta y accesible localmente en la máquina host a través de la dirección de red `http://localhost:5173` una vez que el contenedor reporta un estado saludable.
* **Gateway de Entrada (BFF):** Punto único de acceso perimetral expuesto en el entorno local a través de `http://localhost:8080`, encargado de centralizar, validar las credenciales y canalizar todo el tráfico hacia las redes internas de los microservicios.

## 🔄 Flujo de Comunicación del Sistema (Caso de Uso: Login)
* **Formulario de Identidad:** El usuario introduce sus datos de inicio de sesión en la interfaz web interactiva provista por el componente `frontend-service`.
* **Despacho del Payload:** El cliente envía de manera asíncrona una solicitud HTTP de tipo `POST /api/auth/login` dirigida exclusivamente hacia el puerto expuesto del `bff-service`.
* **Proxy de Red Virtual:** El `bff-service` actúa como proxy perimetral y delega la petición invocando al servicio de autenticación mediante la resolución DNS interna de la red de Docker en `http://auth-service:8081`.
* **Criptografía y Firma:** El `auth-service` verifica las credenciales usando JPA en `auth_db` y, si son correctas, genera un token **JWT** firmado digitalmente con su llave privada RSA, retornando el payload hacia el frontend a través del BFF.
* **Sesión y Autorización:** El cliente almacena el token en su `localStorage`. En cualquier petición de negocio posterior, el frontend adjunta la cabecera `Authorization: Bearer <token>`, permitiendo al BFF interceptar el llamado, extraer el ID usando la llave pública RSA y autorizar la comunicación con el `profile-service` en el puerto interno `8082`.

## ⚙️ Variables de Entorno Clave
```env
JWT_PRIVATE_KEY=${JWT_PRIVATE_KEY}
JWT_PUBLIC_KEY=${JWT_PUBLIC_KEY}
SERVICES_AUTH_URL=http://auth-service:8081
SERVICES_PROFILE_URL=http://profile-service:8082
SERVICES_DONATIONS_URL=http://donations-service:8083
SERVICES_INVENTORY_URL=http://inventory-service:8084
SERVICES_LOGISTIC_URL=http://logistic-service:8085
