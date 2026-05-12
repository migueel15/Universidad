import math
import numpy as np
from pymunk.vec2d import Vec2d


#####################################################################

def aplicar_frenado_aire_rotacional(body, R=None, rho=1.225, Cm=0.02):  #golf 0.07
	"""
	Aplica el torque de resistencia aerodinámica a la rotación (Spin Decay).
	Tau = -0.5 * rho * w^2 * R^5 * Cm
	"""
	
	if R==None:
		R=list(body.shapes)[0].radius 
		
	w = body.angular_velocity  # En radianes por segundo
	
	if abs(w) > 0.01:  # Evitamos cálculos si casi no gira
		# Calculamos la magnitud del torque (cuadrática con omega)
		# Nota: usamos abs(w) para que la magnitud sea siempre positiva
		# y luego aplicamos el signo opuesto al final.
		torque_magnitud = 0.5 * rho * (w**2) * (R**5) * Cm
		
		# El torque debe oponerse al giro actual
		signo_opuesto = -1 if w > 0 else 1
		
		# Aplicamos el torque directamente al cuerpo de Pymunk
		body.torque += signo_opuesto * torque_magnitud


############################################################
def aplicar_rodadura(body,FNorm=None,R=None,Crr=0.02, dt=1/60.0):
	"""
	Aplica un torque disipativo que simula la resistencia a la rodadura.
	No genera movimiento inverso al detenerse.
	
	En R hay que pasar el radio pixeles, para comodidad el usuario
	si R=None, se calcula suponiendo que el body tiene solo un shape
	que es un disco.
	Igual con FNorm, si se pasa None, se calcula como masa*gravedad
	en caso contrario se toma el valor que se pase
	"""

	if R==None:
		R=list(body.shapes)[0].radius 
	if FNorm==None:
		FNorm=(body.mass*body.space.gravity).length	
		
		
	# Para no tener que andar con ángulos de inclinación y otros datos
	# pedimos la FNorm (en liso y sin otras fuerzas será masa*gavedad
	torque_rodadura_max = Crr * FNorm * R
	w = body.angular_velocity
	if abs(w) > 0.01:
		# Direccion opuesta al giro
		dir_freno = -1 if w > 0 else 1
		# Torque necesario para detener la rueda en exactamente un paso (dt)
		# Basado en T = I * alpha -> T = I * (w / dt)
		torque_detencion_total = (body.moment * abs(w)) / dt
		# Elegimos el menor: el fisico o el que la clava a cero
		torque_final = min(torque_rodadura_max, torque_detencion_total)
		# Sumamos al torque ya existente (motor, etc.)
		body.torque += dir_freno * torque_final
	else:
		# Umbral de parada total para evitar micro-vibraciones (jitter)
		body.velocity=(0,0)
		body.angular_velocity = 0
##############################################################

###########################################################
# Calcula el numero de Reynolds en función de la velocidad,
# una longitud característica, la viscosidad y la densidad
# del fluido. Por defecto mu y rho son las del aire en con
# diciones normales.
#-----------------------------------------------------
def get_reynolds(v, D, mu=1.85e-5,rho=1.225):
	"""
	Calcula el número de Reynolds (Re).
	
	Parámetros:
	v -> modulo de velocidad (float): m/s
	D -> longitud_caracteristica (float): m (ej. diámetro de tubería)
	     Si es una esfera es el diametro
	mu -> viscosidad dinamica (float): Pa·s o kg/(m·s)
	rho -> densidad (float): kg/m^3
	"""
	try:
		re = (rho* v * D) / mu
		return re
	except ZeroDivisionError:
		return float('inf')

