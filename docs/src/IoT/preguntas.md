# Preguntas de repaso — Sistemas Distribuidos e IoT

Banco de preguntas elaborado a partir del [Bloque 1 del temario](temario/bloque-1-evolucion-de-los-sistemas-distribuidos.md) y de las [notas de clase](notas-clase/index.md). Las respuestas son orientativas: sirven para comprobar los conceptos y se pueden ampliar con ejemplos propios.

## 1. Fundamentos y evolución de los sistemas distribuidos

1. ¿Qué caracteriza a un sistema distribuido y qué recursos mantiene cada computador de forma independiente?
   **Respuesta orientativa:** Es una aplicación cuyos componentes cooperan desde computadores independientes. Cada uno tiene CPU, memoria, reloj y estado local propios, y se coordinan mediante mensajes.
2. ¿Por qué el usuario puede percibir una sola aplicación aunque internamente intervengan muchos servicios y máquinas?
   **Respuesta orientativa:** La implementación oculta parte de su distribución tras interfaces y protocolos comunes; los distintos servicios cooperan para presentar una única función al usuario.
3. ¿Qué diferencia hay entre computación distribuida, sistemas distribuidos de información y sistemas pervasivos? Propón un ejemplo de cada uno.
   **Respuesta orientativa:** La computación distribuida combina recursos para rendimiento (por ejemplo, un clúster de cálculo); los sistemas de información integran aplicaciones y datos existentes (compra, inventario y pagos); los sistemas pervasivos integran cómputo en objetos móviles, sensores y entornos, como una red urbana IoT.
4. ¿En qué se diferencian un clúster y un grid en hardware, administración y dominios de confianza?
   **Respuesta orientativa:** Un clúster suele tener máquinas similares, red rápida, ubicación común y administración única. Un grid reúne recursos heterogéneos y dominios de distintas organizaciones, por lo que requiere coordinar confianza, identidad y políticas.
5. ¿Qué problema resuelve *volunteer computing* y qué mecanismos necesita para afrontar nodos que se desconectan o producen resultados incorrectos?
   **Respuesta orientativa:** Reparte cómputo entre equipos voluntarios. Como pueden desconectarse o ser no confiables, hay que dividir el trabajo, tolerar tareas ausentes, repetir algunos cálculos y comparar resultados.
6. ¿Cómo se relacionan *utility computing* y *cloud computing*? ¿Qué aporta la virtualización al modelo cloud?
   **Respuesta orientativa:** Utility computing vende recursos medidos según uso; cloud lo hace posible a gran escala mediante virtualización y gestión elástica. La virtualización abstrae el hardware físico y facilita asignar o sustituir recursos sin que el cliente gestione cada máquina.
7. ¿Qué tareas comprende la computación autonómica y qué ejemplo mostraría una recuperación automática ante sobrecarga?
   **Respuesta orientativa:** Incluye configuración, integración, optimización, reparación y protección automáticas. Ante sobrecarga podría crear una instancia, distribuir tráfico hacia ella y retirarla al bajar la demanda.
8. ¿Qué restricciones distinguen a la computación móvil y ubicua de un sistema instalado en un entorno controlado?
   **Respuesta orientativa:** Los sistemas móviles afrontan batería, recursos limitados, movilidad y cobertura variable; los ubicuos integran computadores en objetos cotidianos. En ambos, los componentes pueden aparecer, desaparecer o desconectarse sin que eso sea excepcional.
9. ¿Cuáles son las principales ventajas y costes de distribuir una aplicación? ¿Por qué añadir nodos no garantiza acelerar el sistema?
   **Respuesta orientativa:** Puede aportar rendimiento, crecimiento, disponibilidad y acceso geográfico a recursos; aumenta la complejidad, la administración, la seguridad y el coste de comunicación. Si los nodos esperan mucho unos por otros, la coordinación puede superar el trabajo ahorrado.
10. ¿Qué significa transparencia en un sistema distribuido? Explica las transparencias de acceso, localización, migración, reubicación, replicación, concurrencia y fallo.
   **Respuesta orientativa:** Es ocultar aspectos de la distribución: acceso (representación), localización (dónde está), migración (cambio de ubicación), reubicación (movimiento durante uso), replicación (copias), concurrencia (uso compartido) y fallo (recuperación o continuidad pese a fallos).
11. ¿Por qué la transparencia no puede ocultar por completo la latencia ni los fallos?
   **Respuesta orientativa:** La distancia física impone retardos y una API no puede eliminarlos. Además, ocultar detalles no hace que una red lenta, un servidor caído o una respuesta perdida dejen de afectar al resultado.
12. ¿Qué dimensiones de escalabilidad distingue el temario? Da un ejemplo de sistema que escale bien en tamaño, pero mal geográficamente o administrativamente.
   **Respuesta orientativa:** Escalabilidad de tamaño (usuarios/datos), geográfica (distancia) y administrativa (organizaciones/políticas). Un servicio puede atender muchos usuarios en un centro de datos y sufrir latencias altas o restricciones de confianza al desplegarse en varios continentes u organizaciones.
