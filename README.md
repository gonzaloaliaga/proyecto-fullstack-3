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

### 3. Levantar todos los servicios con Kubernetes

```bash
# =====================================================================
# REQUISITOS PREVIOS (Descomentar solo la primera vez en una PC nueva)
# =====================================================================
# winget install Kubernetes.minikube
# winget install Kubernetes.kubectl

# =====================================================================
# PASO 0: DESTRUCCIÓN TOTAL DEL ENTORNO PREVIO
# =====================================================================
Write-Host "🛑 Deteniendo y destruyendo el clúster previo para iniciar desde cero..." -ForegroundColor Yellow
minikube stop
minikube delete

# =====================================================================
# PASO 1: ENCENDER Y PREPARAR EL CLÚSTER LIMPIO
# =====================================================================
Write-Host "🚀 Iniciando una nueva instancia limpia de Minikube..." -ForegroundColor Green
minikube start --driver=docker
minikube addons enable ingress

# =====================================================================
# PASO 2: CONECTAR ENTORNO Y COMPILAR LAS 6 IMÁGENES LOCALES
# =====================================================================
Write-Host "📦 Conectando al entorno Docker de Minikube y compilando imágenes..." -ForegroundColor Cyan
minikube docker-env | Invoke-Expression

# Componentes Core e Interfaz
docker build -t proyecto-fullstack-3-frontend:latest ./frontend
docker build -t proyecto-fullstack-3-bff:latest ./backend/bff

# Los 5 Microservicios de Negocio
docker build -t proyecto-fullstack-3-ms-auth:latest ./backend/ms-auth
docker build -t proyecto-fullstack-3-ms-profile:latest ./backend/ms-profile
docker build -t proyecto-fullstack-3-ms-logistic:latest ./backend/ms-logistic
docker build -t proyecto-fullstack-3-ms-donation:latest ./backend/ms-donation
docker build -t proyecto-fullstack-3-ms-inventory:latest ./backend/ms-inventory

# =====================================================================
# PASO 3: DESPLEGAR INFRAESTRUCTURA Y CONFIGURACIONES
# =====================================================================
Write-Host "🔐 Aplicando secretos e Ingress global..." -ForegroundColor Magenta
kubectl apply -f k8s/donaton-global-secrets.yaml
kubectl apply -f k8s/ingress.yaml

# =====================================================================
# PASO 4: DESPLEGAR LAS 5 BASES DE DATOS POSTGRES
# =====================================================================
Write-Host "🗄️ Creando instancias de bases de datos independientes..." -ForegroundColor Blue
kubectl apply -f backend/ms-auth/k8s/postgres.yaml
kubectl apply -f backend/ms-profile/k8s/postgres.yaml
kubectl apply -f backend/ms-logistic/k8s/postgres.yaml
kubectl apply -f backend/ms-donation/k8s/postgres.yaml
kubectl apply -f backend/ms-inventory/k8s/postgres.yaml

# =====================================================================
# PASO 5: DESPLEGAR SERVIDORES, PASARELA BFF Y FRONTEND (OPCIÓN C)
# =====================================================================
Write-Host "⚡ Desplegando lógica de negocio (Microservicios, BFF y Frontend)..." -ForegroundColor White
kubectl apply -f backend/ms-auth/k8s/deployment.yaml
kubectl apply -f backend/ms-profile/k8s/deployment.yaml
kubectl apply -f backend/ms-logistic/k8s/deployment.yaml
kubectl apply -f backend/ms-donation/k8s/deployment.yaml
kubectl apply -f backend/ms-inventory/k8s/deployment.yaml
kubectl apply -f backend/bff/k8s/deployment.yaml
kubectl apply -f ./frontend/k8s/deployment.yaml

# =====================================================================
# PASO 6: MONITOREAR EL ARRANQUE EN TIEMPO REAL
# =====================================================================
Write-Host "👀 Monitoreando el estado de los pods. Espera a que todos marquen 1/1 Running..." -ForegroundColor Green
kubectl get pods --watch

# =====================================================================
# NOTA AL FINALIZAR:
# =====================================================================
# Cuando la cuadrícula esté completamente estable en 1/1 Running,
# recuerda abrir una SEGUNDA ventana de PowerShell como Administrador 
# y ejecutar el comando para el puente de red:
#
# minikube tunnel
```

### 4. Acceder a la aplicación

Abrir el navegador en `http://localhost`

### Usuarios

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 1111 | ADMIN |
| logistica | 2222 | LOGISTIC |
| voluntario | 3333 | VOLUNTEER |
| donante | 4444 | DONOR |

## Detener el sistema

```bash
minikube stop
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
