# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
npm run dev      # Start Quasar dev server with hot-reload
npm run build    # Production build to dist/spa
npm run lint     # ESLint on .js, .ts, .vue files
npm run format   # Prettier formatting
```

No test suite is configured.

## Architecture

This is a **Vue 3 + Quasar 2** single-page application (SPA) for a personal CV/resume, built with **Vite** and **TypeScript**.

### Key Patterns

- **Composition API** with `<script setup>` syntax throughout
- **No state management library** — components use local `ref()` state
- **Hash-based routing** (configured in `quasar.config.js`)
- **Direct fetch calls** to a backend API at `http://localhost:8080/v1/curriculum-vitae/` — no API abstraction layer
- **Quasar utility classes** for layout/styling (minimal custom CSS)
- **SCSS** for styles, with Quasar theme variables in `src/css/quasar.variables.scss`

### Data Flow

`MainPage.vue` fetches all data in `onBeforeMount` from three endpoints:
- `/academic-studies` → `AcademicStudy[]`
- `/work-experiences` → `WorkExperience[]`
- `/generic-info` → `GenericInfo`

Data is passed as props to section components (`AcademicStudiesSection`, `WorkExperiencesSection`).

### Type Definitions

All data model interfaces are in `src/components/types/models.ts`.

## Code Style

- Single quotes, semicolons (configured in `.prettierrc`)
- 2-space indentation, LF line endings
- PascalCase for component filenames and names
- ESLint extends `@typescript-eslint/recommended`, `plugin:vue/vue3-essential`, and `prettier`
