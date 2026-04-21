# multiapps — MTA Model Library

## Project Role

This is the **multiapps** repository (`org.cloudfoundry.multiapps`). It is a shared Java
library providing model objects, YAML parsers, and validators for the SAP Multitarget
Application (MTA) specification. It serves as a foundational dependency for the
Cloud Foundry MultiApps Controller.

## Security Boundary

This is an **OPEN SOURCE** repository. Never introduce proprietary logic, credentials,
or internal company context into this codebase.

## Tech Stack

- **Java 25**, compiled with `maven-compiler-plugin` (source/target 25)
- **JUnit 5** (`junit-jupiter` 6.x) + **Mockito** for testing
- **Maven** multi-module build (parent POM: `multiapps-parent`)

## Maven Modules

| Module                  | Purpose                                                        |
|-------------------------|----------------------------------------------------------------|
| `multiapps-common`      | Shared utilities: YAML handling (SnakeYAML), JSON (Jackson), XML binding (JAXB), commons |
| `multiapps-common-test` | Shared test utilities and helpers (JUnit/Mockito at compile scope) |
| `multiapps-mta`         | MTA model objects, parsers, and validators (depends on `multiapps-common`) |
| `multiapps-coverage`    | Aggregates JaCoCo coverage reports across all modules (POM-only) |

## Build Profiles

| Profile    | Purpose                                              |
|------------|------------------------------------------------------|
| `coverage` | Enables JaCoCo agent for code coverage collection    |
| `sonar`    | Runs SonarQube analysis (reports to sonarcloud.io)   |
| `release`  | Signs artifacts (GPG), attaches sources/javadoc, publishes to Maven Central via Sonatype |

## Build & Test Commands

```bash
# Full build with tests
mvn clean install

# Run tests only
mvn test

# Build, skip tests (for quick local iteration)
mvn clean install -DskipTests

# Build a single module (e.g., multiapps-mta)
mvn clean install -pl multiapps-mta -am

# Build with coverage
mvn clean verify -Pcoverage

# Run SonarQube analysis (requires sonar token)
mvn clean verify -Pcoverage,sonar
```

## Formatting Rule

Before completing any task or committing code, you **MUST** run:

```bash
mvn spotless:apply
```

This ensures the codebase follows the standard formatting rules. The Eclipse formatter
configuration files are located in the `ide/` directory for IDE integration.

## Dependency Notes

- Downstream consumers reference this library via the `multiapps.version` property in
  their POM files (e.g., `multiapps-controller/pom.xml`).
- When changing public API (model classes, validator interfaces), coordinate updates in
  downstream repos before merging.
- Build order for cross-repo changes: `multiapps` -> `multiapps-controller`.

## Key Libraries

- **SnakeYAML** — YAML parsing for MTA descriptors
- **Jackson 3.x** — JSON serialization/deserialization
- **Immutables** — annotation-processed value objects (provided scope)
- **Semver4j** — semantic versioning for MTA schema version dispatch
- **Commons Compress** — archive handling for `.mtar` files
