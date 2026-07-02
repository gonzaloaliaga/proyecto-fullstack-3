import { useEffect, useState } from 'react';
import { apiGetDonations } from '../../services/donationService';
import type { Donation } from '../../services/donationService';
import { apiGetCollectionCenters } from '../../services/inventoryService';
import type { CollectionCenter } from '../../services/inventoryService';
import { apiGetNeeds, apiGetShipments } from '../../services/logisticService';
import type { Need, Shipment } from '../../services/logisticService';

export const AdminDashboard = () => {
  const [donations, setDonations] = useState<Donation[]>([]);
  const [centers, setCenters] = useState<CollectionCenter[]>([]);
  const [needs, setNeeds] = useState<Need[]>([]);
  const [shipments, setShipments] = useState<Shipment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const [don, cen, ned, shi] = await Promise.all([
          apiGetDonations(),
          apiGetCollectionCenters(),
          apiGetNeeds(),
          apiGetShipments(),
        ]);
        setDonations(don);
        setCenters(cen);
        setNeeds(ned);
        setShipments(shi);
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Error al cargar datos.';
        setError(msg);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-48">
        <p className="text-gray-500">Cargando panel de administración...</p>
      </div>
    );
  }

  const donacionesPendientes = donations.filter(d => d.status === 'PENDING').length;
  const donacionesEntregadas = donations.filter(d => d.status === 'DELIVERED').length;
  const centrosActivos = centers.filter(c => c.active).length;
  const necesidadesActivas = needs.filter(n => n.status === 'REPORTED' || n.status === 'IN_PROGRESS').length;
  const enviosEnCurso = shipments.filter(s => s.status === 'IN_TRANSIT').length;

  return (
    <div className="space-y-6">

      {error && (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
          <p className="text-sm text-red-700">{error}</p>
        </div>
      )}

      {/* Tarjetas de resumen */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl shadow-sm p-5 border-l-4 border-blue-500">
          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Total donaciones</p>
          <p className="text-3xl font-bold text-gray-800 mt-2">{donations.length}</p>
          <p className="text-xs text-gray-400 mt-1">{donacionesPendientes} pendientes</p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-5 border-l-4 border-green-500">
          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Centros activos</p>
          <p className="text-3xl font-bold text-gray-800 mt-2">{centrosActivos}</p>
          <p className="text-xs text-gray-400 mt-1">de {centers.length} totales</p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-5 border-l-4 border-red-500">
          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Necesidades activas</p>
          <p className="text-3xl font-bold text-gray-800 mt-2">{necesidadesActivas}</p>
          <p className="text-xs text-gray-400 mt-1">de {needs.length} totales</p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-5 border-l-4 border-yellow-500">
          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Envíos en curso</p>
          <p className="text-3xl font-bold text-gray-800 mt-2">{enviosEnCurso}</p>
          <p className="text-xs text-gray-400 mt-1">{donacionesEntregadas} entregadas</p>
        </div>
      </div>

      {/* Tabla: últimas donaciones */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h3 className="font-semibold text-gray-800">Últimas donaciones</h3>
          <span className="text-xs text-gray-400">{donations.length} registros</span>
        </div>
        {donations.length === 0 ? (
          <p className="p-6 text-gray-500 text-sm">Sin donaciones registradas.</p>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left font-medium">Recurso</th>
                <th className="px-6 py-3 text-left font-medium">Cantidad</th>
                <th className="px-6 py-3 text-left font-medium">Origen</th>
                <th className="px-6 py-3 text-left font-medium">Fecha</th>
                <th className="px-6 py-3 text-left font-medium">Estado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {donations.slice(0, 10).map(d => (
                <tr key={d.id} className="hover:bg-gray-50">
                  <td className="px-6 py-3 font-medium text-gray-800">{d.resource}</td>
                  <td className="px-6 py-3 text-gray-600">{d.quantity}</td>
                  <td className="px-6 py-3 text-gray-600">{d.origin}</td>
                  <td className="px-6 py-3 text-gray-600">{d.donationDate}</td>
                  <td className="px-6 py-3">
                    <StatusBadge status={d.status} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Dos columnas: Centros y Necesidades */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        {/* Centros de acopio */}
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100">
            <h3 className="font-semibold text-gray-800">Centros de acopio</h3>
          </div>
          {centers.length === 0 ? (
            <p className="p-6 text-gray-500 text-sm">Sin centros registrados.</p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {centers.map(c => (
                <li key={c.id} className="px-6 py-3 flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{c.name}</p>
                    <p className="text-xs text-gray-500">{c.region}</p>
                  </div>
                  <span className={`text-xs px-2 py-0.5 rounded-full font-semibold ${
                    c.active
                      ? 'bg-green-100 text-green-800'
                      : 'bg-gray-100 text-gray-600'
                  }`}>
                    {c.active ? 'Activo' : 'Inactivo'}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Necesidades recientes */}
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100">
            <h3 className="font-semibold text-gray-800">Necesidades recientes</h3>
          </div>
          {needs.length === 0 ? (
            <p className="p-6 text-gray-500 text-sm">Sin necesidades registradas.</p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {needs.slice(0, 6).map(n => (
                <li key={n.id} className="px-6 py-3 flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{n.resource}</p>
                    <p className="text-xs text-gray-500">{n.location}</p>
                  </div>
                  <NeedBadge status={n.status} />
                </li>
              ))}
            </ul>
          )}
        </div>

      </div>
    </div>
  );
};

const STATUS_MAP: Record<string, { label: string; cls: string }> = {
  PENDING:   { label: 'Pendiente',  cls: 'bg-yellow-100 text-yellow-800' },
  RECEIVED:  { label: 'Recibida',   cls: 'bg-blue-100 text-blue-800' },
  ASSIGNED:  { label: 'Asignada',   cls: 'bg-purple-100 text-purple-800' },
  DELIVERED: { label: 'Entregada',  cls: 'bg-green-100 text-green-800' },
};

const NEED_MAP: Record<string, { label: string; cls: string }> = {
  REPORTED:    { label: 'Reportada',   cls: 'bg-red-100 text-red-800' },
  IN_PROGRESS: { label: 'En curso',    cls: 'bg-yellow-100 text-yellow-800' },
  COVERED:     { label: 'Cubierta',    cls: 'bg-green-100 text-green-800' },
  CANCELLED:   { label: 'Cancelada',   cls: 'bg-gray-100 text-gray-600' },
};

const StatusBadge = ({ status }: { status: string }) => {
  const { label, cls } = STATUS_MAP[status] ?? { label: status, cls: 'bg-gray-100 text-gray-600' };
  return <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${cls}`}>{label}</span>;
};

const NeedBadge = ({ status }: { status: string }) => {
  const { label, cls } = NEED_MAP[status] ?? { label: status, cls: 'bg-gray-100 text-gray-600' };
  return <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${cls}`}>{label}</span>;
};