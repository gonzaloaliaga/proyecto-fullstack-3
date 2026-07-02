const API = '/api';
 
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
 
/**
 * Lanza un Error con el mensaje del backend si la respuesta no es exitosa.
 * Centraliza el manejo de errores para no repetirlo en cada servicio.
 */
async function throwIfError(res: Response): Promise<void> {
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Error ${res.status}`);
  }
}
 
export { API, apiFetch, authHeaders, throwIfError };