13. ¿Qué significa que una interfaz sea abierta? ¿Por qué apertura no equivale necesariamente a código abierto?
   **Respuesta orientativa:** Una interfaz abierta está especificada con precisión suficiente para implementar componentes compatibles. Puede ser abierta aunque su implementación no publique el código fuente.
14. ¿Cómo se puede perder una actualización cuando dos clientes modifican concurrentemente el mismo dato? ¿Por qué un mutex local no basta?
   **Respuesta orientativa:** Dos clientes leen 100; uno escribe 150 y el otro, usando su copia antigua, escribe 130: se pierde la primera actualización. Un mutex de proceso solo coordina hilos que comparten esa memoria, no máquinas independientes.
15. ¿Qué diferencias hay entre un computador paralelo y un sistema distribuido respecto a memoria, comunicación, relojes y fallos?
   **Respuesta orientativa:** El computador paralelo suele tener memoria compartida y coordinación rápida mediante memoria; el distribuido tiene memoria y relojes independientes y se comunica por mensajes. En el distribuido puede fallar una parte mientras otras siguen operando.
16. Enumera las ocho falacias de la computación distribuida y explica cómo se manifiestan al diseñar un servicio real.
   **Respuesta orientativa:** Las ocho falacias son: red fiable, latencia cero, ancho de banda infinito, red segura, topología fija, un solo administrador, transporte gratuito y red homogénea. En diseño implican prever pérdidas, retardos, límites, ataques, cambios, políticas distintas, costes y heterogeneidad.
17. ¿Qué son el estado global, la gestión de fallos y el acuerdo? ¿Por qué son problemas centrales en sistemas distribuidos?
   **Respuesta orientativa:** El estado global es la visión conjunta del sistema; la gestión de fallos busca detectar, ocultar y recuperar fallos; el acuerdo permite que nodos acepten un valor, orden o membresía común. Son difíciles porque ningún nodo ve instantáneamente todo el sistema.
18. Si un nodo no responde antes de un *timeout*, ¿qué se puede concluir y qué no se puede concluir sobre su estado?
   **Respuesta orientativa:** Solo puede concluirse que no respondió dentro del plazo. Puede estar caído, lento, aislado por la red o tener una respuesta retrasada; el timeout no distingue esos casos con certeza.

## 2. Arquitectura del cliente, estado y disponibilidad

19. ¿Qué fallos pueden ocurrir en una interacción de petición y respuesta? ¿Por qué pueden parecer iguales desde el cliente?
   **Respuesta orientativa:** Puede perderse la petición, fallar el servidor durante el procesamiento o perderse la respuesta. El cliente observa silencio en todos los casos y no sabe necesariamente si la operación llegó a ejecutarse.
20. ¿Qué puede ocurrir si se reintenta una operación después de perder la respuesta? Analiza el caso de una orden de pago.
   **Respuesta orientativa:** La operación puede no ejecutarse, ejecutarse una vez o ejecutarse más de una vez si la primera sí ocurrió y solo se perdió la respuesta. Un pago reintentado sin deduplicación podría cobrar dos veces; un identificador de operación permite reconocer repeticiones.
21. ¿Qué son las capas lógicas de interfaz, procesamiento y datos? Reparte entre ellas los componentes de un buscador.
   **Respuesta orientativa:** La interfaz presenta la búsqueda; procesamiento valida la consulta, busca y ordena resultados; datos conserva el índice y documentos. Las fronteras pueden variar según la arquitectura.
22. ¿Qué ventajas y desventajas tiene un cliente ligero frente a uno pesado cuando la conexión es lenta o intermitente?
   **Respuesta orientativa:** Un cliente ligero es fácil de actualizar y administrar, pero depende de la red para casi todo. Uno pesado puede procesar y guardar datos localmente, tolera mejor cortes, aunque cuesta más mantenerlo actualizado y coherente.
23. ¿Qué es un *stub* y cómo puede ocultar la localización, los fallos o la replicación del servidor?
   **Respuesta orientativa:** Es un intermediario local que hace parecer normal una llamada remota y traduce parámetros a mensajes. Puede resolver nombres, reintentar o escoger réplicas, ocultando ciertos detalles a la aplicación.
24. En X Window System, ¿quién es el servidor y qué recursos ofrece? ¿Por qué los nombres de cliente y servidor pueden resultar contraintuitivos?
   **Respuesta orientativa:** El servidor X es el proceso que ofrece pantalla, teclado y ratón; la aplicación remota es su cliente porque solicita operaciones gráficas. Los nombres indican roles de servicio, no qué máquina es más potente o dónde está el usuario.
