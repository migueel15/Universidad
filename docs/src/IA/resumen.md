# Resumen general para el cuestionario

Resumen de los bloques 1 a 4. Cada sección recoge las ideas y fórmulas que más se preguntan.

---

## Idea central de la asignatura

- **El modelo es solo una parte del sistema.** El sistema completo incluye datos, preprocesado, política de decisión, infraestructura, personas, monitorización y fallback.
- **El modelo predice, una política decide y una persona o sistema actúa.** El modelo produce un *score*, no la acción.
- No se puede probar todo el espacio de entradas (**verification gap**). Por eso se usan evidencia estadística, límites físicos, monitorización, caducidad de la evidencia y **fallback seguro**.

---

## Bloque 1.1 — Requisitos de software basado en IA

**Software 1.0 vs 2.0**
- 1.0: reglas escritas a mano y tests exactos.
- 2.0: la lógica se aprende de los datos. El resultado depende de los datos, las etiquetas, el split, los pesos, la semilla y la población.

**Un requisito de IA es un contrato verificable** que fija población y ventana temporal, decisión observable, métrica + umbral + dataset de evaluación, restricciones (memoria, latencia, capacidad humana), cuándo caduca la evidencia, fallback y responsable.

**Plantilla:** *Dada [población], el sistema hará [decisión], con [métrica] ≥ [umbral] en [dataset], cumpliendo [restricciones]. Si [condición de drift], activa [fallback]. Responsable: [owner].*

**Taxonomía de 8 clases de requisitos:** funcional · datos/entrada · calidad predictiva · rendimiento/recursos · fiabilidad/seguridad (*safety*) · seguridad/privacidad · explicabilidad/gobernanza · mantenibilidad/reproducibilidad.

**Matriz de confusión y métricas**

| | Predice positivo | Predice negativo |
|---|---|---|
| Real positivo | TP | FN |
| Real negativo | FP | TN |

- Accuracy = (TP+TN)/n
- Precision = TP/(TP+FP) → de las alarmas, ¿cuántas eran reales?
- Recall = TP/(TP+FN) → de los positivos reales, ¿cuántos detecto?
- F1 = 2·P·R/(P+R) (media armónica)

**La accuracy engaña con eventos raros.** El sistema "nunca falla" saca un 99 % de accuracy y no detecta nada.
**Prueba de admisibilidad:** calcula la métrica para el sistema trivial que no hace nada. Si saca buena nota, la métrica no sirve. Accuracy se rechaza. Precision no se exige nunca sola. Recall y F1 son admisibles.

**Caso de mantenimiento predictivo:** FP = 2,10 €, FN = 480 € (un FN cuesta unas 228 veces más). Coste = 2,10·FP + 480·FN.
- La **baseline** es la regla que ya existe, no el cero.
- F1 y coste pueden preferir candidatos distintos. El requisito debe decir qué criterio manda.
- El umbral se elige minimizando el coste **sujeto a restricciones** (capacidad ≤ 100 visitas).

**Evidencia que caduca:** por ejemplo, si PSI > 0,20 se invalida la validación y se activa el fallback.

**Memoria del modelo:** M = P · b/8. Con 25 M de parámetros: FP32 ocupa 100 MB, FP16 50 MB e INT8 25 MB. Después de cuantizar **hay que revalidar**.
**Puertas de aceptación:** todas (memoria, latencia, recall...) deben cumplirse a la vez.

---

## Bloque 1.2 — Retos en el desarrollo de aplicaciones de IA

**Autopsia D-A-M**: clasifica las causas de fallo en tres planos.
- **Data:** desbalanceo de clases, temporal leakage, data drift.
- **Algorithm:** ilusiones de métrica, concept drift, CACE.
- **Machine:** training-serving skew, límites de SRAM, no determinismo de la GPU.

**Desbalanceo de clases:** IR = N_mayoritaria / N_minoritaria. Por ejemplo, 9.950/50 = 199.
- Specificity = TN/(TN+FP)
- **Balanced accuracy** = (Recall + Specificity)/2. Para el sistema trivial vale 0,5.

**Weighted cross-entropy:** wⱼ = N/(C·Nⱼ). Da más peso a la clase minoritaria. No crea información nueva. Puede subir los FP, empeorar la calibración y obliga a reajustar el umbral.

**Temporal leakage:** se usa información que no existiría en el momento de predecir. Solución: **split temporal**, documentar cuándo se conoce cada columna y simular el estado en el instante de la decisión.

