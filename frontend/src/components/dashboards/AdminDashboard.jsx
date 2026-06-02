export const AdminDashboard = () => {
    return (
        <div className="space-y-6">
            
            {/* Encabezado del Dashboard */}
            <div>
                <h2 className="text-2xl font-bold text-slate-800">Panel de Administración</h2>
                <p className="text-slate-500 text-sm mt-1">Resumen global y control del sistema de donaciones.</p>
            </div>

            {/* Fila de Tarjetas de Estadísticas (Métricas Simuladas) */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                
                {/* Tarjeta 1 */}
                <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm flex items-center gap-4 hover:shadow-md transition-shadow">
                    <div className="w-12 h-12 rounded-lg bg-blue-100 text-blue-600 flex items-center justify-center text-2xl">
                        👥
                    </div>
                    <div>
                        <p className="text-sm font-medium text-slate-500">Usuarios Activos</p>
                        <p className="text-2xl font-bold text-slate-800">124</p>
                    </div>
                </div>

                {/* Tarjeta 2 */}
                <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm flex items-center gap-4 hover:shadow-md transition-shadow">
                    <div className="w-12 h-12 rounded-lg bg-green-100 text-green-600 flex items-center justify-center text-2xl">
                        📦
                    </div>
                    <div>
                        <p className="text-sm font-medium text-slate-500">Donaciones Hoy</p>
                        <p className="text-2xl font-bold text-slate-800">38</p>
                    </div>
                </div>

                {/* Tarjeta 3 */}
                <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm flex items-center gap-4 hover:shadow-md transition-shadow">
                    <div className="w-12 h-12 rounded-lg bg-purple-100 text-purple-600 flex items-center justify-center text-2xl">
                        📍
                    </div>
                    <div>
                        <p className="text-sm font-medium text-slate-500">Zonas Activas</p>
                        <p className="text-2xl font-bold text-slate-800">12</p>
                    </div>
                </div>
            </div>

            {/* Sección de Actividad Reciente */}
            <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm">
                <div className="mb-4">
                    <h3 className="font-bold text-lg text-slate-800">Actividad Reciente</h3>
                    <div className="h-1 w-12 bg-blue-500 rounded-full mt-1"></div>
                </div>
                
                <ul className="space-y-4">
                    {/* Item 1 */}
                    <li className="flex items-center p-3 hover:bg-slate-50 rounded-lg transition-colors gap-3">
                        <span className="w-2 h-2 rounded-full bg-green-500 flex-shrink-0"></span>
                        <div className="flex items-center gap-2">
                            <span className="text-slate-700 font-medium">Nuevo donante registrado: HACE 5 MIN</span>
                        </div>
                    </li>
                    
                    {/* Item 2 */}
                    <li className="flex items-center p-3 hover:bg-slate-50 rounded-lg transition-colors gap-3">
                        <span className="w-2 h-2 rounded-full bg-blue-500 flex-shrink-0"></span>
                        <div className="flex items-center gap-2">
                            <span className="text-slate-700 font-medium">Inventario actualizado: HACE 12 MIN</span>
                        </div>
                    </li>
                    
                    {/* Item 3 */}
                    <li className="flex items-center p-3 hover:bg-slate-50 rounded-lg transition-colors gap-3">
                        <span className="w-2 h-2 rounded-full bg-yellow-500 flex-shrink-0"></span>
                        <div className="flex items-center gap-2">
                            <span className="text-slate-700 font-medium">Alerta de stock bajo: HACE 1 HORA</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    );
};