25. ¿Qué diferencia hay entre un servidor *stateless* y uno con estado? ¿Cómo influye en el escalado y la recuperación?
   **Respuesta orientativa:** Un servidor stateless no conserva contexto privado imprescindible entre solicitudes; cualquiera de sus instancias puede atender la siguiente. Uno con estado simplifica solicitudes, pero complica balanceo, recuperación y continuidad tras reinicios.
26. ¿Dónde puede almacenarse el estado de sesión de una cesta de compra? Compara cookie con almacén compartido, estado en cliente y estado en servidor.
   **Respuesta orientativa:** Puede enviarse un identificador en cookie y guardar la cesta en un almacén compartido; guardar el estado en cliente aumenta datos enviados y requiere protegerlos; mantenerlo en servidor reduce mensajes pero exige afinidad o estado compartido para escalar.
27. ¿En qué se diferencian latencia, *throughput* y ancho de banda? ¿Cuál importa más para transferencias grandes y cuál para muchos intercambios pequeños?
   **Respuesta orientativa:** Latencia es el tiempo de respuesta, throughput el trabajo por unidad de tiempo y ancho de banda la capacidad de transferencia. El ancho de banda importa para volúmenes grandes; la latencia domina protocolos con muchos viajes pequeños.
28. Calcula la disponibilidad a partir de MTBF y MTTR. ¿Qué cambio mejora la disponibilidad: reducir la frecuencia de fallos o reducir el tiempo de reparación?
   **Respuesta orientativa:** Disponibilidad = MTBF / (MTBF + MTTR). Aumenta si sube el tiempo medio entre fallos o baja el tiempo medio de reparación; suele ser eficaz automatizar recuperación para reducir MTTR.
29. ¿Qué significan consistencia fuerte, *read-your-writes*, lecturas monotónicas, consistencia causal y consistencia eventual? ¿Qué aplicación podría tolerar cada garantía?
   **Respuesta orientativa:** Fuerte refleja la última escritura; read-your-writes deja que un cliente vea sus cambios; monotónica evita retroceder a valores viejos; causal conserva orden causa-efecto; eventual permite discrepancias temporales que convergen. Un saldo crítico suele exigir más coordinación que un contador aproximado.
30. ¿Qué compromiso introduce una caché del lado del cliente entre latencia, carga de red y frescura de los datos?
   **Respuesta orientativa:** Las cachés reducen viajes y carga y aceleran aciertos, pero pueden devolver datos antiguos. Hay que equilibrar frescura, latencia, disponibilidad y tráfico mediante caducidad o validación.
31. ¿Cómo afecta una tasa de aciertos del 90 % a la latencia media aproximada si el RTT al servidor es 80 ms? ¿Qué supuesto simplificador contiene ese cálculo?
   **Respuesta orientativa:** Con la aproximación del temario, 10 % de fallos multiplicado por 80 ms da unos 8 ms. Simplifica suponiendo que los aciertos cuestan casi cero y que la latencia de los fallos es el RTT uniforme, sin considerar otros costes.
32. ¿Qué diferencia hay entre un cliente síncrono y uno asíncrono? ¿Qué mecanismos puede usar el segundo para recibir resultados?
   **Respuesta orientativa:** El síncrono espera bloqueado por la respuesta; el asíncrono continúa y recoge después el resultado mediante callback, cola, future o promesa. Este último debe gestionar solicitudes pendientes, errores y respuestas tardías.

## 3. Redes, capas y protocolos

33. ¿Qué función cumple cada capa del modelo OSI? ¿Cómo se agrupan esas funciones en el modelo TCP/IP de cuatro capas?
   **Respuesta orientativa:** OSI separa física, enlace, red, transporte, sesión, presentación y aplicación. TCP/IP agrupa en enlace, Internet, transporte y aplicación, donde aplicación incluye aproximadamente las funciones OSI de sesión y presentación.
34. ¿Qué es una PDU y qué nombres reciben habitualmente las unidades de Ethernet, IP, TCP y UDP?
   **Respuesta orientativa:** Una PDU es la unidad de datos que maneja una capa: Ethernet usa trama, IP datagrama/paquete IP, TCP segmento y UDP datagrama UDP.
35. Describe la encapsulación desde los datos de aplicación hasta los bits transmitidos. ¿Qué hace el receptor en sentido inverso?
   **Respuesta orientativa:** La aplicación genera datos; transporte añade su cabecera; IP añade direcciones; enlace añade información de trama y comprobación; física transmite bits. El receptor procesa las capas inversamente y retira la información de control correspondiente.
36. ¿Qué información añaden las cabeceras de TCP, IP y Ethernet? ¿Cómo contribuye ese control al *overhead*?
   **Respuesta orientativa:** TCP añade puertos, secuencia, ACK y control; IP añade direcciones y campos de encaminamiento; Ethernet añade direcciones de enlace y FCS. Estas cabeceras consumen bytes además de los datos útiles, es decir, producen overhead.
