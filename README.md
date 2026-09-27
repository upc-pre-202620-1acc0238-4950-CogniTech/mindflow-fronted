# MindFlow — App Android

Frontend móvil de MindFlow, la plataforma de bienestar emocional con IA de CogniTech.
Implementa los mockups móviles del Figma (página 2) respetando su paleta, tipografía y espaciados.

## Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **SQLite** (`SQLiteOpenHelper`) para persistencia local
- Arquitectura MVVM: `ui` (pantallas + ViewModels) → `data/repository` → `data/local`
- Tipografía **Inter** (Regular, Medium, SemiBold, Bold, Italic) incluida en `res/font`

## Pantallas

| Pantalla | Descripción |
|---|---|
| Login | Inicio de sesión con correo y contraseña |
| Registro | Creación de cuenta (nombre, correo, contraseña de mínimo 8 caracteres) |
| Dashboard | Registro emocional con categoría, respuesta de MindFlow AI, conversaciones recientes, resumen semanal, intervenciones rápidas (respiración 4-7-8 y micro-meditación) y hábitos diarios |
| Diario (Journal) | Historial emocional con búsqueda, filtros por categoría/estado/fecha y calendario coloreado por sentimiento |
| Hábitos | Mis rutinas, sugerencias de IA e historial. Progreso diario, creación de hábitos y pausa automática cuando la IA detecta estrés |
| Analíticas | Resumen semanal, métricas del mes, fluctuación emocional, nube de palabras, tendencia de ánimo y exportación de reportes (PDF/CSV, Premium) |
| Configuración | Perfil, privacidad (PIN, modo oscuro, recordatorios), suscripción, soporte y eliminación de cuenta |
| Planes | Freemium y Premium |

## Paleta (tokens del Figma)

| Token | Hex | Uso |
|---|---|---|
| Mine Shaft | `#2F2F2F` | Texto principal |
| Gray | `#828282` | Texto secundario |
| Boulder | `#757575` | Placeholders |
| Mercury | `#E5E5E5` | Bordes |
| Catskill White | `#F5F7FA` | Fondos de campos y pantalla |
| Cornflower Blue | `#4F8DF5` | Primario / enlaces |
| Downy | `#6ED3A3` | Secundario / positivo |
| Golden Tainoi | `#FFD166` | Neutral |
| Vivid Tangerine | `#FF8A8A` | Negativo |
| Zest / Serenade | `#E28F22` / `#FFF4E5` | Rachas y alertas |
| Portage | `#8A7CF6` | Premium |
| Sunset Orange | `#FF4A4A` | Zona de peligro |

Degradado de marca: `#4F8DF5 → #6ED3A3`.

## Base de datos local

`mindflow.db` replica las entidades del backend (`mindflow-backend`):

- `users` — id, email, name, password_hash, occupation, timezone, plan, created_at
- `journal_entries` — id, user_id, title, content, category, sentiment, ai_response, created_at
- `habits` — id, user_id, name, category, frequency, created_at
- `habit_logs` — id, habit_id, date (la racha se calcula con días consecutivos)

La sesión activa y las preferencias se guardan en `SharedPreferences`.

## Integración con el backend (pendiente)

La app funciona 100% offline. Para conectarla con `mindflow-backend` (ASP.NET, JSON en `snake_case`, JWT `Bearer`):

| App | Endpoint del backend |
|---|---|
| Login / Registro | `POST /api/v1/users/sign-in`, `POST /api/v1/users/sign-up` |
| Perfil | `GET/PUT /api/v1/users/profile`, `DELETE /api/v1/users` |
| Journal | `GET/POST /journal/entries`, `POST /journal/entries/sync` (offline-first con `client_id`) |
| Hábitos | `GET/POST /habits`, `POST /habit-logs`, `POST /habits/suggestions` |
| MindFlow AI | `POST /chat/conversations` (Gemini) — hoy lo reemplaza `LocalAiResponder` |
| Estrés | `POST /wellness/stress-check` — hoy se calcula localmente |

El backend no tiene URL pública todavía; en local (`dotnet run`) el emulador lo alcanza en `http://10.0.2.2:5000`.

## Estructura

```
app/src/main/java/com/cognitech/mindflow/
├── data/
│   ├── ai/            LocalAiResponder (sentimiento, títulos, respuestas)
│   ├── export/        ReportExporter (PDF / CSV)
│   ├── local/         MindFlowDatabase (SQLite), SessionManager
│   ├── model/         User, JournalEntry, Habit, HabitLog
│   └── repository/    AuthRepository, JournalRepository, HabitRepository
├── ui/
│   ├── auth/          Login, Registro
│   ├── home/          Dashboard
│   ├── journal/       Diario
│   ├── habits/        Hábitos
│   ├── analytics/     Analíticas
│   ├── settings/      Configuración
│   ├── plans/         Planes
│   ├── components/    Menú lateral, header, botones, campos, tarjetas
│   ├── common/        Formateo de fechas y sentimientos
│   ├── navigation/    AppNavigation
│   └── theme/         Colores, tipografía y tema
├── MainActivity.kt
└── MindFlowApplication.kt
```

## Cómo ejecutar

1. Abrir la carpeta en Android Studio.
2. Esperar la sincronización de Gradle.
3. Ejecutar `app` en un emulador o dispositivo (Android 8.0+).

Por línea de comandos:

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
