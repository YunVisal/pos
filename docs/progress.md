# Progress

## Current position
Day 2 — morning, step 1 (not started). Day 1 complete (morning + evening).

## Open items
- Homework (before Day 2): rewrite 3 old-style classes as records, at least one as a sealed hierarchy (e.g. `Money`, `PaymentMethod` → `Cash` / `Card` / `KhqrQr`). Review it at the start of Day 2.
- Lab nits: drop `()` from `@SpringBootTest()` and `public` on test classes; add a trailing newline to `application.yml`.
- Merge `feat/day1-skeleton` into `main` after the evening commit.
- Day 5 candidate: add `maven-enforcer-plugin` to `platform-parent` (require Java 25, Maven 3.9+).
- Session 2: replace or complement `/api/v1/info` with Actuator `/actuator/info` + build-info.

## Revisit
- `<dependencyManagement>` vs `<dependencies>`: answered wrong again in the close-out question, then proved the difference with `dependency:tree` (parent `<dependencies>` adds the library to every child; `<dependencyManagement>` only fixes versions). Check once more on Day 2/5 with a cold question.
- `<parent>` (inherit settings) vs `<module>` (aggregate build): now named inherited `version`/`java.version`, but missed the managed dependency versions.
- Spring basics from the evening: `DispatcherServlet` → handler mapping, Jackson `HttpMessageConverter`, what a bean is and `@ConditionalOnMissingBean`. All new; reinforce in Day 2 layering.
- Machine setup: Homebrew JDK 26 shadowed Temurin 25; fixed via `JAVA_HOME` in `~/.zshrc`.

## Log
| Date | Day / block | Built | Decisions |
| --- | --- | --- | --- |
| 2026-10-05 | Day 1 morning | `platform-parent` (inherits `spring-boot-starter-parent` 4.1.1, Java 25), root aggregator `pom.xml`, `service-template` with `GET /api/v1/ping` + MockMvc test (red → green), `.gitignore`, README; pushed on `feat/day1-skeleton` | Separate root aggregator (`com.sokhamart:pos`) from `platform-parent` (`com.sokhamart.platform`); Spring Boot GA only, no milestones; webmvc starter (Boot 4 name); Conventional Commits + feature branches |
| 2026-10-05 | Day 1 evening | `GET /api/v1/info` (`InfoController` + `InfoResponse` record, constructor injection), `application.yml` with `spring.application.name` and `@project.version@`, MockMvc test with literal expectations. Break it: pinned `spring-webmvc` 6.2.0 → mixed 6.2.0/7.0.9 set → context failed to start; traced with `dependency:tree`. Started `docs/warmups.md`. | Version comes from Maven via resource filtering, not duplicated in YAML; services never set `<version>` on managed dependencies; version changes go in `platform-parent` only |
