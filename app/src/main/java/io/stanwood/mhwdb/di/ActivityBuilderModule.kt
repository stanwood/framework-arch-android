package io.stanwood.mhwdb.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.stanwood.framework.arch.di.scope.ActivityScope
import io.stanwood.mhwdb.feature.main.di.MainActivityModule
import io.stanwood.mhwdb.feature.main.ui.MainActivity

@Module
abstract class ActivityBuilderModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}