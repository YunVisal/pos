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
