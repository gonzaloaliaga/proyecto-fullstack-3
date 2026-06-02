export const ROUTE_TITLES: Record<string, string> = {
  '/admin': 'Panel de Administración General',
  '/donor': 'Portal del Donante',
  '/logistic': 'Control de Inventario y Logística',
  '/volunteer': 'Gestión de Tareas de Voluntariado',
  '/profile': 'Configuración de Mi Perfil',
};

/**
 * Retorna un título adecuado basado en el pathname actual de la URL
 */
export const getHeaderTitle = (pathname: string): string => {
  return ROUTE_TITLES[pathname] || 'Sistema Donatón';
};