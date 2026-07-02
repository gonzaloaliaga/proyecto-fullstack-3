import { API, apiFetch, authHeaders, throwIfError } from './httpClient';

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
  const res = await apiFetch(`${API}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  await throwIfError(res);
  return res.json();
}

export interface UpdateUsernameRequest {
  newUsername: string;
}

export interface UpdateUsernameResponse {
  userId: number;
  newUsername: string;
}

export async function apiUpdateUsername(
  newUsername: string
): Promise<UpdateUsernameResponse> {
  const res = await apiFetch(`${API}/auth/update-username`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ newUsername } satisfies UpdateUsernameRequest),
  });
  await throwIfError(res);
  return res.json();
}