package io.stanwood.mhwdb.feature.armor.di

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.mhwdb.feature.armor.dataprovider.ArmorDataProvider
import io.stanwood.mhwdb.feature.armor.dataprovider.ArmorDataProviderImpl
import io.stanwood.mhwdb.feature.armor.ui.ArmorListFragment
import io.stanwood.mhwdb.glide.GlideAppFactory
import io.stanwood.mhwdb.glide.GlideAppFragmentFactory
import io.stanwood.mhwdb.glide.ImageViewBindingAdapters

@Module
class ArmorListFragmentSubModule {
    @Provides
    internal fun provideGlideAppFragmentFactory(fragment: ArmorListFragment): GlideAppFactory =
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
    internal fun provideArmorDataProvider(
        fragment: ArmorListFragment,
        dataProviderFactory: ViewDataProviderFactory<ArmorDataProviderImpl>
    ): ArmorDataProvider =
        ViewModelProviders.of(fragment, dataProviderFactory).get(ArmorDataProviderImpl::class.java)
}