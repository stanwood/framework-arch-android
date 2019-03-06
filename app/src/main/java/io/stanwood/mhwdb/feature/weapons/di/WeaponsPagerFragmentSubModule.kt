package io.stanwood.mhwdb.feature.weapons.di

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsPagerDataProvider
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsPagerDataProviderImpl
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsPagerFragment

@Module
object WeaponsPagerFragmentSubModule {

    @Provides
    @JvmStatic
    internal fun provideWeaponsPagerDataProvider(
        fragment: WeaponsPagerFragment,
        dataProviderFactory: ViewDataProviderFactory<WeaponsPagerDataProviderImpl>
    ): WeaponsPagerDataProvider =
        ViewModelProviders.of(fragment, dataProviderFactory).get(WeaponsPagerDataProviderImpl::class.java)
}