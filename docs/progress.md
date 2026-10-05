# Progress

## Current position
Day 1 — evening, step 1 (not started). Morning block complete.

## Open items
- Move `PingResponse` from `com.sokhamart.template` into `com.sokhamart.template.api` (do before the lab).
- Merge `feat/day1-skeleton` into `main` once Day 1 evening is done.
- Day 5 candidate: add `maven-enforcer-plugin` to `platform-parent` (require Java 25, Maven 3.9+).

## Revisit
- `<dependencyManagement>` vs `<dependencies>`: thought a BOM entry adds the library to every service. Reinforce in tonight's "Break it".
- `<parent>` (inherit settings) vs `<module>` (aggregate build): named the module half, missed the parent half.
- Machine setup: Homebrew JDK 26 shadowed Temurin 25; fixed via `JAVA_HOME` in `~/.zshrc`.

## Log
| Date | Day / block | Built | Decisions |
| --- | --- | --- | --- |
| 2026-10-05 | Day 1 morning | `platform-parent` (inherits `spring-boot-starter-parent` 4.1.1, Java 25), root aggregator `pom.xml`, `service-template` with `GET /api/v1/ping` + MockMvc test (red → green), `.gitignore`, README; pushed on `feat/day1-skeleton` | Separate root aggregator (`com.sokhamart:pos`) from `platform-parent` (`com.sokhamart.platform`); Spring Boot GA only, no milestones; webmvc starter (Boot 4 name); Conventional Commits + feature branches |
