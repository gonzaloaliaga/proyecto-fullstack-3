# Frontend Service

## Propósito
Interfaz de usuario en React que consume el `bff-service` para autenticación y datos de perfil.

## Funcionalidades
- Inicio de sesión (`login`)
- Almacenamiento seguro del token JWT en `localStorage`
- Mantenimiento de sesión en el cliente
- Consulta y actualización de perfil a través del BFF

## Estructura principal
- `src/App.jsx`
- `src/context/AuthContext.jsx`
- `src/services/authService.ts`
- `src/services/profileService.ts`

## Flujo de autenticación
- El usuario se autentica en `POST http://localhost:8080/api/auth/login`
- El BFF reenvía la petición a `auth-service`
- El `token` se almacena en `localStorage` bajo `donaton_token`
- El frontend lo usa para `Authorization: Bearer <token>` en peticiones de perfil

## Tecnologías
- React 19.2.5
- Vite 8.0.10
- Tailwind CSS 4.3.0

## Ejecución
### Instalar dependencias
```bash
cd frontend-service
npm install
```

### Levantar aplicación
```bash
npm run dev
```

### Docker Compose
```bash
docker compose up --build frontend-service
```

## Observaciones
El frontend comunica con el BFF en `http://localhost:8080` y mantiene la sesión en `localStorage`.
