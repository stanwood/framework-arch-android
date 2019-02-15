package io.stanwood.mhwdb.feature.armors.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.armors.ui.ArmorsFragment

@Module(includes = [ArmorsFragmentSubModule::class])
abstract class ArmorsFragmentModule : FragmentModule<ArmorsFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}