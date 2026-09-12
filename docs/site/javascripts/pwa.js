(() => {
  if (!("serviceWorker" in navigator)) return;

  window.addEventListener("load", () => {
    const manifest = document.querySelector('link[rel="manifest"]');
    if (!manifest) return;

    const manifestUrl = new URL(manifest.href, window.location.href);
    const serviceWorkerUrl = new URL("service-worker.js", manifestUrl);

    navigator.serviceWorker.register(serviceWorkerUrl.href).catch((error) => {
      console.error("Service worker registration failed:", error);
    });
  });
})();
