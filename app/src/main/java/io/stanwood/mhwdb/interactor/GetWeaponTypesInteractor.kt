package io.stanwood.mhwdb.interactor

import io.stanwood.mhwdb.repository.mhw.MhwRepository
import javax.inject.Inject

class GetWeaponTypesInteractor @Inject constructor(private val repository: MhwRepository) {
    fun getWeaponTypes() = repository.fetchWeaponTypes()
}