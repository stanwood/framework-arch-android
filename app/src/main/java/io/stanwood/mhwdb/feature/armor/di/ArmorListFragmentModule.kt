package io.stanwood.mhwdb.feature.armor.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.armor.ui.ArmorListFragment

@Module(includes = [ArmorListFragmentSubModule::class])
abstract class ArmorListFragmentModule : FragmentModule<ArmorListFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}