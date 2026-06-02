import Sidebar from './Sidebar';

export default function MainLayout({ children }) {
    return (
        /* El contenedor principal mantiene el color de fondo en toda la pantalla */
        <div className="min-h-screen bg-slate-50">
            
            {/* Este contenedor flex hará que el Sidebar y el Main midan lo mismo según el contenido */}
            <div className="flex items-stretch">
                
                {/* El Sidebar ahora crecerá o encogerá según el alto de las tarjetas del Main */}
                <Sidebar />

                {/* El área de contenido */}
                <main className="flex-1 p-8">
                    {children}
                </main>
            </div>
        </div>
    );
}