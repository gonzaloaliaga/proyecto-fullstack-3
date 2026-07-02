import { useEffect, useState } from 'react';
import { apiGetDonations, apiCreateDonation } from '../../services/donationService';
import type { Donation } from '../../services/donationService';
import { apiGetCollectionCenters } from '../../services/inventoryService';
import type { CollectionCenter } from '../../services/inventoryService';

const STATUS_LABELS: Record<string, string> = {
  PENDING: 'Pendiente',
  RECEIVED: 'Recibida',
  ASSIGNED: 'Asignada',
  DELIVERED: 'Entregada',
};

const STATUS_COLORS: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-800',
  RECEIVED: 'bg-blue-100 text-blue-800',
  ASSIGNED: 'bg-purple-100 text-purple-800',
  DELIVERED: 'bg-green-100 text-green-800',
};

const emptyForm = {
  resource: '',
  quantity: '',
  origin: '',
  donationDate: '',
  collectionCenterId: '',
};

export const DonorDashboard = () => {
  const [donations, setDonations] = useState<Donation[]>([]);
  const [centers, setCenters] = useState<CollectionCenter[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [creating, setCreating] = useState(false);
  const [formError, setFormError] = useState('');

  const load = async () => {
    try {
      const [don, cen] = await Promise.all([
        apiGetDonations(),
        apiGetCollectionCenters(),
      ]);
      setDonations(don);
      setCenters(cen);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Error al cargar datos.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleFormChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleCreateDonation = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormError('');
    setCreating(true);

    try {
      await apiCreateDonation({
        resource: form.resource,
        quantity: Number(form.quantity),
        origin: form.origin,
        donationDate: form.donationDate,
        collectionCenterId: Number(form.collectionCenterId),
      });
      setForm(emptyForm);
      setShowForm(false);
      await load();
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Error al registrar la donación.';
      setFormError(msg);
    } finally {
      setCreating(false);
    }
  };

  const getCenterName = (id: number) =>
    centers.find(c => c.id === id)?.name ?? `Centro #${id}`;

  if (loading) return <p className="text-gray-500">Cargando donaciones...</p>;

  return (
    <div className="space-y-6">
      {error && (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
          <p className="text-sm text-red-700">{error}</p>
        </div>
      )}

      <div className="flex items-center justify-between">
        <h2 className="text-lg font-semibold text-gray-900">Mis donaciones</h2>
        <button
          type="button"
          onClick={() => setShowForm(prev => !prev)}
          className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-lg transition-all"
        >
          {showForm ? 'Cancelar' : '+ Nueva donación'}
        </button>
      </div>

      {showForm && (
        <form
          onSubmit={handleCreateDonation}
          className="bg-white rounded-xl shadow-sm p-6 grid grid-cols-1 md:grid-cols-2 gap-4"
        >
          {formError && (
            <div className="md:col-span-2 bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
              <p className="text-sm text-red-700">{formError}</p>
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Recurso</label>
            <input
              type="text"
              name="resource"
              value={form.resource}
              onChange={handleFormChange}
              required
              placeholder="Ej: Ropa de invierno"
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={creating}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Cantidad</label>
            <input
              type="number"
              name="quantity"
              value={form.quantity}
              onChange={handleFormChange}
              required
              min={1}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={creating}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Origen</label>
            <input
              type="text"
              name="origin"
              value={form.origin}
              onChange={handleFormChange}
              required
              placeholder="Ej: Empresa TextilSur SpA"
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={creating}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Fecha de donación</label>
            <input
              type="date"
              name="donationDate"
              value={form.donationDate}
              onChange={handleFormChange}
              required
              max={new Date().toISOString().split('T')[0]}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all"
              disabled={creating}
            />
          </div>

          <div className="md:col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-1">Centro de acopio</label>
            <select
              name="collectionCenterId"
              value={form.collectionCenterId}
              onChange={handleFormChange}
              required
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all bg-white"
              disabled={creating}
            >
              <option value="" disabled>Selecciona un centro</option>
              {centers.map(c => (
                <option key={c.id} value={c.id}>{c.name} — {c.region}</option>
              ))}
            </select>
          </div>

          <div className="md:col-span-2">
            <button
              type="submit"
              disabled={creating}
              className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-semibold rounded-lg transition-all"
            >
              {creating ? 'Registrando...' : 'Registrar donación'}
            </button>
          </div>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl shadow-sm p-5">
          <p className="text-sm text-gray-500">Total donaciones</p>
          <p className="text-3xl font-bold text-gray-800 mt-1">{donations.length}</p>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <p className="text-sm text-gray-500">Entregadas</p>
          <p className="text-3xl font-bold text-green-600 mt-1">
            {donations.filter(d => d.status === 'DELIVERED').length}
          </p>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <p className="text-sm text-gray-500">Pendientes</p>
          <p className="text-3xl font-bold text-yellow-600 mt-1">
            {donations.filter(d => d.status === 'PENDING').length}
          </p>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h3 className="font-semibold text-gray-800">Historial de donaciones</h3>
        </div>
        {donations.length === 0 ? (
          <p className="p-6 text-gray-500 text-sm">No hay donaciones registradas.</p>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left font-medium">Recurso</th>
                <th className="px-6 py-3 text-left font-medium">Cantidad</th>
                <th className="px-6 py-3 text-left font-medium">Origen</th>
                <th className="px-6 py-3 text-left font-medium">Centro</th>
                <th className="px-6 py-3 text-left font-medium">Fecha</th>
                <th className="px-6 py-3 text-left font-medium">Estado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {donations.map(d => (
                <tr key={d.id} className="hover:bg-gray-50">
                  <td className="px-6 py-3 font-medium text-gray-800">{d.resource}</td>
                  <td className="px-6 py-3 text-gray-600">{d.quantity}</td>
                  <td className="px-6 py-3 text-gray-600">{d.origin}</td>
                  <td className="px-6 py-3 text-gray-600">{getCenterName(d.collectionCenterId)}</td>
                  <td className="px-6 py-3 text-gray-600">{d.donationDate}</td>
                  <td className="px-6 py-3">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${STATUS_COLORS[d.status]}`}>
                      {STATUS_LABELS[d.status]}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};