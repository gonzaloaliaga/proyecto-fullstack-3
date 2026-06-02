import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';
import { Layout } from './components/Layout';
import { Login } from './pages/Login';

/*Componentes marcadores de posición temporales mientras creamos cada página formal*/
const AdminDashboard = () => <div className="bg-white p-6 rounded-xl shadow-sm"><h2>Panel de Control de Administración</h2><p className="text-gray-500 mt-2">Bienvenido al módulo de gestión global.</p></div>;
const DonorDashboard = () => <div className="bg-white p-6 rounded-xl shadow-sm"><h2>Historial de Mis Donaciones</h2><p className="text-gray-500 mt-2">Aquí podrás ver tus aportes a la comunidad.</p></div>;
const LogisticDashboard = () => <div className="bg-white p-6 rounded-xl shadow-sm"><h2>Control de Inventario</h2><p className="text-gray-500 mt-2">Monitoreo de entradas y salidas de bodega.</p></div>;
const VolunteerDashboard = () => <div className="bg-white p-6 rounded-xl shadow-sm"><h2>Tareas de Voluntariado</h2><p className="text-gray-500 mt-2">Lista de actividades asignadas disponibles.</p></div>;
const ProfilePage = () => <div className="bg-white p-6 rounded-xl shadow-sm"><h2>Ajustes de Cuenta</h2><p className="text-gray-500 mt-2">Edita tus datos personales de contacto.</p></div>;

function AppRoutes() {
  const { isAuthenticated, user, logout } = useAuth();

  if (!isAuthenticated || !user) {
    return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  /*Si está autenticado, cargamos el Layout base con el rol actual*/
  return (
    <Routes>
      <Route element={<Layout userRole={user.role} onLogout={logout} />}>
        {/* Redirección automática según el rol al entrar a la raíz */}
        <Route path="/" element={<Navigate to={`/${user.role.toLowerCase()}`} replace />} />
        
        {/* Rutas Protegidas del Sistema */}
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/donor" element={<DonorDashboard />} />
        <Route path="/logistic" element={<LogisticDashboard />} />
        <Route path="/volunteer" element={<VolunteerDashboard />} />
        <Route path="/profile" element={<ProfilePage />} />

        {/* Captura cualquier otra ruta dentro del sistema */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  );
}