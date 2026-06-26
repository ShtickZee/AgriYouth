package com.wayne.ani_agad.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current_weather") val currentWeather: CurrentWeather,
    @SerializedName("daily") val daily: DailyData
)

data class CurrentWeather(
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("windspeed") val windspeed: Double,
    @SerializedName("weathercode") val weatherCode: Int
)

data class DailyData(
    @SerializedName("precipitation_probability_max") val rainProbability: List<Int>
)

data class WeatherAlert(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val isHighPriority: Boolean
)
