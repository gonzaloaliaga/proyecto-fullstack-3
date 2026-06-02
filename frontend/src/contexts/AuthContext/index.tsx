import { createContext, useContext, useState, ReactNode } from 'react';
import { User, Role } from '../../types/user.types';
import { saveSession, clearSession, getStoredUser } from './index.utils';

/**
 * Definimos qué datos y funciones tendrá nuestro contexto
 */
interface AuthContextProps {
  user: User | null;
  isAuthenticated: boolean;
  login: (userData: User, token: string) => void;
  logout: () => void;
}

/**
 * Creamos el contexto vacío por defecto
 */
const AuthContext = createContext<AuthContextProps | undefined>(undefined);

/**
 * Proveedor del contexto
 */
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  /**
   * Inicializamos el estado leyendo del localStorage por si el usuario recargó la página
   */
  const [user, setUser] = useState<User | null>(getStoredUser());

  const login = (userData: User, token: string) => {
    setUser(userData);
    saveSession(userData, token);
  };

  const logout = () => {
    setUser(null);
    clearSession();
  };

  return (
    <AuthContext.Provider value={{ 
      user, 
      isAuthenticated: !!user, 
      login, 
      logout 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

/**
 * Hook personalizado para usar el AuthContext en cualquier componente
 */
export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};