# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

A small Java teaching project ("Bestellung mit Kundenregistrierung" — order placement with
customer registration) implementing a three-tier architecture with RMI (application ↔
presentation) and JDBC (application ↔ database), deliberately built to mirror three reference
diagrams from the course: the architecture diagram, the flow diagram, and the data model. Code,
comments, and console output are in German. Full walkthrough of the intended design is in
`Bestellsystem/ANLEITUNG.md` — read it before making non-trivial changes, since the mapping from
diagrams to classes/methods is meant to stay exact.

## Build & Run

No build tool (no Maven/Gradle) — plain `javac`/`java`, developed in IntelliJ (project JDK:
openjdk-26). The IntelliJ module (`Bestellsystem_2.iml`) treats `Bestellsystem/src` as the sole
source root and depends on the `mariadb-java-client-3.5.9` library (declared in
`.idea/libraries/`, not vendored in the repo — must be added manually via Project Structure →
Libraries if opening fresh).

Prerequisites: XAMPP (MariaDB + Apache) running, with the schema loaded via phpMyAdmin from
`Bestellsystem/datenbank.sql` (creates database `shop`).

Command-line build/run (from repo root):
```
javac -d out Bestellsystem/src/modell/*.java Bestellsystem/src/datenhaltung/*.java Bestellsystem/src/anwendung/*.java Bestellsystem/src/praesentation/*.java
java -cp "out;mariadb-java-client-3.5.9.jar" anwendung.Server
java -cp "out;mariadb-java-client-3.5.9.jar" praesentation.Client
```
(Windows classpath separator is `;`; Linux/Mac uses `:`.) The server must be started and left
running before the client connects — `Client.main` does an RMI lookup on
`rmi://localhost/Bestelldienst`, and `Server.main` binds it against `LocateRegistry.createRegistry(1099)`.

In IntelliJ: run `Server.java`, then run `Client.java` with the server still active.

There are no automated tests and no linter configured.

## Architecture

Three layers, one package each, wired together by a single RMI facade:

- **`praesentation`** (`Client`) — console UI only. Four methods map 1:1 to the four boxes in the
  architecture diagram: `zeigeWarenkorb`, `zeigeFormular`, `zeigeBestaetigung`,
  `zeigeStornierung`. Talks *only* to the `Bestelldienst` RMI interface, never to individual
  services or repositories directly.
- **`anwendung`** — business logic, one narrow service per concern: `KundenService` (registration
  checks / customer creation), `PreisService` (total price), `VersandService` (stock check +
  ship-date calculation, and decrementing stock after an order), `BestellService` (create/cancel/
  ship an order), `BenachrichtigungsService` (console-only "notification", no real email).
  `BestelldienstImpl` is the *only* class exposed over RMI; it has almost no logic of its own —
  it just fans each interface method out to the one service that owns it, so the client needs a
  single connection instead of one per service. `Server` just starts the RMI registry and binds
  `BestelldienstImpl`.
- **`datenhaltung`** — one repository per table (`KundenRepository`, `BestellRepository`,
  `ArtikelRepository`), matching the architecture diagram exactly. Each repository opens its own
  JDBC connection independently (`jdbc:mariadb://localhost/shop`, user `root`, no password) —
  intentionally not shared/pooled, to keep each class self-contained and understandable in
  isolation (see "Was bewusst einfacher gehalten wurde" in ANLEITUNG.md).
- **`modell`** — plain data classes matching the data model: `Kunde`, `Artikel`, `Position`,
  `Bestellbestaetigung`.

Request flow for placing an order (`BestelldienstImpl.erstelleBestellung`), which is the central
piece of logic tying the services together: `PreisService` computes the total →
`VersandService` checks availability and returns a ship date → `BestellService` persists the
order using those computed values → `VersandService` decrements stock → `BenachrichtigungsService`
prints a confirmation notice → a `Bestellbestaetigung` is returned to the client.

Data model (`Bestellsystem/datenbank.sql`, database `shop`): `kunde`, `artikel`, `bestellung`
(status is one of `offen` / `storniert` / `verschickt`), and `bestellung_artikel` as the
cart/line-items join table (`bestellnr` + `artikelnr` → `menge`).

## Known intentional simplifications (see ANLEITUNG.md)

- The registration form asks for a password but it is never stored — there is no login/auth in
  this project.
- "Notifications" are `System.out` prints, not real emails.
- Each repository manages its own DB connection rather than sharing one.

## Auto-push watcher

`auto-push.ps1` (gitignored, not part of the app) is a local background script that polls
`git status` every 10s and auto-commits/pushes any change to `origin/main` — see `auto-push.log`
for its activity. It runs independently of Claude Code sessions.
