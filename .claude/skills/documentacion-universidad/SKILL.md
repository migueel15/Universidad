---
name: documentacion-universidad
description: Incorpora PDFs, transcripciones, prácticas y otros materiales académicos a la documentación Markdown de este repositorio, organizándolos por asignatura y tipo y enlazándolos con el contenido existente.
---

# Documentación académica de Universidad

Usa este skill cuando el usuario aporte material académico o pida crear, ampliar, corregir o ubicar contenido en `docs/src` de este repositorio.

## Objetivo

Transforma el material fuente en documentación útil, fiel y enlazada con el conocimiento ya existente. Antes de editar, inspecciona los índices y documentos de la asignatura para conocer su estructura, terminología, profundidad, idioma y referencias. No deduzcas datos que la fuente no permita sostener.

## Flujo

1. Identifica la petición: asignatura, curso, tipo (temario, apuntes, práctica, examen u otro), fecha/número, nivel de transformación y si se debe conservar el original. Infierelos del contexto y de la fuente cuando sea razonable. Si una ambigüedad puede causar una ubicación o interpretación materialmente incorrecta, pregunta; mientras tanto puedes inspeccionar documentación independiente.
2. Lee `docs/zensical.toml`, `docs/src/index.md`, el índice de la asignatura y documentos cercanos. Busca conceptos duplicados, enlaces y convenciones antes de crear otro documento. Si la asignatura aún no existe, sigue [references/estructura.md](references/estructura.md).
3. Extrae y analiza el material completo. Para PDF usa extracción de texto disponible; si las páginas son escaneadas, contienen diagramas importantes o la extracción parece incompleta, renderiza e inspecciona las páginas relevantes. Para transcripciones, recorre la grabación de principio a fin y conserva las explicaciones inteligibles del docente. Elimina muletillas y repeticiones vacías, no las explicaciones, preguntas, ejemplos, matices ni énfasis que aporten contenido. Distingue el contenido de la fuente de las aclaraciones añadidas.
4. Elige ubicación y nombre según [references/estructura.md](references/estructura.md). Conserva el archivo fuente en la ubicación acordada o existente si el usuario solicita archivarlo; no lo muevas ni dupliques sin necesidad. Si solo pide convertirlo, no archives automáticamente una copia del PDF.
5. Redacta Markdown didáctico y completo, no una transcripción literal ni un resumen rápido. Si el usuario pide añadir una transcripción sin pedir resumen, el resultado predeterminado son apuntes de estudio extensos, con longitud proporcional al material y cobertura de todos los temas que se puedan recuperar con fiabilidad. No reduzcas la clase a una introducción y unas pocas ideas clave.
   - Conserva el orden y la numeración académicos reconocibles; agrupa por tema solo cuando mejore la consulta sin perder el recorrido de la clase.
   - Incluye los términos técnicos, definiciones, distinciones, fórmulas, cifras, umbrales, procedimientos, preguntas que introduzcan contenido, ejemplos y advertencias del docente. Conserva también los ejemplos aparentemente informales si ilustran un concepto técnico.
   - No omitas contenido porque ya aparezca en el temario: registra lo que el docente añadió, enfatizó, ejemplificó o corrigió en esa sesión y enlaza el desarrollo previo.
   - Usa tablas para comparar conceptos o recopilar cifras, bloques de código para ejemplos ejecutables y diagramas Mermaid para flujos, capas, decisiones, relaciones o secuencias. Añade visuales cuando ayuden a estudiar; no los uses como sustituto de las explicaciones.
   - Amplía conceptos difíciles con el contexto mínimo necesario, pero señala las aclaraciones editoriales y no las atribuyas al docente. Mantén el idioma y convenciones de la asignatura; en materiales de IA/IoT existentes, los términos técnicos clave suelen conservarse en inglés y explicarse en español.
   - Si una parte es ininteligible, no reconstruyas conjeturas: omítela o indica brevemente la limitación. Ante una cifra o término dudoso por reconocimiento de voz, no lo presentes como seguro; conserva la incertidumbre o contrástalo con material de clase disponible, explicando la diferencia.
6. Añade enlaces relativos a material relacionado y actualiza los índices de la asignatura y sección cuando existan. Si creas una asignatura, enlázala desde el índice general. No cambies configuración de Zensical ni edites `docs/site` (salida generada) para incorporar contenido ordinario.
7. Comprueba rutas, enlaces nuevos, encabezados y cercas Markdown/Mermaid. Resume documentos creados o actualizados, ubicación del material fuente, referencias conectadas y limitaciones de extracción o incertidumbres.

## Criterios editoriales

- Separa hechos de la fuente, contexto complementario y dudas. No inventes fecha, número de práctica, asignatura, autoría, resultados ni referencias bibliográficas.
- Conserva fórmulas, código, tablas y terminología con fidelidad; señala inconsistencias de la fuente en vez de corregirlas silenciosamente.
- Para apuntes de una clase, haz una comprobación de cobertura de toda la fuente antes de cerrar: revisa cada tramo o sección y contrasta sus temas, términos, ejemplos y datos con el documento final. El tamaño del original no justifica convertirlo en un resumen rápido.
- Mantén los detalles cuantitativos aportados en clase, indicando su alcance: distingue valores de ejemplo, estimaciones, umbrales docentes y reglas universales; no conviertas los primeros en los últimos.
- Enlaza fuentes externas solo cuando se hayan consultado y aporten valor; prioriza documentación primaria. No atribuyas a la clase explicaciones añadidas.
- Usa encabezados jerárquicos, listas y tablas con moderación. Para diagramas de esta documentación usa cercas `mermaid` compatibles con la configuración actual.
- Cuando existan formatos previos del mismo tipo y asignatura, úsalos como guía de estilo, sin copiar errores o enlaces obsoletos.
