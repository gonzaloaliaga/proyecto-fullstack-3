import { Outlet, useLocation } from 'react-router-dom';
import { Sidebar } from '../Sidebar';
import type { Role } from '../../types/user.types';
import { getHeaderTitle } from './index.utils';
import './index.css';

interface LayoutProps {
  userRole: Role;
  onLogout: () => void;
}

export const Layout = ({ userRole, onLogout }: LayoutProps) => {
  const location = useLocation();
  const currentTitle = getHeaderTitle(location.pathname);

  return (
    <div className="flex w-full min-h-screen bg-gray-100 font-sans antialiased">
      {/* Componente Sidebar Reutilizable */}
      <Sidebar userRole={userRole} onLogout={onLogout} />

      {/* Contenedor Principal Derecho */}
      <div className="flex-1 flex flex-col main-content-viewport">
        {/* Barra Superior de Usuario (Header) */}
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-8 shadow-sm shrink-0">
          <h1 className="text-xl font-semibold text-gray-800 tracking-tight">
            {currentTitle}
          </h1>
          
          <div className="flex items-center space-x-4">
            <span className="w-2 h-2 rounded-full bg-green-500 animate-pulse"></span>
            <span className="text-sm font-medium text-gray-600">Servidor Conectado</span>
          </div>
        </header>

        {/* Área de Contenido Dinámico */}
        <main className="flex-1 p-8">
          <div className="max-w-7xl mx-auto">
            {/* Aquí es donde react-router-dom renderizará cada página (/admin, /donor, etc.) */}
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};