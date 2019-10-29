package io.stanwood.mhwdb.feature.armors.di

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDataProvider
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDataProviderImpl
import io.stanwood.mhwdb.feature.armors.ui.ArmorsFragment
import io.stanwood.mhwdb.glide.GlideAppFactory
import io.stanwood.mhwdb.glide.GlideAppFragmentFactory
import io.stanwood.mhwdb.glide.ImageViewBindingAdapters

@Module
object ArmorsFragmentSubModule {
    @Provides
    internal fun provideGlideAppFragmentFactory(fragment: ArmorsFragment): GlideAppFactory =
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
        fragment: ArmorsFragment,
        dataProviderFactory: ViewDataProviderFactory<ArmorDataProviderImpl>
    ): ArmorDataProvider =
        ViewModelProviders.of(fragment, dataProviderFactory).get(ArmorDataProviderImpl::class.java)
}