package io.stanwood.mhwdb.repository.mhw

import io.stanwood.mhwdb.datasource.net.mhw.MhwArmor

fun MhwArmor.mapToArmor() =
    Armor(this.id, this.type, this.name, this.rank, this.armorSet?.name, this.assets?.imageMale)

