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
3. Extrae y analiza el material. Para PDF usa extracción de texto disponible; si las páginas son escaneadas, contienen diagramas importantes o la extracción parece incompleta, renderiza e inspecciona las páginas relevantes. Para transcripciones, elimina muletillas sin borrar explicaciones, preguntas, ejemplos o matices del docente. Distingue el contenido de la fuente de las aclaraciones añadidas.
4. Elige ubicación y nombre según [references/estructura.md](references/estructura.md). Conserva el archivo fuente en la ubicación acordada o existente si el usuario solicita archivarlo; no lo muevas ni dupliques sin necesidad. Si solo pide convertirlo, no archives automáticamente una copia del PDF.
5. Redacta Markdown didáctico, no una transcripción literal ni un resumen telegráfico salvo que se solicite. Conserva el orden y numeración académicos reconocibles. Amplía conceptos difíciles, incluye ejemplos reproducibles en bloques de código cuando ayuden y diagramas Mermaid cuando aclaren flujos, capas, relaciones o secuencias. Mantén el idioma y convenciones de la asignatura; en materiales de IA/IoT existentes, los términos técnicos clave suelen conservarse en inglés y explicarse en español.
6. Añade enlaces relativos a material relacionado y actualiza los índices de la asignatura y sección cuando existan. Si creas una asignatura, enlázala desde el índice general. No cambies configuración de Zensical ni edites `docs/site` (salida generada) para incorporar contenido ordinario.
7. Comprueba rutas, enlaces nuevos, encabezados y cercas Markdown/Mermaid. Resume documentos creados o actualizados, ubicación del material fuente, referencias conectadas y limitaciones de extracción o incertidumbres.

## Criterios editoriales

- Separa hechos de la fuente, contexto complementario y dudas. No inventes fecha, número de práctica, asignatura, autoría, resultados ni referencias bibliográficas.
- Conserva fórmulas, código, tablas y terminología con fidelidad; señala inconsistencias de la fuente en vez de corregirlas silenciosamente.
- Enlaza fuentes externas solo cuando se hayan consultado y aporten valor; prioriza documentación primaria. No atribuyas a la clase explicaciones añadidas.
- Usa encabezados jerárquicos, listas y tablas con moderación. Para diagramas de esta documentación usa cercas `mermaid` compatibles con la configuración actual.
- Cuando existan formatos previos del mismo tipo y asignatura, úsalos como guía de estilo, sin copiar errores o enlaces obsoletos.
