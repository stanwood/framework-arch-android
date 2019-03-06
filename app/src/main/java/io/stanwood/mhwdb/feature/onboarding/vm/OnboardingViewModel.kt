package io.stanwood.mhwdb.feature.onboarding.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.nav.NavigationTarget
import io.stanwood.mhwdb.feature.main.dataprovider.MainDataProvider
import io.stanwood.mhwdb.feature.onboarding.ui.OnboardingFragmentDirections
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(private val dataProvider: MainDataProvider) : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    private var disposable: Disposable? = null
    val navigator: Observable<NavigationTarget> = navigation

    fun nextPage() {
        if (disposable == null || disposable?.isDisposed == true) {
            disposable = dataProvider.newUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { it.printStackTrace() },
                    onComplete = {
                        navigation.onNext(NavigationTarget(OnboardingFragmentDirections.actionToContainerFragment()))
                    })
        }
    }

    fun destroy() {
        disposable?.dispose()
    }
}