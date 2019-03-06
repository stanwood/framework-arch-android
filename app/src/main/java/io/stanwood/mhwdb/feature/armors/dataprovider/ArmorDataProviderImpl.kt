package io.stanwood.mhwdb.feature.armors.dataprovider

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.framework.arch.core.rx.ResourceTransformer
import io.stanwood.mhwdb.feature.ExceptionMessageMapper
import io.stanwood.mhwdb.feature.armors.vm.ArmorItem
import io.stanwood.mhwdb.interactor.GetArmorInteractor
import javax.inject.Inject

class ArmorDataProviderImpl @Inject constructor(
    private val armorInteractor: GetArmorInteractor,
    private val exceptionMapper: ExceptionMessageMapper
) : ViewDataProvider(), ArmorDataProvider {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val retrySubject = PublishSubject.create<Unit>()

    override val data =
        retrySubject
            .startWith(Unit)
            .switchMap { Observable.concat(Observable.just(Resource.Loading()), mappedArmorSets) }
            .replay(1)
            .autoConnect(1) { disposable += it }

    private val mappedArmorSets
        get() = armorInteractor.getArmor()
            .map { res ->
                res.associateBy(
                    { ArmorItem.SetViewModel(it.id, "${it.name} (${it.rank})") },
                    { armor ->
                        armor.pieces.map {
                            ArmorItem.ArmorViewModel(it.id, it.name, it.image, it.type)
                        }
                    })
            }
            .compose(ResourceTransformer.fromSingle(exceptionMapper))
            .toObservable()

    override fun retry() {
        retrySubject.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}