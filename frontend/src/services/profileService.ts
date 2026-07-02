import { API, apiFetch, authHeaders, throwIfError } from './httpClient';
 
export interface ProfileResponse {
  id: number;
  role: string;
  email: string;
  address: string;
  run: string;
}
 
export interface UpdateProfileRequest {
  email?: string;
  address?: string;
  run?: string;
}
 
export async function apiGetProfile(userId: number): Promise<ProfileResponse> {
  const res = await apiFetch(`${API}/profile/${userId}`, {
    method: 'GET',
    headers: authHeaders(),
  });
  await throwIfError(res);
  return res.json();
}
 
export async function apiUpdateProfile(
  userId: number,
  data: UpdateProfileRequest
): Promise<ProfileResponse> {
  const res = await apiFetch(`${API}/profile/${userId}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(data),
  });
  await throwIfError(res);
  return res.json();
}
