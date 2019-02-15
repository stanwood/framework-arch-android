package io.stanwood.mhwdb.interactor

import io.stanwood.mhwdb.repository.mhw.MhwRepository
import javax.inject.Inject

class GetArmorInteractor @Inject constructor(private val repository: MhwRepository) {
    fun getArmor() = repository.fetchArmorSets()
}