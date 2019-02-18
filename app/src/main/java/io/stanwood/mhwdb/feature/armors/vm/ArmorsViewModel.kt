package io.stanwood.mhwdb.feature.armors.vm

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.core.rx.ResourceStatusTransformer
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
                navigation.onNext(NavigationTarget(NavGraphMainDirections.showArmorDetails(item.id)))
            }
        }
    }

    fun destroy() {
    }
}