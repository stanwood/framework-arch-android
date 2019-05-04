package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Serializable

@Serializable
data class MhwArmorAsset(val imageMale: String? = null, val imageFemale: String? = null)