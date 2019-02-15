package io.stanwood.mhwdb.feature.main.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.stanwood.framework.arch.di.module.ActivityModule
import io.stanwood.framework.arch.di.scope.FragmentScope
import io.stanwood.mhwdb.feature.armors.di.ArmorDetailsFragmentModule
import io.stanwood.mhwdb.feature.armors.ui.ArmorDetailsFragment
import io.stanwood.mhwdb.feature.container.di.ContainerFragmentModule
import io.stanwood.mhwdb.feature.container.ui.ContainerFragment
import io.stanwood.mhwdb.feature.main.ui.MainActivity
import io.stanwood.mhwdb.feature.onboarding.di.OnboardingFragmentModule
import io.stanwood.mhwdb.feature.onboarding.ui.OnboardingFragment

@Module(includes = [MainActivitySubModule::class])
abstract class MainActivityModule : ActivityModule<MainActivity>() {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ContainerFragmentModule::class])
    internal abstract fun contributeContainerFragment(): ContainerFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ArmorDetailsFragmentModule::class])
    internal abstract fun contributeArmorDetailsFragment(): ArmorDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [OnboardingFragmentModule::class])
    internal abstract fun contributeOnboardingFragment(): OnboardingFragment
}