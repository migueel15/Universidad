const CACHE_NAME = 'universidad-pwa-v1'

self.addEventListener('install', (event) => {
  event.waitUntil((async () => {
    const cache = await caches.open(CACHE_NAME)
    await cache.addAll([
      self.registration.scope,
      new URL('manifest.webmanifest', self.registration.scope).href,
      new URL('icons/app-icon-192.png', self.registration.scope).href,
      new URL('icons/app-icon-512.png', self.registration.scope).href,
    ])
    await self.skipWaiting()
  })())
})

self.addEventListener('activate', (event) => {
  event.waitUntil((async () => {
    const keys = await caches.keys()
    await Promise.all(
      keys
        .filter((key) => key.startsWith('universidad-pwa-') && key !== CACHE_NAME)
        .map((key) => caches.delete(key)),
    )
    await self.clients.claim()
  })())
})

self.addEventListener('fetch', (event) => {
  const request = event.request
  const url = new URL(request.url)

  if (request.method !== 'GET' || url.origin !== self.location.origin) return
  if (!url.href.startsWith(self.registration.scope)) return

  if (request.mode === 'navigate') {
    event.respondWith((async () => {
      const cache = await caches.open(CACHE_NAME)
      try {
        const response = await fetch(request)
        if (response.ok) await cache.put(request, response.clone())
        return response
      } catch {
        return await cache.match(request) || await cache.match(self.registration.scope)
      }
    })())
    return
  }

  event.respondWith((async () => {
    const cache = await caches.open(CACHE_NAME)
    try {
      const response = await fetch(request)
      if (response.ok) await cache.put(request, response.clone())
      return response
    } catch (error) {
      const cached = await cache.match(request)
      if (cached) return cached
      throw error
    }
  })())
})
