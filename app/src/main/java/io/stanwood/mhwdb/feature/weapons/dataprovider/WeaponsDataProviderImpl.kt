package io.stanwood.mhwdb.feature.weapons.dataprovider

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.mhwdb.interactor.GetWeaponByTypeInteractor
import javax.inject.Inject

class WeaponsDataProviderImpl @Inject constructor(val interactor: GetWeaponByTypeInteractor) :
    ViewDataProvider(), WeaponsDataProvider {

    private var weaponTypeSubject = BehaviorSubject.create<String>()
    private var disposable: CompositeDisposable = CompositeDisposable()
    override val data =
        weaponTypeSubject
            .switchMap {
                interactor.getWeapons(it).toObservable()
            }
            .replay(1)
            .autoConnect(1) { disposable += it }

    override var weaponType: String = ""
        set(value) {
            field = value
            weaponTypeSubject.onNext(value)
        }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}