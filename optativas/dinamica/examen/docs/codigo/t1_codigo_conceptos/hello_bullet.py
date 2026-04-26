import pybullet as p
import pybullet_data
import time

# Configuración inicial
p.connect(p.GUI)
p.setAdditionalSearchPath(pybullet_data.getDataPath())
p.setGravity(0, 0, -9.81) # Añadimos gravedad para que no flote

# Cargar suelo
p.loadURDF("plane.urdf")

# Cargar cubo pequeño (cube_small tiene un lado de aprox 5cm)
# Lo subimos 0.03 metros para que no colisione con el suelo al nacer
cubo_id = p.loadURDF("cube_small.urdf", [0, 0, 1.03])

# Ajustar cámara: mirando al origen desde (1, 1, 1)
p.resetDebugVisualizerCamera(
    cameraDistance=1.173, 
    cameraYaw=180, 
    cameraPitch=-45, 
    cameraTargetPosition=[0, 0, 0]
)

# Bucle de simulación
while True:
    p.stepSimulation()
    time.sleep(1./240.)
