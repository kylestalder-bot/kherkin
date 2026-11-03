# kherkin security model

Threat model of kherkin as of `03e6de7`. Statements marked *maintainer* are maintainer decisions; the rest describes the code. Security scans use the component names below to tag findings. Update this file when the build, release or trust boundaries change.

## System overview
kherkin (`com.progressive.kherkin`) is an open-source Kotlin/Android **test library** of Gherkin-style (Given/When/Then) UI-test steps on top of Espresso, UI Automator and the Compose testing APIs. It is published to Maven Central as the `common`, `espresso` and `compose` AARs. Consumers add it as an `androidTestImplementation` dependency, so it runs only inside instrumented test APKs on test devices and emulators, never in production apps. `sampleapp` is a demo app that exists to be the target of kherkin's own instrumented tests. There is no server or backend.

## Components
| Component | Code | Notes |
|---|---|---|
| **Common** | `common/` | Shared test core: `BaseIntegrationTestCase`, `InstrumentationTestRunner`, screen/trait model, `IntegrationTestLogger` (logcat), `IntegrationTestFailureCapture` (writes screenshots and `retryTest.txt` to `Environment.getExternalStorageDirectory()/screenshots/<className>/`) |
| **Espresso Steps** | `espresso/` | Espresso and UI Automator steps, custom ViewActions/matchers (`CustomViewAction.kt`, WebView handling), `StepNavigator` |
| **Compose Steps** | `compose/` | `ComposeTestRule` steps, `ComposeStepNavigator`, `TraitVerifier` |
| **Sample App** | `sampleapp/` | Demo app plus kherkin's androidTest suites. Exported launcher `MainActivity` (`singleTask`) that reads a serializable intent extra in `MainFragment.kt`; `WebViewActivity` loads a fixed `R.string.web_url`; `allowBackup=true`; `INTERNET` permission. Not published. |
| **Build & Release** | `build-logic/` (`SharedPublishPlugin.kt`: vanniktech maven-publish, `signAllPublications()`, version from `git describe --tags`), `build.gradle.kts`, `settings.gradle.kts` (`gradlePluginPortal()`, `google()`, `mavenCentral()`), `gradle/` (version catalog, wrapper 9.5.0), `gradle.properties`, `gradlew*` | Dependency resolution and Maven Central publishing/signing configuration. No dependency verification metadata. |
| **CI** | `.github/workflows/build.yml` | `run_tests` (unit tests + managed emulator UI tests) on push to main, tags and `pull_request` to main. `publish_release` (`needs: run_tests`, **no event or ref guard**) runs `./gradlew publishToMavenCentral` with `JRELEASER_MAVENCENTRAL_USERNAME/PASSWORD` and `JRELEASER_GPG_SECRET_KEY/PASSPHRASE`. Actions are pinned by tag (`@v4`), not SHA. |

## Trust boundaries and entry points
1. **Contributor -> repository/CI**: pull requests run repo-controlled Gradle code. Fork PRs get no secrets on `pull_request`, but same-repo branch PRs reach `publish_release` with secrets.
2. **CI -> Maven Central**: `publish_release` holds the publishing and signing secrets.
3. **Upstream -> build**: plugins, libraries and the Gradle distribution are downloaded from the Plugin Portal, Google Maven, Maven Central and services.gradle.org; GitHub Actions from their tags.
4. **kherkin -> consumer test process**: published AARs run with instrumentation privileges in the app-under-test's process (the app's manifest permissions apply).
5. **Device -> Sample App**: other apps on a device can launch the exported `MainActivity` with extras; the WebView loads remote content.

## Assets and sensitivity
All of these matter equally (*maintainer*):
1. Maven Central credentials and GPG signing key (CI secrets): their compromise allows publishing signed malicious `com.progressive.kherkin` artifacts.
2. Integrity of published artifacts: a tampered release runs in every consumer's instrumented tests and CI.
3. Data on consumers' test devices: failure screenshots on shared external storage and logcat output can capture the app-under-test's screens and test-account data.
4. Integrity of the repository and build (build-logic, wrapper, dependencies).

## Attacker profiles
All are in scope (*maintainer*: nothing is out of scope):
- A malicious contributor opening a PR that changes build scripts, the workflow or code.
- A compromised or typosquatted upstream dependency, Gradle plugin or GitHub Action, or dependency confusion across the three Maven repositories.
- Someone with push access to a non-tag branch triggering an unintended publish.
- A co-resident app on a shared test device reading external storage or logcat, or sending intents to Sample App activities.

## Existing controls
- GitHub does not expose secrets to `pull_request` workflows from forks.
- `publish_release` depends on `run_tests`.
- Artifacts are GPG-signed; the signing key is supplied in memory through env vars and is not committed.
- The wrapper and the version catalog pin versions (no checksums).

## Out of scope / accepted risk
**Nothing** (*maintainer*): every finding keeps its full severity, including Sample App, androidTest code and test-device runtime behaviour.

## Infrastructure
No infrastructure repository (*maintainer*). GitHub Actions and Maven Central are the whole release infrastructure.

## Finding tags
- `Component`: one of the components above, from the finding's file path.
- `CWE`: the finding's CWE ID.
