package com.weatherapp.models

import java.io.Serializable
//coordinates for the users location -- for the API response
data class Coord(
    val lon: Double,
    val lat: Double
) : Serializable