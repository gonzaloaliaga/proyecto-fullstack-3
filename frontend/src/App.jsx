import { useContext } from 'react';
import { AuthProvider, AuthContext } from './context/AuthContext';
import Login from './components/Login';
import Profile from './components/Profile';

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