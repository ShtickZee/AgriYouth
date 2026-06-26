package com.wayne.ani_agad.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double = 13.9333,
        @Query("longitude") lon: Double = 121.6167,
        @Query("current_weather") current: Boolean = true,
        @Query("daily") daily: String = "precipitation_probability_max",
        @Query("timezone") tz: String = "Asia/Manila"
    ): WeatherResponse
}

object WeatherService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: WeatherApi = retrofit.create(WeatherApi::class.java)
}
