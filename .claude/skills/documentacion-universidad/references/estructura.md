# Estructura de la documentación académica

## Estado actual del repositorio

Zensical está configurado en `docs/zensical.toml`, con `docs_dir = "src"` y `site_dir = "site"`. El contenido fuente vive en `docs/src`; `docs/site` es salida generada y no debe editarse manualmente. Mermaid está habilitado mediante `pymdownx.superfences`.

Actualmente existen las asignaturas `IA/` e `IoT/`. Cada una tiene `index.md`; los apuntes están en `notas-clase/` con índice propio, nombres fechados `clase-AAAA-MM-DD.md`, y temario en `temario/`. IA organiza el temario en `temario/bloque-1/`; IoT conserva un documento de bloque directamente en `temario/`. Sigue la organización establecida en cada asignatura antes de normalizarla.

## Estructura objetivo para nuevas asignaturas

Usa nombres cortos y estables para carpetas (por ejemplo, `IA`, `IoT`); evita crear variantes para una misma asignatura. Crea solo las secciones necesarias y enlázalas desde los índices.

```text
docs/src/
├── index.md
└── <Asignatura>/
    ├── index.md
    ├── temario/
    │   ├── index.md
    │   └── bloque-1/
    ├── notas-clase/
    │   ├── index.md
    │   └── clase-AAAA-MM-DD.md
    ├── practicas/
    │   ├── index.md
    │   └── practica-01-<tema>.md
    ├── ejercicios/
    │   ├── index.md
    │   └── ejercicios-<tema>.md
    ├── examenes/
    │   ├── index.md
    │   └── examen-AAAA-convocatoria.md
    ├── proyectos/
    │   ├── index.md
    │   └── <proyecto>/
    ├── recursos/
    │   ├── index.md
    │   └── bibliografia.md
    └── fuentes/
        └── <material-original>.pdf
```

Es una guía, no una obligación de crear todas las carpetas. Para proyectos grandes puede convenir `proyectos/<nombre>/` con requisitos, diseño, implementación, entregas y referencias propias.

## Ubicación por tipo

| Contenido | Ubicación preferida | Nombre sugerido |
| --- | --- | --- |
| Desarrollo organizado por bloques/unidades | `temario/` o estructura ya existente | `bloque-1-<tema>.md` o `1-1-<tema>.md` |
| Clase o transcripción de clase | `notas-clase/` | `clase-AAAA-MM-DD.md`; sin fecha: `clase-sin-fecha-<tema>.md` |
| Enunciado y solución comentada de práctica | `practicas/` | `practica-01-<tema>.md` |
| Hoja de problemas o colección de ejercicios | `ejercicios/` | `ejercicios-<tema>.md` |
| Examen, modelo o convocatoria | `examenes/` | `examen-AAAA-<convocatoria>.md` |
| Trabajo longitudinal | `proyectos/<nombre>/` | documentos descriptivos con índice local |
| Bibliografía, enlaces docentes, normativa o apoyo | `recursos/` | nombre temático descriptivo |
| Original aportado que el usuario desea conservar | `fuentes/` o convención ya existente | conservar nombre útil y extensión |

Si un PDF es un conjunto de diapositivas del temario, el Markdown elaborado pertenece a `temario/`; el original solo se archiva en `fuentes/` si el usuario quiere conservarlo o la asignatura ya archiva originales.

## Forma de los documentos

### Notas de clase

```markdown
# Apuntes de clase — <día y fecha>

## <tema principal>

> **Contexto:** <asignatura, curso y fuente, si se conocen>

## <concepto>
Explicación basada en el material, con aclaraciones claramente integradas.

### Ejemplo
...

## Ideas clave
- ...

## Referencias relacionadas
- [Tema relacionado](../temario/...md)
```

Usa el día de la semana solo si se ha verificado. El bloque de contexto es opcional y breve.

### Prácticas

Registra objetivo, requisitos previos, enunciado, procedimiento, código o comandos, resultados observados, explicación y preguntas de reflexión. Separa el enunciado original de una solución propuesta. No inventes resultados ni afirmes haber probado código si no se ejecutó.

### Temario

Preserva unidades y numeración de la asignatura, desarrolla definiciones y relaciones entre conceptos, añade ejemplos y un cierre con ideas clave cuando ayude. Evita crear un capítulo si el material encaja en uno existente.

### Exámenes y soluciones

Indica año y convocatoria solo cuando consten. Distingue examen original, resolución y comentarios. No presentes respuestas inferidas como solución oficial.

## Índices y enlaces

- `docs/src/index.md` enumera las asignaturas.
- `<Asignatura>/index.md` resume la asignatura y enlaza temario, apuntes y otras secciones presentes.
- Cada carpeta de sección puede tener `index.md` con enlaces cronológicos o temáticos.
- Usa enlaces relativos a `.md`; verifica que el destino exista y evita rutas a `docs/site`.
- Al añadir contenido, actualiza el índice más cercano y el de asignatura si mantiene un listado directo.

## Mermaid y código

Usa Mermaid cuando ayude a comprender dependencias, arquitectura, capas, estados o secuencias. Mantén diagramas pequeños y etiquetados con claridad. Indica el lenguaje en los bloques de código (`python`, `bash`, `c`, etc.) y explica su propósito y resultado esperado; no inventes salidas.
