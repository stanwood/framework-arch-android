package io.stanwood.mhwdb.feature.weapons.vm

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsPagerDataProvider
import javax.inject.Inject

class WeaponsPagerViewModel @Inject constructor(private val dataProvider: WeaponsPagerDataProvider) : ViewModel {

    val items = dataProvider.data
        .map {
            if (it is Resource.Success) {
                it.data
            } else {
                emptyList()
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun destroy() {
    }
}