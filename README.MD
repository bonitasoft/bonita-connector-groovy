# Groovy Connector

[![Actions Status](https://github.com/bonitasoft/bonita-connector-groovy/workflows/Build/badge.svg)](https://github.com/bonitasoft/bonita-connector-groovy/actions?query=workflow%3ABuild)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=bonitasoft_bonita-connector-groovy&metric=alert_status)](https://sonarcloud.io/dashboard?id=bonitasoft_bonita-connector-groovy)
[![GitHub release](https://img.shields.io/github/v/release/bonitasoft/bonita-connector-groovy?color=blue&label=Release)](https://github.com/bonitasoft/bonita-connector-groovy/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.bonitasoft.connectors/bonita-connector-groovy.svg?label=Maven%20Central&color=orange)](https://search.maven.org/search?q=g:%22org.bonitasoft.connectors%22%20AND%20a:%22bonita-connector-groovy%22)
[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-yellow.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)

## Compatibility

| Connector           | Bonita          | Java         | Groovy (provided by the Bonita runtime) |
|---------------------|-----------------|--------------|-----------------------------------------|
| 2.x (`master`)      | 12.0 and later  | 17 and later | 4.x (`org.apache.groovy`)               |
| 1.x (up to 1.1.4)   | 7.14 to 11.x    | 11 and later | 3.x (`org.codehaus.groovy`)             |

The connector definition (`scripting-groovy-script` 1.0.1) and its inputs/outputs are unchanged. The scripts are
evaluated with the Groovy of the Bonita runtime: on Bonita 12 they run with Groovy 4, whose changes apply to them
(for example, `groovy.util.XmlSlurper`/`XmlParser` moved to `groovy.xml`).

On a runtime running a security manager (the Bonita 12 Docker image runs ProGrade, `PRO_GRADE=true`), the scripts run
under the restrictions of the `file:/groovy/shell` code base: no environment variable read, no JVM exit, no process
execution, no system property write, no file write outside the temporary directory, no read of sensitive files
(`/etc/passwd`, Tomcat `conf/`...).

## Build

__Clone__ or __fork__ this repository, then at the root of the project run:

`./mvnw`

## Release

In order to create a new release:
- On the release branch, make sure to update the pom version (remove the -SNAPSHOT)
- Run the [Create release workflow](https://github.com/bonitasoft/bonita-connector-groovy/actions/workflows/release.yml), set the version to release as parameter
- Update the `master` with the next SNAPSHOT version.

Once this is done, update the [Bonita marketplace repository](https://github.com/bonitasoft/bonita-marketplace) with the new version of the connector.

## Contributing

We would love you to contribute, pull requests are welcome! Please see the [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## License

The sources and documentation in this project are released under the [GPLv2 License](LICENSE)
