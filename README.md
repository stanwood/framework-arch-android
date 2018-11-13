[![Release](https://jitpack.io/v/stanwood/framework-arch-android.svg?style=flat-square)](https://jitpack.io/#stanwood/framework-arch-android)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)

# stanwood Architecture Utilities (Android)

A set of libraries containing homegrown architecture related utility classes.

## Import

The stanwood Architecture Utilities are hosted on JitPack. Therefore you can simply
import them by adding

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

to your project's `build.gradle`.

Then add this to you app's `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.stanwood.framework-arch-android:di:<insert latest version here>' // aar version available as well
}
```

## Usage

For usage please refer to the README's of the respective libraries. You can find them at the root of the library folders, e.g. _di/README
.md_.

## Contribute

This project follows the [Android Kotlin Code Style](https://android.github.io/kotlin-guides/style.html)
for all Kotlin classes (exception: line length = 140).

The project ships with all necessary IDE settings and checks enabled. Pre-commit we run ktlint to check for adherence. Usually running `
./gradlew ktlintFormat` will fix all errors reported by ktlint.

Our CI runs those checks as well when you create your PR.
