package io.stanwood.mhwdb.feature.weapons.dataprovider

import io.reactivex.Observable
import io.stanwood.framework.arch.core.Resource
import io.stanwood.mhwdb.repository.mhw.Weapon

interface WeaponsDataProvider {
    val data: Observable<Resource<List<Weapon>>>
    var weaponType: String
}