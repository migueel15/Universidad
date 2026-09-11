# Análisis de la evolución de los sistemas distribuidos

## Visión general

Un sistema distribuido es una aplicación cuyos componentes se ejecutan en varios computadores independientes, con memorias y relojes separados, y que solamente pueden coordinarse intercambiando mensajes por una red.

La idea central del documento puede resumirse así:

> Los sistemas distribuidos permiten ganar rendimiento, disponibilidad, alcance geográfico y capacidad de crecimiento, pero trasladan una enorme complejidad al software.

Esa complejidad procede principalmente de cuatro hechos:

1. No existe memoria compartida entre las máquinas.
2. No existe un reloj global perfectamente común.
3. La comunicación tiene latencia, capacidad limitada y posibilidad de pérdida.
4. Algunos componentes pueden fallar mientras los demás continúan funcionando.

---

## 1. Características de los sistemas distribuidos

### 1.1. Definición fundamental

Cada computador posee:

- Su propia CPU.
- Su propia memoria.
- Su propio reloj.
- Su propio estado local.

Un computador no puede leer directamente la memoria de otro. Para influir en él debe enviarle un mensaje a través de la red.

Al mismo tiempo, varios programas cooperan para ofrecer al usuario un único servicio. Por ejemplo, cuando se abre una tienda en línea, pueden intervenir:

- Un servidor web.
- Un servicio de autenticación.
- Un catálogo.
- Un sistema de inventario.
- Una plataforma de pagos.
- Una base de datos.
- Una red de distribución de contenido.

El usuario, sin embargo, percibe una sola aplicación.

Esta es una característica importante: la unidad que ve el usuario no coincide con la fragmentación técnica interna.

### 1.2. Tres familias de sistemas distribuidos

#### Computación distribuida

Su objetivo principal es aumentar el rendimiento combinando recursos.

Ejemplos del documento:

- Clústeres.
- Grids o mallas computacionales.

Caso real: una simulación meteorológica divide el territorio en regiones. Diferentes nodos calculan simultáneamente la evolución de cada región y después intercambian los valores de sus fronteras.

#### Sistemas distribuidos de información

Su objetivo es integrar aplicaciones y datos que ya existen.

Sus mecanismos típicos son:

- Transacciones.
- Llamadas remotas.
- Servicios que conectan sistemas distintos.

Caso real: una compra en línea puede tener que coordinar el catálogo, el almacén, el pago y la empresa de transporte. El problema principal no es hacer un único cálculo rápidamente, sino conseguir que sistemas diferentes cooperen correctamente.

#### Sistemas pervasivos

Su objetivo es que la computación esté presente en dispositivos móviles, sensores y objetos cotidianos.

Los componentes pueden:

- Aparecer.
- Desaparecer.
- Cambiar de red.
- Moverse.
- Permanecer desconectados durante un tiempo.

IoT pertenece a esta familia. En una red de sensores urbanos, la desconexión ocasional no es necesariamente una avería excepcional, sino parte de la operación normal.

### 1.3. Clúster frente a grid

#### Clúster

Un clúster suele estar formado por máquinas similares:

- En el mismo lugar.
- Conectadas mediante una red local rápida.
- Administradas por una sola organización.
- Sometidas a una política de seguridad común.
- Con hardware y software estrechamente coordinados.

Puede utilizarse para simulaciones, análisis de grandes datos o entrenamiento de modelos.

#### Grid

Un grid reúne recursos heterogéneos pertenecientes a organizaciones distintas. Cada organización mantiene sus propias reglas, administradores y políticas.

La diferencia decisiva no es únicamente técnica, sino administrativa:

- Un clúster tiene normalmente un propietario.
- Un grid debe coordinar varios dominios de confianza.

Por ello, un grid necesita tanto protocolos técnicos como acuerdos sobre identidad, acceso, prioridades y uso de recursos.

Caso real: diferentes universidades comparten capacidad de cálculo para analizar datos científicos que ninguna podría procesar por sí sola.

### 1.4. Utility, volunteer y cloud computing

#### Utility computing

Presenta la computación como un servicio medido: el cliente paga por lo que utiliza, de forma parecida a la electricidad.

El documento señala que la idea comercial existe desde la década de 1960, aunque la infraestructura necesaria para aplicarla masivamente apareció mucho después.

#### Volunteer computing

Los usuarios ceden recursos libres de sus computadores a un proyecto.

El inconveniente es que esos recursos no son confiables:

- Un participante puede desconectarse.
- Puede apagar el computador.
- Puede no devolver un resultado.
- Incluso podría devolver un resultado incorrecto.

El sistema debe repartir trabajos pequeños, repetir algunos cálculos y comprobar resultados.

Caso real: un proyecto científico distribuye unidades de análisis entre miles de ordenadores domésticos y asigna la misma unidad a varios participantes para detectar discrepancias.

#### Cloud computing

Combina la idea de cobrar por uso con recursos virtualizados y una gran capacidad de crecimiento.

El usuario solicita recursos sin necesitar conocer:

- La máquina física exacta.
- Las migraciones internas.
- La asignación de cargas.
- La sustitución de hardware averiado.

El proveedor se ocupa de esos aspectos.

### 1.5. Computación autonómica, móvil y ubicua

#### Computación autonómica

Es un sistema que observa su propio estado y realiza tareas de gestión automáticamente. El documento enumera cinco áreas:

- Configuración.
- Integración.
- Optimización.
- Reparación.
- Protección.

