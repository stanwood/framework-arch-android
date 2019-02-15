package io.stanwood.mhwdb.feature.main.di

import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.feature.main.ui.MainActivity

@Module
class MainActivitySubModule {

    @Provides
    internal fun provideMainNavController(activity: MainActivity) =
        activity.findNavController(R.id.nav_host_fragment)


}