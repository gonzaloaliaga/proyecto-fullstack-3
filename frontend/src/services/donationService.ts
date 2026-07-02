import { API, apiFetch, authHeaders, throwIfError } from './httpClient';
 
export type DonationStatus = 'PENDING' | 'RECEIVED' | 'ASSIGNED' | 'DELIVERED';
 
export interface Donation {
  id: number;
  resource: string;
  quantity: number;
  origin: string;
  donationDate: string;
  collectionCenterId: number;
  status: DonationStatus;
}
 
export interface CreateDonationRequest {
  resource: string;
  quantity: number;
  origin: string;
  donationDate: string;
  collectionCenterId: number;
}
 
export async function apiGetDonations(): Promise<Donation[]> {
  const res = await apiFetch(`${API}/donations`, {
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
 
export async function apiCreateDonation(data: CreateDonationRequest): Promise<Donation> {
  const res = await apiFetch(`${API}/donations`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(data),
  });
  await throwIfError(res);
  return res.json();
}