Ejemplo: si un servidor está sobrecargado, el sistema crea otra instancia, dirige parte del tráfico hacia ella y la elimina cuando deja de ser necesaria.

#### Computación móvil

Utiliza dispositivos portátiles y conexiones inalámbricas. Sus restricciones incluyen:

- Batería limitada.
- CPU y memoria limitadas.
- Cambios de cobertura.
- Variación del ancho de banda.
- Movimiento entre redes.

#### Computación ubicua

Integra computadores en objetos ordinarios. El documento la presenta como una tercera etapa histórica tras los computadores centrales y los ordenadores personales.

La consecuencia común de estos modelos es que el software debe tolerar que los componentes aparezcan, desaparezcan o cambien de posición.

### 1.6. Aplicaciones y programación distribuidas

Una aplicación distribuida está formada por varias partes que se ejecutan simultáneamente en máquinas diferentes y cooperan en una tarea común.

El trabajo puede colocarse:

- Cerca del usuario, para reducir el tiempo de respuesta.
- Cerca de los datos, para evitar transferencias grandes.
- En máquinas potentes, para acelerar el procesamiento.

El programador debe resolver problemas que no aparecen de la misma forma en un programa local:

- Nombrar y localizar componentes.
- Representar e intercambiar mensajes.
- Coordinar actividades concurrentes.
- Detectar y gestionar fallos.

### 1.7. Ventajas y desventajas

| Aspecto                | Ventaja                                                       | Coste o dificultad                                   |
| ---------------------- | ------------------------------------------------------------- | ---------------------------------------------------- |
| Coste                  | Máquinas ordinarias pueden sustituir a una máquina enorme     | Hay más equipos que administrar y alimentar          |
| Rendimiento            | Existe paralelismo físico real                                | La comunicación puede anular la ganancia             |
| Crecimiento            | Se pueden añadir nodos                                        | El software debe estar diseñado para aprovecharlos   |
| Disponibilidad         | El servicio puede sobrevivir a la pérdida de un nodo          | Solo ocurre si se preparó explícitamente para ello   |
| Compartición           | Datos y dispositivos son accesibles desde distintos lugares   | Seguridad y privacidad se complican                  |
| Adaptación al problema | Muchos problemas reales ya están geográficamente distribuidos | Programación, pruebas y depuración son más difíciles |

Añadir computadores no acelera automáticamente una aplicación. Si todos esperan continuamente datos de los demás, el sistema puede dedicar más tiempo a comunicarse que a trabajar.

### 1.8. Transparencia

La transparencia consiste en ocultar ciertos detalles de la distribución.

| Tipo         | Qué oculta                                  | Ejemplo                                                         |
| ------------ | ------------------------------------------- | --------------------------------------------------------------- |
| Acceso       | Cómo se representa o alcanza un recurso     | Leer un archivo remoto mediante una interfaz similar a la local |
| Localización | Dónde está el recurso                       | Un nombre estable se traduce a una dirección variable           |
| Migración    | Que el recurso ha cambiado de lugar         | Un buzón se mueve a otro servidor sin cambiar su dirección      |
| Reubicación  | Que se mueve mientras está siendo utilizado | Una llamada continúa al cambiar de estación base                |
| Replicación  | Que existen varias copias                   | Se entrega el vídeo desde la réplica más cercana                |
| Concurrencia | Que otros usuarios comparten el recurso     | Dos transacciones quedan aisladas entre sí                      |
| Fallo        | Que un componente falló y se recuperó       | Se retransmite un segmento perdido sin interrumpir la descarga  |

La transparencia nunca es perfecta. Si un servidor está a 80 ms de distancia, una API puede ocultar su ubicación, pero no eliminar esos 80 ms.

**Ampliación conceptual:** ocultar demasiado también puede perjudicar. Si una llamada remota parece idéntica a una función local, el programador podría olvidar que la operación puede tardar, fallar o ejecutarse más de una vez.

### 1.9. Escalabilidad y apertura

El documento distingue tres dimensiones independientes de escalabilidad:

#### Escalabilidad de tamaño

Aumentan los usuarios o los datos. El límite habitual es algún componente central por el que deben pasar todas las solicitudes.

#### Escalabilidad geográfica

Los nodos se distribuyen entre países o continentes. La latencia deja de ser despreciable y cada viaje adicional de ida y vuelta se vuelve visible.

#### Escalabilidad administrativa

Participan varias organizaciones con políticas distintas. Se necesitan acuerdos sobre identidad, seguridad, responsabilidad y acceso.

Un sistema puede escalar bien en una dimensión y mal en otra. Por ejemplo, puede soportar millones de usuarios dentro de un único centro de datos, pero funcionar mal cuando se despliega entre continentes.

La apertura significa publicar interfaces con precisión suficiente para que otro equipo pueda construir componentes compatibles. No debe confundirse con que el código sea abierto.

### 1.10. Concurrencia y recursos compartidos

El documento presenta una actualización perdida:

1. Dos clientes leen el valor 100.
2. El cliente A suma 50 y escribe 150.
3. El cliente B suma 30 usando todavía su lectura antigua y escribe 130.
4. El resultado correcto debería ser 180, pero queda almacenado 130.

Un mutex local no resuelve el problema si los clientes están en máquinas diferentes: no comparten ni memoria ni bloqueo.

Para resolverlo se necesitan mecanismos distribuidos de ordenación, transacciones, control de concurrencia o acuerdo.

