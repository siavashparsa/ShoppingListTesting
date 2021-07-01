package com.siavashdev.shoppinglisttesting.data.remote

import com.siavashdev.shoppinglisttesting.BuildConfig
import com.siavashdev.shoppinglisttesting.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET("/api/")
    suspend fun searchForImage(

        @Query("q") searchQuery: String,
        @Query("key") apiKey:String = BuildConfig.API_KEY

    ):Response<ImageResponse>

}