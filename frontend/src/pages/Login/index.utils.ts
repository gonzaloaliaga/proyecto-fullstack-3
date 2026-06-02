import type { Role, User } from '../../types/user.types';

interface LoginMockResult {
  success: boolean;
  user?: User;
  token?: string;
  error?: string;
}

/*
 * Simula una llamada de autenticación al BFF/Backend.
 */
export const authenticateMockUser = (email: string, selectedRole: Role): LoginMockResult => {
  if (!email.includes('@')) {
    return { success: false, error: 'Por favor, introduce un correo válido.' };
  }

  /*
   * Generamos un usuario simulado según el rol seleccionado para las pruebas de maquetación
   */
  const namesByRole: Record<Role, string> = {
    ADMIN: 'Administrador General',
    DONOR: 'Donante Solidario',
    LOGISTIC: 'Encargado de Logística',
    VOLUNTEER: 'Voluntario Activo',
  };

  return {
    success: true,
    user: {
      id: Math.random().toString(36).substring(2, 9),
      email: email.toLowerCase(),
      role: selectedRole,
      name: namesByRole[selectedRole],
    },
    token: 'mock-jwt-token-response',
  };
};