**Drift**
- **Data drift / covariate shift:** cambia P(X) y P(Y|X) se mantiene. Ejemplo: un sensor que envejece.
- **Concept drift:** cambia P(Y|X). Ejemplo: cambia el proceso de fabricación. **Solo se detecta con etiquetas recientes.** Un PSI estable no lo descarta.

**PSI** = Σ (Aᵢ − Eᵢ)·ln(Aᵢ/Eᵢ). Por debajo de 0,10 la población es estable. Entre 0,10 y 0,25 el cambio es moderado. A partir de 0,25 el cambio es crítico y toca revalidar. Son valores orientativos, y el PSI es una alarma, no un diagnóstico.

**Training-serving skew:** la feature se calcula distinto en entrenamiento y en producción. Solución: compartir el código, usar un **feature store**, versionar y hacer pruebas de paridad.

**Arithmetic intensity** I = FLOPs/bytes. Si es alta, la carga es *compute-bound*. Si es baja, es *memory-bound* (por ejemplo, un LLM generando token a token).
**Roofline:** R = min(R_pico, BW·I).

**No determinismo de la GPU:** la suma en coma flotante no es asociativa, así que fijar la semilla no basta. Hay que guardar el **hash de los pesos** desplegados.

**CACE** (*Changing Anything Changes Everything*): si cambias un modelo de aguas arriba, se rompen los que dependen de él. Controles: registro de dependencias, **canary** o **shadow deployment** y rollback.

**Reglamento europeo de IA:** riesgo inaceptable (prohibido, como el *social scoring*) · alto riesgo (médico, empleo, infraestructura crítica; exige supervisión humana) · riesgo limitado (chatbots y deepfakes; exige transparencia) · riesgo mínimo (spam, videojuegos).

---

## Bloque 1.3 — Familias de IA

**Simbólico vs conexionista:** hechos + lógica dan respuestas trazables pero frágiles. Datos + optimización dan pesos estadísticos, menos explicables. Se pueden combinar en una **arquitectura híbrida**.

| Familia | Entrada | Salida | Cuándo usarla |
|---|---|---|---|
| Sistemas de reglas | Hechos + reglas | Conclusión | Auditoría, normas estrictas |
| Búsqueda (A*) | Estados + operadores | Secuencia de acciones | Rutas, planificación |
| Optimización clásica | Función objetivo continua | Óptimo | Función diferenciable |
| Branch & Bound | Problema discreto | Óptimo exacto | Problemas combinatorios pequeños o medianos |
| Metaheurísticas | Soluciones candidatas | Buena aproximación | Espacios enormes con poco tiempo |
| Supervisado | X + etiquetas y | Predicción | Hay etiquetas |
| No supervisado | X sin etiquetas | Clusters | Estructura, segmentación |
| Refuerzo (RL) | Estado, acción, recompensa | Política | Decisiones secuenciales |
| Deep learning | Datos de alta dimensión | Representaciones | Visión, lenguaje |

- **Forward chaining**: va de los hechos a la conclusión. Está dirigido por los datos y sirve para monitorizar.
- **Backward chaining**: va de la hipótesis a los hechos. Está dirigido por objetivos y sirve para diagnosticar.
- **A\***: f(n) = g(n) + h(n). Es óptimo si h es **admisible** (nunca sobreestima: h ≤ h*).
- **Descenso por gradiente:** x ← x − η∇f. Si η es grande oscila; si es pequeña va lento. **Newton** usa el Hessiano: da menos pasos pero cada uno es muy caro.
- **Branch & Bound:** branch, bound, incumbent y prune. En maximización se poda la rama si UB ≤ incumbent. En el peor caso es exponencial.
- **Simulated annealing:** acepta empeorar con probabilidad e^(−ΔE/T). Cuando T → 0 se vuelve codicioso.
- **Algoritmo genético:** población → fitness → selección → cruce → mutación → elitismo.
- **PSO:** v = w·v + c₁r₁(pbest − x) + c₂r₂(gbest − x) (inercia, término cognitivo y término social).
- **ACO:** feromonas τ ← (1−ρ)τ + Δτ. La ρ es la evaporación.
- **Q-learning:** Q ← Q + α[r + γ·max Q(s′,a′) − Q]. Lo que va entre corchetes es el **TD error**. α es la tasa de aprendizaje y γ el factor de descuento.
- **MLP:** z = Wᵀx + b, a = f(z). Sin no linealidad, apilar capas equivale a una sola capa lineal. La **ReLU** es la activación estándar. La sigmoide sufre *vanishing gradient*.
- **Backpropagation** usa la regla de la cadena. Entrenar ocupa más memoria que inferir porque guarda las activaciones.
- **Adam:** guarda dos momentos por parámetro (m y v) con corrección de sesgo, lo que sube el uso de VRAM.
- **Atención:** softmax(QKᵀ/√dₖ)·V. Dividir por √dₖ evita saturar la softmax. El coste es **cuadrático** con la longitud de la secuencia.
- **Temperatura:** con T < 1 la salida es más predecible y con T > 1 más diversa. Una T baja **no garantiza que la respuesta sea correcta**.

