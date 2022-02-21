package com.example.android.connectedweather.data

import com.squareup.moshi.Json
import java.io.Serializable

data class ForecastDesc (
    @Json (name="main") val quickDescription: String,
    val description: String
    ) : Serializable
