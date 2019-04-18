# Stanwood Architecture Navigation

We handle navigation by means of the [Navigation Architecture Component](https://developer.android.com/topic/libraries/architecture/navigation/). 

This library provides helper classes for simplified and more consistent navigation handling. This is mainly used by the IntelliJ plugin.

The central class is `NavigationTarget`. This class encapsulates navigation and can easily be extended for other means than classic Architecture Components supported navigation such as dialogs. A `NavigationTarget` is usally passed via RX from the ViewModel to the Fragment.

In the Fragment you can use `NavigationTarget.navigate()` to execute navigation.

Additionally this library brings some extension functions for BottomNavigationViews, NavigationViews and Toolbars which all help you when implementing navigation.

The plugin supports you in setting all those things up.

