# Technology Stack — City Reports

A study reference for the team, and raw material for the report's *"eingesetzte Technik"*
section. For every technology: **what it is · how it works · why it works that way ·
what it does for us · why we chose it over alternatives.**

Verified against the actual project files (versions current as of this draft).

---

## At-a-glance overview

| Layer | Technology | Version | Role |
|-------|-----------|---------|------|
| Backend language | Java | 17 | Server-side logic |
| Backend framework | Spring Boot | 3.3.5 | Web API + wiring |
| Persistence | Spring Data JPA (Hibernate) | via Boot 3.3.5 | Object ↔ table mapping |
| Dev database | H2 | via Boot | In-memory DB for dev/demo |
| Prod database (target) | PostgreSQL | via Boot | Real relational DB |
| Validation | Jakarta Bean Validation | via Boot | Input checking |
| Security | Spring Security | via Boot | Auth, password hashing |
| Backend build | Maven | IntelliJ-bundled | Build + dependencies |
| Android language | Java | (source compat 11) | App logic |
| Android UI | XML layouts + AndroidX Views | AppCompat 1.6.1, ConstraintLayout 2.1.4, Material 1.10.0 | Screens |
| Android build | Gradle (AGP 9.4.1) | wrapper | Build + dependencies |
| Web framework | React | 19 | Staff dashboard UI |
| Web build/dev server | Vite | 8 | Dev server + bundler |
| Web linter | oxlint | 1 | Static code checks |
| Maps (planned) | Leaflet + OpenStreetMap | — | Location display/picker |
| Architecture style | Layered + REST client-server | — | Overall shape |

---

## Backend

### Java 17
- **What:** A statically-typed, compiled, object-oriented language running on the JVM.
- **How:** Source compiles to bytecode; the JVM executes it (and JIT-compiles hot paths to native code). Strong typing catches many errors at compile time.
- **Why that way:** "Write once, run anywhere" via the JVM; the type system and mature tooling favour large, maintainable codebases.
- **For us:** The backend language. Version 17 is a Long-Term-Support release and is the version Android's toolchain plays well with (why we pinned 17, not newer).
- **Chosen over:** Mandated by the professor (Java and/or JavaScript only). No real alternative for the backend.

### Spring Boot 3.3.5
- **What:** A framework that turns Java into a productive web-application platform. It bundles an embedded web server (Tomcat), dependency injection, and "starters" that pre-wire common tech.
- **How:** **Auto-configuration** — Boot inspects which libraries are on the classpath and configures sensible defaults automatically (see a JPA starter → it sets up a datasource + ORM). **Dependency Injection / Inversion of Control** — you annotate classes (`@RestController`, `@Service`, `@Repository`) and Spring instantiates and connects them for you, as singletons, instead of you writing `new` everywhere.
- **Why that way:** Convention over configuration. It removes the mountain of boilerplate that plain Java web apps needed, so a small team ships features instead of plumbing.
- **For us:** Hosts our REST API (`/api/reports`, `/api/auth/login`), wires the controllers → services → repositories, runs the embedded server on port 8080 — no separate server to install.
- **Chosen over:** Plain Java Servlets / Jakarta EE (far more boilerplate, more manual setup) and Quarkus/Micronaut (capable, but Spring Boot has the largest community and the most tutorials, which matters for a student team — when stuck, the answer exists online).

### Spring Data JPA + Hibernate
- **What:** An **ORM** (Object-Relational Mapping) layer. JPA is the Java standard/specification; Hibernate is the implementation Boot uses.
- **How:** You write plain Java entity classes (`Report`, `User`, `Photo`) annotated with `@Entity`; Hibernate maps each class to a table and each field to a column, and generates the SQL for you. "Repository" interfaces (e.g. `ReportRepository extends JpaRepository`) give you CRUD methods for free — you declare a method name like `findByStatus(...)` and Spring Data writes the query.
- **Why that way:** Lets you think in objects, not SQL strings; reduces hand-written, error-prone SQL; and abstracts the specific database so you can swap H2 for PostgreSQL without code changes.
- **For us:** Our three entity classes *are* the database schema (`ddl-auto=update` makes Hibernate create/update the tables from them). This is how we "programmed the DB" — no hand-written SQL.
- **Chosen over:** Raw JDBC (manual SQL + manual result mapping, verbose and error-prone) or MyBatis (more SQL control, but more work). JPA is the standard taught in most SE courses and is the Spring default.

