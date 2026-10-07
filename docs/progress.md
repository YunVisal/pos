# Progress

## Current position
Day 3 — evening, step 1 (not started). Morning core build done (Tasks 1–5, `mvn verify` green), but **uncommitted on `main`**: branch `feat/day3-problem-details` first. Before the evening lab, fix the Day 3 carry-overs below.

## Open items
- **Day 3 carry-overs (do first this evening):**
  - `CreateUnitRequest.code`: `@NotBlank` was removed → `{"name":"X"}` returns 500. Restore it, use `*` in the `@Pattern` regex, fix message `"must be uppercase"` → "uppercase letters and digits" (Q18).
  - `BusinessException` still package-private → `public`; `protected` constructors on the abstract classes.
  - Handler maps only `ConflictException`: `NotFoundException`/`BusinessRuleException` fall into the 500 catch-all. Handle `BusinessException` → 404/409/422.
  - `@ConditionalOnMissingBean` on the `GlobalExceptionHandler` `@Bean`.
  - `UnitControllerTest`: remove 4× `.andDo(print())`; `$.status` compared to `"409"`/`"400"` strings → numbers; `errors.length()` → assert on fields.
  - `platform-web/pom.xml`: redundant `<groupId>`/`<version>` (inherited).
- Tests ran on **JDK 26.0.2** (`mvn` log): Homebrew JDK shadowing Temurin 25 again. Check `echo $JAVA_HOME; mvn -v`.
- Homework nits (`homework/day1/`): rename `Vocher` → `Voucher` and `main` → `Main`.
- Day 1 lab nits: `@SpringBootTest()` → `@SpringBootTest` and drop `public` in `PingControllerTest` / `InfoControllerTest`; trailing newline in `application.yml`.
- `UnitTest`: unused `Unit unit =` in lambdas (use `() -> new Unit(...)`).
- Test names: 24 `testX…` vs `shouldX…`; pick `shouldXWhenY` (coding standard §6).
- `CurrencyControllerTest.java:38`: riel symbol `"R"` → `"៛"`.
- Coding standard (`docs/coding-standard.md`) was written by Claude at my request: rewrite one section in my own words.
- Day 3 lab: `DuplicateCurrencyCodeException` → `ConflictException` (409) + validation on `CreateCurrencyRequest`.
- Day 5 candidate: replace the single ArchUnit rule with `layeredArchitecture()` + feature isolation (see warm-up Q11).
- Day 5 candidate: add `maven-enforcer-plugin` to `platform-parent` (require Java 25, Maven 3.9+).
- Session 2: replace or complement `/api/v1/info` with Actuator `/actuator/info` + build-info.
- Day 4: delete `InMemoryUnitRepository`/`InMemoryCurrencyRepository` when JPA arrives (two beans → `NoUniqueBeanDefinitionException`).

