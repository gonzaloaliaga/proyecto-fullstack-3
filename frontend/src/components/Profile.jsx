import { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';
import MainLayout from './MainLayout';

// DASHBOARDS
import { AdminDashboard } from './dashboards/AdminDashboard';
import { LogisticDashboard } from './dashboards/LogisticDashboard';
import { VolunteerDashboard } from './dashboards/VolunteerDashboard';
import { DonorDashboard } from './dashboards/DonorDashboard';

const ROLE_STRATEGIES = {
    ADMIN: <AdminDashboard />,
    LOGISTIC: <LogisticDashboard />,
    VOLUNTEER: <VolunteerDashboard />,
    DONOR: <DonorDashboard />
};

export default function Profile() {
    const { user, profile, updateProfile, updateUsername, loading, error } = useContext(AuthContext);
    const [newUsername, setNewUsername] = useState('');
    const [newEmail, setNewEmail] = useState('');
    const [newAddress, setNewAddress] = useState('');
    const [newRun, setNewRun] = useState('');
    const [message, setMessage] = useState('');

    // 1. Manejo de estados de carga (importante para la persistencia inicial)
    if (loading && !profile) {
        return (
            <div className="min-h-screen bg-slate-50 flex items-center justify-center">
                <p className="text-lg text-slate-600 animate-pulse">Verificando sesión...</p>
            </div>
        );
    }

    // Si no hay usuario ni perfil tras cargar, el usuario no debería estar aquí
    if (!user || !profile) return null;

    const handleUpdateName = async () => {
        if (!newUsername.trim()) {
            setMessage('El nombre no puede estar vacío.');
            return;
        }
        setMessage('');
        try {
            await updateUsername(newUsername);
            alert("¡Nombre actualizado!");
            setNewUsername('');
        } catch (error) {
            setMessage('No se pudo actualizar el nombre.');
        }
    };

    const handleUpdateProfile = async () => {
        const payload = {
            ...(newEmail.trim() && { email: newEmail }),
            ...(newAddress.trim() && { address: newAddress }),
            ...(newRun.trim() && { run: newRun }),
        };

        if (Object.keys(payload).length === 0) {
            setMessage('Completa al menos un campo para actualizar.');
            return;
        }

        try {
            await updateProfile(payload);
            alert("¡Perfil actualizado!");
            setNewEmail('');
            setNewAddress('');
            setNewRun('');
        } catch (error) {
            setMessage(error.message || 'Error al actualizar el perfil');
        }
    };

    return (
        <MainLayout>
            {/* Header con datos del perfil real desde PostgreSQL */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200 mb-6 flex justify-between items-center">
                <div className="flex items-center gap-4">
                    <div>
                        <h1 className="text-2xl font-bold text-slate-800">
                            Bienvenido {profile.username || user.username}
                        </h1>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-1 mt-2">
                            <p className="text-slate-500 text-sm"><span className="font-bold">Rol:</span> {profile.role}</p>
                            <p className="text-slate-500 text-sm"><span className="font-bold">Email:</span> {profile.email}</p>
                            <p className="text-slate-500 text-sm"><span className="font-bold">Dirección:</span> {profile.address}</p>
                            <p className="text-slate-500 text-sm"><span className="font-bold">RUT:</span> {profile.run}</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Dashboard dinámico */}
            <div className="mb-6">
                {ROLE_STRATEGIES[profile.role] || (
                    <div className="p-4 bg-yellow-50 text-yellow-800 rounded-lg border border-yellow-200">
                        Rol no reconocido: {profile.role}
                    </div>
                )}
            </div>

            {/* Formularios de actualización */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200">
                <h3 className="text-sm font-bold text-slate-700 mb-4 uppercase tracking-wider">Ajustes de Cuenta</h3>
                
                <div className="space-y-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        <input 
                            type="text" 
                            value={newUsername} 
                            onChange={(e) => setNewUsername(e.target.value)} 
                            placeholder="Nuevo nombre de usuario" 
                            className="flex-1 px-4 py-3 bg-slate-50 border border-slate-300 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 transition-all"
                        />
                        <button onClick={handleUpdateName} className="bg-slate-800 hover:bg-slate-900 text-white font-semibold py-3 px-6 rounded-xl transition-colors">
                            Cambiar nombre
                        </button>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <input type="text" value={newEmail} onChange={(e) => setNewEmail(e.target.value)} placeholder="Nuevo Email" className="px-4 py-3 bg-slate-50 border border-slate-300 rounded-xl outline-none" />
                        <input type="text" value={newAddress} onChange={(e) => setNewAddress(e.target.value)} placeholder="Nueva Dirección" className="px-4 py-3 bg-slate-50 border border-slate-300 rounded-xl outline-none" />
                        <input type="text" value={newRun} onChange={(e) => setNewRun(e.target.value)} placeholder="Nuevo RUT" className="px-4 py-3 bg-slate-50 border border-slate-300 rounded-xl outline-none" />
                    </div>

                    <div className="flex justify-between items-center pt-2">
                        {message && <p className="text-red-500 text-sm font-medium">{message}</p>}
                        <button onClick={handleUpdateProfile} className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-8 rounded-xl transition-colors ml-auto">
                            Guardar cambios de perfil
                        </button>
                    </div>
                </div>
            </div>
        </MainLayout>
    );
}