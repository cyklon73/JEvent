![[Java CI]](https://github.com/Utils4J/JEvent/actions/workflows/check.yml/badge.svg)
![[Latest Version]](https://maven.mineking.dev/api/badge/latest/releases/de/cyklon/JEvent?prefix=v&name=Latest%20Version&color=0374b5)

# JEvent

JEvent provides a powerful and lightweight event system based on the syntax of the [Spigot Event System](https://www.spigotmc.org/wiki/using-the-event-api/).

# Installation

JEvent is hosted on a custom repository at [https://maven.mineking.dev](https://maven.mineking.dev/#/releases/de/cyklon/JEvent). Replace VERSION with the lastest version (without the `v` prefix).
Alternatively, you can download the artifacts from jitpack (not recommended).

### Gradle

```groovy
repositories {
  maven { url "https://maven.mineking.dev/releases" }
}

dependencies {
  implementation "de.cyklon:JEvent:VERSION"
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>cyklon</id>
    <url>https://maven.mineking.dev/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.cyklon</groupId>
    <artifactId>JEvent</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```
