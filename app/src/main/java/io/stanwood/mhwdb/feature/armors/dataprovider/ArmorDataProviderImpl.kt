package io.stanwood.mhwdb.feature.armors.dataprovider

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.mhwdb.feature.armors.vm.ArmorItem
import io.stanwood.mhwdb.interactor.GetArmorInteractor
import javax.inject.Inject

class ArmorDataProviderImpl @Inject constructor(val armorInteractor: GetArmorInteractor) : ViewDataProvider(), ArmorDataProvider {

    private var disposable: CompositeDisposable = CompositeDisposable()
    override val data = armorInteractor.getArmor()
        .map { res ->
            when (res) {
                is Resource.Success ->
                    Resource.Success(
                        res.data.associateBy(
                            { ArmorItem.SetViewModel(it.id, "${it.name} (${it.rank})") },
                            { armor -> armor.pieces.map { ArmorItem.ArmorViewModel(it.id, it.name, it.image, it.type) } }).toMap()
                    )
                is Resource.Failed -> Resource.Failed(res.e, null)
                else -> Resource.Loading()
            }
        }
        .subscribeOn(Schedulers.io())
        .toObservable()
        .replay(1)
        .autoConnect(1) { disposable += it }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}