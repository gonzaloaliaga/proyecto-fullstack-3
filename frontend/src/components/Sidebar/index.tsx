import { NavLink } from 'react-router-dom';
import { Role } from '../../types/user.types';
import { getMenuByRole } from './index.utils';
import './index.css';

interface SidebarProps {
  userRole: Role;
  onLogout: () => void;
}

export const Sidebar = ({ userRole, onLogout }: SidebarProps) => {
  const menuItems = getMenuByRole(userRole);

  return (
    <aside className="w-64 bg-gray-900 text-white flex flex-col min-h-screen sidebar-transition shadow-xl">
      {/* Header del Sidebar */}
      <div className="p-6 border-b border-gray-800">
        <h2 className="text-2xl font-bold tracking-wider text-blue-400">DONATÓN</h2>
        <div className="mt-2 text-xs font-semibold text-gray-400 bg-gray-800 inline-block px-2 py-1 rounded uppercase tracking-wide">
          Perfil: {userRole}
        </div>
      </div>

      {/* Navegación */}
      <nav className="flex-1 px-4 py-6 space-y-2">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `block px-4 py-3 rounded-lg transition-colors duration-200 ${
                isActive 
                  ? 'nav-item-active shadow-md' 
                  : 'text-gray-300 hover:bg-gray-800 hover:text-white'
              }`
            }
          >
            {item.title}
          </NavLink>
        ))}
      </nav>

      {/* Footer del Sidebar */}
      <div className="p-4 border-t border-gray-800">
        <button
          onClick={onLogout}
          className="w-full flex items-center justify-center px-4 py-2 text-sm font-medium text-red-400 bg-transparent border border-red-500/30 rounded-lg hover:bg-red-500/10 hover:text-red-300 transition-colors"
        >
          Cerrar Sesión
        </button>
      </div>
    </aside>
  );
};