## Revisit
- `<dependencyManagement>` vs `<dependencies>`: applied correctly for ArchUnit on Day 2 evening (version in parent, no version in service), `<scope>test</scope>` only added after review. One more cold question on Day 5.
- `<parent>` (inherit settings) vs `<module>` (aggregate build): now named inherited `version`/`java.version`, but missed the managed dependency versions.
- Spring basics from the evening: `DispatcherServlet` → handler mapping, Jackson `HttpMessageConverter`, what a bean is and `@ConditionalOnMissingBean`. All new; reinforce in Day 2 layering.
- Visibility: twice blocked by package-private classes (fake repo test, controller → service). Rule: start package-private; `public` only when another package must use it; interface public, implementation hidden.
- Spring bean scanning: couldn't explain why `@Repository` on a test fake broke `@SpringBootTest` (two beans of one type). Recheck: "annotations are for Spring, `new` is for you".
- Git flow: committed on `main` then branched; message not Conventional Commits and mixed homework + feature. Branch first, one change per commit.
- IDE auto-imports: picked Spring's `DurationFormat.Unit` and `DuplicateFormatFlagsException`; Day 2 evening imported `unit.api` classes into `CurrencyController` and left 4 unused imports. Organize Imports before every commit.
- Layering: in the warm-up, said `UnitRepository` sits in domain "because it contains logic" (it's a port, DIP); in break-it, said the controller accessed infrastructure (it bypassed application). Couldn't name violations beyond api→repository (Q11). Reinforce dependency direction on Day 3.
- Tests that don't reach their rule: `"$"` failed on length, not on letters. Ask "would this test still pass if the rule were removed?"
- Domain scope creep: added a 1-char symbol rule nobody asked for. Rules come from the business.
- Asked Claude to write the ArchUnit test, the homework and the commits ("do it for me"). From Session 3, attempt first.
- Machine setup: Homebrew JDK 26 shadowed Temurin 25; fixed via `JAVA_HOME` in `~/.zshrc`.

- Maven/IDE basics: put sources in `src/java` and resources in `src/main/resoruces`; both silently ignored (empty jar, build still green). Standard Directory Layout; reload Maven in VS Code (`Java: Reload Projects`) after POM changes; `-pl` needs `-am` for in-reactor deps.
- Spring bean discovery outside the app package: knew "different package", needed the scan-tree hint; auto-configuration (`@AutoConfiguration` + `.imports` file) was hard, needed it split into two steps. Re-ask on Day 5.
- Constructors: forgot `super(message)` → message `null`, tests green anyway. Same "test doesn't reach the rule" gap as Day 2.
- Leaking internals: put raw `FieldError` and then `ex.getMessage()` into the response. `detail` is for the client, `getMessage()` is for logs.
- Bean Validation: `null` is valid for every constraint except `@NotNull`/`@NotBlank`/`@NotEmpty`; DTO and domain rules drifted in both directions (`KG_X` → 500, `BOX12` → 400).
- Predictions: skipped twice until asked again. Keep insisting on predict-before-run.

## Log
| Date | Day / block | Built | Decisions |
| --- | --- | --- | --- |
| 2026-10-05 | Day 1 morning | `platform-parent` (inherits `spring-boot-starter-parent` 4.1.1, Java 25), root aggregator `pom.xml`, `service-template` with `GET /api/v1/ping` + MockMvc test (red → green), `.gitignore`, README; pushed on `feat/day1-skeleton` | Separate root aggregator (`com.sokhamart:pos`) from `platform-parent` (`com.sokhamart.platform`); Spring Boot GA only, no milestones; webmvc starter (Boot 4 name); Conventional Commits + feature branches |
| 2026-10-05 | Day 1 evening | `GET /api/v1/info` (`InfoController` + `InfoResponse` record, constructor injection), `application.yml` with `spring.application.name` and `@project.version@`, MockMvc test with literal expectations. Break it: pinned `spring-webmvc` 6.2.0 → mixed 6.2.0/7.0.9 set → context failed to start; traced with `dependency:tree`. Started `docs/warmups.md`. | Version comes from Maven via resource filtering, not duplicated in YAML; services never set `<version>` on managed dependencies; version changes go in `platform-parent` only |
| 2026-10-06 | Day 2 morning | Homework review (sealed `PaymentMethod`, exhaustive switch). ADR 0001. `unit` feature test-first through 4 layers: `Unit` record with validation, `UnitRepository` interface + package-private `InMemoryUnitRepository` (`ConcurrentHashMap`), `UnitService` with `DuplicateUnitCodeException`, `UnitController` `POST`/`GET /api/v1/units` (201/200), `FakeUnitRepository` in unit tests. 14 tests green; on `feat/day2-standard` | Feature-first packages `<service>.<feature>.{api,application,domain,infrastructure}`; domain owns repository interface (DIP); fakes without annotations; plural resource URLs |
| 2026-10-06 | Day 2 evening | `UnitResponse` + `CreateUnitRequest`, stream mapping. Lab: `Currency` feature in 4 layers, test-first (code `[A-Z]{3}`, name/symbol not blank, duplicate → `DuplicateCurrencyCodeException`), `POST`/`GET /api/v1/currencies`. Break it: controller → repository compiled, started and passed all tests; added ArchUnit `ArchitectureTest` (api must not depend on `*Repository`), saw 3 violations (ctor param, field, call). Homework `docs/coding-standard.md` (written by Claude). Warm-ups Q9–Q11. 31 tests green | ArchUnit version in `platform-parent` `<dependencyManagement>`, test scope in service; `@ResponseStatus(CREATED)` over `ResponseEntity`; `XResponse.from()` + `stream().map().toList()`; no domain rules beyond the business ask |
| 2026-10-07 | Day 3 morning | ADR 0002. New `platform-web` module (managed in `platform-parent`, `-am` builds). `BusinessException` → `NotFoundException`/`ConflictException`/`BusinessRuleException` (abstract, message only); `DuplicateUnitCodeException extends ConflictException`. `GlobalExceptionHandler extends ResponseEntityExceptionHandler`: 409 for conflict, `handleMethodArgumentNotValid` reusing `ex.getBody()` + `errors[{field,message}]`, catch-all 500 with generic detail + log. `PlatformWebAutoConfiguration` + `AutoConfiguration.imports`. `spring-boot-starter-validation`, `@Valid` + `@Size`/`@Pattern` on `CreateUnitRequest`. 4 new MockMvc tests, red → green. Warm-ups Q12–Q18 | Error handling in a shared library, not the template (copies drift); RFC 9457 via `ProblemDetail`; exceptions carry no `HttpStatus`; 400 = validation (not a business exception), 404/409/422 = business categories; validate in both DTO and domain; auto-configuration over `scanBasePackages`/`@Import` |
