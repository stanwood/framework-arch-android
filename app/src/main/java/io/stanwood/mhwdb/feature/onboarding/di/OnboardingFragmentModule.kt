package io.stanwood.mhwdb.feature.onboarding.di

import dagger.Module
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.mhwdb.feature.onboarding.ui.OnboardingFragment

@Module(includes = [OnboardingFragmentSubModule::class])
abstract class OnboardingFragmentModule : FragmentModule<OnboardingFragment>()