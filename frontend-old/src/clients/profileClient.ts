const API_BASE_URL = 'http://localhost:8080/api/profile';

const profileClient = {
    getUserProfile: async (userId: number | string) => {
        const token = localStorage.getItem('donaton_token');

        try {
            const response = await fetch(`${API_BASE_URL}/${userId}`, {
                method: 'GET',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Requerido por el BFF
                },
            });
            
            if (!response.ok) {
                const error = await response.json().catch(() => ({ message: 'Error al obtener el perfil' }));
                throw new Error(error.message || "Error al obtener el perfil");
            }
            
            return await response.json();
        } catch (error) {
            console.error("Error en getUserProfile:", error);
            throw error;
        }
    },

    patchUserProfile: async (userId: number | string, profileData: any) => {
        const token = localStorage.getItem('donaton_token');

        try {
            const response = await fetch(`${API_BASE_URL}/${userId}`, {
                method: 'PATCH',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(profileData),
            });
            
            if (!response.ok) {
                const error = await response.json().catch(() => ({ message: 'Error al actualizar el perfil' }));
                throw new Error(error.message || "Error al actualizar el perfil");
            }
            
            return await response.json();
        } catch (error) {
            console.error("Error en patchUserProfile:", error);
            throw error;
        }
    }
};

export default profileClient;