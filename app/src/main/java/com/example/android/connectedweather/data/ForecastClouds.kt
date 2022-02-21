package com.example.android.connectedweather.data

import com.squareup.moshi.Json
import java.io.Serializable

class ForecastClouds (
    @Json (name = "all") val cloudPercent: Int
) : Serializable