37. ¿Qué capas procesa normalmente un router y qué ocurre con la trama de enlace al atravesar un router?
   **Respuesta orientativa:** El router procesa principalmente IP (capa 3). Retira la trama del enlace entrante, examina el datagrama IP y lo encapsula en una nueva trama para el siguiente enlace.
38. ¿Qué es un *Service Access Point* (SAP)? ¿Qué identifican las direcciones IP y los puertos en una comunicación de Internet?
   **Respuesta orientativa:** Un SAP es el punto mediante el que una capa ofrece acceso a un servicio. IP identifica interfaces de red y TCP/UDP usan puertos para dirigir datos a procesos o servicios.
39. ¿Qué elementos forman la 5-tupla de una conexión y para qué sirve?
   **Respuesta orientativa:** Protocolo de transporte, IP origen, puerto origen, IP destino y puerto destino. La combinación permite distinguir una comunicación concreta entre muchas conexiones simultáneas.
40. ¿Qué significa que IP ofrezca un servicio *best effort*? ¿Qué propiedades no garantiza?
   **Respuesta orientativa:** IP intenta transportar cada datagrama, pero no garantiza entrega, unicidad, orden ni conexión previa. Fiabilidad y recuperación deben aportarlas capas superiores si se necesitan.
41. ¿Qué propiedades ofrece TCP sobre IP y cómo las consigue?
   **Respuesta orientativa:** TCP ofrece flujo de bytes fiable y ordenado, retransmisiones, detección de pérdidas, control de flujo y congestión y conexión bidireccional. Usa secuencias, ACK, temporizadores y mecanismos de adaptación de envío.
42. Describe el *three-way handshake* de TCP e indica qué información sincronizan los extremos.
   **Respuesta orientativa:** El cliente envía SYN, el servidor contesta SYN+ACK y el cliente devuelve ACK. Con ello confirman la posibilidad de comunicación y sincronizan números iniciales de secuencia.
43. ¿Cómo se utilizan los números de secuencia y los ACK para confirmar bytes recibidos y recuperar pérdidas?
   **Respuesta orientativa:** TCP numera los bytes; los ACK indican el siguiente byte esperado. La falta de confirmación o la detección de huecos provoca retransmisiones para recuperar datos perdidos.
44. ¿Qué diferencia hay entre control de flujo y control de congestión en TCP?
   **Respuesta orientativa:** Control de flujo evita desbordar los buffers del receptor mediante ventana anunciada; control de congestión adapta el envío a la capacidad y congestión de la red.
45. ¿Qué función cumplen `SYN`, `ACK`, `FIN` y `RST`? ¿Por qué el cierre TCP puede necesitar intercambios en ambas direcciones?
   **Respuesta orientativa:** SYN inicia/sincroniza, ACK confirma, FIN solicita cerrar ordenadamente una dirección y RST aborta o rechaza una conexión. Como cada sentido del flujo puede cerrarse separadamente, ambos extremos intercambian cierre y confirmación.
46. ¿Qué campos principales aparecen en la cabecera TCP y qué hace el *urgent pointer*?
   **Respuesta orientativa:** Incluye puertos, secuencia y ACK, longitud, flags, ventana, checksum y opciones. URG y el urgent pointer señalan datos urgentes según el mecanismo TCP, aunque su uso actual es poco habitual.
47. ¿Qué garantías no ofrece UDP? Si una aplicación necesita fiabilidad sobre UDP, ¿dónde debe implementarla?
   **Respuesta orientativa:** UDP no garantiza conexión, entrega, orden, ausencia de duplicados ni retransmisión. Si se requieren, deben implementarse en el protocolo o lógica de la aplicación.
48. Compara TCP y UDP en conexión, orden, retransmisión, control de flujo, coste y casos de uso.
   **Respuesta orientativa:** TCP establece conexión y proporciona orden, retransmisiones y controles con mayor cabecera y coste; sirve para web tradicional y archivos. UDP envía datagramas con menos mecanismo y latencia, útil cuando datos tardíos pierden valor o la aplicación define su propia política.
49. ¿Por qué una lectura periódica de temperatura podría enviarse por UDP? ¿En qué situación sería insuficiente esa elección?
   **Respuesta orientativa:** Una lectura nueva llega pronto y puede reemplazar a la anterior, de modo que retransmitir una muestra vieja quizá desperdicie energía y ancho de banda. UDP sería insuficiente si cada lectura debiera conservarse o su pérdida tuviera consecuencias críticas sin recuperación propia.
50. ¿Por qué el vídeo bajo demanda no implica necesariamente UDP? ¿Qué propiedad de los datos determina mejor la elección del transporte?
   **Respuesta orientativa:** Vídeo bajo demanda puede almacenar en búfer y necesitar todos los bytes en orden, por lo que TCP puede ser conveniente. Importan las consecuencias de pérdida y retraso para la aplicación, no solo que sea audio o vídeo.
