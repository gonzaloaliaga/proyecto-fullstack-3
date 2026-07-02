import { API, apiFetch, authHeaders, throwIfError } from './httpClient';
 
export type NeedStatus = 'REPORTED' | 'IN_PROGRESS' | 'COVERED' | 'CANCELLED';
export type ShipmentStatus = 'PLANNED' | 'IN_TRANSIT' | 'DELIVERED' | 'CANCELLED';
 
export interface Need {
  id: number;
  resource: string;
  quantity: number;
  location: string;
  latitude: number | null;
  longitude: number | null;
  reportedBy: string;
  status: NeedStatus;
  reportedAt: string;
}
 
export interface Shipment {
  id: number;
  donationId: number | null;
  originCenterId: number;
  destination: string;
  status: ShipmentStatus;
  scheduledDate: string;
  deliveredAt: string | null;
}
 
export async function apiGetNeeds(): Promise<Need[]> {
  const res = await apiFetch(`${API}/needs`, {
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
 
export async function apiGetShipments(): Promise<Shipment[]> {
  const res = await apiFetch(`${API}/shipments`, {
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
