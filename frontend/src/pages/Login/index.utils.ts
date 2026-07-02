import { apiLogin } from '../../services/authService';
import { apiGetProfile } from '../../services/profileService';
import type { Role, User } from '../../types/user.types';
 
interface LoginResult {
  success: boolean;
  user?: User;
  token?: string;
  error?: string;
}
 
export async function authenticateUser(
  username: string,
  password: string
): Promise<LoginResult> {
  if (!username.trim() || !password.trim()) {
    return { success: false, error: 'Por favor ingresa tu usuario y contraseña.' };
  }
 
  try {
    const loginData = await apiLogin({ username, password });
 
    localStorage.setItem('donaton_token', loginData.token);
 
    const profileData = await apiGetProfile(loginData.id);
 
    const role = profileData.role as Role;
 
    const user: User = {
      id: String(loginData.id),
      username: loginData.username,
      email: profileData.email,
      role,
      name: loginData.username,
    };
 
    return { success: true, user, token: loginData.token };
 
  } catch (err: unknown) {
    localStorage.removeItem('donaton_token');
    const message = err instanceof Error ? err.message : 'Error de conexión con el servidor.';
    return { success: false, error: message };
  }
}
