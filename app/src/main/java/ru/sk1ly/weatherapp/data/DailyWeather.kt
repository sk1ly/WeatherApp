package ru.sk1ly.weatherapp.data

data class DailyWeather(
    val date: String,
    val maxTemp: Int,
    val minTemp: Int,
    val weatherCode: Int
)