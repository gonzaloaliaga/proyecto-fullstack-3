const BFF_URL = '/bff';
const DONATIONS_URL = '/donations';
const INVENTORY_URL = '/inventory';
const LOGISTIC_URL = '/logistic';

function getToken(): string | null {
  return localStorage.getItem('donaton_token');
}

function authHeaders(): HeadersInit {
  const token = getToken();
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
}

async function apiFetch(url: string, options: RequestInit = {}): Promise<Response> {
  const res = await fetch(url, options);

  if (res.status === 401) {
    localStorage.removeItem('donaton_token');
    localStorage.removeItem('donaton_user');
    window.location.href = '/login';
  }

  return res;
}

// ---------- Auth ----------

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  username: string;
  token: string;
}

export async function apiLogin(payload: LoginRequest): Promise<LoginResponse> {
  const res = await apiFetch(`${BFF_URL}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Error ${res.status}`);
  }

  return res.json() as Promise<LoginResponse>;
}

// ---------- Profile ----------

export interface ProfileResponse {
  id: number;
  role: string;
  email: string;
  address: string;
  run: string;
}

export async function apiGetProfile(userId: number): Promise<ProfileResponse> {
  const res = await apiFetch(`${BFF_URL}/api/profile/${userId}`, {
    method: 'GET',
    headers: authHeaders(),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Error ${res.status}`);
  }

  return res.json() as Promise<ProfileResponse>;
}

export interface UpdateProfileRequest {
  email?: string;
  address?: string;
  run?: string;
}

export async function apiUpdateProfile(
  userId: number,
  data: UpdateProfileRequest
): Promise<ProfileResponse> {
  const res = await apiFetch(`${BFF_URL}/api/profile/${userId}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Error ${res.status}`);
  }

  return res.json() as Promise<ProfileResponse>;
}

// ---------- Donations ----------

const DONATIONS_URL = 'http://localhost:8083';

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
  const res = await apiFetch(`${DONATIONS_URL}/api/donations`, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error(`Error ${res.status}`);
  return res.json();
}

export async function apiCreateDonation(data: CreateDonationRequest): Promise<Donation> {
  const res = await apiFetch(`${DONATIONS_URL}/api/donations`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Error ${res.status}`);
  }
  return res.json();
}

// ---------- Inventory ----------

const INVENTORY_URL = 'http://localhost:8084';

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
  const res = await apiFetch(`${INVENTORY_URL}/api/collection-centers`, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error(`Error ${res.status}`);
  return res.json();
}

export async function apiGetInventoryItems(): Promise<InventoryItem[]> {
  const res = await apiFetch(`${INVENTORY_URL}/api/inventory-items`, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error(`Error ${res.status}`);
  return res.json();
}

// ---------- Logistic ----------

const LOGISTIC_URL = 'http://localhost:8085';

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
  const res = await apiFetch(`${LOGISTIC_URL}/api/needs`, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error(`Error ${res.status}`);
  return res.json();
}

export async function apiGetShipments(): Promise<Shipment[]> {
  const res = await apiFetch(`${LOGISTIC_URL}/api/shipments`, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error(`Error ${res.status}`);
  return res.json();
}
