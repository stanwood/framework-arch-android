package io.stanwood.mhwdb.feature.armor.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.armor.ui.ArmorDetailsFragment

@Module(includes = [ArmorDetailsFragmentSubModule::class])
abstract class ArmorDetailsFragmentModule : FragmentModule<ArmorDetailsFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}