/*
 * Copyright (c) 2019 stanwood GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.mhwdb.datasource.net

import android.app.Application
import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import io.stanwood.debugapp.LoggingInterceptor
import io.stanwood.framework.network.core.interceptor.ConnectivityInterceptor
import io.stanwood.framework.network.core.retrofit.BufferedSourceConverterFactory
import io.stanwood.mhwdb.BuildConfig
import io.stanwood.mhwdb.datasource.net.mhw.MhwApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    internal fun provideHttpClient(context: Application) =
        OkHttpClient.Builder().apply {
            addInterceptor(ConnectivityInterceptor(context))
            cache(File(context.cacheDir, "data")
                .let {
                    it.mkdirs()
                    Cache(it, 512 * 1024)
                })
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(LoggingInterceptor(context, true))
            }
        }.build()

    @Singleton
    @Provides
    internal fun provideRetrofit(httpClient: OkHttpClient) =
        Retrofit.Builder().client(httpClient).baseUrl("https://mhw-db.com/")
            .addConverterFactory(BufferedSourceConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

    @Singleton
    @Provides
    internal fun provideMhwApi(retrofit: Retrofit) = retrofit.create(MhwApi::class.java)

    @Provides
    @Singleton
    fun provideFileSystem(context: Application): FileSystem =
        FileSystemFactory.create(File(context.noBackupFilesDir, "store"))
}