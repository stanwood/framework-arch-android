package io.stanwood.mhwdb.feature.armors.di

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import io.stanwood.framework.arch.di.factory.ViewDataProviderFactory
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDetailsDataProvider
import io.stanwood.mhwdb.feature.armors.dataprovider.ArmorDetailsDataProviderImpl
import io.stanwood.mhwdb.feature.armors.ui.ArmorDetailsFragment
import io.stanwood.mhwdb.glide.GlideAppFactory
import io.stanwood.mhwdb.glide.GlideAppFragmentFactory
import io.stanwood.mhwdb.glide.ImageViewBindingAdapters

@Module
object ArmorDetailsFragmentSubModule {
    @Provides
    internal fun provideGlideAppFragmentFactory(fragment: ArmorDetailsFragment): GlideAppFactory =
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
    internal fun provideArmorDetailsDataProvider(
        fragment: ArmorDetailsFragment,
        dataProviderFactory: ViewDataProviderFactory<ArmorDetailsDataProviderImpl>
    ): ArmorDetailsDataProvider =
        ViewModelProviders.of(
            fragment,
            dataProviderFactory
        ).get(ArmorDetailsDataProviderImpl::class.java)
}