**Ampliación conceptual:** una solución de base de datos podría representar la operación como `saldo = saldo + cantidad`, ejecutarla dentro de una transacción o utilizar control de versiones para rechazar una escritura basada en un valor antiguo.

### 1.11. Sistema paralelo frente a sistema distribuido

| Propiedad               | Computador paralelo              | Sistema distribuido                              |
| ----------------------- | -------------------------------- | ------------------------------------------------ |
| Memoria                 | Compartida entre procesadores    | Privada para cada computador                     |
| Reloj                   | Referencia común                 | Relojes independientes                           |
| Comunicación            | Lecturas y escrituras en memoria | Envío y recepción de mensajes                    |
| Retardo                 | Habitualmente nanosegundos       | Microsegundos o milisegundos                     |
| Fallo                   | Puede detenerse el conjunto      | Una parte puede fallar mientras las demás siguen |
| Herramientas habituales | Hilos, OpenMP                    | Sockets, MPI, HTTP                               |

La característica distintiva es el fallo parcial. Desde fuera, una máquina lenta, una conexión interrumpida y una máquina detenida pueden producir el mismo síntoma: silencio.

Por ello, cualquier detector práctico termina dependiendo de un tiempo límite. Un timeout no demuestra que el otro nodo esté muerto; únicamente indica que no respondió a tiempo.

### 1.12. Las ocho falacias

El documento enumera ocho suposiciones falsas:

1. La red es fiable.
2. La latencia es cero.
3. El ancho de banda es infinito.
4. La red es segura.
5. La topología no cambia.
6. Existe un solo administrador.
7. El transporte no cuesta nada.
8. La red es homogénea.

Estas suposiciones suelen parecer razonables dentro de una máquina, pero son falsas al cruzar una red.

El PDF ofrece órdenes de magnitud de ida y vuelta:

- Dentro de un centro de datos: aproximadamente 0,5 ms.
- Dentro del mismo país: aproximadamente 12 ms.
- Europa–Estados Unidos: aproximadamente 80 ms.
- Europa–Asia: aproximadamente 250 ms.

Son cifras ilustrativas, no garantías universales. Su mensaje es que la distancia física forma parte del diseño.

### 1.13. Problemas esenciales

El bloque concluye con tres grandes problemas:

- **Estado global:** ningún computador ve instantáneamente todo el sistema.
- **Gestión de fallos:** hay que detectar, ocultar y recuperar fallos sin certeza perfecta.
- **Acuerdo:** varios computadores deben aceptar el mismo valor, orden o conjunto de miembros.

Los tres proceden de la ausencia de memoria compartida, reloj común y conocimiento completo.

---

## 2. La perspectiva del cliente

### 2.1. Petición y respuesta

El servidor comienza a ejecutarse y espera. El cliente inicia la interacción enviando una petición.

La comunicación puede ser:

- **Síncrona:** el cliente espera la respuesta.
- **Asíncrona:** continúa trabajando y recoge la respuesta posteriormente.

Existen tres puntos básicos de fallo:

1. Se pierde la petición.
2. El servidor falla mientras trabaja.
3. Se pierde la respuesta.

En los tres casos, el cliente observa silencio.

Reintentar no es neutral. Si el servidor ejecutó la operación pero se perdió la respuesta, una repetición puede ejecutar la operación dos veces.

Caso real: repetir automáticamente una orden de pago podría producir dos cargos.

**Ampliación conceptual:** una técnica habitual es asignar a cada operación un identificador único. El servidor registra los identificadores procesados y devuelve el resultado anterior cuando recibe un duplicado. Esto proporciona deduplicación, no una certeza mágica de “ejecución exactamente una vez”.

### 2.2. Las tres capas lógicas

#### Interfaz de usuario

Incluye pantallas, formularios, menús y representación visual.

#### Procesamiento

Contiene reglas de negocio, cálculos y búsquedas. El documento la presenta como una capa que no guarda datos permanentes.

#### Datos

Incluye archivos, bases de datos, índices y mecanismos para conservar la información de manera consistente.

En un buscador:

- La caja de búsqueda pertenece a la interfaz.
- La generación de consultas y el ranking pertenecen al procesamiento.
- El índice de páginas pertenece a los datos.

La frontera cliente-servidor puede colocarse en diferentes puntos de esta cadena.

### 2.3. Cliente ligero y cliente pesado

El documento presenta cinco posibles repartos, desde un terminal que solo muestra la interfaz hasta un cliente que conserva incluso datos locales.

#### Cliente ligero

Ejecuta poco trabajo local. Es sencillo de actualizar y administrar, pero casi toda acción necesita un viaje por la red.

Ejemplo: un terminal remoto que únicamente muestra una pantalla generada en el servidor.

#### Cliente pesado

Ejecuta parte importante del procesamiento y puede guardar datos localmente. Tolera mejor redes lentas o ausentes, pero resulta más difícil mantener todas sus instalaciones actualizadas.

Ejemplo: una aplicación móvil con base de datos local que sincroniza sus cambios cuando recupera conectividad.

Un gateway IoT también se acerca al extremo “pesado”, porque filtra, transforma y almacena temporalmente datos cerca de los dispositivos.

### 2.4. Software cliente para proporcionar transparencia

El cliente puede ocultar:

- Cuántos servidores existen.
- En qué dirección está cada uno.
- Qué protocolo utiliza.
- Cuándo se selecciona otra réplica.

