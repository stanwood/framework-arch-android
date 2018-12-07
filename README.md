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
    implementation 'com.github.stanwood:framework-arch-android:<insert latest version here>' // aar version available as well
}
```

## Usage

For usage please refer to the README's of the respective libraries. You can find them at the root of the library folders, e.g. _di/README
.md_.

The sample app is located in a [separate repository](https://github.com/stanwood/architecture_sample_github_android) (private for now) to ensure that we package all changes properly and ready to use. We might merge both projects at a later stage.

Find general information below.

## Packaging

We package our classes as follows (WIP):

![Packages](https://imgur.com/PVvcczQ.png)

Generic (app-wide) components belong into the root folder packages while those for specific features are to be moved into the specific feature folders.

_For example_: A `RecordingInteractor` in a video recording app is to be used by many different features and thus resides in the `interactor` package. A `RecordingListInteractor` would just be used within the `recordinglist` feature and thus resides in `feature.recordinglist.interactor`. The models the interactor passes to the outside and accepts should be stored in a `model` subpackage of the respective `interactor` package.

## ViewModels

ViewModels are the glue between data layer and UI. At stanwood we use Android ViewModels as base class.

We use RxJava to transport data to the ViewModel and LiveData for communication between ViewModel and Fragment and UI (XML by means of data
binding).

A typical ViewModel looks like this (dependencies are usually injected via dagger, refer to the `di` library for details):

```kotlin
class HomeViewModel @Inject constructor(
    homeInteractor: HomeInteractor // usually we have at least one interactor to for fetching data
) : ViewModel() { // Android ViewModel

    val text by lazy {
        homeInteractor.getData()
            .toFlowable()
            .map {
                it.description
            }
            .observeOn(AndroidSchedulers.mainThread()) // from io.reactivex.rxjava2:rxandroid
            .toLiveData() // from androidx.lifecycle:lifecycle-reactivestreams-ktx
    }

}
```

A planned sub-library will provide all the necessary dependencies.

For details on how to get hold of the ViewModel in your fragment check out the `di` Fragment documentation.


## Contribute

This project follows the [Android Kotlin Code Style](https://android.github.io/kotlin-guides/style.html)
for all Kotlin classes (exception: line length = 140).

The project ships with all necessary IDE settings and checks enabled. Pre-commit we run ktlint to check for adherence. Usually running `
./gradlew ktlintFormat` will fix all errors reported by ktlint.

Our CI runs those checks as well when you create your PR.
