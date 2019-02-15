package io.stanwood.mhwdb.feature.armor.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.mhwdb.feature.armor.dataprovider.ArmorDetailsDataProvider
import io.stanwood.mhwdb.repository.mhw.Armor
import javax.inject.Inject

class ArmorDetailsViewModel @Inject constructor(private val dataProvider: ArmorDetailsDataProvider) :
    ViewModel {

    fun init(armorId: Long) {
        dataProvider.armorId = armorId
    }

    val header: Observable<Armor> =
        dataProvider.data
            .filter {
                it is Resource.Success
            }
            .map {
                (it as Resource.Success).data
            }
            .observeOn(AndroidSchedulers.mainThread())

    val items: Observable<List<ArmorDetailsItemViewModel>> =
        dataProvider.data.map {
            when (it) {
                is Resource.Success -> listOf(ArmorDetailsItemViewModel(it.data.name))
                else -> emptyList()
            }
        }
            .observeOn(AndroidSchedulers.mainThread())

    fun destroy() {
    }
}