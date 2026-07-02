import { BrowserRouter } from 'react-router-dom';
import { AppRouter } from './routes/AppRouter';
import * as Sentry from '@sentry/react';

export default function App() {
  return (
    <Sentry.ErrorBoundary fallback={<p>Ha ocurrido un error inesperado.</p>}>
      <BrowserRouter>
        <AppRouter />
      </BrowserRouter>
    </Sentry.ErrorBoundary>
  );
}