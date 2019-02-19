package io.stanwood.mhwdb.di

import android.app.Application
import dagger.Module
import dagger.Provides
import io.stanwood.mhwdb.feature.ExceptionMessageMapper
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    internal fun provideExceptionMapper(context: Application) = ExceptionMessageMapper(context)
}