51. ¿Qué es un puerto efímero? ¿Por qué TCP y UDP pueden usar el mismo número de puerto sin identificar el mismo endpoint?
   **Respuesta orientativa:** Es un puerto temporal que el sistema asigna normalmente al cliente. TCP y UDP mantienen espacios de puertos separados, así que protocolo forma parte de la identificación del endpoint/conexión.
52. Relaciona SSH, SMTP, DNS, HTTP, HTTPS y MQTT con sus puertos habituales y transportes indicados en los apuntes.
   **Respuesta orientativa:** SSH TCP/22; SMTP TCP/25; DNS UDP o TCP/53; HTTP TCP/80; HTTPS TCP/443; MQTT habitualmente TCP/1883 sin TLS, según estos apuntes.
53. ¿Qué tres puntos de una petición remota pueden fallar y por qué un *timeout* no permite distinguirlos siempre?
   **Respuesta orientativa:** Puede perderse la petición, fallar o demorarse el servidor, o perderse la respuesta. En cada caso el cliente puede observar únicamente que no recibió respuesta antes del límite.
54. ¿Qué propósito tiene `TIME_WAIT` y por qué un servidor puede no poder reutilizar inmediatamente una dirección y puerto?
   **Respuesta orientativa:** TIME_WAIT mantiene durante un periodo información que evita que segmentos antiguos interfieran con una conexión nueva que reutilice identificadores. Por eso la dirección/puerto puede seguir temporalmente ocupado tras cerrar.
55. ¿Cómo detecta Ethernet corrupción mediante FCS/CRC? ¿Detectar un error implica que Ethernet retransmita la trama?
   **Respuesta orientativa:** El emisor calcula FCS con CRC y el receptor lo recalcula; una diferencia indica corrupción. Detectar no implica retransmitir: la recuperación depende del protocolo o tecnología.
56. ¿Qué comprueba el *checksum* de TCP/UDP y por qué su cálculo incluye una pseudo-cabecera IP?
   **Respuesta orientativa:** Ayuda a detectar corrupción de cabecera y datos; su pseudo-cabecera incluye datos IP como direcciones para vincular el segmento/datagrama al origen y destino correctos.
57. ¿Qué es la MTU? Diferencia la segmentación TCP de la fragmentación IP y explica por qué conviene evitar datagramas UDP grandes.
   **Respuesta orientativa:** MTU es el tamaño máximo transportable por un enlace. TCP segmenta su flujo en unidades adecuadas; IP puede fragmentar datagramas. En UDP, perder un fragmento inutiliza el datagrama completo, por lo que se prefieren mensajes moderados.
58. ¿Cómo permite NAT que varios equipos privados compartan una IPv4 pública? ¿Por qué dificulta conexiones entrantes no solicitadas?
   **Respuesta orientativa:** El router reemplaza combinaciones privadas de dirección/puerto por una dirección pública y puertos externos y guarda una tabla para devolver respuestas. Para tráfico entrante nuevo necesita una regla o mecanismo que indique el destino privado.
59. ¿Qué comprueba `ping` y qué limitación tiene interpretar la ausencia de respuesta como prueba de que un host está caído?
   **Respuesta orientativa:** ping suele medir respuesta ICMP Echo y RTT aproximado. El host puede estar activo y filtrar ICMP, así que no responder no prueba que esté desconectado.
60. Explica cómo `traceroute` utiliza el TTL y los mensajes ICMP para descubrir saltos.
   **Respuesta orientativa:** Envía paquetes con TTL creciente. Cada router reduce el TTL; al llegar a cero descarta el paquete y suele enviar ICMP Time Exceeded, revelando ese salto.
61. ¿Qué función cumplen `socket`, `bind`, `listen`, `accept` y `connect` en la API de sockets?
   **Respuesta orientativa:** socket crea el endpoint; bind asigna dirección/puerto local; listen lo pone a escuchar; accept acepta una conexión y entrega un socket nuevo; connect inicia conexión desde el cliente.
62. ¿Por qué una llamada a `read` o `write` puede procesar solo parte de los bytes solicitados?
   **Respuesta orientativa:** Las llamadas pueden devolver menos bytes de los solicitados por disponibilidad parcial, límites del buffer, interrupciones o estado del socket. El programa debe revisar el resultado y repetir según el protocolo de mensajes.
63. ¿Qué proporciona TLS y cómo se relaciona con HTTPS? ¿Qué funciones suelen cumplir la criptografía simétrica y la asimétrica?
   **Respuesta orientativa:** TLS aporta confidencialidad, integridad y autenticación; HTTPS es HTTP protegido con TLS. La criptografía asimétrica suele ayudar a autenticar e intercambiar secretos, y la simétrica cifra eficientemente grandes volúmenes.
