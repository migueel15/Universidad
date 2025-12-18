from kazoo.client import KazooClient
import sys
import os

zk_hosts = os.getenv("ZOOKEEPER_HOSTS", "127.0.0.1:2181")

if len(sys.argv) != 3:
    print("Uso: python init_config.py <sampling_period> <api_url>")
    sys.exit(1)

sampling_period = sys.argv[1]
api_url = sys.argv[2]

client = KazooClient(hosts=zk_hosts)
client.start()

# Crear/actualizar configuración
client.ensure_path("/config")
client.ensure_path("/config/sampling_period")
client.set("/config/sampling_period", sampling_period.encode("utf-8"))
client.ensure_path("/config/api_url")
client.set("/config/api_url", api_url.encode("utf-8"))

print(f"Configuración inicializada: período={sampling_period}s,url={api_url}")
client.stop()