#######################################################################
#Calcula el coeficiente de arraste para UNA ESFERA para valor del número de reynolds
#Si crisis es False, no tiene en cuenta la crisis de arrastre y supone
#que Cd es constante después de la zona de Newton
#---------------------------------------------------------------------
def get_Cd(v,D,mu=1.85e-5,rho=1.225,crisis=False,golf=False):
	"""
	Calcula el coeficiente de arrastre (Cd) para una esfera en función de Re.
	Re lo calcula con la función de arriba a partir de:
	v -> modulo de velocidad (float): m/s
	D -> longitud_caracteristica (float): m (ej. diámetro de tubería)
	     Si es una esfera es el diametro
	mu -> viscosidad dinamica (float): Pa·s o kg/(m·s)
	rho -> densidad (float): kg/m^3
		
	El cálculo es, en principio para una esfera lisa, si crisis=False no
	se tiene en cuenta la cirsis de arrastre (aprox. Re>2e5 porque en muchos
	casos es difícil de conseguir)
	Si golf=True, tenemos en cuenta los dimples (hoyuelos) y la crisis de
	arrastre aparece antes re>40000
	"""
	
	Re=get_reynolds(v,D,mu=mu,rho=rho)
	
	if Re == float('inf'):
		return 0.0
	
	#----- especial para la pelota de golf:
	if golf:
		if Re>40000:
			return 0.22 #Cd reducido por la crisis de arrastre
		else:
			return 0.5 #Antes de la crisis Cd es mayor que para la esfera lisa	
	
	############## esto ya es lo que había antes para esferas lisas
	
	# 1. Schiller–Naumann para Re < 1000
	if Re < 1000:
		# Cd = (24/Re) * (1 + 0.15 * Re^0.687)
		return (24.0 / Re) * (1.0 + 0.15 * (Re**0.687))
	
	# 2. Valor constante 0.44 hasta 2x10^5
	# (Schiller-Naumann en Re=1000 es ~0.441, el empalme es casi continuo)
	elif Re <= 2e5 or crisis==False:
		return 0.44
	
	# 3. Crisis de arrastre: Interpolación log-log de 2e5 a 3e5 (de 0.44 a 0.1)
	elif Re <= 3e5:
		re1, cd1 = 2e5, 0.44
		re2, cd2 = 3e5, 0.1
		
		log_re = math.log10(Re)
		log_re1, log_cd1 = math.log10(re1), math.log10(cd1)
		log_re2, log_cd2 = math.log10(re2), math.log10(cd2)
		
		log_cd = log_cd1 + (log_cd2 - log_cd1) * (log_re - log_re1) / (log_re2 - log_re1)
		return 10**log_cd
	
	# 4. Recuperación: Interpolación log-log de 3e5 a 2e6 (de 0.1 a 0.2)
	elif Re <= 2e6:
		re1, cd1 = 3e5, 0.1
		re2, cd2 = 2e6, 0.2
		
		log_re = math.log10(Re)
		log_re1, log_cd1 = math.log10(re1), math.log10(cd1)
		log_re2, log_cd2 = math.log10(re2), math.log10(cd2)
		
		log_cd = log_cd1 + (log_cd2 - log_cd1) * (log_re - log_re1) / (log_re2 - log_re1)
		return 10**log_cd
	
	# 5. Para Re > 2e6, valor constante
	else:
		return 0.2
#######################################################################		
#######################################################################


#################################################################################
#### Dada la velocidad calcula la corrección al coeficiente de arrastre debido
#### a la velocidad supersónica o proxima a supersónica. Por defecto toma
#### como velocidad del sonido  340 m/s
#### El resultado de esta función hay que multiplicarlo al valor Cd
def mach_correction(velocity,v_sound=340):
	"""
	Aplica el factor de corrección por compresibilidad (Mach) al Cd de Reynolds.
	"""
	#v_sound = 340.0 # m/s aprox.
	mach = velocity / v_sound
	
	if mach < 0.8:
		factor = 1.0
	elif mach < 1.2:
		# Transición: subida brusca
		factor = 1.0 + 1.25 * (mach - 0.8)
	else:
		# Supersónico: factor basado en Miller-Bailey simplificado
		factor = 1.5 + (0.5 / mach)
		
	return factor		
###############################################################################

	
##############################################################################
def get_rho(h): #obtiene la densidad del aire en función de la altitud
	rho0, T0 = 1.225, 288.15
	L, R, g = 0.0065, 287.05, 9.80665
	# Limitamos a la troposfera (11km)
	h_corr = max(0.0, min(h, 11000.0))
	temp_local = T0 - L * h_corr
	exponente = (g / (R * L)) - 1
	return rho0 * (temp_local / T0)**exponente
##############################################################################	
	

##############################################################################
def vel_sonido_temp(temp_k): #velocidad del sonido en función de la temperatura
	# Constantes para el aire
	gamma = 1.4
	R = 287.05
	# Velocidad en m/s
	c = math.sqrt(gamma * R * temp_k)
	return c
####################################################################
def vel_sonido_altitud(alt_m,T0=288.15): #Velocidad del sonido en función de la altitud.
	# El parámetro opcional es la temperatura a nivel del mar, por defecto 15 grados C
	L =  0.0065
	# Obtenemos T local (ISA)
	y = max(0, min(alt_m, 11000))
	t_local = T0 - L * y			
	# Usamos la funcion previa
	return vel_sonido_temp(t_local)
######################################################################


