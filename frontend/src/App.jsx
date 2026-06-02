import { useContext } from 'react';
import { AuthProvider, AuthContext } from './contexts/AuthContext';
import Login from './pages/Login';
import Profile from './pages/Profile';

function Content() {
    const { user } = useContext(AuthContext);
    return user ? <Profile /> : <Login />;
}

export default function App() {
    return (
        <AuthProvider>
            <Content />
        </AuthProvider>
    );
}