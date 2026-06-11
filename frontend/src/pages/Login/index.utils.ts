import type { Role, User } from '../../types/user.types';

interface LoginMockResult {
  success: boolean;
  user?: User;
  token?: string;
  error?: string;
}

/**
 * Simulamos una base de datos de usuarios para desarrollo
 */
const MOCK_USERS = [
  { email: 'admin@donaton.cl', password: 'admin123', role: 'ADMIN' as Role, name: 'Administrador General' },
  { email: 'donante@donaton.cl', password: 'user123', role: 'DONOR' as Role, name: 'Juan Pérez (Donante)' },
  { email: 'logistica@donaton.cl', password: 'log123', role: 'LOGISTIC' as Role, name: 'María Gómez (Logística)' },
  { email: 'voluntario@donaton.cl', password: 'vol123', role: 'VOLUNTEER' as Role, name: 'Carlos Díaz (Voluntario)' },
];

/**
 * Simula la llamada real de autenticación al Backend/BFF.
 */
export const authenticateMockUser = (email: string, password: string): LoginMockResult => {
  if (!email || !password) {
    return { success: false, error: 'Por favor, ingresa tu correo y contraseña.' };
  }

  // Buscamos si las credenciales coinciden con nuestra "base de datos"
  const foundUser = MOCK_USERS.find(u => u.email === email && u.password === password);

  if (!foundUser) {
    return { success: false, error: 'Credenciales inválidas. Verifica tu correo y contraseña.' };
  }

  return {
    success: true,
    user: {
      id: Math.random().toString(36).substring(2, 9),
      email: foundUser.email,
      role: foundUser.role,
      name: foundUser.name,
    },
    token: `mock-jwt-token-for-${foundUser.role.toLowerCase()}`,
  };
};