################################################################################################
################################################################################################
### Utiliza todo lo de arriba para calcular y APLICAR el drag (fuerza opuesta a la velocidad del cuerpo, o
### a la velociad relativa del cuerpo con respecto al aire si hay viento
################################################################################################
def aplicar_newton(body,AREA_M2, M_PX=1, Cd=0.47,alt_m=0,v_viento=[0,0],CORRECT_RHO=False,MACH=False,offset=(0,0)):  #M_PX factor de escala m/pixel
	# si se pone alt_m (altitud en m) =0 no tiene en cuenta el efecto de la altitud en la densidad ni velocidad del sonido
	# v_viento se pasa en m/s
	# offset es un desplazamiento del punto de aplicación de la fuerza de fricción
	# 1. Calculamos la velocidad RELATIVA (Vectorial) v_ms es la velocidad del proyectil en m/s
	# si v_viento==0 no afecta en nada
	v_viento=Vec2d(*v_viento) #no se aplica el factor porque viene en m/s
	v_ms = body.velocity * M_PX
	# v_rel es la diferencia con el viento y v_rel_mag su modulo
	v_rel = v_ms - v_viento 
	v_rel_mag = v_rel.length
	# --- FILTRO DE ESTABILIDAD ---
	# Si la diferencia es menor a 0.1 m/s, consideramos que 
	# el objeto ya "flota" con el viento y no aplicamos mas arrastre.
	if v_rel_mag < 0.1: return 	
	# 2. Densidad y Cd (con correccion Mach si esta activa)
	rho = get_rho(alt_m) if CORRECT_RHO else 1.225	
	if MACH:
		# IMPORTANTE: El Mach se calcula con la velocidad RELATIVA
		Cd=Cd*mach_correction(v_rel_mag)
	
	# 3. Aplicamos la fuerza opuesta a la velocidad relativa
		#ya sale convertida en unidades de pymunk
	f_drag =- 0.5 * rho  * Cd * AREA_M2 * v_rel_mag * v_rel/M_PX
	# Convertimos la fuerza de vuelta a la escala del motor (Pymunk)

	#El desplazamiento del punto de aplicación hay que hacerlo teniendo en cuenta
	# que el objeto puede estar girado  
	offset_rot=(offset[0]*math.cos(body.angle),offset[1]*math.sin(body.angle))
	
	# Hay que aplicar la fuerza en un punto externo para evitar la influencia del giro
	# del cuerpo
	body.apply_force_at_world_point(f_drag, body.position+offset_rot)
	
	#print(f"{f_drag.x/M_PX:5.3f} {f_drag.y/M_PX:5.3f} {v_rel_mag:5.3f} {v_rel.x:5.3f} {v_rel.y:5.3f}")
	
	return f_drag
################################################################################################



####################### calculo de la fuerza Magnus ####################################


def aplicar_magnus(body,AREA_M2, M_PX=1, k=0.7, v_viento=[0,0],offset=(0,0)):  #M_PX factor de escala m/pixel
	# k es la cte que lleva el coeficiente C_M, pongo por defecto la del fúbol
	# ponemos la densidad del aire para h=0
	# v_viento se pasa en m/s
	# offset es un desplazamiento del punto de aplicación de la fuerza de magnus
	# 1. Calculamos la velocidad RELATIVA (Vectorial) v_ms es la velocidad del proyectil en m/s
	# si v_viento==0 no afecta en nada
	v_viento=Vec2d(*v_viento) #no se aplica el factor porque viene en m/s
	v_ms = body.velocity * M_PX
	# v_rel es la diferencia con el viento y v_rel_mag su modulo
	v_rel = v_ms - v_viento 
	v_rel_mag = v_rel.length
	# --- FILTRO DE ESTABILIDAD ---
	# Si la diferencia es menor a 0.1 m/s, consideramos que 
	# el objeto ya "flota" con el viento y no aplicamos mas arrastre.
	if v_rel_mag < 0.1: return 	
	# 2. Densidad al nivel del mar
	rho = get_rho(0)
	
	
	# 3. Calculamos magnus:
	# 3.1 dirección y sentido. Vector unitario normal a la trayectoria
	u_m = Vec2d(-v_rel[1], v_rel[0]).normalized()
	# 3.2 Calculamos la expresión de la fuerza
	  # La k se ha pasado como parámetro (tipo de deporte)
	R=list(body.shapes)[0].radius*M_PX #suponemos una esfera, con un solo shape
	##### FALTA S arriba ######
	S=R*body.angular_velocity/v_rel_mag
	Cm=k*S/(2+abs(S))  #el signo menos requerido por el producto vectorial, lo incorpora la S del numerador
	  #Esto da la fuerza ya convertida a unidades de Pymunk
	f_drag = 0.5 * rho * Cm * AREA_M2 * v_rel_mag**2*u_m/M_PX
	
	#3.3 El desplazamiento del punto de aplicación hay que hacerlo teniendo en cuenta
	     # que el objeto puede estar girado  
	offset_rot=(offset[0]*math.cos(body.angle),offset[1]*math.sin(body.angle))
	
	# Hay que aplicar la fuerza en un punto externo para evitar la influencia del giro
	# del cuerpo
	body.apply_force_at_world_point(f_drag, body.position+offset_rot)
	
	#print(f"{f_drag.x/M_PX:5.3f} {f_drag.y/M_PX:5.3f} {v_rel_mag:5.3f} {v_rel.x:5.3f} {v_rel.y:5.3f}")
	
	return f_drag
################################################################################################


		
if __name__ == "__main__": 
	print(get_Cd(55,0.03,golf=True))
