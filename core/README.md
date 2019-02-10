# Stanwood Architecture Core

The core library contains the most important basic components of our architecture such as our (very simple) definition of a `ViewModel` (don't confuse it with Google's `ViewModel`!), `ViewDataProvider`, `Resource` as well as some RxJava 2 helper classes. All other libraries depend on this library so that you rarely need to pull this in manually.

The stanwood IntelliJ plugin will generate most of its classes with dependencies on classes defined in here.