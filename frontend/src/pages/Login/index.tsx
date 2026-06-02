import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import type { Role } from '../../types/user.types';
import { authenticateMockUser } from './index.utils';
import './index.css';

export const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [role, setRole] = useState<Role>('DONOR');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const result = authenticateMockUser(email || `${role.toLowerCase()}@donaton.cl`, role);

    if (result.success && result.user && result.token) {
      login(result.user, result.token);
    } else {
      setError(result.error || 'Error al iniciar sesión');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center login-bg px-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-2xl p-8 space-y-6">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900 tracking-tight">
            Donatón
          </h2>
          <p className="mt-2 text-sm text-gray-500">
            Ingresa al sistema de gestión comunitaria
          </p>
        </div>

        {error && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded text-sm text-red-700">
            {error}
          </div>
        )}

        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Correo Electrónico (Opcional para pruebas)
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder={`${role.toLowerCase()}@donaton.cl`}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Perfil de Acceso (Modo Desarrollo)
            </label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value as Role)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
            >
              <option value="DONOR">Donante</option>
              <option value="VOLUNTEER">Voluntario</option>
              <option value="LOGISTIC">Logística</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>

          <button
            type="submit"
            className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-md hover:shadow-lg transition-all duration-200 mt-2"
          >
            Iniciar Sesión
          </button>
        </form>
      </div>
    </div>
  );
};