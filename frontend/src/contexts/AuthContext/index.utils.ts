import type { User } from '../../types/user.types';

const USER_STORAGE_KEY = 'donaton_user';
const TOKEN_STORAGE_KEY = 'donaton_token';

export const saveSession = (user: User, token: string): void => {
  localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
  localStorage.setItem(TOKEN_STORAGE_KEY, token);
};

export const clearSession = (): void => {
  localStorage.removeItem(USER_STORAGE_KEY);
  localStorage.removeItem(TOKEN_STORAGE_KEY);
};

export const getStoredUser = (): User | null => {
  const storedUser = localStorage.getItem(USER_STORAGE_KEY);
  if (!storedUser) return null;
  
  try {
    return JSON.parse(storedUser) as User;
  } catch (error) {
    console.error("Error parseando el usuario del storage", error);
    return null;
  }
};