import { API, apiFetch, authHeaders, throwIfError } from './httpClient';
 
export interface CollectionCenter {
  id: number;
  name: string;
  address: string;
  region: string;
  capacity: number;
  active: boolean;
}
 
export interface InventoryItem {
  id: number;
  collectionCenter: CollectionCenter;
  resource: string;
  quantity: number;
  unit: string;
  lastUpdated: string;
}
 
export async function apiGetCollectionCenters(): Promise<CollectionCenter[]> {
  const res = await apiFetch(`${API}/collection-centers`, {
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
 
export async function apiGetInventoryItems(): Promise<InventoryItem[]> {
  const res = await apiFetch(`${API}/inventory-items`, {
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
