import DefaultTheme from 'vitepress/theme'
import { withBase } from 'vitepress'
import './custom.css'

if (import.meta.env.PROD && typeof navigator !== 'undefined' && 'serviceWorker' in navigator) {
  const registerServiceWorker = () => {
    navigator.serviceWorker
      .register(withBase('/service-worker.js'), { scope: withBase('/') })
      .catch((error) => console.error('No se pudo registrar el service worker:', error))
  }

  if (document.readyState === 'complete') registerServiceWorker()
  else window.addEventListener('load', registerServiceWorker, { once: true })
}

export default DefaultTheme
