/*
 * Copyright (c) 2019 stanwood GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.framework.arch.nav

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

interface NavigationTarget {
    fun navigate(navController: NavController)
}

open class Direction(
    val navDirections: NavDirections,
    val navOptions: NavOptions? = null,
    val navigatorExtras: Navigator.Extras? = null
) : NavigationTarget {

    override fun navigate(navController: NavController) {
        if (navigatorExtras != null && navOptions != null) {
            throw IllegalArgumentException(
                "You can't set both NavOptions AND Navigator.Extras.\n$navDirections\n$navOptions\n$navigatorExtras"
            )
        }

        if (navigatorExtras != null) {
            navController.navigate(navDirections, navigatorExtras)
        } else {
            navController.navigate(navDirections, navOptions)
        }
    }
}

open class Destination(
    @IdRes val destinationId: Int,
    val bundle: Bundle? = null,
    val navOptions: NavOptions? = null,
    val navigatorExtras: Navigator.Extras? = null
) : NavigationTarget {

    override fun navigate(navController: NavController) {
        navController.navigate(destinationId, bundle, navOptions, navigatorExtras)
    }
}

open class Back : NavigationTarget {

    override fun navigate(navController: NavController) {
        navController.popBackStack()
    }
}

open class Up : NavigationTarget {

    override fun navigate(navController: NavController) {
        navController.navigateUp()
    }
}