Un **stub** es una pieza de software que presenta una llamada aparentemente normal y transforma sus parámetros en mensajes de red.

Puede aportar:

- Transparencia de localización, resolviendo un nombre.
- Transparencia de fallos, probando otro servidor.
- Transparencia de replicación, seleccionando una de varias copias.

Desde la aplicación puede parecer una llamada; en la red pueden producirse varias.

### 2.5. El ejemplo de X Window System

En X Window los nombres pueden parecer invertidos:

- El servidor X se ejecuta delante del usuario y controla pantalla, teclado y ratón.
- La aplicación remota es el cliente y solicita al servidor que dibuje.

Esto demuestra que “cliente” y “servidor” describen roles en una interacción, no el tamaño o potencia de las máquinas.

### 2.6. Servidores con estado y sin estado

| Aspecto                      | Sin estado                         | Con estado                                    |
| ---------------------------- | ---------------------------------- | --------------------------------------------- |
| Información entre peticiones | Ninguna                            | Guarda información del cliente                |
| Contenido de cada petición   | Todo lo necesario                  | Puede ser más breve                           |
| Reinicio                     | El cliente apenas lo nota          | Puede perderse la sesión                      |
| Escalado                     | Cualquier servidor puede responder | Hay que volver al mismo o compartir el estado |
| Ejemplo                      | Lectura web independiente          | Servidor con archivos abiertos por cliente    |

Un diseño sin estado suele escalar y recuperarse mejor, porque una petición puede enviarse a cualquier instancia.

“Sin estado” no significa que el sistema no tenga datos. Significa que el proceso que atiende la petición no conserva contexto privado imprescindible entre peticiones.

### 2.7. Estado de sesión

Una cesta de compra necesita persistir entre varias peticiones. Si el servidor de aplicación es stateless, el estado puede situarse en tres lugares:

#### Identificador en una cookie y estado en un almacén compartido

El cliente devuelve un identificador; todos los servidores consultan con él una base común.

#### Estado en el cliente

El cliente conserva y reenvía los datos. El servidor permanece completamente sin estado, pero los mensajes aumentan.

#### Estado en el servidor

Reduce la información enviada, pero complica el reparto entre servidores y la recuperación tras fallos.

La cookie suele ser solo una referencia. No tiene por qué contener toda la cesta.

### 2.8. Latencia y throughput

#### Latencia

Tiempo que transcurre desde una petición hasta su respuesta. Normalmente se expresa en milisegundos.

#### Throughput o rendimiento agregado

Trabajo completado por unidad de tiempo: solicitudes por segundo o megabytes por segundo.

Una red con mayor ancho de banda puede transferir más datos sin reducir necesariamente el tiempo de ida y vuelta. Por eso:

- El ancho de banda importa para mover grandes volúmenes.
- La latencia importa en protocolos con muchas interacciones pequeñas.

Si un diálogo necesita seis viajes Madrid–Tokio, el retardo acumulado puede superar un segundo antes de entregar información útil.

### 2.9. Disponibilidad

El documento usa:

\[
Disponibilidad = \frac{MTBF}{MTBF + MTTR}
\]

- **MTBF:** tiempo medio entre fallos.
- **MTTR:** tiempo medio de reparación.

Ejemplo del PDF:

\[
\frac{200}{200+4}\approx 98\%
\]

La disponibilidad puede mejorarse haciendo que el componente falle con menor frecuencia o reparándolo más deprisa. La recuperación automática suele ser más económica que intentar construir hardware que nunca falle.

Tiempos de indisponibilidad aproximados por año:

- 99 %: 5.256 minutos.
- 99,9 %: 526 minutos.
- 99,99 %: 53 minutos.
- 99,999 %: 5 minutos.

Cada nueve adicional elimina aproximadamente nueve décimas partes del tiempo de caída y suele exigir combatir nuevas clases de fallos.

### 2.10. Modelos de consistencia

| Modelo               | Promesa                                                 | Experiencia observable                           |
| -------------------- | ------------------------------------------------------- | ------------------------------------------------ |
| Fuerte               | Cada lectura ve la última escritura                     | Comportamiento intuitivo, con mayor coordinación |
| Read-your-writes     | Un cliente ve siempre sus propios cambios               | El comentario propio aparece enseguida           |
| Lecturas monotónicas | Tras ver un valor nuevo no se vuelve a uno antiguo      | La interfaz no “retrocede en el tiempo”          |
| Causal               | Un efecto no aparece antes que su causa                 | Una respuesta no precede al mensaje respondido   |
| Eventual             | Las réplicas convergen cuando cesan las actualizaciones | Dos dispositivos pueden discrepar temporalmente  |

Toda garantía requiere cierto grado de coordinación entre copias; esa coordinación consume viajes de red. Utilizar una consistencia más débil puede ser una decisión correcta si la aplicación tolera discrepancias temporales.

Ejemplos:

- Saldo bancario: suele necesitar garantías fuertes en las operaciones críticas.
- Contador aproximado de visualizaciones: puede aceptar consistencia eventual.
- Conversación: requiere al menos preservar causalidad para no mostrar respuestas antes que preguntas.

**Ampliación conceptual:** “consistencia fuerte” se usa aquí de manera introductoria. En literatura técnica existen definiciones más precisas, como linealizabilidad, consistencia secuencial y serialización de transacciones.

### 2.11. Caché del lado del cliente

Una caché conserva respuestas recientes. Un acierto evita utilizar la red; un fallo obliga a consultar el servidor.

