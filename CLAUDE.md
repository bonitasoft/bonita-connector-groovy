# Bonita Groovy Connector

## Project Overview

- **Name**: Bonita Groovy Connector
- **Artifact**: `org.bonitasoft.connectors:bonita-connector-groovy`
- **Version**: 1.1.5-SNAPSHOT
- **Description**: Bonita connector that evaluates a Groovy script at runtime, with optional variable bindings passed in from the process context.
- **License**: GPL-2.0
- **Tech stack**: Java 11, Maven, Bonita Engine 7.14.0, Groovy 3.x (provided by Bonita runtime), JUnit 4, Mockito, AssertJ

## Build Commands

```bash
# Full build with tests (default goal)
./mvnw clean verify

# Skip tests
./mvnw clean verify -DskipTests

# Run tests only
./mvnw test

# Check license headers (runs at validate phase automatically)
./mvnw validate

# Apply/format license headers
./mvnw license:format

# Package connector ZIP
./mvnw clean package

# Deploy to Maven Central (requires GPG key)
./mvnw clean deploy -Pdeploy
```

The build produces:
- `target/bonita-connector-groovy-<version>.jar`
- `target/bonita-connector-groovy-<version>-*.zip` — Bonita connector assembly

## Architecture

### Class hierarchy

```
AbstractConnector (bonita-common)
  └── GroovyScriptConnector    # Single connector class; the entire implementation
```

### Key patterns

- **Minimal surface area**: The entire connector is one class (`GroovyScriptConnector`).
- **Script evaluation**: Uses `groovy.lang.GroovyShell` with a `Binding` populated from the `variables` input parameter (a `List<List<Object>>` of key-value pairs).
- **API accessor injection**: If a variable key matches `ExpressionConstants.API_ACCESSOR`, the Bonita `APIAccessor` is injected into the binding rather than a plain value.
- **Input parameters**:
  - `script` (String) — the Groovy script text; mandatory.
  - `variables` (List<List<Object>>) — optional list of `[name, value]` pairs bound into the script.
- **Output parameter**: `result` (Object) — the return value of the evaluated script.
- **License check**: `license-maven-plugin` enforces the GPL header on all `.java` files at the `validate` phase.
- **Resources**: Connector `.def` and `.impl` descriptors in `src/main/resources-filtered/`, filtered with Maven properties at build time.

## Testing Strategy

- Framework: JUnit 4 + AssertJ + Mockito
- Tests verify script execution, variable binding, API accessor injection, and null-script validation.
- No external services required; `GroovyShell` is exercised directly.
- Coverage enforced via JaCoCo.
- SonarCloud project key: `bonitasoft_bonita-connector-groovy`.

## Commit Message Format

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <short description>

[optional body]

[optional footer(s)]
```

Common types: `feat`, `fix`, `chore`, `refactor`, `test`, `docs`, `ci`.

Examples:
```
fix: propagate GroovyRuntimeException as ConnectorException
chore: bump Bonita engine to 7.15.0
test: add test for API accessor injection in binding
```

## Release Process

1. Remove `-SNAPSHOT` from `version` in `pom.xml`.
2. Update `groovy.def.version` if the connector definition changed.
3. Commit: `chore: release X.Y.Z`.
4. Tag: `git tag X.Y.Z`.
5. Deploy to Maven Central: `./mvnw clean deploy -Pdeploy` (requires GPG key and Central credentials in `~/.m2/settings.xml`).
6. Push tag: `git push origin X.Y.Z`.
7. Bump to next `-SNAPSHOT` and commit: `chore: prepare next development iteration`.
