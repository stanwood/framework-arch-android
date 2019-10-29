package io.stanwood.mhwdb.feature.weapons.di

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsDataProvider
import io.stanwood.mhwdb.feature.weapons.dataprovider.WeaponsDataProviderImpl
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsFragment
import io.stanwood.mhwdb.glide.GlideAppFactory
import io.stanwood.mhwdb.glide.GlideAppFragmentFactory
import io.stanwood.mhwdb.glide.ImageViewBindingAdapters

@Module
object WeaponsFragmentSubModule {
    @Provides
    internal fun provideGlideAppFragmentFactory(fragment: WeaponsFragment): GlideAppFactory =
        GlideAppFragmentFactory(fragment)

    @Provides
    internal fun provideImageViewBindingAdapters(glideAppFactory: GlideAppFactory): ImageViewBindingAdapters =
        ImageViewBindingAdapters(glideAppFactory)

    @Provides
    internal fun provideGlideDataBindingComponent(imageViewBindingAdapters: ImageViewBindingAdapters): DataBindingComponent =
        object : DataBindingComponent {
            override fun getImageViewBindingAdapters(): ImageViewBindingAdapters =
                imageViewBindingAdapters
        }

    @Provides
    internal fun provideWeaponsDataProvider(
        fragment: WeaponsFragment,
        dataProviderFactory: ViewDataProviderFactory<WeaponsDataProviderImpl>
    ): WeaponsDataProvider =
        ViewModelProviders.of(
            fragment,
            dataProviderFactory
        ).get(WeaponsDataProviderImpl::class.java)
}