package ru.sk1ly.weatherapp.data

data class CurrentWeather(
    val dateTime: String = "",
    val temp: String = "0.0",
    val weatherCode: Int = 0
)