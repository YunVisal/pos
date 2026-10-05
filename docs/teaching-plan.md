# Java Real System — Instructor Teaching Plan

Oct 3, 2026 · @Visal Yun

## How this course runs

We will build the Real System in 60 learning days: 12 sessions of 5 days each, 2 hours per day split into a morning hour and an evening hour after work. Every day ends with something running on your machine, and every session ends with a demo you can show.

**Schedule.** 5 days per session, 2 hours per day: a 1-hour morning block and a 1-hour evening block after work. That is 120 class hours over 12 weeks. Homework is optional and fits best on weekends.

**Daily rhythm (two 1-hour blocks).** You write the code; the instructor guides. Each block runs one step at a time: every step ends with one task for you (answer a question, make a prediction, write code, or run a command), and the next step starts only after you've done it.

*Morning hour — build the core path:*

1. Business context (5 min): the store problem today's code solves, and how you would approach it.
2. Design questions (15 min): a few short questions, one at a time: where the logic lives, sync or async, what can go wrong.
3. Core build (40 min): you build the day's core path in 3–6 small tasks, each with acceptance criteria and, where it fits, a failing test. Low-value setup (compose files, realm exports, seed data) is provided so the hour goes to the concept. The block closes with a short comparison of your solution against the reference approach.

*Evening hour — extend it:*

4. Explain it back (10 min): explain the morning's code in two sentences; gaps get corrected.
5. Your lab (35 min): extend your own code with the day's lab task, then ask for a review.
6. Break it & recap (15 min): predict what will break, run it, explain the result, then recap.

**When you're stuck.** Hints come one level at a time: a nudge, then the relevant API or doc, then a short snippet. The full solution comes only when you ask for it.

**Support fades.** In Sessions 1–2 a short worked example (about 10 lines) may follow your first attempt. From Session 3 onward you always attempt first.

**Running it in Claude Code.** `/day N morning` or `/day N evening` starts or resumes a block; `/handoff` records the exact step you stopped at in `docs/progress.md`, so the next session picks up mid-block.

The heavier days (Days 27, 32, 38, 48) will spill over, since building takes longer than watching. Provide part of their setup up front, and let Day 5 of the session absorb the rest.

**Day 5 of every session** is integration day: finish the lab, write the session's required test, demo the milestone, and do a short code review. It is also the catch-up buffer.

**The running store.** All data in the course belongs to one fictional business: *Sokha Mart*, a mini-mart with two branches (Toul Kork and BKK1), an owner, a manager, two cashiers, and three suppliers. You create it in Session 4, stock it in Sessions 5–7, sell from it in Session 8, and watch its day on the dashboard in Session 11. The final demo in Session 12 is one full business day at Sokha Mart.

**Phases and checkpoints.**

| Phase | Sessions | Checkpoint demo |
| --- | --- | --- |
| A. Foundation | 1–2 | A template service behind the Gateway, discovered by Consul, configured by Config Server |
| B. Platform & SaaS core | 3–4 | Owner registers, logs in, and sets up Sokha Mart with two branches in the Owner Portal |
| C. Core business transactions | 5–8 | Product → Purchase → Stock IN → POS sale → Stock OUT |
| D. Distributed hardening | 9–10 | Payment survives a downed service; sales events reach Inventory and Reporting through Kafka |
| E. Operations & production | 11–12 | 14 services and 3 apps deployed; full business day demo |

**Threads that run through every session.** One required test per session, the request ID and trace followed across services from Session 2 onward, and a "known risks" list that later sessions pay off. Sales → Inventory starts as a synchronous REST call in Session 8 on purpose, so that Session 10 can refactor it to Kafka and you feel why.

## Session 1 — Foundation & Engineering Standards (Days 1–5)

By Day 5 you have a reusable service template and the first Platform Libraries, so every one of the 14 services starts from the same structure.

### Day 1 — Tooling, Java 25 and the project skeleton

- **Goal:** a multi-module Maven project that builds and runs on JDK 25 and Spring Boot 4.
- **Concept:** the course map and the 14 services; modern Java you will use daily (records, sealed interfaces, pattern matching for `switch`, virtual threads); Maven parent POM, BOM and dependency management; Git branching and commit conventions.
- **Core build:** create the `platform-parent` POM and a `service-template` module; first `GET /api/v1/ping`; push to a Git repo with a `.gitignore` and README.
- **Your lab:** add a `GET /api/v1/info` endpoint returning app name and version from `application.yml`.
- **Break it:** mismatch a dependency version and read the Maven dependency tree to find it.
- **Homework:** rewrite three small classes from old-style Java into records and sealed types.

### Day 2 — Clean Code, SOLID and the layering standard

- **Goal:** one agreed package structure every service will copy.
- **Concept:** SOLID with retail examples; layers (`api` → `application` → `domain` → `infrastructure`); what each layer may and may not import; DTO vs entity vs domain model; naming rules.
- **Core build:** a sample `Unit` feature (from Reference Data) end to end through every layer, in-memory first.
- **Your lab:** build a `Currency` feature following the exact same layering.
- **Break it:** put a repository call in a controller; we discuss why the review rejects it, then add an ArchUnit rule that fails the build.
- **Homework:** write the team's one-page coding standard in your own words.

### Day 3 — REST API design, validation and error handling

