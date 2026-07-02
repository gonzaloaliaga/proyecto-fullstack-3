import * as Sentry from '@sentry/react'

const dsn = import.meta.env.VITE_SENTRY_DSN

if (dsn) {
  Sentry.init({
    dsn,
    environment: import.meta.env.VITE_SENTRY_ENVIRONMENT ?? 'development',
    integrations: [
      Sentry.browserTracingIntegration(),
    ],
    tracesSampleRate: 1.0,
    sendDefaultPii: false,
  })
} else {
  console.warn('[GlitchTip] VITE_SENTRY_DSN no configurado, el reporte de errores está deshabilitado.')
}
