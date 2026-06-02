import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function Sidebar() {
    const { profile, logout } = useContext(AuthContext);

    const menuOptions = {
        ADMIN: [
            { label: 'Panel Global', icon: '📊' },
            { label: 'Gestionar Usuarios', icon: '👥' },
            { label: 'Configuración', icon: '⚙️' }
        ],
        LOGISTIC: [
            { label: 'Inventario', icon: '📦' },
            { label: 'Rutas de Entrega', icon: '🚚' }
        ],
        VOLUNTEER: [
            { label: 'Mis Tareas', icon: '📋' },
            { label: 'Puntos de Ayuda', icon: '📍' }
        ],
        DONOR: [
            { label: 'Mis Donaciones', icon: '❤️' },
            { label: 'Nueva Donación', icon: '➕' }
        ]
    };

    const options = menuOptions[profile?.role] || [];

    return (
        <div className="w-64 min-h-screen bg-slate-900 text-white flex flex-col">
            <div className="p-6">
                <h1 className="text-2xl font-bold text-blue-400">Donatón</h1>
            </div>

            <nav className="px-4 space-y-2 mb-8">
                {options.map((opt, index) => (
                    <button key={index} className="w-full flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-slate-800 transition-colors">
                        <span>{opt.icon}</span>
                        <span className="font-medium">{opt.label}</span>
                    </button>
                ))}
            </nav>

            <div className="p-4 border-t border-slate-800">
                <button 
                    onClick={logout}
                    className="w-full flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-red-900/30 text-red-400 transition-colors"
                >
                    <span>🚪</span>
                    <span className="font-medium">Cerrar Sesión</span>
                </button>
            </div>
        </div>
    );
}