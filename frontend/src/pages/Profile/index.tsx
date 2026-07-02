import { useEffect, useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { apiGetProfile, apiUpdateProfile } from '../../services/profileService';
import type { ProfileResponse } from '../../services/profileService';
import { apiUpdateUsername } from '../../services/authService';

export const ProfilePage = () => {
  const { user, updateUsername } = useAuth();
  const [profile, setProfile] = useState<ProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [form, setForm] = useState({
    email: '',
    address: '',
    run: '',
  });

  const [usernameForm, setUsernameForm] = useState('');
  const [editingUsername, setEditingUsername] = useState(false);
  const [savingUsername, setSavingUsername] = useState(false);
  const [usernameError, setUsernameError] = useState('');
  const [usernameSuccess, setUsernameSuccess] = useState('');

  useEffect(() => {
    if (!user) return;

    const load = async () => {
      try {
        const data = await apiGetProfile(Number(user.id));
        setProfile(data);
        setForm({
          email: data.email,
          address: data.address,
          run: data.run,
        });
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Error al cargar el perfil.';
        setError(msg);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [user]);

  useEffect(() => {
    if (user) setUsernameForm(user.username);
  }, [user]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSaving(true);

    try {
      const updated = await apiUpdateProfile(Number(user!.id), form);
      setProfile(updated);
      setSuccess('Perfil actualizado correctamente.');
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Error al guardar el perfil.';
      setError(msg);
    } finally {
      setSaving(false);
    }
  };

  const handleUsernameSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setUsernameError('');
    setUsernameSuccess('');

    if (!usernameForm.trim() || usernameForm === user?.username) {
      setEditingUsername(false);
      setUsernameForm(user?.username ?? '');
      return;
    }

    setSavingUsername(true);
    try {
      const result = await apiUpdateUsername(usernameForm.trim());
      updateUsername(result.newUsername);
      setUsernameSuccess('Nombre de usuario actualizado correctamente.');
      setEditingUsername(false);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Error al actualizar el nombre de usuario.';
      setUsernameError(msg);
    } finally {
      setSavingUsername(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-48">
        <p className="text-gray-500">Cargando perfil...</p>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="bg-white rounded-xl shadow-sm p-6">
        <div className="flex items-center space-x-4 mb-6">
          <div className="w-16 h-16 rounded-full bg-blue-100 flex items-center justify-center">
            <span className="text-2xl font-bold text-blue-600">
              {user?.username?.charAt(0).toUpperCase()}
            </span>
          </div>
          <div className="flex-1">
            {editingUsername ? (
              <form onSubmit={handleUsernameSubmit} className="flex items-center gap-2">
                <input
                  type="text"
                  value={usernameForm}
                  onChange={(e) => setUsernameForm(e.target.value)}
                  className="px-3 py-1.5 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                  disabled={savingUsername}
                  autoFocus
                />
                <button
                  type="submit"
                  disabled={savingUsername}
                  className="text-xs font-semibold text-blue-600 hover:text-blue-700 disabled:text-blue-300"
                >
                  {savingUsername ? 'Guardando...' : 'Guardar'}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setEditingUsername(false);
                    setUsernameForm(user?.username ?? '');
                    setUsernameError('');
                  }}
                  disabled={savingUsername}
                  className="text-xs font-semibold text-gray-500 hover:text-gray-700"
                >
                  Cancelar
                </button>
              </form>
            ) : (
              <div className="flex items-center gap-2">
                <h2 className="text-xl font-semibold text-gray-900">{user?.username}</h2>
                <button
                  type="button"
                  onClick={() => setEditingUsername(true)}
                  className="text-xs font-semibold text-blue-600 hover:text-blue-700"
                >
                  Editar
                </button>
              </div>
            )}
            <span className="inline-block mt-1 px-3 py-0.5 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
              {profile?.role}
            </span>
            {usernameError && (
              <p className="text-xs text-red-600 mt-1">{usernameError}</p>
            )}
            {usernameSuccess && (
              <p className="text-xs text-green-600 mt-1">{usernameSuccess}</p>
            )}
          </div>
        </div>

        {error && (
          <div className="mb-4 bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
            <p className="text-sm text-red-700">{error}</p>
          </div>
        )}

        {success && (
          <div className="mb-4 bg-green-50 border-l-4 border-green-500 p-4 rounded-md">
            <p className="text-sm text-green-700">{success}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Correo electrónico
            </label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={saving}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Dirección
            </label>
            <input
              type="text"
              name="address"
              value={form.address}
              onChange={handleChange}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={saving}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              RUN
            </label>
            <input
              type="text"
              name="run"
              value={form.run}
              onChange={handleChange}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={saving}
            />
          </div>

          <button
            type="submit"
            disabled={saving}
            className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-semibold rounded-lg transition-all"
          >
            {saving ? 'Guardando...' : 'Guardar cambios'}
          </button>
        </form>
      </div>
    </div>
  );
};