---

## Bloque 1.4 — Hardware y dimensionamiento

- **Tiempo ≈ cómputo + movimiento de datos.** Antes de comprar hardware, hay que saber cuál es el cuello de botella.
- **CPU:** pocos núcleos flexibles; buena con lógica irregular y baja latencia. **GPU:** paralelismo masivo con tensores regulares y lotes grandes. **TPU:** matrices densas de baja precisión; es poco flexible.
- **Memory wall:** L1 tarda unos 1 ns, la DRAM unos 100 ns y un SSD unos 100.000 ns. Mover datos cuesta mucha más energía que operar con ellos.
- Patrones de comunicación: **broadcast, scatter, gather, reduction**.
- **Kernel fusion:** junta MatMul + Bias + ReLU en un solo kernel para no ir a la DRAM entre pasos.
- **Speedup** S = T₁/Tₚ · **eficiencia** E = S/p. Lo normal es escalado **sublineal**. Puede ser **superlineal** si cada trozo cabe en la caché.
- **Ley de Amdahl:** S(p) = 1/(f + (1−f)/p). El máximo es **S_max = 1/f**. Por ejemplo, con f = 5 % el máximo es 20×.
- **Pesos:** P·b/8. FP32 → INT8 divide la memoria entre 4. La cuantización es una puerta calidad-recursos: después **hay que revalidar**.
- **Energía** E = P·t. Se comparan **julios por tarea**, no vatios. Ejemplo: GPU 220 W × 8 s = 1.760 J frente a CPU 65 W × 40 s = 2.600 J.

---

## Bloque 1.5 — Estadística básica

