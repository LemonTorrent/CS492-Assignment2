package com.example.android.connectedweather.data

import java.io.Serializable

data class ForecastInfo (
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
) : Serializable