package io.stanwood.mhwdb.repository.mhw

data class Armor(val id: Long, val type: String, val name: String, val rank: String, val setName: String? = null, val image: String? = null)