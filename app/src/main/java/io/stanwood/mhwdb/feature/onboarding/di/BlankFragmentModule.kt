package io.stanwood.mhwdb.feature.onboarding.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.onboarding.ui.BlankFragment

@Module(includes = [BlankFragmentSubModule::class])
abstract class BlankFragmentModule : FragmentModule<BlankFragment>() {

    // TODO : Provide e.g. Fragments created by this Fragment
}