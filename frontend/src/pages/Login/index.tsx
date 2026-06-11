import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { authenticateMockUser } from './index.utils';
import './index.css';

export const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // Llamamos a nuestra utilidad que simula la validación
    const result = authenticateMockUser(email, password);

    if (result.success && result.user && result.token) {
      login(result.user, result.token);
    } else {
      setError(result.error || 'Ocurrió un error inesperado al iniciar sesión.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center login-bg px-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-2xl p-8 space-y-8">
        
        {/* Cabecera del Formulario */}
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900 tracking-tight">
            Donatón
          </h2>
          <p className="mt-2 text-sm text-gray-500">
            Ingresa tus credenciales para acceder al sistema
          </p>
        </div>

        {/* Mensaje de Error */}
        {error && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
            <p className="text-sm text-red-700 font-medium">{error}</p>
          </div>
        )}

        {/* Formulario */}
        <form className="space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-1">
            <label className="block text-sm font-medium text-gray-700">
              Correo Electrónico
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="ejemplo@donaton.cl"
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
              required
            />
          </div>

          <div className="space-y-1">
            <label className="block text-sm font-medium text-gray-700">
              Contraseña
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-md hover:shadow-lg transition-all duration-200 mt-4 focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Iniciar Sesión
          </button>
        </form>

        {/* Helper visual para ti mientras pruebas */}
        <div className="mt-6 border-t border-gray-200 pt-4">
          <p className="text-xs text-center text-gray-400">
            Cuentas de prueba: admin@donaton.cl / donante@donaton.cl <br/> (clave: admin123 / user123)
          </p>
        </div>

      </div>
    </div>
  );
};