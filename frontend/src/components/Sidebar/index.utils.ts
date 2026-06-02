import { Role, MenuItem } from '../../types/user.types';

/**
 *  Definimos todas las opciones del menú de la plataforma
 **/
export const MENU_ITEMS: MenuItem[] = [
  {
    title: 'Dashboard Admin',
    path: '/admin',
    roles: ['ADMIN'],
  },
  {
    title: 'Mis Donaciones',
    path: '/donor',
    roles: ['DONOR', 'ADMIN'],
  },
  {
    title: 'Gestión Logística',
    path: '/logistic',
    roles: ['LOGISTIC', 'ADMIN'],
  },
  {
    title: 'Tareas Voluntario',
    path: '/volunteer',
    roles: ['VOLUNTEER', 'ADMIN'],
  },
  {
    title: 'Mi Perfil',
    path: '/profile',
    roles: ['ADMIN', 'DONOR', 'LOGISTIC', 'VOLUNTEER'],
  }
];

/**
 * Utilidad para filtrar qué items puede ver el usuario actual
 **/
export const getMenuByRole = (role: Role): MenuItem[] => {
  return MENU_ITEMS.filter(item => item.roles.includes(role));
};