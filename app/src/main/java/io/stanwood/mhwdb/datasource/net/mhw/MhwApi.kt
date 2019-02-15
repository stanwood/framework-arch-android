package io.stanwood.mhwdb.datasource.net.mhw

import io.reactivex.Single
import okio.BufferedSource
import retrofit2.http.GET

interface MhwApi {
    @GET("weapons")
    fun fetchWeapon(): Single<BufferedSource>

    @GET("armor")
    fun fetchArmor(): Single<BufferedSource>
}