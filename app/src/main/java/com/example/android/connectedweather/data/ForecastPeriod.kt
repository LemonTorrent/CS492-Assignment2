package com.example.android.connectedweather.data

import com.squareup.moshi.Json
import java.io.Serializable

data class ForecastPeriod(
    //val year: Int,
    //val month: Int,
    //val day: Int,
    val dt_txt: String,
    // @Json(name = "temp_max") val highTemp: Double,
    //@Json(name = "temp_min")val lowTemp: Double,
    val pop: Double,
    val main: ForecastInfo,
    val weather: List<ForecastDesc>,
    val clouds: ForecastClouds,
    val wind: ForecastWind
) : Serializable
