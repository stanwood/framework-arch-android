package io.stanwood.mhwdb.feature.weapons.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsPagerFragment

@Module(includes = [WeaponsPagerFragmentSubModule::class])
abstract class WeaponsPagerFragmentModule : FragmentModule<WeaponsPagerFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}