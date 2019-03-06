package io.stanwood.mhwdb.feature.container.vm

import androidx.annotation.IdRes
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.nav.NavigationTarget
import io.stanwood.mhwdb.NavGraphContainerDirections
import io.stanwood.mhwdb.R
import javax.inject.Inject

class ContainerViewModel @Inject constructor() : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    val navigator: Observable<NavigationTarget> = navigation

    fun selectMenuItem(@IdRes id: Int, @IdRes currentSelectedId: Int) =
        if (id == currentSelectedId) {
            true
        } else when (id) {
            R.id.action_armor -> NavGraphContainerDirections.showArmor()
            R.id.action_weapon -> NavGraphContainerDirections.showWeapons()
            R.id.thirdItem -> NavGraphContainerDirections.showArmor()
            else -> null
        }
            ?.let {
                navigation.onNext(NavigationTarget(it))
                true
            }
            ?: false

    fun destroy() {
    }
}