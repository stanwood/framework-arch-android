package io.stanwood.mhwdb.feature.armors.dataprovider

import io.reactivex.Observable
import io.stanwood.framework.arch.core.Resource
import io.stanwood.mhwdb.repository.mhw.Armor

interface ArmorDetailsDataProvider {
    val data: Observable<Resource<Armor>>
    var armorId: Long
}