package io.stanwood.mhwdb.feature.weapons.dataprovider

import io.reactivex.Observable
import io.stanwood.framework.arch.core.Resource
import io.stanwood.mhwdb.repository.mhw.WeaponType

interface WeaponsPagerDataProvider {
    val data: Observable<Resource<List<WeaponType>>>
    fun retry()
}