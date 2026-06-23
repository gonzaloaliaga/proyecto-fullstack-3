import { useEffect, useState } from 'react';
import { apiGetDonations, apiGetCollectionCenters } from '../../services/api';
import type { Donation, CollectionCenter } from '../../services/api';

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

export const DonorDashboard = () => {
  const [donations, setDonations] = useState<Donation[]>([]);
  const [centers, setCenters] = useState<CollectionCenter[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
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
    load();
  }, []);

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
