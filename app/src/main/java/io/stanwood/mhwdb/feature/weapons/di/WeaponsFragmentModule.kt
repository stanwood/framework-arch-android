package io.stanwood.mhwdb.feature.weapons.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsFragment

@Module(includes = [WeaponsFragmentSubModule::class])
abstract class WeaponsFragmentModule : FragmentModule<WeaponsFragment>() {


}