64. ¿Qué propiedades de un dispositivo IoT (energía, memoria, conectividad, latencia) afectan a la selección del protocolo y al tamaño o frecuencia de los mensajes?
   **Respuesta orientativa:** Batería y CPU limitadas favorecen mensajes eficientes; memoria limita buffers; cortes requieren almacenamiento/reintentos locales; latencia y vigencia del dato orientan TCP/UDP, tamaños y frecuencia. También importan seguridad y coste de comunicación.
65. ¿Cómo desacopla MQTT a productores y consumidores? ¿Qué función cumple el broker y qué transporte se menciona habitualmente en los apuntes?
   **Respuesta orientativa:** Los productores publican mensajes en temas al broker y los consumidores se suscriben; el broker enruta sin que cada productor conozca a cada consumidor. Los apuntes describen MQTT tradicional sobre TCP, normalmente puerto 1883 sin TLS.

## 4. Flynn y redes móviles *ad hoc*

66. ¿Qué dos flujos utiliza la taxonomía de Flynn para clasificar arquitecturas?
   **Respuesta orientativa:** Clasifica por flujos de instrucciones y flujos de datos.
67. Compara SISD, SIMD, MISD y MIMD en número de flujos de instrucciones y datos, e indica un ejemplo de cada una.
   **Respuesta orientativa:** SISD: una instrucción/uno dato, CPU secuencial; SIMD: una instrucción/múltiples datos, GPU; MISD: múltiples instrucciones/un flujo de datos, caso poco común como procesamiento redundante; MIMD: múltiples instrucciones/múltiples datos, clúster o multicore.
68. ¿Por qué aplicar el mismo filtro a muchos píxeles encaja con SIMD, mientras que un clúster suele ser MIMD?
   **Respuesta orientativa:** El filtro aplica la misma operación a muchos píxeles simultáneamente (SIMD). En un clúster los procesadores pueden ejecutar tareas distintas sobre datos distintos (MIMD).
69. ¿Qué es una MANET? ¿Qué papel pueden desempeñar sus nodos además de generar y recibir datos?
   **Respuesta orientativa:** Es una red inalámbrica descentralizada de nodos móviles que colaboran para comunicarse. Un nodo puede reenviar paquetes y servir de nodo intermedio además de ser host.
70. ¿Qué caracteriza a una VANET y cómo se relaciona con MANET?
   **Respuesta orientativa:** Es una MANET especializada cuyos nodos son principalmente vehículos, con movilidad rápida condicionada por carreteras y aplicaciones de tráfico y seguridad.
71. Distingue V2V y V2I con un escenario de seguridad vial.
   **Respuesta orientativa:** V2V es comunicación entre vehículos; V2I enlaza vehículo e infraestructura vial. Por ejemplo, dos coches comparten una frenada (V2V) y un coche recibe aviso de semáforo o unidad de carretera (V2I).
72. ¿Qué cambia en la topología y el encaminamiento cuando los nodos móviles entran, salen o se desplazan?
   **Respuesta orientativa:** Cambian enlaces y rutas disponibles; puede ser necesario descubrir nuevos vecinos y recalcular rutas, y los nodos pueden desaparecer mientras se reenvían mensajes.
73. ¿Qué información de control se añade durante la encapsulación y cómo cambia la unidad observada al subir o bajar por las capas?
   **Respuesta orientativa:** Cada capa añade control como puertos, direcciones, números de secuencia o direcciones de enlace. Al bajar se ve una unidad más encapsulada (datos, segmento/datagrama, paquete, trama, bits); al subir se retiran cabeceras.

## 5. Estilos y arquitecturas distribuidas

74. ¿Qué diferencia hay entre organización lógica de componentes y su colocación física en máquinas?
   **Respuesta orientativa:** La lógica define componentes y responsabilidades; la colocación física decide en qué procesos o máquinas se ejecutan. Separarlas permite cambiar despliegue sin cambiar necesariamente el diseño lógico.
75. ¿Qué son el acoplamiento espacial y temporal? Clasifica llamada directa, cola de mensajes, publicación/suscripción y almacén compartido según esas dimensiones.
   **Respuesta orientativa:** Espacial indica si los participantes deben conocerse; temporal, si deben estar activos a la vez. Llamada directa: acoplada en ambas; cola: espacio acoplado al destino/cola y tiempo desacoplado; pub/sub: emisor no conoce receptores pero coinciden activos; almacén compartido: ambos desacoplados en espacio y tiempo.
76. ¿Qué distingue el patrón cliente-servidor y cuáles son sus posibles cuellos de botella y puntos únicos de fallo?
   **Respuesta orientativa:** El cliente inicia peticiones a un servidor que espera en dirección conocida. El servidor concentra tráfico/carga y, si es único, puede ser cuello de botella y punto único de fallo.
