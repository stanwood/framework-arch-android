package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class MhwWeaponAsset(@Optional val icon: String? = null, @Optional val image: String? = null)