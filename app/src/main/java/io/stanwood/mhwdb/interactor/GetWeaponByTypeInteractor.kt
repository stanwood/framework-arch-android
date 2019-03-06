package io.stanwood.mhwdb.interactor

import io.stanwood.mhwdb.repository.mhw.MhwRepository
import javax.inject.Inject

class GetWeaponByTypeInteractor @Inject constructor(val repository: MhwRepository) {
    fun getWeapons(weaponType: String) = repository.fetchWeaponByType(weaponType)
}