El documento aproxima la latencia media así:

\[
L_{media} \approx proporción\ de\ fallos \times RTT
\]

Con un 90 % de aciertos y un RTT de 80 ms:

\[
0,1 \times 80 = 8\ ms
\]

La contrapartida es la obsolescencia: una copia local puede dejar de reflejar el servidor.

Las políticas mencionadas son:

- Caducidad tras un tiempo.
- Validación con el servidor.

Ejemplos:

- Caché del navegador.
- Caché de resolución de nombres.
- Base local de una aplicación móvil.

**Ampliación conceptual:** invalidar cachés es difícil porque hay que equilibrar frescura, disponibilidad y tráfico. Entre las estrategias habituales están TTL, validación condicional, versiones e invalidaciones enviadas por el servidor.

### 2.12. Clientes síncronos y asíncronos

Un cliente síncrono se bloquea durante el viaje completo. El código es lineal y sencillo.

Un cliente asíncrono puede continuar, mostrar progreso o aceptar acciones. La respuesta se recibe mediante:

- Callback.
- Cola.
- Future o promesa.

Su coste es que debe registrar operaciones pendientes y decidir qué hacer si una respuesta no llega.

El bloque concluye con tres decisiones independientes:

- Cliente ligero o pesado.
- Servidor con estado o sin estado.
- Uso o no de caché.

---

## 3. La perspectiva de red

### 3.1. Capas: OSI y TCP/IP

Las capas separan responsabilidades. Cada capa ofrece un servicio a la superior y oculta parte de su implementación.

OSI define siete capas:

1. Física.
2. Enlace de datos.
3. Red.
4. Transporte.
5. Sesión.
6. Presentación.
7. Aplicación.

El modelo TCP/IP del documento utiliza cuatro:

- Enlace.
- Internet.
- Transporte.
- Aplicación.

La capa de aplicación TCP/IP engloba aproximadamente las capas 5–7 de OSI; la capa de enlace engloba las capas 1–2.

### 3.2. Encapsulación

Al enviar datos:

1. La aplicación produce el contenido.
2. Transporte añade su cabecera.
3. IP añade otra.
4. Enlace añade otra más.

En recepción, cada capa elimina su cabecera.

El ejemplo usa aproximadamente:

- Ethernet: 14 bytes.
- IPv4: 20 bytes.
- TCP: 20 bytes.

Son unos 54 bytes antes de contar cabeceras de aplicación u opciones. Enviar muchos mensajes diminutos multiplica el coste fijo, por lo que agruparlos puede mejorar la eficiencia.

### 3.3. Direcciones, puertos y conexiones

La dirección IP identifica una interfaz de red, no estrictamente una máquina ni un programa. Un portátil puede tener una dirección Wi-Fi y otra Ethernet.

El puerto identifica el punto de comunicación del proceso. Tiene 16 bits:

\[
0 \ldots 65535
\]

Una conexión se identifica mediante cinco valores:

- Protocolo.
- Dirección de origen.
- Puerto de origen.
- Dirección de destino.
- Puerto de destino.

Los servicios usan puertos conocidos; los clientes suelen usar puertos efímeros.

El documento indica que los puertos inferiores a 1024 son privilegiados y muestra como rangos efímeros 49152–65535 en el estándar y 32768–60999 en Linux.

**Ampliación conceptual:** las restricciones exactas de los puertos privilegiados y los rangos efímeros dependen del sistema operativo y de su configuración.

### 3.4. Servicios conocidos

| Puerto | Servicio | Transporte | Finalidad                                 |
| -----: | -------- | ---------- | ----------------------------------------- |
|     22 | SSH      | TCP        | Acceso remoto y copia segura              |
|     25 | SMTP     | TCP        | Correo entre servidores                   |
|     53 | DNS      | UDP y TCP  | Traducción de nombres                     |
|     80 | HTTP     | TCP        | Web sin cifrado                           |
|    123 | NTP      | UDP        | Sincronización de relojes                 |
|    443 | HTTPS    | TCP        | Web cifrada con TLS                       |
|   1883 | MQTT     | TCP        | Publicación/suscripción para dispositivos |

Los puertos fijos resuelven un problema inicial: si el cliente desconoce incluso el puerto del servicio, no puede comenzar a negociar.

### 3.5. TCP

TCP establece una conexión mediante:

1. `SYN`.
2. `SYN + ACK`.
3. `ACK`.

Esto introduce aproximadamente un viaje de ida y vuelta antes de los primeros datos.

TCP proporciona:

- Entrega ordenada de bytes.
- Retransmisión de segmentos perdidos.
- Notificación de error si no puede recuperarse.
- Control de flujo para no desbordar al receptor.
- Control de congestión para adaptar la velocidad a la red.

En un intercambio corto, el establecimiento y el cierre pueden representar una parte importante del coste.

#### Cabecera TCP

El documento destaca:

- Puerto origen y destino.
- Número de secuencia.
- Número de confirmación.
- Longitud de cabecera.
- Banderas como SYN, ACK, FIN, RST y PSH.
- Ventana de recepción.
- Checksum.
- Opciones.

La cabecera fija ocupa 20 bytes y puede incluir hasta 40 bytes de opciones.

#### TCP es un flujo de bytes

TCP conserva el orden, pero no las fronteras lógicas de los mensajes.

Tres llamadas a `send` podrían recibirse mediante:

- Dos llamadas a `read`.
- Cinco llamadas.
- Una sola llamada.

