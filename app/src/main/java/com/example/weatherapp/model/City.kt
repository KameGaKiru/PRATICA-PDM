package com.example.weatherapp.model

import com.google.android.gms.maps.model.LatLng

data class City(val name : String,
                val location: LatLng? = null,
                var weather: Weather? = null,
                var forecast: List<Forecast>? = null
)
