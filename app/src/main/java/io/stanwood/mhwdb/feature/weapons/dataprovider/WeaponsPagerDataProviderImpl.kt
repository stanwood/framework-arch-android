package io.stanwood.mhwdb.feature.weapons.dataprovider

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.framework.arch.core.rx.ResourceTransformer
import io.stanwood.mhwdb.feature.ExceptionMessageMapper
import io.stanwood.mhwdb.interactor.GetWeaponTypesInteractor
import javax.inject.Inject

class WeaponsPagerDataProviderImpl @Inject constructor(
    private val weaponTypesInteractor: GetWeaponTypesInteractor,
    private val exceptionMapper: ExceptionMessageMapper
) :
    ViewDataProvider(),
    WeaponsPagerDataProvider {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val retrySubject = PublishSubject.create<Unit>()
    override val data =
        retrySubject
            .startWith(Unit)
            .switchMap {
                Observable.concat(
                    Observable.just(Resource.Loading()),
                    weaponTypesInteractor
                        .getWeaponTypes()
                        .compose(ResourceTransformer.fromSingle(exceptionMapper))
                        .toObservable()
                )
            }
            .replay(1)
            .autoConnect(1) { disposable += it }

    override fun retry() {
        retrySubject.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}