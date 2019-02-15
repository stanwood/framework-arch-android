package io.stanwood.mhwdb.feature.armors.dataprovider

import io.reactivex.Observable
import io.stanwood.framework.arch.core.Resource
import io.stanwood.mhwdb.feature.armors.vm.ArmorItem

interface ArmorDataProvider {
    val data: Observable<Resource<Map<ArmorItem.SetViewModel, List<ArmorItem.ArmorViewModel>>>>
}