- **Goal:** consistent URLs, status codes and error bodies across all services.
- **Concept:** resource naming, versioning (`/api/v1`), pagination parameters, idempotent methods; Bean Validation; RFC 9457 Problem Details as the single error format; business vs technical exceptions.
- **Core build:** a `GlobalExceptionHandler` in a new `platform-web` library; validation on create/update DTOs; a `BusinessException` hierarchy.
- **Your lab:** apply validation and proper error responses to your `Currency` API, including a 409 for a duplicate code.
- **Break it:** send malformed JSON, missing fields and a wrong type; every case must return the same error shape.
- **Homework:** design (on paper) the URL list for the Product Service.

### Day 4 — PostgreSQL, Liquibase and Docker

- **Goal:** real persistence with versioned schema changes.
- **Concept:** database-per-service; JPA mapping basics and the N+1 trap; Liquibase changelogs, rollbacks and never editing an applied changeset; audit columns (`created_at`, `created_by`, `version`).
- **Core build:** Postgres in Docker, Liquibase changelog for `units`, JPA repository, a `BaseEntity` in a `platform-data` library.
- **Your lab:** move `Currency` to Postgres with its own changelog and a unique constraint.
- **Break it:** edit an applied changeset and watch Liquibase refuse to start; fix it properly with a new changeset.
- **Homework:** write a Dockerfile for the template service.

### Day 5 — Logging, Request ID, OpenAPI, tests — integration day

- **Goal:** a template that is observable, documented and tested.
- **Concept:** structured JSON logs, log levels, never logging secrets; a request ID filter with MDC; OpenAPI via springdoc; test pyramid; Testcontainers.
- **Core build:** `platform-logging` with the request ID filter; OpenAPI config; one unit test and one Testcontainers integration test.
- **Your lab (required test):** a Testcontainers test proving the duplicate `Currency` returns 409.
- **Checkpoint demo:** clone the template, rename it, and have a new service running with DB, docs and tests in under 15 minutes.
- **Code review:** layering, error format, changelogs, test quality.

## Session 2 — Microservices Infrastructure (Days 6–10)

By Day 10 one `docker compose up` starts Gateway, Keycloak, Consul, Config Server and two template services, and a request is traceable across all of them.

### Day 6 — Docker Compose and the local environment

- **Goal:** the whole platform starts with one command.
- **Concept:** why microservices need shared infrastructure; container networking, volumes, health checks, `depends_on` with conditions; `.env` files.
- **Core build:** a `compose.yml` with Postgres, Keycloak, Consul and two copies of the template (`reference-data-service`, `media-service` stubs).
- **Your lab:** add health checks so services wait for Postgres to be healthy.
- **Break it:** stop Postgres mid-run and read what each service logs.
- **Homework:** draw the container diagram from memory.

### Day 7 — Consul and Config Server

- **Goal:** services find each other by name and read config from one place.
- **Concept:** service registration, health-based discovery, client-side load balancing; centralized config for `local`, `dev`, `prod` profiles; secrets concept (what never goes in Git; environment variables and vault-style stores).
- **Core build:** register both services in Consul; Config Server backed by a Git config repo; profile-specific DB URLs.
- **Your lab:** move all hard-coded settings of your service into the config repo and switch profiles.
- **Break it:** run two instances of one service, kill one, and watch Consul stop routing to it.
- **Homework:** list every setting that differs between local and prod.

### Day 8 — API Gateway, routing and CORS

- **Goal:** the frontend talks to one URL only.
- **Concept:** Spring Cloud Gateway routes, predicates, filters; `lb://` routing via Consul; CORS done once at the gateway; request ID created at the edge and passed downstream.
- **Core build:** gateway routes `/api/reference/**` and `/api/media/**`; global CORS for `localhost:4200`; request ID header filter.
- **Your lab:** add a route with a path rewrite and a response header filter.
- **Break it:** call a service directly vs through the gateway; trigger a CORS error and fix it in one place.
- **Homework:** read the Gateway filter order documentation and explain it in five sentences.

### Day 9 — Keycloak, OAuth2, OIDC and JWT

- **Goal:** the gateway rejects requests without a valid token.
- **Concept:** OAuth2 roles (resource owner, client, authorization server, resource server); OIDC; authorization code + PKCE for Angular; access vs refresh tokens; reading a JWT.
- **Core build:** a realm, a public `owner-portal` client and a confidential gateway client; gateway as resource server validating JWTs.
- **Your lab:** get a token with Postman, decode it, and call a protected route; then call with an expired token.
- **Break it:** change one character in a JWT signature and confirm a 401.
- **Homework:** sequence-diagram the authorization code + PKCE flow.

### Day 10 — Tracing + Angular primer — integration day

- **Goal:** follow one request end to end, and have Angular ready for Session 3.
- **Concept:** distributed tracing with Micrometer Tracing (trace ID vs request ID); Angular essentials: standalone components, signals, services, `HttpClient`, environments.
- **Core build:** tracing in all services with trace IDs in logs; a minimal Angular app calling `/api/reference/units` through the gateway.
- **Your lab (required test):** an integration test that the gateway returns 401 without a token and 200 with one.
- **Checkpoint demo (Phase A):** `docker compose up`, log in via Keycloak, call a service through the gateway, and find the same trace ID in every log line.
- **Code review:** compose file, config repo layout, gateway routes.

## Session 3 — Identity, Security & Access Control (Days 11–15)

By Day 15 an owner can register and log in from Angular, and every API checks both who you are and what you may do.

### Day 11 — User Service and the registration flow

