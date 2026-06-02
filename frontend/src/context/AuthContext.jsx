/* eslint-disable react/prop-types */
import { createContext, useState, useEffect } from 'react';
import profileClient from '../clients/profileClient';
import authClient from '../clients/authclient';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); 
    const [profile, setProfile] = useState(null); 
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 1. Efecto de carga inicial: Verifica si hay una sesión previa
    useEffect(() => {
        const initAuth = async () => {
            const token = localStorage.getItem('donaton_token');
            const userId = localStorage.getItem('user_id');
            const username = localStorage.getItem('username');

            if (token && userId && username) {
                try {
                    // Si hay token, intenta recuperar el perfil directamente
                    // Esto valida implícitamente si el token sigue siendo útil
                    await fetchUserProfile(userId);
                    setUser({ id: userId, username: username }); // Restaura el estado completo del usuario
                } catch (err) {
                    console.error("Sesión expirada o inválida:", err);
                    logout(); // Si el token falló, limpia todo
                }
            }
            setLoading(false);
        };

        initAuth();
    }, []);

    // 2. Efecto para obtener perfil cuando el usuario cambia (ej: tras el login)
    useEffect(() => {
        if (user && user.id && !profile) {
            fetchUserProfile(user.id);
        }
    }, [user?.id]);

    const fetchUserProfile = async (userId) => {
        setLoading(true);
        setError(null);
        try {
            const profileData = await profileClient.getUserProfile(userId);
            setProfile(profileData);
        } catch (err) {
            setError(err.message || "Error al cargar el perfil");
            throw err; 
        } finally {
            setLoading(false);
        }
    };

    const login = async (username, password) => {
        setLoading(true);
        setError(null);
        try {
            const userData = await authClient.login(username, password);
            // El authClient ya guarda el token en localStorage según lo implementamos antes
            setUser({ id: userData.id, username: userData.username }); // Solo guardar id y username en el estado
            return userData;
        } catch (err) {
            const mensajeError = err.message === "Failed to fetch"
                ? "Error de conexión con el servidor."
                : "Credenciales incorrectas.";
            setError(mensajeError);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const logout = () => {
        // Limpia estados de React
        setUser(null);
        setProfile(null);
        setError(null);
        // Limpia persistencia física
        authClient.logout(); 
    };

    const updateProfile = async (updatedData) => {
        if (!user) throw new Error('No hay usuario logueado');
        const updatedProfile = await profileClient.patchUserProfile(user.id, updatedData);
        setProfile(updatedProfile);
        return updatedProfile;
    };

    const updateUsername = async (newUsername) => {
        if (!user) throw new Error('No hay usuario logueado');
        
        await authClient.updateUsername(newUsername);
        
        const updatedUser = { ...user, username: newUsername };
        setUser(updatedUser);
        
        if (profile) {
            setProfile({ ...profile, username: newUsername });
        }

        localStorage.setItem('username', newUsername);

        return updatedUser;
    };

    return (
        <AuthContext.Provider value={{ 
            user, 
            setUser, 
            profile, 
            updateProfile,
            updateUsername,
            loading, 
            error, 
            login, 
            logout,
            fetchUserProfile 
        }}>
            {children}
        </AuthContext.Provider>
    );
};