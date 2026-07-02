import { useEffect, useState } from 'react';
import { apiGetInventoryItems } from '../../services/inventoryService';
import type { InventoryItem } from '../../services/inventoryService';
import { apiGetNeeds, apiGetShipments } from '../../services/logisticService';
import type { Need, Shipment } from '../../services/logisticService';

const NEED_COLORS: Record<string, string> = {
  REPORTED: 'bg-red-100 text-red-800',
  IN_PROGRESS: 'bg-yellow-100 text-yellow-800',
  COVERED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-gray-100 text-gray-600',
};

const SHIPMENT_COLORS: Record<string, string> = {
  PLANNED: 'bg-blue-100 text-blue-800',
  IN_TRANSIT: 'bg-yellow-100 text-yellow-800',
  DELIVERED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-gray-100 text-gray-600',
};

export const LogisticDashboard = () => {
  const [items, setItems] = useState<InventoryItem[]>([]);
  const [needs, setNeeds] = useState<Need[]>([]);
  const [shipments, setShipments] = useState<Shipment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    const load = async () => {
      try {
        const [inv, ned, shi] = await Promise.all([
          apiGetInventoryItems(),
          apiGetNeeds(),
          apiGetShipments(),
        ]);
        setItems(inv);
        setNeeds(ned);
        setShipments(shi);
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Error al cargar datos logísticos.';
        setError(msg);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  if (loading) return <p className="text-gray-500">Cargando datos logísticos...</p>;

  return (
    <div className="space-y-6">
      {error && (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
          <p className="text-sm text-red-700">{error}</p>
        </div>
      )}

      {/* Inventario */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h3 className="font-semibold text-gray-800">Inventario por centro</h3>
        </div>
        {items.length === 0 ? (
          <p className="p-6 text-gray-500 text-sm">Sin items registrados.</p>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left font-medium">Centro</th>
                <th className="px-6 py-3 text-left font-medium">Recurso</th>
                <th className="px-6 py-3 text-left font-medium">Cantidad</th>
                <th className="px-6 py-3 text-left font-medium">Unidad</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {items.map(item => (
                <tr key={item.id} className="hover:bg-gray-50">
                  <td className="px-6 py-3 text-gray-800">{item.collectionCenter.name}</td>
                  <td className="px-6 py-3 text-gray-600">{item.resource}</td>
                  <td className="px-6 py-3 font-medium text-gray-800">{item.quantity}</td>
                  <td className="px-6 py-3 text-gray-600">{item.unit}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Necesidades */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h3 className="font-semibold text-gray-800">Necesidades reportadas</h3>
        </div>
        {needs.length === 0 ? (
          <p className="p-6 text-gray-500 text-sm">Sin necesidades registradas.</p>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left font-medium">Recurso</th>
                <th className="px-6 py-3 text-left font-medium">Cantidad</th>
                <th className="px-6 py-3 text-left font-medium">Ubicación</th>
                <th className="px-6 py-3 text-left font-medium">Reportado por</th>
                <th className="px-6 py-3 text-left font-medium">Estado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {needs.map(n => (
                <tr key={n.id} className="hover:bg-gray-50">
                  <td className="px-6 py-3 font-medium text-gray-800">{n.resource}</td>
                  <td className="px-6 py-3 text-gray-600">{n.quantity}</td>
                  <td className="px-6 py-3 text-gray-600">{n.location}</td>
                  <td className="px-6 py-3 text-gray-600">{n.reportedBy}</td>
                  <td className="px-6 py-3">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${NEED_COLORS[n.status]}`}>
                      {n.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Envíos */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h3 className="font-semibold text-gray-800">Envíos</h3>
        </div>
        {shipments.length === 0 ? (
          <p className="p-6 text-gray-500 text-sm">Sin envíos registrados.</p>
        ) : (
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left font-medium">Destino</th>
                <th className="px-6 py-3 text-left font-medium">Fecha programada</th>
                <th className="px-6 py-3 text-left font-medium">Estado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {shipments.map(s => (
                <tr key={s.id} className="hover:bg-gray-50">
                  <td className="px-6 py-3 font-medium text-gray-800">{s.destination}</td>
                  <td className="px-6 py-3 text-gray-600">{s.scheduledDate}</td>
                  <td className="px-6 py-3">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${SHIPMENT_COLORS[s.status]}`}>
                      {s.status}
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