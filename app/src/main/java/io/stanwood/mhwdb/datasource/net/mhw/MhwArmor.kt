package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class MhwArmor(
    val id: Long,
    val type: String,
    val rank: String,
    val name: String, @Optional val assets: MhwArmorAsset? = null, @Optional val armorSet: MhwArmorSetInfo? = null
)