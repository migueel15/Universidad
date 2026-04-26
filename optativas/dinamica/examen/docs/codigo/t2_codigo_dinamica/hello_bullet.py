import pybullet as p
import pybullet_data
import time

# 1. Configuración inicial
p.connect(p.GUI)
p.setAdditionalSearchPath(pybullet_data.getDataPath())
#p.setGravity(0, 0, -9.81)

# 2. Cargar suelo y configurar su rebote
suelo_id = p.loadURDF("plane.urdf")
p.changeDynamics(suelo_id, -1, restitution=0.7) # -1 se refiere al cuerpo base

# 3. Cargar cubo y configurar su rebote
# Lo ponemos a 1 metro de altura para que tenga energía al caer
cubo_id = p.loadURDF("cube_small.urdf", [0, 0, 1.0])
p.changeDynamics(cubo_id, -1, restitution=0.7)

# 4. Ajustar cámara
p.resetDebugVisualizerCamera(
    cameraDistance=1.5, 
    cameraYaw=45, 
    cameraPitch=-30, 
    cameraTargetPosition=[0, 0, 0]
)

# 5. Bucle de simulación
print("Simulación iniciada. Presiona Ctrl+C en la terminal para salir.")
try:
    while True:
        p.stepSimulation()
        time.sleep(1./240.)
except KeyboardInterrupt:
    p.disconnect()