- **Centralidad:** media (sensible a extremos), mediana (robusta) y moda (útil en datos categóricos).
- **Dispersión:** varianza (en la muestra se divide entre n−1), desviación típica e **IQR** = Q3 − Q1.
- **Outliers de Tukey:** valores fuera de [Q1 − 1,5·IQR, Q3 + 1,5·IQR]. Primero se investigan; no se borran automáticamente.
- **Regla 68–95–99,7:** porcentaje de datos dentro de ±1σ, ±2σ y ±3σ. Solo vale para distribuciones normales.
- **Forma:** *skewness* (asimetría) y *kurtosis* (peso de las colas).
- **Distribuciones:** normal · log-normal (el logaritmo es normal) · uniforme · **Poisson** (cuenta eventos) · **exponencial** (tiempo entre eventos).
- **Z-score** z = (x−μ)/σ: cambia la escala, **no la forma**. **Min-max:** lleva los datos a [0,1]. Los parámetros se calculan **solo con el conjunto de entrenamiento**; si no, hay leakage.
- **MAE** penaliza los errores de forma lineal. **MSE** los penaliza al cuadrado, así que los errores grandes pesan más. **RMSE** vuelve a las unidades originales. L1 es la distancia Manhattan y L2 la euclídea.
- **Distancias entre distribuciones:** KL (no es simétrica y puede ser infinita) · Wasserstein (*earth mover's*) · **KS** (máxima diferencia entre las funciones de distribución acumuladas).
- **p-value:** probabilidad de obtener un resultado igual o más extremo si H₀ es cierta. Si p < α, se rechaza H₀. Si p ≥ α, **no se demuestra que sean iguales**.
- **Tests:** Mann–Whitney (2 grupos, por rangos) · Kruskal–Wallis (varios grupos, por rangos) · ANOVA (medias) · **Bonferroni** α/m para comparaciones múltiples.
- **Particiones:** train (ajustar) · validation (elegir configuración) · test (estimar al final; se usa una vez). Se estratifica si hay desbalanceo y se divide por tiempo si los datos son temporales.

---

## Bloque 2 — Arquitectura y diseño

- **Cuatro vistas** de la arquitectura: componentes · flujo de datos · composición · despliegue.
- **Data contract:** campos, tipos, unidades, rangos y valores nulos. Rechaza los datos malos en la entrada.
- **Predicción probabilística, acción determinista:** el model service propone y el **policy/rules engine** decide. Si hay timeout o un dato inválido, se usa un **fallback determinista**.
- **Reproducibilidad (6 piezas):** código · datos · split · configuración · entorno · pesos.
- **Pipeline:** problema y ground truth → adquisición → validación → split sin leakage → features → entrenamiento → evaluación → empaquetado → despliegue → monitorización → vuelta al inicio.
  - Distingue **event time** (cuándo ocurrió) de **ingestion time** (cuándo llegó).
  - Tipos de datos ausentes: **MCAR** (completamente al azar) · **MAR** (lo explican otras variables) · **MNAR** (depende del propio valor ausente).
  - Cargar un *pickle* de una fuente no confiable puede ejecutar código.
- **Requisitos no funcionales → límites físicos:** latencia, throughput, memoria y energía. Las latencias se miden con **p95/p99**, no con la media.
- **Data parallelism:** cada dispositivo tiene una copia del modelo y procesa una parte de los datos. **Model/pipeline parallelism:** el modelo se reparte entre dispositivos.
- **Cloud vs Edge:** la cloud es escalable pero depende de la red. El edge tiene latencia local y mejor privacidad, pero recursos limitados.
- **VRAM** ≈ pesos + activaciones + **KV cache** + overhead. Por ejemplo, un modelo de 8B en FP16 ocupa unos 16 GB solo en pesos. La KV cache crece con el contexto y la concurrencia.
- **Acoplamiento:** API síncrona · asíncrona/eventos (reintentos, idempotencia) · **circuit breaker**/fallback.
- **La fiabilidad se multiplica a lo largo de la cadena:** 0,99⁴ ≈ 96 %.
- **Training** (horas o días) vs **inference** (milisegundos) vs **prediction** (una salida concreta).
- **Reentrenamiento:** desde cero · warm start · fine-tuning · incremental (riesgo de olvidar lo aprendido). Un trigger **lanza una evaluación, no un despliegue automático**. Hay que comparar también por **subgrupos** y desplegar con canary.
- **Batch vs online serving.** **Human-in-the-loop** por zonas de confianza: aprobar automáticamente, revisión humana o bloquear.
- **RAG:** recupera documentos y los añade al contexto; **no es fine-tuning**. Se evalúa con **Recall@k** y **Precision@k**. Ejemplo: 4 documentos relevantes, se recuperan 3 y 2 son relevantes → R = 0,5 y P ≈ 0,67.

---

## Bloque 3 — Calidad de datos (caso de bicicletas, 17.379 horas)

- **Estados de los datos:** RAW → VALID → FEATURE. Cada columna tiene un papel: identificador, entrada o target.
- **CHECK → COUNT → DECIDE:** por cada regla se cuentan las filas que la incumplen y se decide una acción. Ejemplos: humedad 0 tratada como dato ausente; viento 0 con un indicador aparte.
- **Valor ausente** (falta el dato) vs **valor imposible** (contradice el dominio).
- **Duplicados:** se definen por la identidad lógica (fecha + hora).
- **Variables cíclicas** como la hora se codifican con sin/cos(2π·h/24), para que la hora 23 quede junto a la 0.
- **Ventanas *trailing*:** solo usan el pasado, para evitar leakage.
- **Exploración:** la distribución tiene sesgo a la derecha, así que la media queda por encima de la mediana. Percentiles. Outliers con **FLAG → EXPLAIN → DECIDE**: los picos reales de las 8:00 y las 17:00 se conservan.
- **Pearson r** ∈ [−1, 1]: mide relación lineal, **no causalidad**. Si r ≈ 0,99, una de las dos variables sobra.
- **Cantidad vs calidad:** la curva de aprendizaje tiene rendimientos decrecientes, y **pocas filas limpias valen más que muchas con ruido** en las etiquetas.
- **Cobertura:** si una categoría tiene menos de 30 filas, se consiguen más datos o la decisión pasa a una persona. Orden de trabajo: **COVER → CLEAN → GROW**.
- **Leakage:** barajar antes del split, usar ventanas centradas o columnas que componen el target. Si el error es sospechosamente bajo, hay que buscar leakage.
- **Sampling bias:** se entrena con datos que no representan el uso real. Ejemplo: entrenar solo con 2011 y probar en 2012.
- **Umbral, ROC y AUC:** al bajar el umbral sube el recall y suben los FP. AUC = 0,5 equivale al azar y AUC = 1 a una separación perfecta. **La métrica sigue a la decisión.**
- **Validación cruzada temporal** (*forward folds*). Barajar da resultados demasiado optimistas.
- **Versionado:** hash SHA-256 de los datos · **DVC** (datos grandes fuera de Git) · **Parquet** (formato columnar, más pequeño y rápido que CSV).

---

## Bloque 4 — Datos masivos (3,3 M de viajes)

- **"Demasiado grande" lo decide la complejidad de la tarea**, no el número de filas. Si duplicas los datos, una tarea O(n) tarda el doble y una O(n²) tarda el cuádruple.
- **Chunks (*out-of-core*):** baja mucho la memoria pico y tarda algo más. Solo sirve para resultados **que se pueden sumar** (la suma sí, la mediana no).
- **PARTITION · PROCESS · COMBINE:** las partes deben ser independientes, combinables y equilibradas. **La partición más lenta marca el ritmo.**
- **Skew:** si una partición concentra la mayoría de los datos, se pierde el speedup. Ejemplo: dividir por estación da 1,4× y dividir por tiempo da 6×.
- **MEASURE · DIVIDE · STOP WHEN E FALLS:** se añaden procesadores mientras la eficiencia E supere un umbral (por ejemplo 0,7). Amdahl: S_max = 1/f.
- **BATCH IF COMPLETE · STREAM IF FRESH:** batch para datos completos aunque lleguen tarde; streaming para resultados en minutos.
- **Ventanas *tumbling*** (franjas fijas sin solapamiento). Se cuenta por **event time**, no por arrival time. Con *allowed lateness* y **replay** del log se corrigen los registros que llegan tarde.
- **Dimensionar un stream:** λ = eventos/s · ancho de banda = λ × bytes por evento · margen. Se dimensiona con la **hora punta**.
- **Lambda:** capa batch + capa speed → capa serving. Tiene dos caminos de código. **Kappa:** un único streaming job y un *replay* del log para recalcular. Si no hace falta respuesta en minutos, basta con batch. Si la lógica no puede ejecutarse en un stream, lambda. En otro caso, kappa.
- **Muestreo:** SE = s/√n. El tamaño de muestra es n = (s/SE)². Multiplicar n por 16 divide el error entre 4.
- **Casos raros:** la probabilidad de que no aparezca ninguno es P = (1−π)ⁿ. Hacen falta muchas más filas, así que se usan todas o se estratifica.
- **Muestreo estratificado:** si se rebalancea la muestra, **siempre hay que reponderar** (w = proporción en la población / proporción en la muestra).
- **SPLIT FIRST · THEN SAMPLE:** el test no se toca nunca.
- **Todas las filas** para totales exactos, facturación, auditorías y casos raros. **Muestra** para medias y percentiles, indicando su error.
- **Surrogate model:** EVALUATE FEW · PREDICT ALL · CONFIRM THE BEST. Un random forest sirve de surrogate y el simulador confirma la mejor configuración.
- **Dos tipos de rendimiento:** calidad (no depende de la máquina) y recursos (dependen de la máquina). Throughput = 1/latencia media.
- **WARM UP · REPEAT · MEDIAN:** la primera llamada es más lenta. Se compara con medianas.
- **Latencia de cola:** los límites se fijan sobre **p99**, no sobre la media. Si una página hace k llamadas, la probabilidad de que alguna sea lenta es P = 1 − (1−q)ᵏ.
- **TEST AGAINST THE NOISE:** se usa Kruskal–Wallis con p < 0,05 y un **control A vs A** que debe dar p ≥ 0,05.
- **Profiling** (cProfile): busca el *hotspot* antes de optimizar. Ejemplo: `iterrows` frente a una operación vectorizada fue unas 3.000 veces más rápido.
- **Complejidad de McCabe** M = decisiones + 1 = número de tests necesarios. Cuentan if, elif, for, while, and y or, pero no else. Conviene dividir una función si M > 10.

---

## Trampas típicas de examen

1. Una accuracy alta con clases desbalanceadas no significa nada. Compárala con el sistema trivial.
2. Un PSI estable **no** descarta el concept drift.
3. Una temperatura baja **no** hace que la respuesta sea correcta.
4. Un p ≥ 0,05 **no** demuestra que sean iguales.
5. El z-score **no** convierte los datos en normales.
6. La cuantización obliga a **revalidar**.
7. Más GPUs no pasan de 1/f (Amdahl).
8. Se comparan julios por tarea, no vatios.
9. La normalización y el muestreo se ajustan **solo con train**. El test no se toca.
10. Un trigger de reentrenamiento lanza una **evaluación**, no un despliegue.
11. RAG ≠ fine-tuning.
12. La latencia se limita con p95/p99, no con la media.
