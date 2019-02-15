package io.stanwood.mhwdb.feature.armor.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.armor.ui.ArmorFragment

@Module(includes = [ArmorFragmentSubModule::class])
abstract class ArmorFragmentModule : FragmentModule<ArmorFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}