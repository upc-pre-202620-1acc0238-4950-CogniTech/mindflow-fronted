# MindFlow — App Android

Frontend móvil de MindFlow, la plataforma de bienestar emocional con IA de CogniTech.

## Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **SQLite** (`SQLiteOpenHelper`) para persistencia local
- Arquitectura MVVM: `ui` (pantallas + ViewModels) → `data/repository` → `data/local`

## Pantallas

| Pantalla | Descripción |
|---|---|
| Login | Inicio de sesión con correo y contraseña |
| Registro | Creación de cuenta (nombre, correo, contraseña de mínimo 8 caracteres) |
| Home | Registro de cómo te sientes, categoría, respuesta de MindFlow AI y conversaciones recientes |

## Base de datos local

`mindflow.db` replica las entidades del backend (`mindflow-backend`):

- `users` — id, email, name, password_hash, created_at
- `journal_entries` — id, user_id, content, category, sentiment, ai_response, created_at

La sesión activa se guarda en `SharedPreferences`.

> La respuesta de MindFlow AI se genera localmente (`LocalAiResponder`) hasta conectar el
> endpoint de chat del backend. El acceso con Google también queda pendiente de esa integración.

## Estructura

```
app/src/main/java/com/cognitech/mindflow/
├── data/
│   ├── ai/            LocalAiResponder
│   ├── local/         MindFlowDatabase (SQLite), SessionManager
│   ├── model/         User, JournalEntry
│   └── repository/    AuthRepository, JournalRepository
├── ui/
│   ├── auth/          LoginScreen, RegisterScreen, AuthViewModel
│   ├── home/          HomeScreen, HomeViewModel
│   ├── components/    Botones, campos y chips reutilizables
│   ├── navigation/    AppNavigation
│   └── theme/         Colores y tema
├── MainActivity.kt
└── MindFlowApplication.kt
```

## Cómo ejecutar

1. Abrir la carpeta en Android Studio.
2. Esperar la sincronización de Gradle.
3. Ejecutar `app` en un emulador o dispositivo (Android 7.0+).

Por línea de comandos:

```bash
./gradlew assembleDebug
```
