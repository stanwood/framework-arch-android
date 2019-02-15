package io.stanwood.mhwdb.interactor

import io.stanwood.mhwdb.repository.mhw.MhwRepository
import javax.inject.Inject

class GetArmorByIdInteractor @Inject constructor(private val repository: MhwRepository) {
    fun getArmor(id: Long) = repository.fetchArmorById(id)
}