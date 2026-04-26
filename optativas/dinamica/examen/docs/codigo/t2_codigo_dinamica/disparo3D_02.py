"""
SIMULACIÓN FÍSICA EN 3D CON PYBULLET
- Disparo: ESPACIO | Reinicio: ENTER | Salir: ESC
- Control: Flechas para Ángulo y Potencia
- Objetivo con caída libre tras cuenta atrás
"""
import pybullet as p
import pybullet_data
import time
import math

def simular():
	# Configuración inicial
	physicsClient = p.connect(p.GUI)
	p.setAdditionalSearchPath(pybullet_data.getDataPath())
	p.setGravity(0, 0, -9.81)
	
	def inicializar_escena():
		p.resetSimulation()
		p.setGravity(0, 0, -9.8)
		# Suelo
		p.loadURDF("plane.urdf")
		
		# Proyectil (Esfera azul)
		visual_p = p.createVisualShape(shapeType=p.GEOM_SPHERE, radius=0.5, rgbaColor=[0, 0, 1, 1])
		collision_p = p.createCollisionShape(shapeType=p.GEOM_SPHERE, radius=0.5)
		b_proyectil = p.createMultiBody(baseMass=1, baseCollisionShapeIndex=collision_p, 
										baseVisualShapeIndex=visual_p, basePosition=[0, 0, 0.5])
		
		# Objetivo (Esfera roja)
		visual_o = p.createVisualShape(shapeType=p.GEOM_SPHERE, radius=0.5, rgbaColor=[1, 0, 0, 1])
		collision_o = p.createCollisionShape(shapeType=p.GEOM_SPHERE, radius=0.5)
		# Inicialmente estático (masa 0) para que no caiga
		b_objetivo = p.createMultiBody(baseMass=0, baseCollisionShapeIndex=collision_o, 
									   baseVisualShapeIndex=visual_o, basePosition=[10, 0, 8])
		
		return b_proyectil, b_objetivo

	# Variables de control
	angulo = 45.0
	potencia = 15.0
	proyectil_id, objetivo_id = inicializar_escena()
	
	disparado = False
	objetivo_cae = False
	cuenta_atras = 5.0
	finalizado = False
	
	# Ajustar cámara
	p.resetDebugVisualizerCamera(
		cameraDistance=20, 
		cameraYaw=0, 
		cameraPitch=-20, 
		cameraTargetPosition=[5, 0, 4])

	p.B3G_ESCAPE=27  #tecla escape
	last_time = time.time()
	while p.isConnected():
		now = time.time()
		dt = now - last_time
		last_time = now
		
		keys = p.getKeyboardEvents()
		
		# Lógica de salida y reinicio
		if p.B3G_ESCAPE in keys and keys[p.B3G_ESCAPE] & p.KEY_WAS_TRIGGERED:
			break
		if finalizado and p.B3G_RETURN in keys and keys[p.B3G_RETURN] & p.KEY_WAS_TRIGGERED:
			proyectil_id, objetivo_id = inicializar_escena()
			disparado, objetivo_cae, finalizado = False, False, False
			cuenta_atras = 5.0

		# Controles de disparo
		if not disparado:
			if p.B3G_LEFT_ARROW in keys: angulo += 1
			if p.B3G_RIGHT_ARROW in keys: angulo -= 1
			if p.B3G_UP_ARROW in keys: potencia += 0.2
			if p.B3G_DOWN_ARROW in keys: potencia -= 0.2
			
			# Debug lines para la mira
			p.removeAllUserDebugItems()
			rad = math.radians(angulo)
			p.addUserDebugLine([0,0,0.5], [math.cos(rad)*20, 0, math.sin(rad)*20 + 0.5], [0,1,0], 3)
			
			if p.B3G_SPACE in keys and keys[p.B3G_SPACE] & p.KEY_WAS_TRIGGERED:
				rad = math.radians(angulo)
				p.resetBaseVelocity(proyectil_id, [potencia * math.cos(rad), 0, potencia * math.sin(rad)])
				disparado = True

		# Timer
		if not objetivo_cae:
			cuenta_atras -= dt
			if cuenta_atras <= 0:
				# Hacer que el objetivo tenga masa para que la gravedad actúe
				p.changeDynamics(objetivo_id, -1, mass=1)
				objetivo_cae = True
				if not disparado:
					rad = math.radians(angulo)
					p.resetBaseVelocity(proyectil_id, [potencia * math.cos(rad), 0, potencia * math.sin(rad)])
					disparado = True

		p.stepSimulation()
		
		# Detección de contacto o suelo
		contactos = p.getContactPoints(proyectil_id, objetivo_id)
		pos_o, _ = p.getBasePositionAndOrientation(objetivo_id)
		if len(contactos) > 0 or pos_o[2] < 0.6:
			finalizado = True
			
		time.sleep(1./240.)

	p.disconnect()

if __name__ == "__main__":
	simular()
