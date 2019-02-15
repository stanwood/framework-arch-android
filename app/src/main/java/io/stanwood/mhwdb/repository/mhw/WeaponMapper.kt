package io.stanwood.mhwdb.repository.mhw

import io.stanwood.mhwdb.datasource.net.mhw.MhwWeapon

fun MhwWeapon.mapToWeapon() =
    Weapon(this.id, this.type, this.name, this.assets?.image)

fun MhwWeapon.mapToWeaponType() =
    WeaponType(this.type, this.type.replace('-', ' '))