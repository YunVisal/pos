# Progress

## Current position
Day 2 — evening, step 0 (not started). Day 2 morning complete (core build green, 14 tests).

## Open items
- **Day 2 evening step 0 (~5 min):** add `UnitResponse` (with `static from(Unit)`) and rename `UnitRequest` → `CreateUnitRequest`; `UnitController` must not return domain `Unit` (lines 26, 31). Prefer `@ResponseStatus(CREATED)` over `new ResponseEntity<>`. In `UnitControllerTest`: rename `shouldContainPCSOnGetAll` (uses KG) and assert `isCreated()` on the setup POST.
- Homework nits (`homework/day1/`): `Money` — remove debug `println`s, `!=` on strings, null-check `currency`; rename `Vocher` → `Voucher`, `main` → `Main`; add `case Vocher` to the switch.
- Day 1 lab nits (still open): `@SpringBootTest()` → `@SpringBootTest` and drop `public` in `PingControllerTest` / `InfoControllerTest`; trailing newline in `application.yml`.
- Small cleanups: `UnitTest` unused `Unit unit =` in lambdas; `UnitServiceTest` unused `DuplicateFormatFlagsException` import.
- Day 5 candidate: add `maven-enforcer-plugin` to `platform-parent` (require Java 25, Maven 3.9+).
- Session 2: replace or complement `/api/v1/info` with Actuator `/actuator/info` + build-info.
- Day 4: delete `InMemoryUnitRepository` when the JPA repository arrives (two `@Repository` beans → `NoUniqueBeanDefinitionException`).

## Revisit
- `<dependencyManagement>` vs `<dependencies>`: answered wrong again in the close-out question, then proved the difference with `dependency:tree` (parent `<dependencies>` adds the library to every child; `<dependencyManagement>` only fixes versions). Check once more on Day 2/5 with a cold question.
- `<parent>` (inherit settings) vs `<module>` (aggregate build): now named inherited `version`/`java.version`, but missed the managed dependency versions.
- Spring basics from the evening: `DispatcherServlet` → handler mapping, Jackson `HttpMessageConverter`, what a bean is and `@ConditionalOnMissingBean`. All new; reinforce in Day 2 layering.
- Visibility: twice blocked by package-private classes (fake repo test, controller → service). Rule: start package-private; `public` only when another package must use it; interface public, implementation hidden.
- Spring bean scanning: couldn't explain why `@Repository` on a test fake broke `@SpringBootTest` (two beans of one type). Recheck: "annotations are for Spring, `new` is for you".
- Git flow: committed on `main` then branched; message not Conventional Commits and mixed homework + feature. Branch first, one change per commit.
- IDE auto-imports: picked Spring's `DurationFormat.Unit` and `DuplicateFormatFlagsException`. Check imports.
- Machine setup: Homebrew JDK 26 shadowed Temurin 25; fixed via `JAVA_HOME` in `~/.zshrc`.

## Log
| Date | Day / block | Built | Decisions |
| --- | --- | --- | --- |
| 2026-10-05 | Day 1 morning | `platform-parent` (inherits `spring-boot-starter-parent` 4.1.1, Java 25), root aggregator `pom.xml`, `service-template` with `GET /api/v1/ping` + MockMvc test (red → green), `.gitignore`, README; pushed on `feat/day1-skeleton` | Separate root aggregator (`com.sokhamart:pos`) from `platform-parent` (`com.sokhamart.platform`); Spring Boot GA only, no milestones; webmvc starter (Boot 4 name); Conventional Commits + feature branches |
| 2026-10-05 | Day 1 evening | `GET /api/v1/info` (`InfoController` + `InfoResponse` record, constructor injection), `application.yml` with `spring.application.name` and `@project.version@`, MockMvc test with literal expectations. Break it: pinned `spring-webmvc` 6.2.0 → mixed 6.2.0/7.0.9 set → context failed to start; traced with `dependency:tree`. Started `docs/warmups.md`. | Version comes from Maven via resource filtering, not duplicated in YAML; services never set `<version>` on managed dependencies; version changes go in `platform-parent` only |
| 2026-10-06 | Day 2 morning | Homework review (sealed `PaymentMethod`, exhaustive switch). ADR 0001. `unit` feature test-first through 4 layers: `Unit` record with validation, `UnitRepository` interface + package-private `InMemoryUnitRepository` (`ConcurrentHashMap`), `UnitService` with `DuplicateUnitCodeException`, `UnitController` `POST`/`GET /api/v1/units` (201/200), `FakeUnitRepository` in unit tests. 14 tests green; on `feat/day2-standard` | Feature-first packages `<service>.<feature>.{api,application,domain,infrastructure}`; domain owns repository interface (DIP); fakes without annotations; plural resource URLs |
