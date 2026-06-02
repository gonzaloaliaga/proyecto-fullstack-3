export type Role = 'ADMIN' | 'DONOR' | 'LOGISTIC' | 'VOLUNTEER';

export interface User {
  id: string;
  email: string;
  role: Role;
  name: string;
}

export interface MenuItem {
  title: string;
  path: string;
  roles: Role[];
}