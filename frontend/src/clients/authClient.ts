const API_BASE_URL = 'http://localhost:8080/api/auth';

// Definición de la interfaz basada en tu AuthResponse de Kotlin
interface AuthResponse {
    id: number;
    username: string;
    token: string;
}

const authClient = {
    // Lógica de Login: Almacena el token si la respuesta es exitosa
    login: async (username: string, password: string): Promise<AuthResponse> => {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        if (!response.ok) throw new Error("Error de autenticación");

        const data: AuthResponse = await response.json();
        
        // Guarda el token para usarlo en futuras peticiones
        localStorage.setItem('donaton_token', data.token);
        localStorage.setItem('user_id', data.id.toString());
        localStorage.setItem('username', data.username);
        
        return data;
    },

    // Lógica de Actualización de Nombre (Usa el token Bearer)
    updateUsername: async (newUsername: string) => {
        const token = localStorage.getItem('donaton_token');
        
        const response = await fetch(`${API_BASE_URL}/update-username`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Inyecta el token
            },
            body: JSON.stringify({ newUsername })
        });

        if (!response.ok) throw new Error("No se pudo actualizar el nombre");
        return await response.json();
    },

    logout: () => {
        localStorage.removeItem('donaton_token');
        localStorage.removeItem('user_id');
        localStorage.removeItem('username');
    }
};

export default authClient;