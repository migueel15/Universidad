import { defineConfig } from 'vitepress'
import { withMermaid } from 'vitepress-plugin-mermaid'
import { pagefindPlugin } from 'vitepress-plugin-pagefind'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const configDir = path.dirname(fileURLToPath(import.meta.url))
const configuredBase = process.env.VITEPRESS_BASE ?? '/'
const base = configuredBase.endsWith('/') ? configuredBase : `${configuredBase}/`

const link = (text: string, link: string) => ({ text, link })

export default withMermaid(defineConfig({
  title: 'Universidad',
  description: 'Uni docs',
  lang: 'es-ES',
  base,
  cleanUrls: true,
  outDir: path.resolve(configDir, '../../site'),
  appearance: true,
  head: [
    ['link', { rel: 'manifest', href: `${base}manifest.webmanifest` }],
    ['link', { rel: 'apple-touch-icon', href: `${base}icons/apple-touch-icon.png` }],
    ['meta', { name: 'theme-color', content: '#1e1e2e' }],
    ['meta', { name: 'mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black-translucent' }],
  ],
  markdown: {
    math: true,
    config(md) {
      md.core.ruler.before('inline', 'tex-delimiters', (state) => {
        for (const token of state.tokens) {
          if (token.type !== 'inline') continue
          token.content = token.content
            .replace(/\\\(([\s\S]+?)\\\)/g, (_, tex: string) => `$${tex}$`)
        }
      })
    },
  },
  vite: {
    optimizeDeps: {
      include: ['mermaid', 'fastdom', 'fastdom/extensions/fastdom-promised.js'],
    },
    plugins: [pagefindPlugin({
      btnPlaceholder: 'Buscar',
      placeholder: 'Buscar en la documentación…',
      emptyText: 'No se encontraron resultados',
      heading: '{{searchResult}} resultados',
    })],
  },
  themeConfig: {
    nav: [
      link('Inicio', '/'),
      link('Inteligencia Artificial', '/IA/'),
      link('Internet de las Cosas', '/IoT/'),
    ],
    sidebar: {
      '/IA/': [
        { text: 'Asignatura', collapsed: false, items: [link('Resumen', '/IA/')] },
        { text: 'Temario', collapsed: false, items: [
          link('1.1 Requisitos de software basado en IA', '/IA/temario/bloque-1/1-1-requisitos-de-software-basado-en-ia'),
          link('1.2 Retos en aplicaciones basadas en IA', '/IA/temario/bloque-1/1-2-retos-en-el-desarrollo-de-aplicaciones-basadas-en-ia'),
          link('1.3 Familias de inteligencia artificial', '/IA/temario/bloque-1/1-3-familias-de-inteligencia-artificial'),
          link('1.4 Plataformas hardware', '/IA/temario/bloque-1/1-4-plataformas-hardware-y-dimensionamiento-basico'),
          link('1.5 Conceptos de estadística', '/IA/temario/bloque-1/1-5-conceptos-basicos-de-estadistica'),
          link('Bloque 2 · Arquitectura y diseño', '/IA/temario/bloque-2/2-arquitectura-y-diseno'),
          link('Bloque 3 · Calidad de datos', '/IA/temario/bloque-3/3-calidad-de-datos'),
        ] },
        { text: 'Notas de clase', collapsed: false, items: [
          link('Índice', '/IA/notas-clase/'),
          link('14 oct 2026 · Calidad de datos', '/IA/notas-clase/clase-2026-10-14'),
          link('13 oct 2026 · Operación en producción', '/IA/notas-clase/clase-2026-10-13'),
          link('7 oct 2026 · Sistemas de IA', '/IA/notas-clase/clase-2026-10-07'),
          link('23 sep 2026 · Estadística y datos', '/IA/notas-clase/clase-2026-09-23'),
          link('18 sep 2026 · Búsqueda y optimización', '/IA/notas-clase/clase-2026-09-18'),
        ] },
      ],
      '/IoT/': [
        { text: 'Asignatura', collapsed: false, items: [link('Resumen', '/IoT/')] },
        { text: 'Temario', collapsed: false, items: [
          link('Bloque 1 · Evolución de los sistemas distribuidos', '/IoT/temario/bloque-1-evolucion-de-los-sistemas-distribuidos'),
          link('Bloque 2 · Elasticidad y escalabilidad', '/IoT/temario/bloque-2-elasticidad-y-escalabilidad'),
        ] },
        { text: 'Notas de clase', collapsed: false, items: [
          link('Índice', '/IoT/notas-clase/'),
          link('14 oct 2026 · Comunicación colectiva', '/IoT/notas-clase/clase-2026-10-14'),
          link('7 oct 2026 · Sharding y replicación', '/IoT/notas-clase/clase-2026-10-07'),
          link('30 sep 2026 · Consistencia', '/IoT/notas-clase/clase-2026-09-30'),
          link('16 sep 2026 · Comunicación en red', '/IoT/notas-clase/clase-2026-09-16'),
          link('14 sep 2026 · Introducción', '/IoT/notas-clase/clase-2026-09-14'),
        ] },
        { text: 'Repaso', collapsed: false, items: [link('Preguntas', '/IoT/preguntas')] },
      ],
    },
    outline: { label: 'En esta página', level: [2, 3] },
    socialLinks: [{ icon: 'github', link: 'https://github.com/migueel15/Universidad' }],
  },
}))
