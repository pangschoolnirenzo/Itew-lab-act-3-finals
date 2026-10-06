# TaskNote Cloud Sync (Week 14/15 Lab)

Offline-first task app: Compose UI -> ViewModel -> Repository -> Room (single source of truth) + Retrofit (MockAPI.io).

## Before you run
1. Create a MockAPI.io project with a `tasks` resource: `id`, `title` (String), `note` (String), `is_done` (Boolean), `created_at` (Number). Add 3-5 sample tasks.
2. Open `data/remote/NetworkModule.kt` and replace `<your-id>` in `BASE_URL` (must end with `/`).
3. Sync Gradle, run on an emulator/device.

## Packages
- `data/local` Room (TaskEntity, TaskDao, TaskDatabase) | `data/remote` Retrofit (TaskDto, TaskApiService, NetworkModule)
- `data/mapper` DTO <-> Entity <-> Domain | `data/repository` TaskRepository (only class touching Room/API)
- `domain/model` Task | `ui/tasks` ViewModel, UiState, screens | `util` AppResult, AppError, safeApiCall | `di` AppContainer

## Report answers (guide questions)
- **Why a Migration in a real app?** `fallbackToDestructiveMigration()` wipes the DB on a version bump. Fine for a re-downloadable cache, but a real app with local-only data (unsynced edits, drafts) would lose user data. A `Migration` alters the schema in place and keeps it.
- **Why Level.BODY off in release?** It logs full request/response bodies, which can leak personal data/tokens to Logcat and slows the app.
- **Why catch order matters in safeApiCall?** `SocketTimeoutException` is a subclass of `IOException`. Catch clauses are checked top to bottom, so if `IOException` came first, timeouts would be reported as "No internet".
- **Serialization test without `ignoreUnknownKeys`:** run the test and paste the `SerializationException` (unknown key `extra`) into your report.
