package io.stanwood.mhwdb.feature.onboarding.di

import androidx.databinding.DataBindingComponent
import dagger.Module
import dagger.Provides
import io.stanwood.mhwdb.feature.onboarding.ui.OnboardingFragment
import io.stanwood.mhwdb.glide.GlideAppFactory
import io.stanwood.mhwdb.glide.GlideAppFragmentFactory
import io.stanwood.mhwdb.glide.ImageViewBindingAdapters

@Module
object OnboardingFragmentSubModule {
    @Provides
    internal fun provideGlideAppFragmentFactory(fragment: OnboardingFragment): GlideAppFactory =
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
}