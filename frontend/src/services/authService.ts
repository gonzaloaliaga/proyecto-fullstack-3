import { API, apiFetch, throwIfError } from './httpClient';
 
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
