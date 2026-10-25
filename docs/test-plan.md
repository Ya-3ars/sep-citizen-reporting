# Test Plan — City Reports

Status: draft (WS 2026/27). Owner: Louis.
This document defines **how** the City Reports system will be tested. It is written
before implementation so the test cases describe intended behaviour and act as the
target the implementation is built toward. Test execution happens in the
integration/QA phase once the code exists.

This plan is the *Softwaretest* (dynamic) half of Verification & Validation.
- **Verification** — does the product meet the requirements?
- **Validation** — are the users (see personas) satisfied in their work environment?

---

## 1. Scope & strategy

- **In scope:** all functional requirements from the Pflichtenheft, tested across the
  four levels below (unit → integration → system → acceptance). The citizen submit
  flow and the staff view/edit/delete flow are the priority paths.
- **Out of scope (for this project):** large-scale load/performance testing,
  security penetration testing, cross-browser matrix testing beyond one modern
  browser. (Noted here so the boundary is explicit.)

## 2. Test levels

| Level | What it checks | Tool | Component |
|-------|----------------|------|-----------|
| Unit | a single class/method in isolation (e.g. ReportService logic, validation) | JUnit 5 | backend |
| Integration | layers working together (Controller → Service → Repository → DB) | Spring Boot Test + MockMvc | backend |
| System | the running app end-to-end via its HTTP API | manual + Postman/curl | backend + clients |
| Acceptance (UAT) | does the app satisfy the user stories / personas | manual checklist | Android + Web |

Regression: whenever a bug is fixed, the test that would have caught it is added so
it cannot silently return.

## 3. Test cases (derived from use-cases)

> **TODO — pending use-cases (board #12).** The formal test cases will be written
> here once the UML use-case diagram is defined, since each test case is derived
> directly from a use-case. The required format for every case is:
> **ID · Vorbedingung (precondition) · Eingabedaten (input) · Aktion (action) · erwartetes Ergebnis (expected result).**
>
> Example of the intended format (to be expanded from the real use-cases):
>
> | ID | Precondition | Input | Action | Expected result |
> |----|--------------|-------|--------|-----------------|
> | TC-01 | app running | valid title, description, lat/long | POST /api/reports | HTTP 201; report saved; status = OPEN; created_at set |
> | TC-02 | app running | latitude missing | POST /api/reports | HTTP 400; validation error; nothing saved |
> | TC-03 | 1 report exists | that report's id | DELETE /api/reports/{id} | HTTP 204; report and its photos removed |
>
> Target: at least one test case per use-case, including the main (happy) path and
> the key error/edge cases.

## 4. Test data

- A set of valid sample reports (one per category: defect, pollution, parking).
- Edge cases: report with no photo; report at coordinate boundaries; overly long
  title/description; missing required fields.
- A seeded staff user for the staff-only endpoints.

## 5. Tooling (Werkzeugeinsatz)

- **JUnit 5** — backend unit tests (already available via spring-boot-starter-test).
- **Spring Boot Test + MockMvc** — backend integration tests against the web layer.
- **Postman / curl** — manual system-level API checks.
- Code changes are reviewed informally via GitHub pull requests before merge.
