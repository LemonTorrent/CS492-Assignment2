package com.example.android.connectedweather.data

import java.io.Serializable

class ForecastWind (
    val speed: Float,
    val deg: Int,
    val gust: Float
) : Serializable