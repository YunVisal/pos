# Warm-up Questions

Each block's warm-up questions, my answer, and the corrected answer.

## Day 1 — evening

### Q1. What happens when someone calls `GET /api/v1/ping`? What turns `PingResponse` into JSON?

**My answer:** The program finds the function mapped to the endpoint and executes it. I didn't know how `PingResponse` becomes JSON.

**Corrected answer:**
- Spring's `DispatcherServlet` receives every HTTP request. It looks at `@RequestMapping("/api/v1")` + `@GetMapping("/ping")` and calls `ping()`.
- Because the class is a `@RestController`, the return value is written to the response body (not treated as a view name).
- An `HttpMessageConverter` backed by **Jackson** (brought in by the webmvc starter) reads the record's components and writes `{"message":"pong"}`. No hand-written JSON code needed.

### Q2. Name two concrete things `service-template` inherits from `platform-parent` via `<parent>`.

**My answer:** `version` and `java.version`.

**Corrected answer:** Both correct.
- **`version`**: `service-template` has no `<version>` of its own, so it inherits `0.1.0-SNAPSHOT`.
- **`java.version` = 25**: the compiler targets Java 25.
- **The big one I missed: dependency and plugin versions.** `spring-boot-starter-webmvc` and `spring-boot-maven-plugin` have no `<version>` in my POM. The versions come from the `<dependencyManagement>` / `<pluginManagement>` that `platform-parent` inherits from `spring-boot-starter-parent` 4.1.1.
- `<parent>` = inherit settings. `<module>` (in the root `pom.xml`) = which projects get built together. They are separate mechanisms.

## Day 2 — morning

### Q1. (Homework) The `switch` over sealed `PaymentMethod` has no `case Vocher` and no `default`. Does it compile?

**My answer:** No. The switch doesn't cover all permitted records and has no default.

**Corrected answer:** Correct. Because the interface is sealed, the compiler knows every subtype and demands an exhaustive switch. A new payment method later becomes a compile error everywhere it's missing.

### Q2. Fix it with `case Vocher` or `default`?

**My answer:** `case Vocher`, because every implementation should be handled.

**Corrected answer:** Correct. A `default` would also silence the check for the *next* subtype added.

### Q3. What does each layer do for "create a Unit"?

**My answer:** api: endpoints. application: app logic. domain: structure of the data. infrastructure: 3rd parties such as the database.

**Corrected answer:**
- api: right; it also owns the request/response DTOs.
- application: *orchestrates* the use case (load, call domain, save, return). It does not hold business rules.
- domain: the data **and its rules** ("code is 1–10 uppercase chars"), so an invalid `Unit` can't be built.
- infrastructure: right.

### Q4. How does `application` save a Unit without importing `infrastructure`?

**My answer:** Dependency injection of infrastructure.

**Corrected answer:** Half right. DI delivers the object, but the *type* requested must be an interface defined in **domain** (`UnitRepository`), implemented in infrastructure. That's Dependency Inversion (the D in SOLID).

### Q5. On Day 4 we swap in-memory storage for Postgres. Which layers change?

**My answer:** The infrastructure layer.

**Corrected answer:** Correct. Only infrastructure gets a new implementation; domain, application and api are untouched.

### Q6. Layer-first (A) or feature-first (B) packages?

**My answer:** A, because domain and infrastructure code can be reused across features.

**Corrected answer:** B. What changes together sits together; true reuse goes into `platform-*` libraries; feature-first allows package-private infrastructure. See `docs/adr/0001-package-structure-and-layering.md`.

### Q7. Day 4 adds `JpaUnitRepository` (`@Repository`) next to `InMemoryUnitRepository` (`@Repository`). What happens at startup?

**My answer:** Two `UnitRepository` beans → `NoUniqueBeanDefinitionException`. Remove `@Repository` from one.

**Corrected answer:** Correct. In practice we delete the in-memory one. When two implementations are really needed: `@Primary`, `@Qualifier`, `@Profile`, or inject `List<T>` to get all of them. Many beans of one type are fine; only a single-bean injection point must be unambiguous.

### Q8. (Close) What goes wrong if controllers keep returning domain objects?

**My answer:** It will return unnecessary fields.

**Corrected answer:** Right, and it gets worse:
- **Leaks internals:** `id`, `createdBy`, `version`, later `business_id`, all sent to the browser.
- **API is welded to the domain:** rename a field in `Unit` and the POS app and Owner Portal break, even though no one meant to change the API.
- A `UnitResponse` DTO is the contract: the domain can change freely; the mapping in the api layer absorbs it.
