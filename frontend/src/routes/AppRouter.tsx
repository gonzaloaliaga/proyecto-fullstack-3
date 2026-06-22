import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Layout } from '../components/Layout';
import { Login } from '../pages/Login';
import { ProfilePage } from '../pages/Profile';
import { DonorDashboard } from '../pages/Donor';
import { LogisticDashboard } from '../pages/Logistic';

const AdminDashboard = () => (
  <div className="bg-white p-6 rounded-xl shadow-sm">
    <h2 className="text-lg font-semibold text-gray-900">Panel de Administración</h2>
    <p className="text-gray-500 mt-2">Bienvenido al módulo de gestión global.</p>
  </div>
);

const VolunteerDashboard = () => (
  <div className="bg-white p-6 rounded-xl shadow-sm">
    <h2 className="text-lg font-semibold text-gray-900">Tareas de Voluntariado</h2>
    <p className="text-gray-500 mt-2">Lista de actividades asignadas disponibles.</p>
  </div>
);

export const AppRouter = () => {
  const { isAuthenticated, user, logout } = useAuth();

  if (!isAuthenticated || !user) {
    return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  return (
    <Routes>
      <Route element={<Layout userRole={user.role} onLogout={logout} />}>
        <Route path="/" element={<Navigate to={`/${user.role.toLowerCase()}`} replace />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/donor" element={<DonorDashboard />} />
        <Route path="/logistic" element={<LogisticDashboard />} />
        <Route path="/volunteer" element={<VolunteerDashboard />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
};
