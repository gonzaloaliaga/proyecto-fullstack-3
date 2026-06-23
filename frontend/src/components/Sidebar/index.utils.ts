import type { Role, MenuItem } from '../../types/user.types';

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

export const getMenuByRole = (role: Role): MenuItem[] => {
  return MENU_ITEMS.filter(item => item.roles.includes(role));
};