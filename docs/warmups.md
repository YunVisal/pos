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

### Q9. (Day 2 evening warm-up) Explain the `Unit` feature in two sentences: what each layer does, which way dependencies point, and why `UnitRepository` lives in `domain`.

**My answer:** api is the request/response point, application holds each use case, domain holds the data model and rules, infrastructure talks to storage. `UnitRepository` lives in domain because it contains business rules and application logic.

**Corrected answer:** Layer roles correct. Missing: dependencies point **inward**: api → application → domain ← infrastructure. `UnitRepository` holds no logic. It is a **port**: the domain says "I need to save and find units" in its own terms, and infrastructure implements it. That is Dependency Inversion: the domain never imports infrastructure, so swapping `InMemoryUnitRepository` for JPA on Day 4 doesn't touch domain or application code.

### Q10. (Day 2 evening break-it) `CurrencyController` calls `CurrencyRepository.findAll()` directly. It compiles, Spring starts, and all tests pass. Why does review still reject it?

**My prediction (compile / tests / start):** all yes, "because `CurrencyRepository` has a bean that implements it".

**Corrected prediction:** all yes, but for three separate reasons. Compile: the interface is `public`, and the compiler checks only visibility. Tests: the HTTP response is the same JSON. Start: exactly one `CurrencyRepository` bean. **Nothing in the toolchain stops a layering violation.**

**My answer (why reject):** api should not access infrastructure directly; each controller needs its own use case.

**Corrected answer:** The controller depends on the **domain port**, not on infrastructure (that's why it compiled). The violation is that **api skipped the application layer**. Why that matters:
1. **Rules get bypassed silently.** Add "only currencies Sokha Mart has enabled" to `CurrencyService.findAll()`, and this path ignores it, with no test going red. Two roads to the same data drift apart.
2. **Application concerns live in the service:** `@Transactional` (Day 4), tenant filtering by `business_id`, and permission checks. Skipping the service skips all of them; in a multi-tenant SaaS that's a data leak.

Fix: reviews catch it only if someone notices, so an ArchUnit test makes the **build** reject it.

### Q11. (Day 2 evening close) Our ArchUnit rule bans only `api` → `*Repository`. Name two other layering violations it would **not** catch.

**My answer:** didn't know.

**Corrected answer:** Any dependency that isn't `api` → `*Repository` slips through. For example:
1. **application → api:** `CurrencyService.create(CreateCurrencyRequest)` or returning `CurrencyResponse`. The use case is now tied to HTTP DTOs.
2. **domain → infrastructure / framework:** `Unit` importing `InMemoryUnitRepository`, or JPA/Spring annotations on the domain model.
3. **api → infrastructure (not named `*Repository`):** e.g. a JPA entity or an HTTP client used directly in a controller.
4. **feature → feature:** `currency.api` importing `unit.api.CreateUnitRequest` (the stray import from tonight's review).

Fix: replace the single rule with ArchUnit's `layeredArchitecture()` (each layer declares who may access it) plus a feature-isolation rule. Day 5 candidate.