La aplicación debe introducir framing:

- Un campo de longitud.
- Un separador.
- Una estructura que permita encontrar el final.

Este es uno de los errores clásicos al programar sockets: asumir que una escritura corresponde a una lectura.

### 3.6. UDP

UDP tiene una cabecera de 8 bytes:

- Puerto origen.
- Puerto destino.
- Longitud.
- Checksum.

No establece conexión ni repara pérdidas. Conserva, en cambio, las fronteras entre datagramas: un envío corresponde a un datagrama.

Resulta apropiado cuando una respuesta tardía es peor que la ausencia de respuesta:

- Voz.
- Vídeo.
- Lecturas periódicas de sensores.
- Sincronización de tiempo.

Elegir UDP no elimina la necesidad de fiabilidad; la desplaza a la aplicación cuando resulte necesaria.

### 3.7. Elegir TCP o UDP

La pregunta guía del documento es:

> ¿Es preferible una respuesta correcta pero tardía a no recibir respuesta?

- Si la respuesta es sí, TCP suele ser adecuado.
- Si es no, UDP puede ser mejor.

Ejemplos del PDF:

- Página web: TCP, porque importan todos los bytes y su orden.
- Archivo: TCP, porque perder un byte lo corrompe.
- Resolución de nombres: UDP para consultas pequeñas que pueden repetirse.
- Sincronización de reloj: UDP, porque un dato temporal retransmitido puede llegar obsoleto.
- Voz y vídeo: UDP, porque una trama tardía ya no es útil.
- Telemetría: UDP o MQTT sobre TCP, dependiendo de la importancia de cada lectura.

**Ampliación conceptual:** no debe confundirse el transporte con la semántica de la aplicación. Un protocolo puede usar UDP y añadir confirmaciones selectivas, o usar TCP y aun así necesitar deduplicación de operaciones.

### 3.8. API de sockets

Servidor:

1. `socket()`
2. `bind()`
3. `listen()`
4. `accept()`
5. `read()`
6. `write()`
7. `close()`

Cliente:

1. `socket()`
2. `connect()`
3. `write()`
4. `read()`
5. `close()`

`accept()` devuelve un nuevo socket dedicado al cliente. El socket original continúa escuchando nuevas conexiones.

Cada operación puede fallar y su resultado debe comprobarse.

### 3.9. MTU y fragmentación

La MTU es el máximo que puede transportar una trama. En Ethernet suele considerarse 1500 bytes.

TCP divide automáticamente el flujo. Con cabeceras IPv4 y TCP de 20 bytes cada una, quedan normalmente 1460 bytes; con la opción timestamp de 12 bytes, 1448.

En UDP, un datagrama excesivamente grande puede fragmentarse en IP. Si desaparece un fragmento, el receptor no puede reconstruir el datagrama completo.

El PDF recomienda mantener los mensajes UDP por debajo de aproximadamente 1400 bytes, especialmente en protocolos de sensores.

### 3.10. Direcciones privadas y NAT

Rangos privados citados:

- `10.0.0.0/8`
- `172.16.0.0/12`
- `192.168.0.0/16`

El router NAT sustituye la dirección y el puerto privados por su dirección pública y un puerto disponible. Mantiene una tabla para devolver cada respuesta al equipo correcto.

Las conexiones iniciadas desde dentro funcionan naturalmente. Recibir una conexión externa no solicitada es más difícil porque el router no sabe a qué equipo interno enviarla.

Esto afecta especialmente a:

- P2P.
- Cámaras domésticas.
- Sensores.
- Dispositivos situados detrás de routers residenciales.

La frase del documento resume el compromiso: NAT conservó direcciones, pero rompió la conectividad directa extremo a extremo.

### 3.11. Herramientas de diagnóstico

El documento propone cuatro utilidades:

- `ping`: tiempo de ida y vuelta y pérdida básica.
- `traceroute` o `tracepath`: routers atravesados.
- `ss`: sockets y estados como `LISTEN`, `ESTABLISHED` o `TIME_WAIT`.
- `netcat`: cliente o servidor mínimo para probar una conexión.

La idea metodológica es separar primero el problema de red del problema de la aplicación.

---

## 4. Arquitecturas

### 4.1. Organización lógica y colocación física

Primero se decide qué componentes lógicos existen; después, en qué máquinas se ejecutan.

El documento distingue dos acoplamientos:

#### Acoplamiento espacial

Un componente debe conocer la identidad o dirección del otro.

#### Acoplamiento temporal

Ambos deben estar activos al mismo tiempo.

Sus combinaciones producen cuatro estilos:

| Espacio     | Tiempo      | Estilo                  |
| ----------- | ----------- | ----------------------- |
| Acoplado    | Acoplado    | Llamada directa         |
| Acoplado    | Desacoplado | Cola de mensajes        |
| Desacoplado | Acoplado    | Publicación/suscripción |
| Desacoplado | Desacoplado | Almacén compartido      |

Ejemplo: en una cola, el productor conoce la cola, pero el consumidor puede procesar el mensaje horas después. En publicación/suscripción, emisor y receptores coinciden temporalmente, pero el emisor no necesita conocer a cada receptor.

### 4.2. Cliente-servidor

Los roles son asimétricos:

- El servidor arranca y espera.
- El cliente inicia la comunicación.
- El servidor escucha en una dirección de transporte conocida.

Problemas estructurales:

