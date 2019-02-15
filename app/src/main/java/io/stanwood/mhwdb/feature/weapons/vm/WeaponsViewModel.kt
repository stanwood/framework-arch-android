package io.stanwood.mhwdb.feature.weapons.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.nav.NavigationTarget
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsDataProvider
import javax.inject.Inject

class WeaponsViewModel @Inject constructor(val dataProvider: WeaponsDataProvider) : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    val navigator = navigation.firstElement()
    val items: Observable<List<WeaponsItemViewModel>> = dataProvider.data.map { list ->
        if (list is Resource.Success) {
            list.data.map {
                WeaponsItemViewModel(it.name, "", it.image)
            }
        } else {
            emptyList()
        }
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun init(weaponTypeId: String) {
        dataProvider.weaponType = weaponTypeId
    }

    fun itemClicked(item: WeaponsItemViewModel) {
        // TODO: Add click handling
    }

    fun destroy() {
    }
}