77. Compara servidores iterativos, con proceso/hilo por cliente y dirigidos por eventos en simplicidad, recursos y concurrencia.
   **Respuesta orientativa:** Iterativo es sencillo pero un cliente lento bloquea a los demás; proceso/hilo por cliente facilita concurrencia a costa de recursos por conexión; dirigido por eventos escala muchas conexiones con menos recursos, pero exige gestionar estados y eventos parciales.
78. ¿Qué diferencia hay entre arquitectura de dos niveles y tres niveles? ¿Qué coste aparece al convertir una frontera local en una llamada de red?
   **Respuesta orientativa:** Dos niveles reparten las capas lógicas entre dos ubicaciones; tres suelen separar interfaz, procesamiento y datos. Una llamada que era local pasa a tener latencia, fallos y posibles repeticiones de red.
79. Distingue distribución vertical y horizontal. ¿Cómo podrían combinarse detrás de un balanceador?
   **Respuesta orientativa:** Vertical reparte capas distintas entre máquinas; horizontal replica una misma capa para dividir carga y mejorar capacidad/disponibilidad. Se pueden separar tres niveles y tener varias instancias de procesamiento tras un balanceador.
80. ¿Qué significa que en una arquitectura P2P cada nodo pueda actuar como cliente y servidor? ¿Qué recursos aporta cada peer?
   **Respuesta orientativa:** Cada peer puede solicitar y servir información y aportar almacenamiento, cómputo o ancho de banda. Así, al crecer participantes puede crecer también parte de la capacidad disponible.
81. ¿Cómo localiza datos una DHT y qué expresa el coste de búsqueda aproximado (O(\log N))?
   **Respuesta orientativa:** La DHT asigna claves a posiciones/nodos de un espacio lógico y usa vecinos/atajos para localizar la clave. O(log N) indica que el número de pasos crece aproximadamente con el logaritmo del tamaño de la red.
82. ¿En qué se diferencian redes P2P estructuradas, híbridas y no estructuradas? ¿Qué centralización parcial conserva una red híbrida?
   **Respuesta orientativa:** La estructurada define organización y búsquedas eficientes (p. ej. DHT); la híbrida mantiene índice central y transfiere datos entre peers; la no estructurada difunde consultas por vecinos con robustez pero puede desperdiciar tráfico.
83. ¿Qué es *churn* en una red P2P y cómo afectan NAT y el descubrimiento de peers a la conectividad directa?
   **Respuesta orientativa:** Churn es entrada, salida o desconexión frecuente de peers. NAT dificulta conexiones entrantes directas y el descubrimiento debe localizar nodos alcanzables o usar técnicas/intermediarios de conectividad.
84. ¿Cuándo conviene mover código hacia los datos en vez de transferir los datos al código?
   **Respuesta orientativa:** Cuando el código es relativamente pequeño y los datos son grandes o están distribuidos, mover el cálculo puede evitar transferir grandes volúmenes.
85. Compara movilidad débil y fuerte, y explica la diferencia entre estrategias *push* y *pull*.
   **Respuesta orientativa:** Movilidad débil mueve el código y lo inicia de nuevo; fuerte incluye estado de ejecución para continuar, algo complejo. Push lo inicia el emisor; pull significa que el receptor solicita el código.
86. ¿Qué significa que un recurso sea fijo, *fastened* o *unattached*? ¿Qué opciones hay cuando el recurso no puede migrar con el código?
   **Respuesta orientativa:** Fijo no puede moverse; fastened puede moverse con coste alto; unattached es fácil de trasladar. Se puede trasladar una referencia, copiar datos o mover el recurso según su tamaño y coste.
87. ¿Cómo ofrece una memoria compartida distribuida la ilusión de un espacio de direcciones único? ¿Qué coste queda oculto al programador?
   **Respuesta orientativa:** Una capa hace que accesos parezcan variables locales, pero detecta fallos de página/acceso, localiza y transfiere datos por red. Los mensajes y la latencia siguen existiendo, aunque no sean explícitos en el código.
88. Compara on-premises, IaaS, PaaS y SaaS según qué administra el usuario y qué administra el proveedor.
   **Respuesta orientativa:** On-premises: usuario administra todo; IaaS: proveedor hardware/red/virtualización y usuario SO-runtime-app-datos; PaaS: proveedor también plataforma y runtime, usuario app/datos; SaaS: proveedor opera la aplicación y el usuario configura uso y sus datos.

## 6. IoT, Edge, Cloud e integración

89. ¿Cómo se reparten las responsabilidades entre dispositivo, Edge y Cloud según latencia, volumen de datos y capacidad de cómputo?
   **Respuesta orientativa:** El dispositivo captura y puede reaccionar con latencia mínima; Edge filtra, agrega, almacena temporalmente y decide cerca; Cloud conserva históricos, analiza muchos dispositivos y entrena modelos. Al subir aumenta capacidad y latencia tolerable.
