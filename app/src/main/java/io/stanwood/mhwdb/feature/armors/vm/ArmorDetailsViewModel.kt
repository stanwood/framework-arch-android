package io.stanwood.mhwdb.feature.armors.vm

import io.reactivex.android.schedulers.AndroidSchedulers
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDetailsDataProvider
import javax.inject.Inject

class ArmorDetailsViewModel @Inject constructor(private val dataProvider: ArmorDetailsDataProvider) :
    ViewModel {

    fun init(armorId: Long) {
        dataProvider.armorId = armorId
    }

    val data =
        dataProvider.data
            .filter { it.data != null }
            .map { it.data!! }
            .observeOn(AndroidSchedulers.mainThread())!!

    val items =
        data.map {
            listOf(ArmorDetailsItemViewModel(it.name))
        }!!

    fun destroy() {
    }
}