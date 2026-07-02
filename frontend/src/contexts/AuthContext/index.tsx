import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import type { User } from '../../types/user.types';
import { saveSession, clearSession, getStoredUser } from './index.utils';

interface AuthContextProps {
  user: User | null;
  isAuthenticated: boolean;
  login: (userData: User, token: string) => void;
  logout: () => void;
  updateUsername: (newUsername: string) => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(getStoredUser());

  const login = (userData: User, token: string) => {
    setUser(userData);
    saveSession(userData, token);
  };

  const logout = () => {
    setUser(null);
    clearSession();
  };

  const updateUsername = (newUsername: string) => {
    setUser(prev => {
      if (!prev) return prev;
      const updated = { ...prev, username: newUsername };
      const token = localStorage.getItem('donaton_token');
      if (token) saveSession(updated, token);
      return updated;
    });
  };

  return (
    <AuthContext.Provider value={{
      user,
      isAuthenticated: !!user,
      login,
      logout,
      updateUsername
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};