90. En una cámara que genera vídeo continuamente, ¿qué tareas conviene realizar en el dispositivo, en Edge y en Cloud? Justifica la distribución.
   **Respuesta orientativa:** El dispositivo adquiere vídeo; Edge detecta eventos y conserva o envía fragmentos relevantes; Cloud almacena históricos y entrena modelos. Así se reduce tráfico y se mantienen respuestas rápidas aunque Cloud no esté disponible.
91. ¿Qué ventaja aporta un búfer Edge cuando se pierde la conexión con Cloud? ¿Qué funcionalidad debería mantenerse localmente por requisitos de latencia?
   **Respuesta orientativa:** El búfer conserva lecturas durante el corte para enviarlas después; Edge puede mantener alarmas o control local que no tolere una ida y vuelta a Cloud. La lógica de seguridad crítica debe funcionar localmente.
92. En una fábrica conectada, ¿qué operaciones podrían tolerar consistencia eventual y cuáles necesitarían garantías más fuertes?
   **Respuesta orientativa:** Paneles y métricas aproximadas pueden tolerar convergencia eventual. Órdenes de parada, enclavamientos o cambios críticos de estado requieren confirmación y garantías más fuertes y definidas.
93. Diseña el recorrido de una lectura desde un sensor hasta un panel: indica transporte, posible broker, filtrado Edge, almacenamiento Cloud y mecanismo de visualización.
   **Respuesta orientativa:** El sensor publica medidas por MQTT/TCP o transporte adecuado; broker enruta; Edge valida, filtra y almacena si no hay conexión; Cloud guarda históricos; una API/servicio alimenta el panel, que puede usar caché para lecturas recientes.
94. Si MQTT usa TCP, ¿qué garantías de transporte aporta TCP y qué decisiones de entrega o procesamiento siguen perteneciendo a la aplicación?
   **Respuesta orientativa:** TCP aporta flujo ordenado y fiable entre extremos mientras la conexión pueda recuperarse. La aplicación aún decide semántica de mensajes, duplicados, persistencia, confirmación de negocio, reintentos y qué hacer con mensajes obsoletos.
95. ¿Cómo se relacionan replicación, caché y consistencia? ¿Por qué reducir latencia con copias puede aumentar el coste de coordinación?
   **Respuesta orientativa:** Cachés y réplicas acercan datos y reducen latencia, pero pueden divergir. Mantenerlas coherentes exige invalidación o coordinación, que añade tráfico, viajes y dependencia de nodos durante fallos.
96. ¿Qué ocurre con el estado de sesión cuando se replica horizontalmente un servicio? Propón una ubicación que permita atender peticiones desde varias instancias.
   **Respuesta orientativa:** Si cada instancia conserva la sesión local, otra instancia puede no conocerla. Guardar sesión en un almacén compartido (o reenviar estado firmado desde cliente) permite que distintas instancias atiendan peticiones.
97. ¿Qué efectos tendría que se pierda la respuesta a una orden de parada de una máquina y el cliente la reintente? ¿Cómo podría evitarse ejecutar dos veces la misma operación?
   **Respuesta orientativa:** Si la parada se ejecutó pero la respuesta se perdió, repetir puede emitir la orden otra vez o producir efectos duplicados. Usar un identificador único y registrar operaciones procesadas permite responder a duplicados sin repetir el efecto.
98. Analiza qué partes de una fábrica conectada pueden seguir operando ante la caída de un sensor, la red local o Cloud. ¿Qué decisiones de arquitectura mejoran la disponibilidad?
   **Respuesta orientativa:** Sensores restantes y control Edge pueden seguir recogiendo o actuando durante fallos parciales; Cloud puede reponerse más tarde con datos almacenados. Redundancia de sensores/servicios, búferes, modos seguros y recuperación automatizada elevan disponibilidad.
99. ¿Qué compromisos aparecen entre latencia, consistencia, disponibilidad, simplicidad, coste y control al diseñar un sistema distribuido?
   **Respuesta orientativa:** Reducir latencia con réplicas puede debilitar consistencia; más disponibilidad y tolerancia a fallos cuestan coordinación, operación y dinero; abstraer simplifica uso pero reduce control. La elección depende de qué errores y demoras tolere el servicio.
100. Elige una aplicación IoT (agricultura, ciudad, salud o industria) y justifica su arquitectura, protocolo, estrategia de almacenamiento, comportamiento ante desconexiones y requisitos de seguridad.
   **Respuesta orientativa:** Una propuesta razonable identifica requisitos: por ejemplo, agricultura con sensores de humedad publica lecturas periódicas; gateway Edge filtra y almacena durante cortes; Cloud conserva históricos y calcula tendencias; alarmas locales controlan riego; TLS, identidad de dispositivos y control de acceso protegen comunicaciones y órdenes. Deben justificarse transporte y consistencia según criticidad.
