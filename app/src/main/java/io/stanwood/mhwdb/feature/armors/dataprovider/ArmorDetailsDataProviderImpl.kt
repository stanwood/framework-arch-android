package io.stanwood.mhwdb.feature.armors.dataprovider

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.stanwood.framework.arch.core.ViewDataProvider
import io.stanwood.framework.arch.core.rx.ResourceTransformer
import io.stanwood.mhwdb.interactor.GetArmorByIdInteractor
import javax.inject.Inject

class ArmorDetailsDataProviderImpl @Inject constructor(val interactor: GetArmorByIdInteractor) : ViewDataProvider(),
    ArmorDetailsDataProvider {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private val idSubject = BehaviorSubject.create<Long>()
    override var armorId: Long = 0
        set(value) {
            field = value
            idSubject.onNext(field)
        }
    override val data =
        idSubject.switchMap {
            interactor.getArmor(it).compose(ResourceTransformer.fromSingle()).toObservable()
        }
            .replay(1)
            .autoConnect(1) { disposable += it }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}