- Todo el tráfico atraviesa el servidor.
- Su capacidad limita el sistema.
- Si es único, constituye un punto único de fallo.

La replicación y el balanceo horizontal intentan eliminar estas limitaciones.

### 4.3. Tres diseños de servidor

#### Iterativo

Atiende un cliente hasta terminar antes de aceptar el siguiente.

Ventajas:

- Muy sencillo.
- Predecible.

Problema: un cliente lento bloquea a todos los demás.

#### Un proceso o hilo por cliente

Cada cliente recibe su propio flujo de ejecución.

- Un proceso ofrece aislamiento.
- Un hilo comparte la memoria del proceso.

Es fácil de programar, pero miles de clientes consumen una cantidad importante de memoria y planificación.

#### Dirigido por eventos

Un proceso gestiona muchos sockets y pregunta al sistema operativo cuáles están listos mediante mecanismos como `select`, `poll` o `epoll`.

Consume menos recursos por cliente, pero obliga a estructurar el programa como eventos y estados parciales.

Caso real: un servicio de chat con decenas de miles de conexiones principalmente inactivas encaja mejor en un diseño dirigido por eventos que en un proceso completo por usuario.

### 4.4. Arquitecturas de dos y tres niveles

#### Dos niveles

Las tres capas lógicas se reparten entre dos máquinas. La frontera puede situarse en distintos puntos.

#### Tres niveles

- Interfaz.
- Procesamiento.
- Datos.

Cada nivel puede sustituirse o escalarse por separado.

Cada frontera tiene un coste: una llamada local se convierte en una llamada de red que puede ser lenta, fallar o repetirse.

Los microservicios prolongan este principio: dividen el procesamiento en numerosos servicios pequeños. Multiplican tanto la independencia como los viajes de red y los modos de fallo.

### 4.5. Distribución vertical y horizontal

#### Vertical

Coloca capas diferentes en máquinas diferentes. Su objetivo es organizar y separar responsabilidades.

#### Horizontal

Replica una misma capa en varias máquinas. Cada réplica procesa parte de las peticiones o conserva parte de los datos.

Su objetivo es aumentar:

- Capacidad.
- Disponibilidad.
- Paralelismo.

Un sistema real normalmente combina ambas: tres niveles verticales y decenas de instancias horizontales en el nivel de procesamiento detrás de un balanceador.

### 4.6. Peer-to-peer

En P2P cada nodo puede actuar como cliente y como servidor. No existe por diseño un nodo especial para todas las operaciones.

#### Redes estructuradas y DHT

Una tabla hash distribuida transforma el nombre de un elemento en una posición de un espacio lógico, por ejemplo un anillo.

Cada peer conserva atajos. La búsqueda requiere aproximadamente:

\[
O(\log N)
\]

Con alrededor de un millón de nodos, unas veinte decisiones binarias bastan como orden de magnitud.

Cada nuevo peer aporta almacenamiento y ancho de banda, al contrario que un servidor central donde cada cliente añade carga pero no capacidad.

#### P2P híbrido

Un índice central conserva las direcciones de los peers que tienen un archivo. El archivo se transfiere directamente entre peers y no atraviesa el índice.

Esto mantiene ligero el componente central, aunque continúa siendo relevante para localizar participantes.

El cliente puede descargar diferentes fragmentos de varios peers y compartir al mismo tiempo los fragmentos ya recibidos.

#### Redes no estructuradas

Cada peer conoce algunos vecinos aleatorios y propaga las consultas entre ellos.

Ventajas:

- Simplicidad.
- Robustez ante cambios.

Coste:

- Se desperdicia ancho de banda difundiendo consultas.

**Ampliación conceptual:** P2P no significa necesariamente ausencia absoluta de servidores. Muchos sistemas combinan coordinación centralizada, descubrimiento mediante terceros y transferencia directa.

### 4.7. Migración de código

La regla general es mover el elemento más pequeño:

- Si el código es pequeño y los datos enormes, llevar el código a los datos.
- Si los datos son pequeños, enviarlos al lugar donde se encuentra el procesamiento.

#### Movilidad débil

Solo se mueve el código y comienza desde el inicio en la nueva máquina.

Ejemplo del documento: un navegador descarga y ejecuta un script.

#### Movilidad fuerte

Se mueven código, datos y estado de ejecución. El programa continúa donde se detuvo. Es potente, pero poco habitual.

#### Push y pull

- **Push:** el emisor inicia el traslado.
- **Pull:** el receptor solicita el código.

Un navegador realiza movilidad débil mediante pull: solicita el código y lo comienza localmente.

#### Recursos asociados

| Recurso    | Movilidad        | Ejemplo                                | Estrategia                   |
| ---------- | ---------------- | -------------------------------------- | ---------------------------- |
| Fijo       | No puede moverse | Pantalla, sensor, hardware local       | Mantener referencia remota   |
| Fastened   | Moverlo es caro  | Base grande, colección de archivos     | Copiar o mantener referencia |
| Unattached | Fácil de mover   | Caché, archivo temporal, tabla pequeña | Trasladarlo junto al código  |

La verdadera dificultad no suele ser trasladar las instrucciones, sino conservar sus vínculos con recursos que no pueden seguirlas.

### 4.8. Memoria compartida distribuida

La memoria compartida distribuida ofrece al programa un único espacio de direcciones aunque las máquinas no compartan memoria física.

El programa usa variables normales. Una capa inferior:

- Detecta el acceso.
- Localiza los datos.
- Transfiere la página necesaria.

