package io.stanwood.mhwdb.repository.mhw

import io.stanwood.mhwdb.datasource.net.mhw.MhwArmorSetInfo

fun MhwArmorSetInfo.mapToArmorSet(armor: List<Armor>) =
    ArmorSet(this.id, this.name, this.rank, armor)

