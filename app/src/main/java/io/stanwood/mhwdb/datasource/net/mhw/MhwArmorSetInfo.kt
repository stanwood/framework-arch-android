package io.stanwood.mhwdb.datasource.net.mhw

import kotlinx.serialization.Serializable

@Serializable
class MhwArmorSetInfo(val id: Long, val name: String, val rank: String, val pieces: List<Long>)