### H2 (development / demo database)
- **What:** A lightweight relational database written in Java that can run **in-memory** (inside the app's own process).
- **How:** When the app starts, Spring launches H2 in RAM (`jdbc:h2:mem:cityreports`). Hibernate creates the tables; data lives in memory and disappears when the app stops. A web console (`/h2-console`) lets you inspect it.
- **Why that way:** Zero setup — no database server to install, configure, or host. Ideal for development and for a demo on a single machine.
- **For us:** The **default** DB. It's why the app "just runs" after clicking Run, and why the demo needs nothing hosted anywhere. (For the demo we can switch to H2 *file-mode* so seeded data survives restarts.)
- **Chosen over:** Running PostgreSQL locally during dev (needs install + a running server — friction we avoided) or SQLite (fine, but H2 integrates more naturally with the JPA/Spring ecosystem and speaks standard SQL).

### PostgreSQL (production target — configured, not required to run)
- **What:** A powerful, open-source, production-grade relational database server.
- **How:** Runs as a separate server process; apps connect over the network (`jdbc:postgresql://localhost:5432/...`). We have a dedicated Spring profile (`application-postgres.properties`) that points the app at it.
- **Why that way:** Real deployments need a persistent, concurrent, robust DB — H2-in-memory isn't that. Postgres is the standard open-source choice.
- **For us:** The "if this went live" target. We do **not** run it for the project. Its value is demonstrating that our app is **database-agnostic via JPA** — switching from H2 to PostgreSQL is a one-line profile change, **no code changes**. That's a clean architecture talking point.
- **Chosen over:** MySQL/MariaDB (comparable; Postgres is generally preferred for standards-compliance and features) or a cloud DB (overkill, costs money, needs hosting).

### Jakarta Bean Validation
- **What:** A standard for declaring input rules with annotations (`@NotNull`, `@Size`, etc.).
- **How:** You annotate DTO fields; Spring validates incoming request bodies automatically and rejects bad input with HTTP 400 before it reaches your logic.
- **Why that way:** Declarative validation keeps rules next to the data and out of the business logic.
- **For us:** Guards the API — e.g. a report submission missing a required field is rejected cleanly.
- **Chosen over:** Hand-written `if` checks in every controller (repetitive, easy to forget).

### Spring Security + BCrypt
- **What:** The framework's security layer; BCrypt is a password-hashing algorithm.
- **How:** Spring Security filters every HTTP request through a configurable chain (our `SecurityConfig`). Passwords are stored as BCrypt hashes (one-way, salted), never plaintext. Staff auth is planned via JWT tokens.
- **Why that way:** Security is hard to get right by hand; a vetted framework handles the dangerous details. BCrypt is deliberately slow to resist brute-force attacks.
- **For us:** Currently scaffold-stage (everything permitted so clients can call stubs during parallel dev); will enforce the public (citizen) vs staff split once JWT auth lands.
- **Chosen over:** Rolling your own auth (a classic security mistake) or plaintext/weak hashing (unacceptable).

### Maven (backend build tool)
- **What:** A build-and-dependency-management tool for Java.
- **How:** You declare dependencies and build config in `pom.xml` (declarative XML). Maven downloads the libraries, compiles, tests, and packages the app, following a fixed lifecycle.
- **Why that way:** Declarative and predictable — the same `pom.xml` builds the same way everywhere.
- **For us:** Resolves all the Spring libraries and builds the backend. IntelliJ's bundled Maven runs it (nothing installed system-wide).
- **Chosen over:** **Gradle** (which we *do* use for Android). Maven is Spring's documentation default and uses boring, predictable XML — better for a student team than Gradle's more powerful but trickier build-as-code. (Note: Android *must* use Gradle — Google ships it that way — so the project uses both, each in its own component. That's standard for a hybrid app, not an inconsistency.)

---

## Android

### Java (Android app language)
- **What / why:** Same language as the backend, mandated by the professor (no Kotlin allowed). Shared language across backend + Android is a small bonus.
- **For us:** All Activity logic and the backend HTTP calls are written in Java.
- **Note for the defense:** The Gradle build sets Java source/target compatibility to 11 (an Android toolchain default); our code is plain Java. A library called `activity-ktx` appears in the dependency list — it's a Kotlin-flavoured AndroidX utility pulled in transitively by the template; **our own code contains no Kotlin** (verified: the build reports `compileDebugKotlin NO-SOURCE`).

### XML layouts + AndroidX Views
- **What:** The classic Android UI approach — screens defined in XML layout files, driven by Java `Activity` classes. AndroidX provides the widget libraries: **AppCompat** (backward-compatible UI), **ConstraintLayout** (flexible positioning), **Material Components** (Google's design widgets).
- **How:** An XML file (`activity_main.xml`) declares the view tree; the Activity inflates it with `setContentView(...)` and wires behaviour via `findViewById`. This is the "Empty Views Activity" template.
- **Why that way:** Separates UI structure (XML) from behaviour (Java) — and XML pairs naturally with Java.
- **For us:** Our screens (the connection-test screen now; the report-submission + map screens later). We deliberately picked **Views, not Jetpack Compose**, because Compose is Kotlin-first and awkward in pure Java — Views is the correct choice given the no-Kotlin rule, and it reuses the XML the team already learned.
- **Chosen over:** **Jetpack Compose** (modern, but effectively requires Kotlin — ruled out).

### Gradle (Android build tool, AGP 9.4.1)
- **What:** Android's build system (the Android Gradle Plugin on top of Gradle).
- **How:** Build logic in `build.gradle.kts` files; a version catalog (`libs.versions.toml`) centralises dependency versions. Gradle compiles code, processes resources, and packages the APK. The **Gradle wrapper** (`gradlew`) pins the exact Gradle version so every machine builds identically — this is why a teammate can just pull and build.
- **Why that way:** Android's entire toolchain (SDK, resource compiler, APK packaging) is built on Gradle — it's not optional for Android.
- **For us:** Builds and packages the Android app; `minSdk 24` (supports ~95% of devices).
- **Chosen over:** Nothing — Gradle is mandatory for Android.

---

## Web

### React 19
- **What:** A JavaScript **library** for building user interfaces from reusable components.
- **How:** You describe UI as components that return markup (JSX, which compiles to plain JavaScript). React keeps a **virtual DOM** and, when your data (*state*) changes, efficiently updates only the parts of the real page that changed.
- **Why that way:** Declarative UI ("describe what the screen should look like for this state") is easier to reason about than manually poking the DOM, especially for a stateful dashboard (list → filter → detail → edit).
- **For us:** The staff dashboard — fetching and displaying reports, and later filters/detail/edit forms.
- **Defense point:** React is a **JavaScript library** — it compiles to plain JS, so it satisfies the professor's "JavaScript" requirement. The code we write is JavaScript/JSX.
- **Chosen over:** Plain vanilla JS (simpler, but weaker for a stateful multi-view dashboard) or Angular/Vue (Angular is heavier and TypeScript-centric; Vue is fine but React has the largest ecosystem). We weighed vanilla JS seriously; React won on fit for a dashboard with shared state.

### Vite 8
- **What:** A fast build tool + development server for modern web projects.
- **How:** In dev it serves files over native ES modules with **hot module replacement** (edits appear instantly without a full reload); for production it bundles/minifies via Rollup.
- **Why that way:** Near-instant dev feedback and a clean dev/build split, without heavy configuration.
- **For us:** `npm run dev` runs the dashboard on `localhost:5173`; `npm run build` produces the deployable bundle.
- **Chosen over:** Create-React-App (deprecated, slow) or Webpack (powerful but config-heavy). Vite is the current standard.

### oxlint
- **What:** A fast JavaScript/React linter (static analysis for JS).
- **How:** Scans source for likely bugs and style issues without running it.
- **For us:** Catches mistakes early in the web code — and counts toward the *Werkzeugeinsatz* (tooling) grading dimension.

---

## Cross-cutting concepts

### REST API (client–server contract)
- **What:** The backend exposes resources over HTTP (`GET/POST/PUT/DELETE /api/reports`); both clients (Android, Web) talk to it the same way, exchanging JSON.
- **Why:** One backend, two front-ends — a shared, language-neutral contract lets the native Android app and the React web app reuse the exact same server. This is the backbone of the hybrid architecture.

### Layered architecture + patterns we use
- **Layers:** `Controller` (HTTP) → `Service` (business logic) → `Repository` (data) → DB. Each layer talks only to the next, keeping concerns separated.
- **Design patterns already present** (worth naming in the architecture section): **Singleton** (Spring beans are singleton-scoped), **Dependency Injection / Inversion of Control** (Spring wiring), **Repository / DAO** (Spring Data), **Layered architecture**. We recognise patterns the framework already applies rather than bolting on contrived ones.

### Maps: Leaflet + OpenStreetMap (planned)
- **What:** OpenStreetMap is free, open map data; Leaflet is a lightweight JS library to display interactive maps.
- **Why:** Reports carry a map location (core requirement); the brief explicitly recommends this pair. Free, no API key, no usage limits (vs. Google Maps).
- **For us:** Web dashboard shows report markers on a map; Android lets citizens pick a location. (Android may use a native map component instead of Leaflet — to be decided when we build the picker.)
