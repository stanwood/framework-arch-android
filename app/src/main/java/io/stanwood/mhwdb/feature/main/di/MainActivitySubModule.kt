package io.stanwood.mhwdb.feature.main.di

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.framework.arch.di.scope.ActivityScope
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.feature.main.dataprovider.MainDataProvider
import io.stanwood.mhwdb.feature.main.dataprovider.MainDataProviderImpl
import io.stanwood.mhwdb.feature.main.ui.MainActivity

@Module
object MainActivitySubModule {

    @Provides
    @JvmStatic
    internal fun provideMainNavController(activity: MainActivity) =
        activity.findNavController(R.id.nav_host_fragment)

    @Provides
    @ActivityScope
    @JvmStatic
    internal fun provideMainLoader(
        activity: MainActivity,
        viewModelFactory: ViewDataProviderFactory<MainDataProviderImpl>
    ): MainDataProvider =
        ViewModelProviders.of(activity, viewModelFactory).get(MainDataProviderImpl::class.java)
}