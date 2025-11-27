from rediscluster import RedisCluster
import os

REDIS_HOST = os.getenv("REDIS_HOST")
REDIS_HOST2 = os.getenv("REDIS_HOST2")
REDIS_HOST3 = os.getenv("REDIS_HOST3")


def get_cluster_client():
    startup_nodes = [
        {"host": REDIS_HOST, "port": 6379},
        {"host": REDIS_HOST2, "port": 6379},
        {"host": REDIS_HOST3, "port": 6379},
    ]

    return RedisCluster(
        startup_nodes=startup_nodes,
        decode_responses=True,
        skip_full_coverage_check=True,
        socket_timeout=5,
        socket_connect_timeout=5,
    )
