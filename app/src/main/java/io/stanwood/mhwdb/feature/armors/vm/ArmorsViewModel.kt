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

package io.stanwood.mhwdb.feature.armors.vm

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.core.rx.ResourceStatusTransformer
import io.stanwood.framework.arch.nav.Direction
import io.stanwood.framework.arch.nav.NavigationTarget
import io.stanwood.mhwdb.NavGraphMainDirections
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDataProvider
import javax.inject.Inject

class ArmorsViewModel @Inject constructor(private val dataProvider: ArmorDataProvider) : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    val navigator = navigation.firstElement()!!
    private val expandedSetsSubject = BehaviorSubject.create<MutableSet<Long>>()
    val expandedSets = mutableSetOf<Long>()

    val items =
        dataProvider.data
            .filter { it.data != null }
            .map { it.data!! }
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { data ->
                expandedSetsSubject.startWith(expandedSets)
                    .map { selectedSets ->
                        mutableListOf<ArmorItem>().apply {
                            data.forEach {
                                it.key.selected = selectedSets.contains(it.key.id)
                                add(it.key)
                                if (it.key.selected) {
                                    addAll(it.value)
                                }
                            }
                        }.toList()
                    }
            }!!

    fun retry() {
        dataProvider.retry()
    }

    val status = dataProvider.data
        .compose(ResourceStatusTransformer.fromObservable())
        .observeOn(AndroidSchedulers.mainThread())!!

    fun itemClicked(item: ArmorItem) {
        when (item) {
            is ArmorItem.SetViewModel -> {
                if (!expandedSets.remove(item.id)) {
                    expandedSets.add(item.id)
                }
                expandedSetsSubject.onNext(expandedSets)
            }
            is ArmorItem.ArmorViewModel -> {
                navigation.onNext(Direction(NavGraphMainDirections.showArmorDetails(item.id)))
            }
        }
    }

    fun destroy() {
    }
}