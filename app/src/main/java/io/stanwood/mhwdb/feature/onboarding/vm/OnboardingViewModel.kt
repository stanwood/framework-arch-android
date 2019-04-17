/*
 * Copyright (c) 2019 stanwood GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.mhwdb.feature.onboarding.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.nav.Direction
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
                        navigation.onNext(Direction(OnboardingFragmentDirections.actionToContainerFragment()))
                    })
        }
    }

    fun destroy() {
        disposable?.dispose()
    }
}