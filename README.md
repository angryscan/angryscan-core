# Angry Data Core

Angry Data Core is a Kotlin Multiplatform library for locating sensitive data (PII) inside free-form text. The project focuses on Russian banking and personal identifiers and ships with tuned regular expressions, contextual validation logic, and two execution engines: a high-performance HyperScan backend for the JVM and a portable Kotlin regex engine that runs everywhere Kotlin does. Native bindings are also provided so the scanner can be embedded from non-JVM applications.

## Features
- Kotlin Multiplatform distribution with JVM, JS, and Native targets published to Maven Central.
- Dual detection engines: `HyperScanEngine` (Hyperscan powered, `AutoCloseable`) for throughput-sensitive JVM workloads and `KotlinEngine` for portable scanning.
- Hardened matchers for bank card numbers (Luhn + BIN validation), CVV/CVC, bank account numbers, Russian passports, SNILS, OMS, INN, phone numbers, vehicle licence plates, full names, addresses, logins, passwords, e-mail addresses, IPv4/IPv6, and customizable user signatures.
- `Match` results include the detected value, ten characters of surrounding context, and absolute character offsets to simplify redaction or highlighting.
- Built-in matcher registry (`org.angryscan.common.extensions.Matchers`) plus Kotlin Serialization support, making it easy to persist engine configurations or ship them over the wire.
- Kotlin/Native shared library (`AngryData.dll`/`libAngryData.so`/`libAngryData.dylib`) with C ABI exports for integrations such as Python.

## Modules
- `:kotlin-lib` - core multiplatform library with engines, matchers, shared constants, and publishing settings.
- `:native-lib` - Kotlin/Native wrapper that exposes C-callable detection functions backed by the core library.
- `python-example` - `ctypes` demo that consumes the native shared library and prints detection results.

## Installation
All artifacts are published under the `org.angryscan` group.

### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // Multiplatform artifact; usable from JVM or MPP projects
    implementation("org.angryscan:core:1.3.5")
}
```

### Maven
```xml
<dependency>
    <groupId>org.angryscan</groupId>
    <artifactId>core</artifactId>
    <version>1.3.5</version>
</dependency>
```

### Kotlin/JS (Gradle)
```kotlin
dependencies {
    implementation("org.angryscan:core-js:1.3.5")
}
```

> Note: The HyperScan backend relies on the `com.gliwka.hyperscan` wrapper. Ensure your runtime platform is supported by that dependency; otherwise fall back to the portable `KotlinEngine`.

## Usage

### Detecting with HyperScan on the JVM
```kotlin
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.extensions.Matchers

val text = """
    Client Ivan Ivanov lives at Moscow, Tverskaya street 1.
    Card 4276 8070 1492 7948 (CVV 123), phone +7 (916) 123-45-67 and email ivanov@example.org.
""".trimIndent()

HyperScanEngine(Matchers.filterIsInstance<IHyperMatcher>()).use { engine ->
    val matches = engine.scan(text)
    matches.forEach { match ->
        println("${match.matcher.name}: ${match.value} at ${match.startPosition}-${match.endPosition}")
    }
}
```

### Portable detection with KotlinEngine
```kotlin
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.extensions.Matchers

val engine = KotlinEngine(Matchers.filterIsInstance<IKotlinMatcher>())
val matches = engine.scan(text)
```

### Customising the matcher set
```kotlin
import org.angryscan.common.matchers.CardNumber
import org.angryscan.common.matchers.UserSignature
import org.angryscan.common.engine.kotlin.KotlinEngine

val customEngine = KotlinEngine(
    listOf(
        CardNumber(checkCardBins = false),            // skip BIN validation for testing data
        UserSignature(
            name = "Corporate stamp",
            searchSignatures = mutableListOf("OOO Romashka", "ZAO Test")
        )
    )
)
```

### Result structure
Each `Match` contains:

| Field | Description |
| --- | --- |
| `value` | Extracted token. |
| `before` / `after` | Up to ten characters of context on each side. |
| `startPosition` / `endPosition` | Absolute offsets counted from zero. |
| `matcher` | Reference to the matcher that produced the hit. |

Use this metadata to redact or highlight findings in downstream systems.

## Building and Testing

```bash
# Run all tests (JVM + JS + Native)
./gradlew check

# JVM-specific tests
./gradlew :kotlin-lib:jvmTest

# Produce a fat JAR with all dependencies
./gradlew :kotlin-lib:jvmShadowJar
```

> On Windows, use `gradlew.bat` instead of `./gradlew`.

### Native shared library

```bash
./gradlew :native-lib:linkNativeReleaseShared
```

The command produces `native-lib/build/bin/native/releaseShared/AngryData.*`. The Python demo in `python-example/interop.py` shows how to load this library with `ctypes`, call functions such as `detectPassport`, and clean detected text using the exported utilities.

## Contributing
- Keep additions ASCII unless the feature requires Cyrillic or other Unicode characters already present in the codebase.
- Add or update tests for new matchers or engine features.
- Run `./gradlew check` before opening a pull request.

## License

Angry Data Core is distributed under the Apache License 2.0. See `LICENSE` for details.