- **Goal:** registering in our app creates a Keycloak user and a local profile.
- **Concept:** identity (Keycloak) vs profile (User Service); why we never store passwords ourselves; Keycloak Admin API; keeping the two in sync.
- **Core build:** User Service with `POST /register` creating the Keycloak user and a `user_profile` row linked by Keycloak subject ID.
- **Your lab:** `GET /me` and `PUT /me` for profile updates (name, phone, language).
- **Break it:** Keycloak call succeeds but the DB insert fails; we discuss the half-created user and add compensation.
- **Homework:** list which user fields live in Keycloak and which in User Service, with reasons.

### Day 12 — Access Control Service: roles, permissions, authorities

- **Goal:** a permission model that fits Owner, Manager and Staff.
- **Concept:** role vs permission vs authority; RBAC scoped per business and per branch (Sokha Mart's manager is not manager of another shop); permission naming (`product:create`, `sale:refund`).
- **Core build:** Access Control Service schema (roles, permissions, role\_permissions, member\_roles with business/branch scope) and the default role templates.
- **Your lab:** an API to assign and revoke a role for a staff member.
- **Break it:** try assigning a role in a business you don't belong to; it must return 403.
- **Homework:** write the permission matrix for Owner, Manager, Cashier.

### Day 13 — JWT validation, method security, current user

- **Goal:** every service enforces permissions the same way through a shared library.
- **Concept:** resource server config; mapping JWT claims to authorities; `@PreAuthorize`; where permissions come from (token claims vs a lookup to Access Control); a `CurrentUser` abstraction.
- **Core build:** a `platform-security` library with JWT conversion, `CurrentUser`, and permission checks; applied to User Service.
- **Your lab:** protect your Session 1 `Currency` API: read for everyone, write only with `reference:manage`.
- **Break it:** a cashier token calls an owner endpoint; confirm 403 with a Problem Details body.
- **Homework:** explain authentication vs authorization with one Sokha Mart example each.

### Day 14 — Audit and security best practices + Angular auth

- **Goal:** who did what is recorded, and Angular can log in.
- **Concept:** audit fields from `CurrentUser`; security headers; least privilege; token storage in the browser; Angular OIDC with PKCE, an HTTP interceptor and a route guard.
- **Core build:** JPA auditing wired to `CurrentUser`; Angular login, logout, token refresh, interceptor and `authGuard`.
- **Your lab:** an Angular "My profile" page using `GET/PUT /me`.
- **Break it:** let the access token expire and confirm the refresh works silently.
- **Homework:** review the OWASP API Security Top 10 list and mark which ones we already cover.

### Day 15 — Integration day

- **Goal:** registration to protected API, end to end.
- **Your lab (required test):** permission tests: owner allowed, cashier forbidden, other business forbidden.
- **Checkpoint demo:** register as Sokha Mart's owner in Angular, log in, view profile, and show a 403 for a cashier.
- **Code review:** security library, no permission logic in controllers, audit columns filled.

## Session 4 — SaaS Core + Angular Owner Portal (Days 16–20)

By Day 20 Sokha Mart exists as a business with two branches and a subscription plan, set up entirely from the Store Owner Portal.

### Day 16 — Business Service and multi-tenancy

- **Goal:** a tenancy model every later service depends on.
- **Concept:** business (tenant) vs branch vs location; `business_id` on every business row; tenant resolved from the token or a header, never from the request body; row-level isolation strategy.
- **Core build:** Business Service: create business, branches, owner membership; a `TenantContext` added to `platform-security`.
- **Your lab:** branch CRUD with the rule that only the owner can add a branch.
- **Break it:** request Sokha Mart's branches with another business's token; must return 404, not the data.
- **Homework:** list every future table that needs `business_id` and which also need `branch_id`.

### Day 17 — Subscription Service and entitlements

- **Goal:** plans control what a business may do.
- **Concept:** plan, billing cycle, subscription status (trial, active, past\_due, cancelled); entitlements as limits (`max_branches`, `max_products`); checking entitlements synchronously for now.
- **Core build:** plans seeded by Liquibase; start a trial on business creation; `GET /entitlements`.
- **Your lab:** Business Service refuses a third branch when the plan allows two (sync call to Subscription).
- **Break it:** Subscription Service is down; adding a branch fails. Added to the known-risks list for Session 9.
- **Homework:** design the state diagram for subscription status.

### Day 18 — Reference Data and Media services

- **Goal:** two supporting services built quickly from the template.
- **Concept:** global vs business-specific reference data; object storage with MinIO; presigned upload URLs vs streaming through the service; file size and type validation.
- **Core build:** Media Service with presigned upload to MinIO and a `media` metadata table.
- **Your lab:** finish Reference Data (units, product types, payment methods, statuses) from your Session 1 work.
- **Break it:** upload a 50 MB file and a `.exe` renamed to `.png`; both must be rejected.
- **Homework:** decide where the business logo URL should be stored and why.

### Day 19 — Angular architecture: Store Owner Portal

- **Goal:** the portal shell every later screen plugs into.
- **Concept:** folder structure (core, shared, features), lazy-loaded routes, guards for roles and entitlements, interceptors for auth, tenant header and errors; smart vs presentational components.
- **Core build:** portal shell with layout, menu by permission, business switcher, global error toast.
- **Your lab:** Business setup and Branch list screens.
- **Break it:** hide a menu item but call its URL directly; the guard and the API must both stop it.
- **Homework:** draw the portal's route tree.

### Day 20 — Integration day

- **Goal:** SaaS onboarding end to end.
- **Your lab:** logo upload from Angular to MinIO; subscription page showing plan and limits.
- **Required test:** tenant isolation test across two businesses.
- **Checkpoint demo (Phase B):** register, create Sokha Mart, add Toul Kork and BKK1, upload the logo, and hit the branch limit.
- **Code review:** tenant handling, no `business_id` taken from request bodies.

## Session 5 — Product, Partner & Design Patterns (Days 21–25)

By Day 25 Sokha Mart has a searchable product catalog and its three suppliers, and you have used Factory, Mapper and Strategy in real code.

### Day 21 — Product model: SKU, category, brand, barcode, units

- **Goal:** a product model that survives real retail cases.
- **Concept:** product vs SKU (variants like 330 ml and 1.5 L); category tree; brand; one SKU with several barcodes; base unit and pack units (1 case = 24 cans); cost price vs selling price and why price history matters.
- **Core build:** Product Service schema and Liquibase changelogs; create product with SKUs; barcode uniqueness per business.
- **Your lab:** category and brand CRUD, with a rule that a category with products cannot be deleted.
- **Break it:** two SKUs with the same barcode in one business; must fail with 409.
- **Homework:** model five real products from a local mini-mart on paper.

### Day 22 — Factory and Mapper patterns

- **Goal:** clean object creation and conversion instead of copy-paste.
- **Concept:** Factory for building valid aggregates (`ProductFactory` enforcing rules at creation); MapStruct mappers between DTO, domain and entity; keeping mapping out of controllers.
- **Core build:** `ProductFactory` and MapStruct mappers; refactor yesterday's code to use them.
- **Your lab:** a `SkuFactory` that generates SKU codes from a configurable pattern.
- **Break it:** a mapper that silently drops a new field; enable MapStruct's unmapped-target error to catch it at build time.
- **Homework:** find one place in your earlier code that should be a factory.

### Day 23 — Search, filter, pagination, sorting + Strategy

- **Goal:** fast product search that scales past 10,000 SKUs.
- **Concept:** offset vs keyset pagination; JPA Specifications for dynamic filters; indexes for search columns; Strategy pattern for pricing rules (fixed, markup %, margin %).
- **Core build:** `GET /products?q=&category=&brand=&page=&sort=`; a `PricingStrategy` interface with three implementations chosen per business setting.
- **Your lab:** barcode lookup endpoint `GET /products/by-barcode/{code}`, built for POS speed.
- **Break it:** seed 100,000 products, run search with and without an index, compare with `EXPLAIN ANALYZE`.
- **Homework:** write when you'd pick keyset over offset pagination.

### Day 24 — Partner Service and reusable Angular components

- **Goal:** suppliers and customers, plus UI parts we reuse in three apps.
- **Concept:** one Partner model with types vs two services; contact info, payment terms, credit limit; reusable Angular components: data table with server-side paging, search box with debounce, form field with error display.
- **Core build:** Partner Service; a shared Angular `data-table` and `search-input`.
- **Your lab:** Supplier list and form screens using the shared components.
- **Break it:** type fast in the search box and count API calls before and after debounce.
- **Homework:** list which components the POS app will reuse.

### Day 25 — Integration day

- **Goal:** master data end to end.
- **Your lab:** Product management UI (list, filter, create with SKUs and barcodes, product image via Media Service).
- **Required test:** pricing strategy unit tests plus a search integration test.
- **Checkpoint demo:** load Sokha Mart's catalog and three suppliers, search by name and barcode.
- **Code review:** where each pattern lives and whether it earns its place.

## Session 6 — Purchasing & Transaction Design (Days 26–30)

By Day 30 Sokha Mart can order from a supplier, get approval, receive goods (including partial deliveries) and return damaged items.

### Day 26 — Purchase Order and the state machine

- **Goal:** a purchase order that can only move through valid states.
- **Concept:** why workflows are not CRUD; states `DRAFT → SUBMITTED → APPROVED → PARTIALLY_RECEIVED → RECEIVED`, plus `CANCELLED`; transitions as methods on the aggregate; business rules (no approve without lines, only managers approve).
- **Core build:** Purchase Service with the `PurchaseOrder` aggregate, lines, and an enum-based state machine with guard checks.
- **Your lab:** submit and cancel endpoints with their rules.
- **Break it:** try to approve a cancelled order and edit an approved one; both must fail with a clear business error.
- **Homework:** draw the full state diagram with who may trigger each transition.

### Day 27 — Transaction boundaries and optimistic locking

- **Goal:** each business action is atomic and safe under concurrent edits.
- **Concept:** `@Transactional` placement in the application layer; propagation and read-only; what is not covered by a local transaction (calls to other services); lost updates and the `@Version` column; returning 409 on conflict.
- **Core build:** application service methods with clear boundaries; optimistic locking on `PurchaseOrder`.
- **Your lab:** an Angular PO screen that handles 409 by reloading and showing what changed.
- **Break it:** two managers approve and edit the same PO at once; observe the conflict.
- **Homework:** explain why a transaction should not wrap an HTTP call to another service.

### Day 28 — Goods Receipt and partial receiving

- **Goal:** record what actually arrived, which is often not what was ordered.
- **Concept:** goods receipt as its own document; received vs ordered quantity; partial receipts updating PO state; cost price captured at receipt; Strategy for receiving policies (allow over-receipt or not).
- **Core build:** `GoodsReceipt` with lines referencing PO lines; PO status recalculated after each receipt.
- **Your lab:** reject receipts that exceed the ordered quantity unless the business allows it.
- **Break it:** receive the same delivery twice; note the duplicate and add it to known risks for idempotency.
- **Homework:** list what Inventory will need from a goods receipt.

### Day 29 — Purchase Return + idempotency foundation

- **Goal:** return goods to a supplier, and make critical POSTs safe to retry.
- **Concept:** returns linked to receipts; quantity limits; idempotency keys (`Idempotency-Key` header), stored request hash and response; why networks cause duplicates.
- **Core build:** Purchase Return flow; an idempotency filter in `platform-web` applied to receive and return.
- **Your lab:** apply the idempotency key to PO creation and test a retried request.
- **Break it:** resend the same receipt with the same key (returns the first response) and with a different body (returns 422).
- **Homework:** list every endpoint in the system that needs an idempotency key.

### Day 30 — Integration day

- **Goal:** the supplier purchasing workflow end to end in Angular.
- **Your lab:** Angular screens: PO list with status filters, create, approve, receive, return.
- **Required test:** state transition tests covering every allowed and forbidden move, plus an optimistic locking test.
- **Checkpoint demo:** order drinks from a Sokha Mart supplier, approve as manager, receive in two deliveries, return two damaged cases.
- **Code review:** rules inside the aggregate, transaction placement, idempotency coverage.

## Session 7 — Inventory & Data Consistency (Days 31–35)

By Day 35 every stock change at Sokha Mart is a ledger entry, balances stay correct under concurrent updates, and a goods receipt automatically becomes Stock IN.

### Day 31 — Stock movement ledger and balances

- **Goal:** stock you can explain line by line.
- **Concept:** why a single `quantity` column is not enough; append-only movement ledger (type, reference document, quantity, cost); balance per SKU per location as a derived, maintained value; warehouse and location model per branch.
- **Core build:** Inventory Service with `locations`, `stock_movements`, `stock_balances`; Stock IN and Stock OUT in one transaction that writes the ledger and updates the balance.
- **Your lab:** `GET /stock?sku=&location=` and a movement history endpoint.
- **Break it:** update a balance without a ledger row; then run a reconciliation query that catches the mismatch.
- **Homework:** write the reconciliation SQL that rebuilds balances from the ledger.

### Day 32 — Concurrency and consistency

- **Goal:** no lost or negative stock when two operations hit the same SKU.
- **Concept:** race conditions on balances; atomic `UPDATE ... SET qty = qty - ?` vs optimistic locking vs `SELECT ... FOR UPDATE`; allowing or forbidding negative stock per business; isolation levels in PostgreSQL.
- **Core build:** atomic decrement with a non-negative check; optimistic locking as the alternative; compare both.
- **Your lab:** a concurrent test firing 50 parallel Stock OUTs of 1 unit against a balance of 30.
- **Break it:** run the same test with a naive read-modify-write and watch the balance go wrong.
- **Homework:** explain which strategy you would choose for a busy POS and why.

### Day 33 — Adjustment, transfer and stock count

- **Goal:** the three everyday stock operations beyond buying and selling.
- **Concept:** adjustment with reason codes (damaged, expired, lost); transfer as two ledger rows (OUT at source, IN at destination) in one transaction, or in-transit when locations are far apart; stock count sessions, counted vs system quantity, variance posting.
- **Core build:** adjustment and transfer APIs.
- **Your lab:** stock count: start session, enter counts, post variances as adjustments.
- **Break it:** a transfer where the destination location doesn't exist; nothing may be posted.
- **Homework:** design the approval rule for large adjustments.

### Day 34 — Purchase Receipt → Stock IN, idempotency, domain events

- **Goal:** purchasing and inventory connected without duplicates.
- **Concept:** domain event design (`GoodsReceived`, `StockChanged`: name, payload, IDs, version); for now Purchase calls Inventory synchronously after commit; idempotency by reference document ID (unique constraint on `source_type + source_id + line`); duplicate protection.
- **Core build:** Purchase publishes `GoodsReceived` internally; a handler calls Inventory's Stock IN with the receipt ID as the idempotency key.
- **Your lab:** purchase return → Stock OUT with the same protection.
- **Break it:** retry the same receipt call three times; stock must increase once. Then stop Inventory during a receipt and add "receipt saved, stock not updated" to known risks for Sessions 9–10.
- **Homework:** write the event contracts for the four inventory events in a shared `events` module.

### Day 35 — Integration day

- **Goal:** inventory visible and trustworthy in the portal.
- **Your lab:** Angular stock screens: balances by branch, movement history, transfer, stock count.
- **Required test:** the concurrency test from Day 32 plus a duplicate-receipt test.
- **Checkpoint demo:** receive a PO, see stock rise at Toul Kork, transfer 10 units to BKK1, count stock and post a variance.
- **Code review:** ledger always written, unique constraints, transaction boundaries.

## Session 8 — Sales + POS / Cashier App (Days 36–40)

By Day 40 a cashier at Sokha Mart can open a shift, scan items, apply a discount, check out, print a receipt and process a return, with stock going down in real time.

### Day 36 — Sales model: sale, lines, discounts, totals

- **Goal:** sales math that is always correct to the riel.
- **Concept:** sale header and lines; snapshotting price and product name at sale time; line vs order discounts (amount or percent) via Strategy; tax; rounding rules for KHR and USD; `BigDecimal`, never `double`.
- **Core build:** Sales Service with the `Sale` aggregate and a `TotalsCalculator`; states `OPEN → COMPLETED` or `VOIDED`.
- **Your lab:** discount rules: max discount by role, and a manager override.
- **Break it:** compute totals with `double` and compare with `BigDecimal` across 1,000 random carts.
- **Homework:** write ten tricky total cases (mixed currency, rounding, stacked discounts) as test data.

### Day 37 — Shifts and the POS app foundation

- **Goal:** a fast cashier app built on what you already have.
- **Concept:** shift open/close with opening cash, expected vs counted cash; POS UX rules (keyboard first, big targets, under 200 ms per scan, works with a USB scanner as keyboard input); reusing Session 5 components and the auth foundation.
- **Core build:** shift API; POS Angular app shell with login, shift open screen, and branch/register selection.
- **Your lab:** shift close screen with cash count and difference.
- **Break it:** try to sell without an open shift; both UI and API must block it.
- **Homework:** time yourself on a 10-item checkout and list the slowest step.

### Day 38 — Scan → Cart → Checkout and inventory availability

- **Goal:** the core selling loop.
- **Concept:** barcode lookup via the Day 23 endpoint; cart held client-side, validated server-side at checkout; checking availability with Inventory (sync); Sales → Stock OUT as a synchronous call with the sale ID as idempotency key.
- **Core build:** scan, cart with quantity edits, checkout calling Inventory Stock OUT.
- **Your lab:** a product search popup for items without a barcode (loose vegetables, for example).
- **Break it:** stop Inventory and try to check out; selling stops. This coupling is the main known risk that Sessions 9 and 10 fix.
- **Homework:** write down every way the checkout call can fail and what the cashier should see.

### Day 39 — Receipts and sales returns

- **Goal:** proof of sale and a controlled return process.
- **Concept:** receipt number sequences per branch; printable receipt layout (80 mm thermal); returns referencing the original sale, partial returns, return reasons, manager approval; Stock IN on return.
- **Core build:** receipt endpoint and print view; return flow with Stock IN.
- **Your lab:** reprint receipt and a daily sales list for the current shift.
- **Break it:** return more units than were sold, or return twice; both must fail.
- **Homework:** list what the receipt must show to satisfy a customer and an auditor.

### Day 40 — Integration day

- **Goal:** a cashier shift end to end.
- **Your lab:** polish POS speed: focus management, keyboard shortcuts, error messages a cashier understands.
- **Required test:** totals calculator tests using your Day 36 cases, plus a checkout idempotency test.
- **Checkpoint demo (Phase C):** product → purchase → Stock IN → open shift → sell 20 items → return one → close shift, with stock correct at every step.
- **Code review:** money handling, sync calls listed with their failure behaviour.

## Session 9 — Payment + Resilience Engineering (Days 41–45)

By Day 45 Sokha Mart takes Cash, KHQR and Bank Transfer, pays suppliers, issues refunds, and keeps selling when a dependent service is slow or down.

### Day 41 — Payment Service: methods, Cash and Bank Transfer

- **Goal:** one payment model for customers, suppliers and refunds.
- **Concept:** payment as its own aggregate linked to a sale, PO or return; statuses `PENDING → SUCCEEDED / FAILED / CANCELLED`; Strategy per payment method; split payments (part cash, part KHQR); change calculation for cash.
- **Core build:** Payment Service with `PaymentMethodHandler` strategies for Cash and Bank Transfer; sale checkout creates a payment.
- **Your lab:** split payment at the POS.
- **Break it:** pay the same sale twice by double-clicking; the idempotency key from Session 6 must stop it.
- **Homework:** draw the payment state diagram including refund states.

### Day 42 — KHQR, asynchronous payment status and refunds

- **Goal:** a QR payment flow that copes with waiting for the customer.
- **Concept:** generate a KHQR code, then wait for confirmation via callback or polling; timeouts and expiry; verifying callbacks; refunds linked to sales returns; supplier payments against goods receipts.
- **Core build:** a KHQR strategy against a provider sandbox (or a simulator we write if sandbox access isn't ready); POS shows the QR and waits.
- **Your lab:** refund flow from a sales return and supplier payment from the Owner Portal.
- **Break it:** the callback arrives twice and late, after expiry; handle both safely.
- **Homework:** list the information a refund must keep for audit.

### Day 43 — Timeout, Retry, Circuit Breaker, Fallback

- **Goal:** pay off the known-risks list from Sessions 4, 7 and 8.
- **Concept:** failure modes in distributed systems; Resilience4j timeouts, retries with backoff (only for idempotent calls), circuit breaker states, fallbacks; what a sensible fallback is for each call.
- **Core build:** wrap Sales → Inventory, Sales → Payment and Business → Subscription with timeout, retry and circuit breaker.
- **Your lab:** choose and implement fallbacks (for example, cached entitlements for Subscription).
- **Break it:** add 5 seconds of latency to Inventory with a fault-injection flag; watch the breaker open and close.
- **Homework:** explain why retrying a non-idempotent call can double-charge a customer.

### Day 44 — Rate limiting and bulkheads

- **Goal:** protect sensitive APIs and keep one slow dependency from sinking a service.
- **Concept:** rate limiting at the gateway (token bucket, Redis-backed; Redis is introduced here and deepened in Session 11); per-user vs per-IP keys; bulkheads with separate thread pools or semaphores; virtual threads and their effect on bulkheads.
- **Core build:** Redis in Compose; gateway rate limits on login, registration and payment endpoints.
- **Your lab:** a bulkhead around the KHQR provider call.
- **Break it:** fire 200 login requests per second and confirm 429 responses with a `Retry-After` header.
- **Homework:** propose rate limits for five endpoints with reasons.

### Day 45 — Integration day

- **Goal:** failure and recovery as a demo, not an accident.
- **Your lab:** resilience settings moved to Config Server per environment.
- **Required test:** a circuit breaker test with WireMock simulating a failing dependency.
- **Checkpoint demo:** sell with Cash and KHQR, refund a return, pay a supplier; then kill Inventory and show what the cashier sees and how the system recovers.
- **Code review:** every remote call has timeout and a decided failure behaviour. Note what still feels fragile: Session 10 fixes it.

## Session 10 — Kafka & Event-Driven Architecture (Days 46–50)

By Day 50 a sale at Sokha Mart completes even when Inventory is down, and stock catches up through Kafka with no lost or doubled movements.

### Day 46 — Kafka fundamentals

- **Goal:** a working mental model of Kafka before touching business code.
- **Concept:** topics, partitions, offsets, brokers (KRaft mode); producers, consumers and consumer groups; ordering per partition and choosing a key (`sku_id`? `business_id`?); delivery semantics: at-most-once, at-least-once, effectively-once.
- **Core build:** Kafka in Compose; a producer and consumer with Spring Kafka; a UI (such as Kafka UI) to inspect topics.
- **Your lab:** run two consumers in one group and then in two groups; observe partition assignment.
- **Break it:** kill a consumer mid-stream and watch rebalancing and redelivery.
- **Homework:** decide the partition key for sales events and justify it.

### Day 47 — REST vs Kafka: refactor Sales → Inventory

- **Goal:** remove the coupling you felt on Day 38.
- **Concept:** decision rules: sync when the caller needs the answer now (barcode lookup, price), async when it only needs the fact recorded (stock out, reporting); domain events vs commands; eventual consistency and what the cashier sees meanwhile.
- **Core build:** Sales publishes `SaleCompleted`; Inventory consumes it and posts Stock OUT; the sync call is removed.
- **Your lab:** the same refactor for `GoodsReceived` from Purchase to Inventory.
- **Break it:** stop Inventory, make ten sales, start Inventory; stock catches up. Then crash Sales between DB commit and publish; an event is lost. That is tomorrow's problem.
- **Homework:** reclassify every service call in the system as "stays REST" or "becomes an event".

### Day 48 — Transactional Outbox

- **Goal:** never lose an event and never publish one for a rolled-back transaction.
- **Concept:** the dual-write problem; outbox table written in the same transaction; a relay that publishes and marks rows (polling publisher vs CDC with Debezium, and why we pick polling here); outbox cleanup.
- **Core build:** an outbox module in `platform-event` with a polling relay; Sales and Purchase switched to it.
- **Your lab:** Payment publishes `PaymentSucceeded` and `RefundIssued` through the outbox.
- **Break it:** repeat yesterday's crash test; the event now survives.
- **Homework:** explain the guarantee the outbox gives and the one it doesn't.

### Day 49 — Idempotent consumers, retries, dead letters, versioning

- **Goal:** consumers that are safe when Kafka delivers twice or a message is bad.
- **Concept:** at-least-once means duplicates; processed-event table keyed by event ID; retry topics with backoff; dead letter topics and how humans handle them; event versioning and contracts (additive changes, version field, consumer tolerance).
- **Core build:** idempotent consumer support in `platform-event`; retry and DLT config for Inventory.
- **Your lab:** Subscription publishes `SubscriptionChanged`; Business consumes it to update cached entitlements.
- **Break it:** replay the same event 5 times (stock changes once); send a malformed event (it lands in the DLT and the consumer keeps going).
- **Homework:** write version 2 of `SaleCompleted` with a new field without breaking version 1 consumers.

### Day 50 — Integration day

- **Goal:** the event backbone proven under failure.
- **Your lab:** a small DLT viewer and reprocess endpoint for operators.
- **Required test:** a duplicate/failed event test suite with Testcontainers Kafka.
- **Checkpoint demo (Phase D):** sell during an Inventory outage, recover, and show the ledger matches every sale exactly once.
- **Code review:** outbox usage, consumer idempotency, event contracts documented.

## Session 11 — Reporting, Notification + Admin Portal (Days 51–55)

By Day 55 Sokha Mart's owner sees today's sales and profit on a dashboard, gets a low-stock alert, and the SaaS team runs all businesses from the Platform Admin Portal.

### Day 51 — Reporting Service as an event-fed read model

- **Goal:** reports without querying other services' databases.
- **Concept:** CQRS-style read models; Reporting consumes `SaleCompleted`, `GoodsReceived`, `StockChanged`, `PaymentSucceeded`; pre-aggregated daily tables per business and branch; rebuilding a read model by replaying events.
- **Core build:** Reporting consumers and `daily_sales` / `daily_purchases` tables.
- **Your lab:** a stock valuation report per branch from `StockChanged`.
- **Break it:** drop the reporting tables and rebuild them by resetting the consumer group offset.
- **Homework:** list which reports need real time and which can be minutes behind.

### Day 52 — Profit, dashboards and Redis caching

- **Goal:** fast, correct numbers on the owner's dashboard.
- **Concept:** gross profit = sales − cost of goods sold, using cost captured at sale time; dashboard queries; Redis cache-aside, TTLs, key design with `business_id`, invalidation on events; what never to cache (balances at checkout).
- **Core build:** profit report and dashboard API cached in Redis, invalidated by incoming events.
- **Your lab:** Owner/Manager dashboard in Angular (today vs yesterday, top products, sales by branch, low stock list).
- **Break it:** measure dashboard response time cold vs warm cache; then serve stale data on purpose and fix the invalidation.
- **Homework:** write the cache key scheme for all reporting endpoints.

### Day 53 — Notification Service

- **Goal:** the right person hears about the right event.
- **Concept:** notifications as event consumers; channels (in-app, email, Telegram bot as an option); templates and language (Khmer/English); user preferences; deduplication so one low-stock event doesn't spam.
- **Core build:** low stock alert from `StockChanged` crossing a reorder level; in-app notification list with unread count.
- **Your lab:** payment received and subscription-expiring notifications.
- **Break it:** a burst of 100 stock changes on one SKU must produce one alert, not 100.
- **Homework:** design the notification preference screen.

### Day 54 — Platform Admin Portal

- **Goal:** the third Angular app, built mostly from reused parts.
- **Concept:** platform-admin realm roles vs business roles; cross-tenant access done safely and audited; support and moderation workflows (suspend business, extend trial, view as support with consent); platform reports.
- **Core build:** Admin Portal shell reusing the shared component library; business list with subscription status.
- **Your lab:** suspend/reactivate business and change plan, with audit log entries.
- **Break it:** a platform admin token tries to sell at a POS; must be denied.
- **Homework:** list which shared components each of the three apps uses.

### Day 55 — Integration day

- **Goal:** all three apps connected to the full backend.
- **Your lab:** platform report: active businesses, MRR by plan, trials ending this week.
- **Required test:** a reporting consumer test checking totals after replayed and duplicated events.
- **Checkpoint demo:** a day of sales at Sokha Mart appears on the dashboard, a low-stock alert fires, and the admin extends Sokha Mart's trial.
- **Code review:** read model ownership, cache invalidation, admin audit.

## Session 12 — Production Readiness & Deployment (Days 56–60)

By Day 60 the full system is deployed through a pipeline, monitored, backed up, and demonstrated as one complete business day at Sokha Mart.

### Day 56 — Testing strategy and security hardening

- **Goal:** confidence before release.
- **Concept:** review the test pyramid built across 11 sessions; contract tests between services and for event schemas; E2E concepts with Playwright for the three apps; hardening: dependency and image scanning, secrets out of config repos, least-privilege DB users, Keycloak production settings, security headers, CORS for real domains.
- **Core build:** a Playwright E2E test for the POS checkout; dependency scanning in the build.
- **Your lab:** one contract test between Sales and Inventory events.
- **Break it:** a seeded vulnerable dependency must fail the build.
- **Homework:** complete the security checklist for your two favourite services.

### Day 57 — Performance and observability

- **Goal:** know how the system behaves under load and see problems before users report them.
- **Concept:** query optimization (indexes, N+1, `EXPLAIN ANALYZE`, connection pool sizing); load testing with k6 or Gatling; Actuator health, liveness and readiness; Micrometer metrics to Prometheus, Grafana dashboards, log aggregation; tuning rate limits and circuit breakers from real numbers.
- **Core build:** Prometheus + Grafana in Compose; a dashboard for checkout latency, error rate and Kafka consumer lag.
- **Your lab:** load-test checkout at 50 concurrent cashiers and fix the slowest query you find.
- **Break it:** create consumer lag on purpose and find it on the dashboard.
- **Homework:** define three alerts and their thresholds.

### Day 58 — CI/CD, Docker and deployment

- **Goal:** every merge can reach production the same way.
- **Concept:** pipeline stages (build, test, scan, image, deploy); image tagging and versioning; production config via Config Server and secrets; release strategies (rolling, blue-green, canary) and which fits each service; Liquibase in deployment and backward-compatible migrations (expand/contract); backups and restore drills for PostgreSQL.
- **Core build:** GitHub Actions pipeline for one service to a staging environment.
- **Your lab:** extend the pipeline to the remaining services and the three Angular apps.
- **Break it:** deploy a migration that renames a column used by the old version; then redo it with expand/contract.
- **Homework:** run and time a backup restore.

### Day 59 — Architecture review and refactoring

- **Goal:** look at the whole system with fresh eyes.
- **Concept:** review against the standards from Day 2: service boundaries, sync vs async choices, shared library scope, duplicated logic, the known-risks list (each item resolved or consciously accepted).
- **Core build:** review two services against the Day 2 standards and apply the agreed refactors.
- **Your lab:** refactor one weak area you choose, with tests proving behaviour is unchanged.
- **Homework:** write a one-page architecture decision record (ADR) for the most important decision in the course.

### Day 60 — Final demo: one business day at Sokha Mart

- **Goal:** prove the Real System works end to end in production.
- **Final demo script:** owner logs in → orders from a supplier → manager approves → goods received, stock rises → cashier opens shift → sells with Cash and KHQR → a return and refund → Inventory is killed mid-shift and selling continues → low-stock alert fires → shift closes → dashboard shows the day's sales and profit → admin views Sokha Mart in the Admin Portal.
- **Final code review:** each student presents one service and defends its design.
- **Outcome:** Architecture → Code → Integration → Testing → Deployment → Production, as one complete system.
