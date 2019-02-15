package io.stanwood.mhwdb.feature.weapons.dataprovider

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.mhwdb.interactor.GetWeaponTypesInteractor
import javax.inject.Inject

class WeaponsPagerDataProviderImpl @Inject constructor(val weaponTypesInteractor: GetWeaponTypesInteractor) : ViewDataProvider(),
    WeaponsPagerDataProvider {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val querySubject = BehaviorSubject.create<String>()
    override val data = weaponTypesInteractor
        .getWeaponTypes()
        .toObservable()
        .replay(1)
        .autoConnect(1) { disposable += it }


    override fun onCleared() {
        super.onCleared()
    }
}