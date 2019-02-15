package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class MhwWeapon(val id: Long, val type: String, val name: String, @Optional val assets: MhwWeaponAsset? = null)