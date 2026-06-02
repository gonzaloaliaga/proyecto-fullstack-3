import { useState, useContext, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login, error, loading, user } = useContext(AuthContext);

    // Si el usuario ya existe (por persistencia), el App.js 
    // mostrará el perfil automáticamente por el condicional {user ? <Profile /> : <Login />}
    
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await login(username, password);
            // Al terminar el login, el estado 'user' del Contexto cambia
            // y App.js mostrará <Profile /> instantáneamente.
        } catch (err) {
            setPassword('');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-blue-600 px-4">
            <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-sm flex flex-col items-center">
                
                <div className="flex flex-col items-center mb-8">
                    <img 
                        src="/favicon.svg" 
                        alt="Logo Donatón" 
                        className={`w-16 h-16 mb-3 ${loading ? 'animate-bounce' : ''}`} 
                    />
                    <h1 className="text-2xl font-bold text-slate-800 tracking-tight">Donatón</h1>
                </div>
                
                <form onSubmit={handleLogin} className="w-full space-y-4 flex flex-col items-center">
                    <input 
                        type="text" 
                        placeholder="usuario" 
                        value={username}
                        onChange={(e) => setUsername(e.target.value)} 
                        className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:border-blue-400 outline-none transition-all text-sm"
                        required 
                        disabled={loading}
                    />
                    
                    <input 
                        type="password" 
                        placeholder="contraseña" 
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} 
                        className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:border-blue-400 outline-none transition-all text-sm"
                        required 
                        disabled={loading}
                    />

                    {error && (
                        <p className="text-xs text-red-500 font-medium py-1 text-center w-full">
                            {error}
                        </p>
                    )}

                    <button 
                        type="submit" 
                        disabled={loading}
                        className={`w-full ${loading ? 'bg-slate-400' : 'bg-slate-800 hover:bg-slate-700'} text-white font-bold py-3.5 rounded-xl transition-all duration-200 shadow-md mt-2`}
                    >
                        {loading ? 'autenticando...' : 'ingresar'}
                    </button>
                </form>
            </div>
        </div>
    );
}