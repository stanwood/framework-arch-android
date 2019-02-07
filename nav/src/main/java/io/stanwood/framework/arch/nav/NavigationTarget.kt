package io.stanwood.framework.arch.nav

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

data class NavigationTarget(val navDirections: NavDirections, val navOptions: NavOptions? = null)