Ventajas:

- Modelo de programación más simple.
- Referencias en lugar de formatos explícitos de mensajes.
- Uso de hardware ordinario.

Costes:

- Una lectura aparentemente sencilla puede activar una transferencia de red.
- El rendimiento depende de la localidad.
- El programador deja de ver dónde está el coste.

La frase clave del PDF es que los mensajes no desaparecen: solamente dejan de ser visibles para el programador.

### 4.9. Cloud: IaaS, PaaS y SaaS

| Modelo      | Administra el usuario                           | Administra el proveedor            |
| ----------- | ----------------------------------------------- | ---------------------------------- |
| On-premises | Todo                                            | Nada                               |
| IaaS        | SO, runtime, aplicación y datos                 | Hardware, red y virtualización     |
| PaaS        | Aplicación y datos                              | Todo lo situado bajo la aplicación |
| SaaS        | Principalmente sus datos y configuración de uso | La plataforma completa             |

Ejemplos:

- On-premises: servidor en el edificio de la organización.
- IaaS: máquinas virtuales alquiladas.
- PaaS: plataforma administrada para desplegar aplicaciones.
- SaaS: correo o herramientas ofimáticas web.

Cuanto más alto es el nivel:

- Menos administra el cliente.
- Menos control tiene sobre los detalles internos.

El producto decisivo es la elasticidad: la capacidad puede aparecer en minutos y retirarse cuando baja la demanda.

### 4.10. IoT, Edge y Cloud

El documento distribuye las responsabilidades según dos variables:

1. ¿Con qué rapidez debe producirse la respuesta?
2. ¿Cuántos datos habría que transportar?

#### Dispositivo

- Sensores y actuadores.
- Memoria pequeña.
- Alimentación por batería.
- Desconexiones frecuentes.
- Reacciones en milisegundos.

#### Edge

- Está cerca de los dispositivos.
- Filtra y agrega información.
- Mantiene un búfer local.
- Puede actuar sin conexión a la nube.
- Responde en decenas de milisegundos.

#### Cloud

- Conserva históricos.
- Construye paneles.
- Compara muchos dispositivos.
- Entrena modelos.
- Proporciona almacenamiento muy amplio.
- Tolera respuestas de segundos u horas.

El volumen de datos disminuye desde dispositivo hacia cloud, porque el edge filtra y resume. Al mismo tiempo, la latencia aceptable aumenta.

Caso de uso: una cámara produce 20 Mbit/s.

- El dispositivo captura vídeo.
- El edge detecta movimiento o identifica eventos.
- Solo los fragmentos relevantes y metadatos viajan a la nube.
- La nube almacena históricos y entrena nuevos modelos.
- El modelo actualizado puede desplegarse de nuevo al edge.

Si se dependiera exclusivamente de la nube, una caída de conexión impediría una reacción inmediata. Si todo se hiciera en el dispositivo, faltarían almacenamiento y capacidad de análisis global.

---

## 5. Conexión entre los conceptos

El documento presenta decisiones que no son independientes:

- Una aplicación global aumenta la latencia.
- Para reducirla se introducen réplicas y cachés.
- Las copias generan problemas de consistencia.
- Mantener consistencia requiere coordinación.
- La coordinación añade viajes de red y reduce disponibilidad durante fallos.
- Para crecer se replica horizontalmente.
- La replicación obliga a decidir dónde reside el estado de sesión.
- P2P elimina parte del cuello central, pero introduce descubrimiento, churn y problemas de NAT.
- IoT necesita tolerar desconexiones, por lo que desplaza procesamiento y caché al edge.
- TCP simplifica la entrega fiable de bytes, pero no resuelve las fronteras de mensajes, la duplicación de operaciones ni la consistencia de datos.
- UDP reduce establecimiento y conserva datagramas, pero la aplicación debe aceptar o reparar pérdidas.

Un diseño distribuido es, por tanto, un conjunto de compromisos. No existe una arquitectura que maximice simultáneamente latencia, consistencia, disponibilidad, simplicidad, control y coste.

## 6. Caso integrador: una fábrica conectada

Una fábrica permite relacionar casi todo el bloque:

1. Los sensores son sistemas pervasivos y pueden desconectarse.
2. Envían telemetría pequeña mediante UDP o MQTT sobre TCP.
3. El edge controla maquinaria con latencia de milisegundos.
4. Mantiene un búfer si la nube queda inaccesible.
5. La nube conserva históricos y entrena modelos predictivos.
6. Las interfaces web utilizan una arquitectura de tres niveles.
7. El procesamiento se replica horizontalmente tras un balanceador.
8. Los servidores de aplicación son stateless; las sesiones se guardan en un almacén compartido.
9. Los paneles almacenan datos recientes en caché.
10. La telemetría puede aceptar consistencia eventual, pero una orden de parada requiere garantías más fuertes.
11. Los timeouts detectan ausencia de respuesta, aunque no pueden distinguir perfectamente entre avería y lentitud.
12. La transparencia permite sustituir una instancia sin afectar al operador, pero no elimina el retardo físico.
13. La disponibilidad depende tanto de evitar fallos como de recuperar rápidamente.
14. El sistema debe seguir funcionando parcialmente cuando fallen la red, un sensor o un servicio cloud.

Este ejemplo refleja la tesis esencial del PDF: distribuir permite situar cada función donde resulta más útil, pero obliga a diseñar explícitamente comunicación, estado, consistencia, concurrencia y fallos.
