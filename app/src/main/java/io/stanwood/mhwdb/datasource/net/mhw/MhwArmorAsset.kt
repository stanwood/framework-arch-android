package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class MhwArmorAsset(@Optional val imageMale: String? = null, @Optional val imageFemale: String? = null)