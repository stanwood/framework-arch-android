package io.stanwood.mhwdb.feature.armor.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.stanwood.framework.arch.core.Resource
import io.stanwood.framework.arch.core.ViewModel
import io.stanwood.framework.arch.nav.NavigationTarget
import io.stanwood.mhwdb.NavGraphMainDirections
import io.stanwood.mhwdb.feature.armor.dataprovider.ArmorDataProvider
import javax.inject.Inject

class ArmorViewModel @Inject constructor(private val dataProvider: ArmorDataProvider) : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    val navigator = navigation.firstElement()
    private val expandedSetsSubject = BehaviorSubject.create<MutableSet<Long>>()
    val expandedSets = mutableSetOf<Long>()

    val items: Observable<List<ArmorItem>> =
        dataProvider.data
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { grouped ->
                when (grouped) {
                    is Resource.Success -> expandedSetsSubject.startWith(expandedSets)
                        .map { selectedSets ->
                            mutableListOf<ArmorItem>().apply {
                                grouped.data.forEach {
                                    it.key.selected = selectedSets.contains(it.key.id)
                                    add(it.key)
                                    if (it.key.selected) {
                                        addAll(it.value)
                                    }
                                }
                            }.toList()
                        }
                    else -> Observable.just(emptyList())
                }
            }

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